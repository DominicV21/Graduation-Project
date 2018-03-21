/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import chdsw.philips.com.haraldlib.services.HBServiceHandler;

import static android.bluetooth.BluetoothDevice.BOND_BONDED;

/**
 * Represents a remote Bluetooth device.
 * A {@link HBDevice} lets you create a connection with the device or query information about it.
 * This class is a wrapper around the {@link BluetoothDevice}.
 */
public class HBDevice extends BluetoothGattCallback {
    public static final String TAG = "HBDevice";
    private static final String DESCRIPTOR_UUID = "00002902-0000-1000-8000-00805f9b34fb";
    private static final int SERVICES_NOT_FOUND = 129;

    private enum QueueStatus {IDLE, WORKING}

    private Context context;
    private Handler bleHandler;
    private BluetoothDevice device;
    private UUID primaryServiceUUID;
    private Map<UUID, HBServiceHandler> supportedServices;
    private HBDeviceListener listener;
    private List<UUID> deviceServices;
    private Queue<HBWriteCommand> subscribeQueue;
    private Queue<HBCommand> readWriteQueue;
    private BluetoothGatt bluetoothGatt;
    private State state;
    private QueueStatus queueStatus;
    private boolean createBond;

    public enum State {
        Disconnected, Disconnecting, Connecting, Connected
    }

    /**
     * This abstract class is used to implement BluetoothGatt callbacks.
     */
    private final BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {

        /**
         * Callback indicating when GATT client has connected/disconnected to/from a remote
         * GATT server.
         *
         * @param gatt GATT client
         * @param status Status of the connect or disconnect operation.
         *               {@link BluetoothGatt#GATT_SUCCESS} if the operation succeeds.
         * @param newState Returns the new connection state. Can be one of
         *                  {@link BluetoothProfile#STATE_DISCONNECTED} or
         *                  {@link BluetoothProfile#STATE_CONNECTED}
         */
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                HBLogger.i(TAG, String.format("Device %s state connected", getAddress()));

                if (status == BluetoothGatt.GATT_SUCCESS) {
                    if (createBond && device.getBondState() != BOND_BONDED) {
                        HBLogger.v(TAG, String.format("Bond required for characteristic an none exists, creating.."));
                        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
                        context.registerReceiver(bondStateReceiver, filter);
                        device.createBond();
                    } else {
                        gatt.discoverServices();
                    }
                } else {
                    HBLogger.i(TAG, String.format("Device %s gatt error status %d", getAddress(), status));
                    gatt.disconnect();
                }
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                HBLogger.i(TAG, String.format("Device %s state disconnected", getAddress()));
                gatt.close();
                state = State.Disconnected;
                listener.disconnected(HBDevice.this);
            }
        }

        /**
         * Callback invoked when the list of remote services, characteristics and descriptors
         * for the remote device have been updated, ie new services have been discovered.
         *
         * @param gatt GATT client invoked {@link BluetoothGatt#discoverServices}
         * @param status {@link BluetoothGatt#GATT_SUCCESS} if the remote device
         *               has been explored successfully.
         */
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            HBLogger.v(TAG, String.format("onServicesDiscovered device: %s, status: %d", getAddress(), status));

            if (status == SERVICES_NOT_FOUND) {
                HBLogger.i(TAG, "Services not found");
                state = State.Disconnected;
                listener.connectFailed(HBDevice.this);
                return;
            }

            deviceServices.clear();

            for (BluetoothGattService service : gatt.getServices()) {
                if (supportedServices.containsKey(service.getUuid())) {
                    HBLogger.v(TAG, String.format("Service found device: %s, service: %s", getAddress(), service.getUuid()));
                    deviceServices.add(service.getUuid());

                    for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                        if ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                            HBLogger.v(TAG, String.format("Characteristic notify device: %s, service: %s", getAddress(), characteristic.getUuid()));
                            subscribeQueue.add(new HBWriteCommand(service.getUuid(),
                                    characteristic.getUuid(), BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE));
                        }

                        if ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0) {
                            HBLogger.v(TAG, String.format("Characteristic indicate device: %s, service: %s", getAddress(), characteristic.getUuid()));
                            subscribeQueue.add(new HBWriteCommand(service.getUuid(),
                                    characteristic.getUuid(), BluetoothGattDescriptor.ENABLE_INDICATION_VALUE));
                        }
                    }
                }
            }

            checkSubscribeQueue();
        }

        /**
         * Callback indicating the result of a descriptor write operation.
         *
         * @param gatt GATT client invoked {@link BluetoothGatt#writeDescriptor}
         * @param descriptor Descriptor that was written to the associated
         *                   remote device.
         * @param status The result of the write operation
         *               {@link BluetoothGatt#GATT_SUCCESS} if the operation succeeds.
         */
        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            HBLogger.v(TAG, String.format("onDescriptorWrite device: %s, descriptor: %s", getAddress(), descriptor.getUuid()));
            checkSubscribeQueue();
        }

        /**
         * Callback reporting the result of a characteristic read operation.
         *
         * @param gatt GATT client invoked {@link BluetoothGatt#readCharacteristic}
         * @param characteristic Characteristic that was read from the associated
         *                       remote device.
         * @param status {@link BluetoothGatt#GATT_SUCCESS} if the read operation
         *               was completed successfully.
         */
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            HBLogger.v(TAG, String.format("onCharacteristicRead device: %s, characteristic: %s", getAddress(), characteristic.getUuid()));

            HBServiceHandler serviceHandler = supportedServices.get(characteristic.getService().getUuid());
            if (serviceHandler != null) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    serviceHandler.characteristicValueUpdated(getAddress(), characteristic);
                } else {
                    serviceHandler.characteristicErrorResponse(getAddress(), characteristic, status);
                }
            }

            queueStatus = QueueStatus.IDLE;
            checkReadWriteQueue();
        }

        /**
         * Callback triggered as a result of a remote characteristic notification.
         *
         * @param gatt GATT client the characteristic is associated with
         * @param characteristic Characteristic that has been updated as a result
         *                       of a remote notification event.
         */
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            HBLogger.v(TAG, String.format("onCharacteristicChanged device: %s, service: %s, characteristic: %s", getAddress(), characteristic.getService().getUuid(), characteristic.getUuid()));

            HBServiceHandler serviceHandler = supportedServices.get(characteristic.getService().getUuid());
            if (serviceHandler != null) {
                serviceHandler.characteristicValueUpdated(getAddress(), characteristic);
            }
        }

        /**
         * Callback indicating the result of a characteristic write operation.
         *
         * @param gatt GATT client invoked {@link BluetoothGatt#writeCharacteristic}
         * @param characteristic Characteristic that was written to the associated
         *                       remote device.
         * @param status The result of the write operation
         *               {@link BluetoothGatt#GATT_SUCCESS} if the operation succeeds.
         */
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            HBLogger.v(TAG, String.format("onCharacteristicWrite device: %s, characteristic: %s", getAddress(), characteristic.getUuid()));

            HBServiceHandler serviceHandler = supportedServices.get(characteristic.getService().getUuid());
            if (serviceHandler != null) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    serviceHandler.characteristicValueUpdated(getAddress(), characteristic);
                } else {
                    serviceHandler.characteristicErrorResponse(getAddress(), characteristic, status);
                }
            }

            queueStatus = QueueStatus.IDLE;
            checkReadWriteQueue();
        }

    };

    private final BroadcastReceiver bondStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);

                switch (state) {
                    case BluetoothDevice.BOND_BONDING:
                        HBLogger.i(TAG, String.format("Bond state to bonding for device: %s", getAddress()));
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        HBLogger.i(TAG, String.format("Bond state to bonded for device: %s", getAddress()));
                        context.unregisterReceiver(bondStateReceiver);
                        bluetoothGatt.discoverServices();
                        break;
                    case BluetoothDevice.BOND_NONE:
                        HBLogger.i(TAG, String.format("Bond state to none for device: %s", getAddress()));
                        context.unregisterReceiver(bondStateReceiver);
                        listener.connectFailed(HBDevice.this);
                        break;
                }
            }
        }
    };

    /**
     * Constructs a new device wrapper around {@code device}.
     *
     * @param context            Android application environment.
     * @param device             Wrapped Android bluetooth device.
     * @param primaryServiceUUID Primary UUID of the device.
     * @param supportedServices  Supported services by {@link HBCentral}.
     * @param listener           Callback to {@link HBCentral}.
     */
    public HBDevice(Context context, BluetoothDevice device, UUID primaryServiceUUID,
                    Map<UUID, HBServiceHandler> supportedServices, HBDeviceListener listener) {
        this.context = context;
        this.device = device;
        this.primaryServiceUUID = primaryServiceUUID;
        this.supportedServices = supportedServices;
        this.listener = listener;
        this.bleHandler = new Handler();
        this.deviceServices = new ArrayList();
        this.subscribeQueue = new LinkedList<>();
        this.readWriteQueue = new LinkedList<>();
        this.state = State.Disconnected;
        this.queueStatus = QueueStatus.IDLE;
    }

    /**
     * Connect with the bluetooth device.
     */
    public void connect(boolean createBond) {
        HBLogger.v(TAG, String.format("connect device: %s, state: %s", getAddress(), state));

        if (state == State.Disconnected) {
            this.state = State.Connecting;
            this.createBond = createBond;
            bleHandler.post(new Runnable() {
                @Override
                public void run() {
                    bluetoothGatt = device.connectGatt(context, false, bluetoothGattCallback);
                }
            });
        } else {
            listener.connectFailed(HBDevice.this);
        }
    }

    /**
     * Disconnect from the bluetooth device.
     */
    public void disconnect() {
        HBLogger.v(TAG, String.format("disconnect device: %s, state: %s", getAddress(), state));

        if (state == State.Connected || state == State.Connecting) {
            this.state = State.Disconnecting;
            bleHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (bluetoothGatt != null)
                        bluetoothGatt.disconnect();
                }
            });
        } else {
            listener.disconnected(HBDevice.this);
        }
    }

    /**
     * Get the address of the bluetooth device
     *
     * @return Address of the bluetooth device
     */
    public String getAddress() {
        HBLogger.v(TAG, String.format("getAddress device: %s", device.getAddress()));
        return device.getAddress();
    }

    public UUID getPrimaryService() {
        HBLogger.v(TAG, String.format("getPrimaryService device: %s", getAddress()));
        return this.primaryServiceUUID;
    }

    /**
     * Get the services supported by the connected bluetooth device.
     * Only services that are also supported by {@link HBCentral} are included.
     *
     * @return Supported device services.
     */
    public List<UUID> getServices() {
        HBLogger.v(TAG, String.format("getServices device: %s", getAddress()));
        return Collections.unmodifiableList(deviceServices);
    }

    /**
     * Get the support for a specific service by the device
     * Only services that are also supported by {@link HBCentral} are included.
     *
     * @return Supported for specific service.
     */
    public boolean supportsService(UUID serviceUUID) {
        HBLogger.v(TAG, String.format("supportsService device: %s, serviceUUID: %s", getAddress(), serviceUUID));
        return deviceServices.contains(serviceUUID);
    }

    public State getState() {
        HBLogger.v(TAG, String.format("getState device: %s, state: %s", getAddress(), state));
        return state;
    }

    /**
     * Read a characteristic of the connected device.
     * The command contains information about the service and characteristic to read.
     *
     * @param command Specifies which service and characteristic to read.
     */
    public void readCharacteristic(HBReadCommand command) {
        HBLogger.v(TAG, String.format("readCharacteristic device: %s, service: %s, characteristic: %s",
                getAddress(), command.getServiceUUID(), command.getCharacteristicUUID()));
        readWriteQueue.add(command);
        checkReadWriteQueue();
    }

    /**
     * Write a value of a characteristic of the connected device
     * The command contains information about the service, characteristic and value to write.
     *
     * @param command Specifies which service, characteristic and value to write.
     */
    public void writeCharacteristic(HBWriteCommand command) {
        HBLogger.v(TAG, String.format("writeCharacteristic device: %s, service: %s, characteristic: %s",
                getAddress(), command.getServiceUUID(), command.getCharacteristicUUID()));
        readWriteQueue.add(command);
        checkReadWriteQueue();
    }

    /**
     * Execute the next command in the subscribe queue.
     * A queue is used because the calls have to be executed sequentially.
     * If the read or write fails, the next command in the queue is executed.
     */
    private void checkSubscribeQueue() {
        HBLogger.v(TAG, String.format("checkSubscribeQueue device: %s", getAddress()));

        bleHandler.post(new Runnable() {
            @Override
            public void run() {
                if (bluetoothGatt == null) {
                    HBLogger.i(TAG, String.format("GATT is null for device: %s", getAddress()));
                    subscribeQueue.clear();
                    return;
                }

                if (subscribeQueue.size() > 0) {
                    HBWriteCommand bluetoothCommand = subscribeQueue.poll();
                    HBLogger.v(TAG, String.format("checkSubscribeQueue device: %s, service: %s, characteristic: %s, value: %s",
                            getAddress(), bluetoothCommand.getServiceUUID(), bluetoothCommand.getCharacteristicUUID(),
                            Arrays.toString(bluetoothCommand.getValue())));

                    try {
                        BluetoothGattService service = bluetoothGatt.getService(bluetoothCommand.getServiceUUID());
                        BluetoothGattCharacteristic characteristic = service.getCharacteristic(bluetoothCommand.getCharacteristicUUID());

                        bluetoothGatt.setCharacteristicNotification(characteristic, true);
                        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(DESCRIPTOR_UUID));
                        descriptor.setValue(bluetoothCommand.getValue());
                        if (!bluetoothGatt.writeDescriptor(descriptor)) {
                            HBLogger.i(TAG, String.format("WriteDescriptor failed for device: %s", getAddress()));
                            checkSubscribeQueue();
                        }
                    } catch (Exception ex) {
                        HBLogger.e(TAG, String.format("checkSubscribeQueue failed for device: %s", getAddress()), ex);
                        checkSubscribeQueue();
                    }
                } else {
                    HBLogger.v(TAG, String.format("checkSubscribeQueue completed device: %s", getAddress()));
                    state = State.Connected;
                    listener.connected(HBDevice.this);
                }
            }
        });
    }

    /**
     * Execute the next command in the read write queue.
     * A read or write is executed based on the command type.
     * A queue is used because the calls have to be executed sequentially.
     * If the read or write fails, the next command in the queue is executed.
     */
    private void checkReadWriteQueue() {
        HBLogger.v(TAG, String.format("checkReadWriteQueue device: %s", getAddress()));

        bleHandler.post(new Runnable() {
            @Override
            public void run() {
                if (queueStatus == QueueStatus.IDLE) {
                    queueStatus = QueueStatus.WORKING;

                    if (bluetoothGatt == null) {
                        HBLogger.i(TAG, String.format("GATT is null for device: %s", getAddress()));
                        readWriteQueue.clear();
                        queueStatus = QueueStatus.IDLE;
                        listener.onReadWriteError(HBDevice.this);
                        return;
                    }

                    if (readWriteQueue.size() > 0) {
                        HBCommand bluetoothCommand = readWriteQueue.poll();

                        try {
                            BluetoothGattService service = bluetoothGatt.getService(bluetoothCommand.getServiceUUID());
                            BluetoothGattCharacteristic characteristic = service.getCharacteristic(bluetoothCommand.getCharacteristicUUID());

                            boolean success = false;
                            if (bluetoothCommand instanceof HBWriteCommand) {
                                HBLogger.v(TAG, String.format("HBWriteCommand device: %s, service: %s, characteristic: %s, value: %s",
                                        getAddress(), bluetoothCommand.getServiceUUID(), bluetoothCommand.getCharacteristicUUID(),
                                        Arrays.toString(((HBWriteCommand) bluetoothCommand).getValue())));

                                if ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                                    HBWriteCommand writeCommand = (HBWriteCommand) bluetoothCommand;
                                    characteristic.setValue(writeCommand.getValue());
                                    success = bluetoothGatt.writeCharacteristic(characteristic);
                                }
                            } else {
                                HBLogger.v(TAG, String.format("HBReadCommand device: %s, service: %s, characteristic: %s",
                                        getAddress(), bluetoothCommand.getServiceUUID(), bluetoothCommand.getCharacteristicUUID()));

                                if ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                                    success = bluetoothGatt.readCharacteristic(characteristic);
                                }
                            }

                            if (!success) {
                                HBLogger.i(TAG, String.format("Read or write failed for device: %s", getAddress()));
                                listener.onReadWriteError(HBDevice.this);
                                queueStatus = QueueStatus.IDLE;
                                checkReadWriteQueue();
                            }
                        } catch (Exception ex) {
                            HBLogger.e(TAG, String.format("checkReadWriteQueue failed for device: %s", getAddress()), ex);
                            listener.onReadWriteError(HBDevice.this);
                            queueStatus = QueueStatus.IDLE;
                            checkReadWriteQueue();
                        }
                    } else {
                        HBLogger.v(TAG, String.format("checkReadWriteQueue completed device: %s", getAddress()));
                        queueStatus = QueueStatus.IDLE;
                    }
                }
            }
        });
    }
}