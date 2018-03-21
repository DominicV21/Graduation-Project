/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

import chdsw.philips.com.haraldlib.services.HBServiceHandler;
import chdsw.philips.com.haraldlib.services.batterylevel.HBBatteryListener;
import chdsw.philips.com.haraldlib.services.batterylevel.HBBatteryListenerHandler;
import chdsw.philips.com.haraldlib.services.batterylevel.HBBatteryServiceHandler;
import chdsw.philips.com.haraldlib.services.bloodpressure.HBBloodPressureListener;
import chdsw.philips.com.haraldlib.services.bloodpressure.HBBloodPressureListenerHandler;
import chdsw.philips.com.haraldlib.services.bloodpressure.HBBloodPressureServiceHandler;
import chdsw.philips.com.haraldlib.services.bloodpressure.object.HBRecordProcedure;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.HBPhysicalActivityMonitorListener;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.HBPhysicalActivityMonitorListenerHandler;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.HBPhysicalActivityMonitorServiceHandler;
import chdsw.philips.com.haraldlib.services.currenttime.HBCurrentTimeListener;
import chdsw.philips.com.haraldlib.services.currenttime.HBCurrentTimeListenerHandler;
import chdsw.philips.com.haraldlib.services.currenttime.HBCurrentTimeServiceHandler;
import chdsw.philips.com.haraldlib.services.currenttime.object.HBCurrentTime;
import chdsw.philips.com.haraldlib.services.currenttime.object.HBLocalTimeInformation;
import chdsw.philips.com.haraldlib.services.deviceinformation.HBDeviceInformationListener;
import chdsw.philips.com.haraldlib.services.deviceinformation.HBDeviceInformationListenerHandler;
import chdsw.philips.com.haraldlib.services.deviceinformation.HBDeviceInformationServiceHandler;
import chdsw.philips.com.haraldlib.services.devicetime.HBDeviceTimeListener;
import chdsw.philips.com.haraldlib.services.devicetime.HBDeviceTimeListenerHandler;
import chdsw.philips.com.haraldlib.services.devicetime.HBDeviceTimeServiceHandler;
import chdsw.philips.com.haraldlib.services.devicetime.object.HBTimeUpdateOperand;
import chdsw.philips.com.haraldlib.services.healththermometer.HBHealthThermometerListener;
import chdsw.philips.com.haraldlib.services.healththermometer.HBHealthThermometerListenerHandler;
import chdsw.philips.com.haraldlib.services.healththermometer.HBHealthThermometerServiceHandler;
import chdsw.philips.com.haraldlib.services.heartrate.HBHeartRateListener;
import chdsw.philips.com.haraldlib.services.heartrate.HBHeartRateListenerHandler;
import chdsw.philips.com.haraldlib.services.heartrate.HBHeartRateServiceHandler;
import chdsw.philips.com.haraldlib.services.nordicdfu.HBNordicDFUListener;
import chdsw.philips.com.haraldlib.services.nordicdfu.HBNordicDFUListenerHandler;
import chdsw.philips.com.haraldlib.services.nordicdfu.HBNordicDFUServiceHandler;
import chdsw.philips.com.haraldlib.services.philipsextension.HBExtendedUserDataType;
import chdsw.philips.com.haraldlib.services.philipsextension.HBPhilipsExtensionListener;
import chdsw.philips.com.haraldlib.services.philipsextension.HBPhilipsExtensionListenerHandler;
import chdsw.philips.com.haraldlib.services.philipsextension.HBPhilipsExtensionServiceHandler;
import chdsw.philips.com.haraldlib.services.philipsextension.data.HBExtendedUserData;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorControlPoint;
import chdsw.philips.com.haraldlib.services.userdata.HBUserDataListener;
import chdsw.philips.com.haraldlib.services.userdata.HBUserDataListenerHandler;
import chdsw.philips.com.haraldlib.services.userdata.HBUserDataServiceHandler;
import chdsw.philips.com.haraldlib.services.userdata.HBUserDataType;
import chdsw.philips.com.haraldlib.services.userdata.data.HBUserData;
import chdsw.philips.com.haraldlib.services.weightscale.HBWeightScaleListener;
import chdsw.philips.com.haraldlib.services.weightscale.HBWeightScaleListenerHandler;
import chdsw.philips.com.haraldlib.services.weightscale.HBWeightScaleServiceHandler;

/**
 * Central class to connect and communicate with bluetooth devices.
 */
public class HBCentral {
    public static final String TAG = "HBCentral";
    private static final long CONNECT_TIMEOUT = 10_000L;

    private enum HBScanMode {IDLE, SCANNING, CONNECTING}

    private Context context;
    private Handler callBackHandler;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothScanner;
    private HBConnectionListenerHandler connectionListenerHandler;
    private Map<String, HBDeviceConnectionListenerHandler> deviceConnectionListeners;
    private Map<String, HBDevice> connectedHbDevices;
    private Map<UUID, HBServiceHandler> supportedServices;
    private HBScanMode scanMode = HBScanMode.IDLE;
    private UUID connectPrimaryServiceUUID;
    private String connectDeviceAddress;
    private Handler timeoutHandler;
    private Runnable timeoutRunnable;

    //region Callbacks

    /**
     * Callback for BLE scan results, supported from Android 5.0 and higher
     * At each scan result, see if this is the device to connect with, if so stop scanning
     */
    @SuppressLint("NewApi")
    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            synchronized (this) {
                BluetoothDevice device = result.getDevice();
                HBLogger.v(TAG, String.format("onScanResult device: %s", device.getAddress()));
                if (isCorrectDevice(device, new HBScanRecord(result.getScanRecord()))) {
                    stopScanning();
                    connect(device);
                }
            }
        }
    };

    /**
     * Callback for BLE scan results, supported until Android 4.4
     * At each scan result, see if this is the device to connect with, if so stop scanning
     */
    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            synchronized (this) {
                HBLogger.v(TAG, String.format("onLeScan device: %s", device.getAddress()));
                if (isCorrectDevice(device, HBScanRecord.parseFromBytes(scanRecord))) {
                    stopScanning();
                    connect(device);
                }
            }
        }
    };

    /**
     * Callback from each connected device
     */
    private HBDeviceListener deviceListener = new HBDeviceListener() {

        /**
         * Successfully connected with the device, add it to the connected device list
         * and notify the listener.
         * @param hbDevice {@link HBDevice} that connected.
         */
        @Override
        public void connected(HBDevice hbDevice) {
            HBLogger.i(TAG, String.format("Connected device: %s", hbDevice.getAddress()));

            updateScanMode(HBScanMode.IDLE);

            if (connectionListenerHandler != null) {
                connectionListenerHandler.connected(hbDevice.getAddress(), hbDevice.getPrimaryService());
                connectionListenerHandler = null;
            }
        }

        /**
         * The connection with the device failed, remove it from the connected device list
         * and notify the listener.
         * @param hbDevice {@link HBDevice} of which connect failed.
         */
        @Override
        public void connectFailed(HBDevice hbDevice) {
            HBLogger.i(TAG, String.format("Connect failed device: %s", hbDevice.getAddress()));

            removeDisconnectedDevice(hbDevice);
            updateScanMode(HBScanMode.IDLE);

            if (connectionListenerHandler != null) {
                connectionListenerHandler.connectFailed(hbDevice.getAddress(), hbDevice.getPrimaryService());
                connectionListenerHandler = null;
            }
        }

        /**
         * The device disconnected, remove it from the connected devices list
         * and notify the listener.
         * @param hbDevice {@link HBDevice} that disconnected.
         */
        @Override
        public void disconnected(HBDevice hbDevice) {
            HBLogger.i(TAG, String.format("Disconnected device: %s", hbDevice.getAddress()));

            removeDisconnectedDevice(hbDevice);

            HBDeviceConnectionListener listener = getDeviceConnectionListener(hbDevice.getAddress());
            if (listener != null) {
                listener.disconnected(hbDevice.getAddress());
            }
        }

        /**
         * Read or write failed on the device, notify the listener
         * @param hbDevice {@link HBDevice} of which read/write failed.
         */
        @Override
        public void onReadWriteError(HBDevice hbDevice) {
            HBLogger.i(TAG, String.format("Read write error device: %s", hbDevice.getAddress()));
            HBCentral.this.onReadWriteError(hbDevice.getAddress());
        }
    };

    //endregion

    /**
     * Construct a new Harald Central.
     *
     * @param context Android application environment.
     * @param handler Handler to use for callbacks.
     */
    public HBCentral(@NonNull Context context, @NonNull Handler handler) {
        this.context = context;
        this.callBackHandler = handler;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.deviceConnectionListeners = new WeakHashMap<>();
        this.connectedHbDevices = new HashMap<>();
        this.supportedServices = new HashMap<>();
        this.timeoutHandler = new Handler();

        HBLogger.registerLogger(new HBLogger.LogCatLogger());

        initialiseServices();
    }

    /**
     * Get the services supported by HBCentral
     *
     * @return All supported services
     */
    public List<UUID> getSupportedServices() {
        HBLogger.v(TAG, String.format("getSupportedServices"));
        List<UUID> services = new ArrayList<>();
        services.addAll(this.supportedServices.keySet());
        return services;
    }

    //region Device Connection

    /**
     * Add HBDeviceConnectionListener listener to listeners
     *
     * @param listener Listener to add
     */
    public void addDeviceConnectionListener(@NonNull String address, @NonNull HBDeviceConnectionListener listener) {
        HBLogger.v(TAG, String.format("addDeviceConnectionListener address: %s", address));
        HBDeviceConnectionListenerHandler listenerHandler = new HBDeviceConnectionListenerHandler(callBackHandler, listener);
        this.deviceConnectionListeners.put(address, listenerHandler);
    }

    /**
     * Remove HBDeviceConnectionListener listener from listeners based on address
     *
     * @param address Address of device listener to remove
     */
    public void removeDeviceConnectionListener(@NonNull String address) {
        HBLogger.v(TAG, String.format("removeDeviceConnectionListener address: %s", address));
        this.deviceConnectionListeners.remove(address);
    }

    /**
     * Connect with a new device based on the primary service UUID.
     *
     * @param serviceUUID Primary service of the device to connect with.
     */
    public void connect(@NonNull UUID serviceUUID, @NonNull HBConnectionListener listener) {
        HBLogger.v(TAG, String.format("connect serviceUUID: %s", serviceUUID));
        discoverBLEDevices(null, serviceUUID, listener);
    }

    /**
     * Connect with a specific device based on the address and primary service UUID.
     *
     * @param deviceAddress Address of the device to connect with.
     * @param serviceUUID   Primary service of the device to connect with.
     */
    public void connect(@NonNull String deviceAddress, @NonNull UUID serviceUUID, @NonNull HBConnectionListener listener) {
        HBLogger.v(TAG, String.format("connect deviceAddress: %s, serviceUUID: %s", deviceAddress, serviceUUID));
        discoverBLEDevices(deviceAddress, serviceUUID, listener);
    }

    /**
     * Disconnect with the a specific device.
     *
     * @param deviceAddress Address of the device to disconnect from.
     */
    public void disconnect(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("disconnect deviceAddress: %s", deviceAddress));
        HBDevice hbDevice = connectedHbDevices.get(deviceAddress);
        if (hbDevice != null) {
            hbDevice.disconnect();
        }
    }

    /**
     * Get all supported services of a specific device.
     *
     * @param deviceAddress Address of the device.
     * @return List of supported services.
     */
    public List<UUID> getDeviceSupportedServices(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("getDeviceSupportedServices deviceAddress: %s", deviceAddress));
        HBDevice hbDevice = connectedHbDevices.get(deviceAddress);
        if (hbDevice != null) {
            return hbDevice.getServices();
        }
        return new ArrayList();
    }

    /**
     * Get the state of device
     *
     * @param deviceAddress Address of the device.
     * @return Device state
     */
    public HBDevice.State getState(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("getState deviceAddress: %s", deviceAddress));
        HBDevice hbDevice = connectedHbDevices.get(deviceAddress);
        if (hbDevice != null) {
            return hbDevice.getState();
        }
        HBLogger.v(TAG, String.format("State not found, send disconnected deviceAddress: %s", deviceAddress));
        return HBDevice.State.Disconnected;
    }

    //endregion

    //region Health Thermometer Service

    /**
     * Add listener to health thermometer service for device
     *
     * @param deviceAddress Address of the device
     * @param listener      Listener callback
     */
    public void addHealthThermometerServiceListener(@NonNull final String deviceAddress, @NonNull final HBHealthThermometerListener listener) {
        HBLogger.v(TAG, String.format("addHealthThermometerServiceListener deviceAddress: %s", deviceAddress));
        HBServiceHandler handler = supportedServices.get(HBHealthThermometerServiceHandler.SERVICE_UUID);
        HBHealthThermometerListenerHandler listenerHandler = new HBHealthThermometerListenerHandler(callBackHandler, listener);
        handler.addServiceListener(deviceAddress, listenerHandler);
    }

    /**
     * Remove listener from health thermometer service for device
     *
     * @param deviceAddress Address of the device
     */
    public void removeHealthThermometerServiceListener(@NonNull final String deviceAddress) {
        HBLogger.v(TAG, String.format("removeHealthThermometerServiceListener deviceAddress: %s", deviceAddress));
        HBServiceHandler handler = supportedServices.get(HBHealthThermometerServiceHandler.SERVICE_UUID);
        handler.removeServiceListener(deviceAddress);
    }

    /**
     * Get the temperature type of a specific device.
     *
     * @param deviceAddress Address of the device.
     */
    public void getTemperatureType(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("getTemperatureType deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBHealthThermometerServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBHealthThermometerServiceHandler) handler).getTemperatureType(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    /**
     * Get the measurement interval of a specific device.
     *
     * @param deviceAddress Address of the device.
     */
    public void getMeasurementInterval(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("getMeasurementInterval deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBHealthThermometerServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBHealthThermometerServiceHandler) handler).getMeasurementInterval(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    /**
     * Set the measurement interval of a specific device.
     *
     * @param deviceAddress Address of the device.
     */
    public void setMeasurementInterval(@NonNull String deviceAddress, int interval) {
        HBLogger.v(TAG, String.format("setMeasurementInterval deviceAddress: %s, interval: %d", deviceAddress, interval));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBHealthThermometerServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBHealthThermometerServiceHandler) handler).setMeasurementInterval(device, interval);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    //endregion

    //region Current Time Service

    /**
     * Add listener to current time service for device
     *
     * @param deviceAddress Address of the device
     * @param listener      Listener callback
     */
    public void addCurrentTimeServiceListener(@NonNull final String deviceAddress, @NonNull final HBCurrentTimeListener listener) {
        HBLogger.v(TAG, String.format("addCurrentTimeServiceListener deviceAddress: %s", deviceAddress));
        HBServiceHandler handler = supportedServices.get(HBCurrentTimeServiceHandler.SERVICE_UUID);
        HBCurrentTimeListenerHandler listenerHandler = new HBCurrentTimeListenerHandler(callBackHandler, listener);
        handler.addServiceListener(deviceAddress, listenerHandler);
    }

    /**
     * Remove listener from current time service for device
     *
     * @param deviceAddress Address of the device
     */
    public void removeCurrentTimeServiceListener(@NonNull final String deviceAddress) {
        HBLogger.v(TAG, String.format("removeCurrentTimeServiceListener deviceAddress: %s", deviceAddress));
        HBServiceHandler handler = supportedServices.get(HBCurrentTimeServiceHandler.SERVICE_UUID);
        handler.removeServiceListener(deviceAddress);
    }

    /**
     * Get the current time of a specific device
     *
     * @param deviceAddress Address of the device.
     */
    public void getCurrentTime(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("getCurrentTime deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBCurrentTimeServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBCurrentTimeServiceHandler) handler).getCurrentTime(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    /**
     * Set the current time of a specific device
     *
     * @param deviceAddress Address of the device.
     * @param currentTime   Current time to set
     */
    public void setCurrentTime(@NonNull String deviceAddress, @NonNull HBCurrentTime currentTime) {
        HBLogger.v(TAG, String.format("setCurrentTime deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBCurrentTimeServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBCurrentTimeServiceHandler) handler).setCurrentTime(device, currentTime);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    /**
     * Get the local time information of a specific device
     *
     * @param deviceAddress Address of the device.
     */
    public void getLocalTimeInformation(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("getLocalTimeInformation deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBCurrentTimeServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBCurrentTimeServiceHandler) handler).getLocalTimeInformation(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    /**
     * Get the local time information of a specific device
     *
     * @param deviceAddress        Address of the device.
     * @param localTimeInformation Local time information to set
     */
    public void setLocalTimeInformation(@NonNull String deviceAddress, @NonNull HBLocalTimeInformation localTimeInformation) {
        HBLogger.v(TAG, String.format("setLocalTimeInformation deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBCurrentTimeServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBCurrentTimeServiceHandler) handler).setLocalTimeInformation(device, localTimeInformation);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    /**
     * Get the reference time information of a specific device
     *
     * @param deviceAddress Address of the device.
     */
    public void getReferenceTimeInformation(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("getReferenceTimeInformation deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBCurrentTimeServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBCurrentTimeServiceHandler) handler).getReferenceTimeInformation(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    //endregion

    //region Nordic DFU Service

    /**
     * Add listener to nordic DFU service for device
     *
     * @param deviceAddress Address of the device
     * @param listener      Listener callback
     */
    public void addNordicDFUServiceListener(@NonNull final String deviceAddress, @NonNull final HBNordicDFUListener listener) {
        HBLogger.v(TAG, String.format("addNordicDFUServiceListener deviceAddress: %s", deviceAddress));
        HBServiceHandler handler = supportedServices.get(HBNordicDFUServiceHandler.SERVICE_UUID);
        HBNordicDFUListenerHandler listenerHandler = new HBNordicDFUListenerHandler(callBackHandler, listener);
        handler.addServiceListener(deviceAddress, listenerHandler);
    }

    /**
     * Remove listener from nordic DFU service for device
     *
     * @param deviceAddress Address of the device
     */
    public void removeNordicDFUServiceListener(@NonNull final String deviceAddress) {
        HBLogger.v(TAG, String.format("removeNordicDFUServiceListener deviceAddress: %s", deviceAddress));
        HBServiceHandler handler = supportedServices.get(HBNordicDFUServiceHandler.SERVICE_UUID);
        handler.removeServiceListener(deviceAddress);
    }

    /**
     * Start the OTA on the specified device with specific OTA service type
     *
     * @param deviceAddress Address of the device to update
     * @param file          Firmware update file
     */
    public void startNordicDFU(@NonNull String deviceAddress, @NonNull File file) {
        HBLogger.v(TAG, String.format("startNordicDFU deviceAddress: %s, file: %s", deviceAddress, file.getPath()));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBNordicDFUServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBNordicDFUServiceHandler) handler).startDFU(device, file);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    //endregion

    //region Battery Level Service

    /**
     * Add listener to battery service for device
     *
     * @param deviceAddress Address of the device
     * @param listener      Listener callback
     */
    public void addBatteryServiceListener(@NonNull final String deviceAddress, @NonNull final HBBatteryListener listener) {
        HBLogger.v(TAG, String.format("addBatteryServiceListener deviceAddress: %s", deviceAddress));
        HBServiceHandler handler = supportedServices.get(HBBatteryServiceHandler.SERVICE_UUID);
        HBBatteryListenerHandler listenerHandler = new HBBatteryListenerHandler(callBackHandler, listener);
        handler.addServiceListener(deviceAddress, listenerHandler);
    }

    /**
     * Remove listener from battery service for device
     *
     * @param deviceAddress Address of the device
     */
    public void removeBatteryServiceListener(@NonNull final String deviceAddress) {
        HBLogger.v(TAG, String.format("removeBatteryServiceListener deviceAddress: %s", deviceAddress));
        HBServiceHandler handler = supportedServices.get(HBBatteryServiceHandler.SERVICE_UUID);
        handler.removeServiceListener(deviceAddress);
    }

    /**
     * Get the battery level of a specific device
     *
     * @param deviceAddress Address of the device.
     */
    public void getBatteryLevel(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("getBatteryLevel deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBBatteryServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBBatteryServiceHandler) handler).getBatteryLevel(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    //endregion

    //region Device Information Service

    /**
     * Add listener to device information service for device
     *
     * @param deviceAddress Address of the device
     * @param listener      Listener callback
     */
    public void addDeviceInformationServiceListener(@NonNull final String deviceAddress, @NonNull final HBDeviceInformationListener listener) {
        HBLogger.v(TAG, String.format("addDeviceInformationServiceListener deviceAddress: %s", deviceAddress));
        HBServiceHandler handler = supportedServices.get(HBDeviceInformationServiceHandler.SERVICE_UUID);
        HBDeviceInformationListenerHandler listenerHandler = new HBDeviceInformationListenerHandler(callBackHandler, listener);
        handler.addServiceListener(deviceAddress, listenerHandler);
    }

    /**
     * Remove listener from device information service for device
     *
     * @param deviceAddress Address of the device
     */
    public void removeDeviceInformationServiceListener(@NonNull final String deviceAddress) {
        HBLogger.v(TAG, String.format("removeDeviceInformationServiceListener deviceAddress: %s", deviceAddress));
        HBServiceHandler handler = supportedServices.get(HBDeviceInformationServiceHandler.SERVICE_UUID);
        handler.removeServiceListener(deviceAddress);
    }

    /**
     * Get the manufacturer name of a specific device
     *
     * @param deviceAddress Address of the device.
     */
    public void getManufacturerName(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("getManufacturerName deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBDeviceInformationServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBDeviceInformationServiceHandler) handler).getManufacturerName(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    /**
     * Get the model number of a specific device
     *
     * @param deviceAddress Address of the device.
     */
    public void getModelNumber(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("getModelNumber deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBDeviceInformationServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBDeviceInformationServiceHandler) handler).getModelNumber(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    /**
     * Get the serial number of a specific device
     *
     * @param deviceAddress Address of the device.
     */
    public void getSerialNumber(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("getSerialNumber deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBDeviceInformationServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBDeviceInformationServiceHandler) handler).getSerialNumber(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    /**
     * Get the hardware revision of a specific device
     *
     * @param deviceAddress Address of the device.
     */
    public void getHardwareRevision(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("getHardwareRevision deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBDeviceInformationServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBDeviceInformationServiceHandler) handler).getHardwareRevision(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    /**
     * Get the firmware revision of a specific device
     *
     * @param deviceAddress Address of the device.
     */
    public void getFirmwareRevision(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("getFirmwareRevision deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBDeviceInformationServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBDeviceInformationServiceHandler) handler).getFirmwareRevision(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    /**
     * Get the software revision of a specific device
     *
     * @param deviceAddress Address of the device.
     */
    public void getSoftwareRevision(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("getSoftwareRevision deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBDeviceInformationServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBDeviceInformationServiceHandler) handler).getSoftwareRevision(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    /**
     * Get the system ID of a specific device
     *
     * @param deviceAddress Address of the device.
     */
    public void getSystemID(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("getSystemID deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBDeviceInformationServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBDeviceInformationServiceHandler) handler).getSystemID(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    /**
     * Get the regulatory certification data list of a specific device
     *
     * @param deviceAddress Address of the device.
     */
    public void getRegulatoryCertificationDataList(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("getRegulatoryCertificationDataList deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBDeviceInformationServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBDeviceInformationServiceHandler) handler).getRegulatoryCertificationDataList(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    /**
     * Get the PnP ID of a specific device
     *
     * @param deviceAddress Address of the device.
     */
    public void getPnPID(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("getPnPID deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBDeviceInformationServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBDeviceInformationServiceHandler) handler).getPnPID(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    //endregion

    //region Weight Scale Service

    /**
     * Add listener to weight scale service for device
     *
     * @param deviceAddress Address of the device
     * @param listener      Listener callback
     */
    public void addWeightScaleServiceListener(@NonNull final String deviceAddress, @NonNull final HBWeightScaleListener listener) {
        HBLogger.v(TAG, String.format("addWeightScaleServiceListener deviceAddress: %s", deviceAddress));
        HBServiceHandler handler = supportedServices.get(HBWeightScaleServiceHandler.SERVICE_UUID);
        HBWeightScaleListenerHandler listenerHandler = new HBWeightScaleListenerHandler(callBackHandler, listener);
        handler.addServiceListener(deviceAddress, listenerHandler);
    }

    /**
     * Remove listener from weight scale service for device
     *
     * @param deviceAddress Address of the device
     */
    public void removeWeightScaleServiceListener(@NonNull final String deviceAddress) {
        HBLogger.v(TAG, String.format("removeWeightScaleServiceListener deviceAddress: %s", deviceAddress));
        HBServiceHandler handler = supportedServices.get(HBWeightScaleServiceHandler.SERVICE_UUID);
        handler.removeServiceListener(deviceAddress);
    }

    /**
     * Get the weight scale feature of a specific device
     */
    public void getWeightScaleFeature(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("getWeightScaleFeature deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBWeightScaleServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBWeightScaleServiceHandler) handler).getWeightScaleFeature(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    //endregion

    //region User Data Service

    /**
     * Add listener to user data service for device
     *
     * @param deviceAddress Address of the device
     * @param listener      Listener callback
     */
    public void addUserDataServiceListener(@NonNull final String deviceAddress, @NonNull final HBUserDataListener listener) {
        HBLogger.v(TAG, String.format("addUserDataServiceListener deviceAddress: %s", deviceAddress));
        HBServiceHandler handler = supportedServices.get(HBUserDataServiceHandler.SERVICE_UUID);
        HBUserDataListenerHandler listenerHandler = new HBUserDataListenerHandler(callBackHandler, listener);
        handler.addServiceListener(deviceAddress, listenerHandler);
    }

    /**
     * Remove listener from user data service for device
     *
     * @param deviceAddress Address of the device
     */
    public void removeUserDataServiceListener(@NonNull final String deviceAddress) {
        HBLogger.v(TAG, String.format("removeUserDataServiceListener deviceAddress: %s", deviceAddress));
        HBServiceHandler handler = supportedServices.get(HBUserDataServiceHandler.SERVICE_UUID);
        handler.removeServiceListener(deviceAddress);
    }

    /**
     * Register new user with a consent code on device
     *
     * @param deviceAddress Address of the device
     * @param consentCode   Consent code of user
     */
    public void registerUser(@NonNull String deviceAddress, int consentCode) {
        HBLogger.v(TAG, String.format("registerUser deviceAddress: %s, consentCode: %d", deviceAddress, consentCode));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBUserDataServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBUserDataServiceHandler) handler).registerUser(device, consentCode);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    /**
     * Set the current user with index and consent code
     *
     * @param deviceAddress Address of the device
     * @param userIndex     Index of the user
     * @param consentCode   Consent code of user
     */
    public void setUser(@NonNull String deviceAddress, int userIndex, int consentCode) {
        HBLogger.v(TAG, String.format("setUser deviceAddress: %s, userIndex: %d, consentCodeL %d", deviceAddress, userIndex, consentCode));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBUserDataServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBUserDataServiceHandler) handler).setUser(device, userIndex, consentCode);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    /**
     * Delete the current user
     *
     * @param deviceAddress Address of the device
     */
    public void deleteCurrentUser(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("deleteCurrentUser deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBUserDataServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBUserDataServiceHandler) handler).deleteCurrentUser(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    /**
     * Get user data of a specific type
     *
     * @param deviceAddress Address of the device
     * @param type          Type to request
     */
    public void getUserData(@NonNull String deviceAddress, HBUserDataType type) {
        HBLogger.v(TAG, String.format("getUserData deviceAddress: %s, type: %s", deviceAddress, type.toString()));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBUserDataServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBUserDataServiceHandler) handler).getUserData(device, type);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    /**
     * Write user data to device
     *
     * @param deviceAddress Address of the device
     * @param data          Data to write
     */
    public void setUserData(@NonNull String deviceAddress, HBUserData data) {
        HBLogger.v(TAG, String.format("setUserData deviceAddress: %s, type: %s", deviceAddress, data.getUserDataType().toString()));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBUserDataServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBUserDataServiceHandler) handler).setUserData(device, data);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    /**
     * Get all users from the device
     *
     * @param deviceAddress Address of the device
     */
    public void getAllUsersUDS(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("getAllUsers deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBUserDataServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBUserDataServiceHandler) handler).getAllUsers(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    /**
     * Delete user with index from device
     *
     * @param deviceAddress Address of the device
     * @param userIndex     Index of the user
     */
    public void deleteUserUDS(@NonNull String deviceAddress, @NonNull int userIndex) {
        HBLogger.v(TAG, String.format("deleteUser deviceAddress: %s, userIndex: %d", deviceAddress, userIndex));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBUserDataServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBUserDataServiceHandler) handler).deleteUser(device, userIndex);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    /**
     * Delete all users from the device
     *
     * @param deviceAddress Address of the device
     */
    public void deleteAllUsersUDS(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("deleteAllUsers deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBUserDataServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBUserDataServiceHandler) handler).deleteAllUsers(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    //endregion

    //region Philips Extension Service

    /**
     * Add listener to philips extension service for device
     *
     * @param deviceAddress Address of the device
     * @param listener      Listener callback
     */
    public void addPhilipsExtensionListener(@NonNull final String deviceAddress, @NonNull final HBPhilipsExtensionListener listener) {
        HBLogger.v(TAG, String.format("addPhilipsExtensionListener deviceAddress: %s", deviceAddress));
        HBServiceHandler handler = supportedServices.get(HBPhilipsExtensionServiceHandler.SERVICE_UUID);
        HBPhilipsExtensionListenerHandler listenerHandler = new HBPhilipsExtensionListenerHandler(callBackHandler, listener);
            handler.addServiceListener(deviceAddress, listenerHandler);
    }

    /**
     * Remove listener from philips extension service for device
     *
     * @param deviceAddress Address of the device
     */
    public void removePhilipsExtensionListener(@NonNull final String deviceAddress) {
        HBLogger.v(TAG, String.format("removePhilipsExtensionListener deviceAddress: %s", deviceAddress));
        HBServiceHandler handler = supportedServices.get(HBPhilipsExtensionServiceHandler.SERVICE_UUID);
        handler.removeServiceListener(deviceAddress);
    }

    /**
     * Get all users from the device
     *
     * @param deviceAddress Address of the device
     */
    public void getAllUsers(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("getAllUsers deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBPhilipsExtensionServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBPhilipsExtensionServiceHandler) handler).getAllUsers(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    /**
     * Delete user with index from device
     *
     * @param deviceAddress Address of the device
     * @param userIndex     Index of the user
     */
    public void deleteUser(@NonNull String deviceAddress, @NonNull int userIndex) {
        HBLogger.v(TAG, String.format("deleteUser deviceAddress: %s, userIndex: %d", deviceAddress, userIndex));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBPhilipsExtensionServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBPhilipsExtensionServiceHandler) handler).deleteUser(device, userIndex);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    /**
     * Delete all users from the device
     *
     * @param deviceAddress Address of the device
     */
    public void deleteAllUsers(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("deleteAllUsers deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBPhilipsExtensionServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBPhilipsExtensionServiceHandler) handler).deleteAllUsers(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    /**
     * Get extended user data of a specific type
     *
     * @param deviceAddress Address of the device
     * @param type          Type to request
     */
    public void getExtendedUserData(@NonNull String deviceAddress, HBExtendedUserDataType type) {
        HBLogger.v(TAG, String.format("getExtendedUserData deviceAddress: %s, type: %s", deviceAddress, type.toString()));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBPhilipsExtensionServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBPhilipsExtensionServiceHandler) handler).getExtendedUserData(device, type);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    /**
     * Write extended user data to device
     *
     * @param deviceAddress Address of the device
     * @param data          Data to write
     */
    public void setExtendedUserData(@NonNull String deviceAddress, HBExtendedUserData data) {
        HBLogger.v(TAG, String.format("setExtendedUserData deviceAddress: %s, type: %s", deviceAddress, data.getExtendedUserDataType().toString()));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBPhilipsExtensionServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBPhilipsExtensionServiceHandler) handler).setExtendedUserData(device, data);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    //endregion

    //region Heart Rate Service

    public void addHeartRateServiceListener(@NonNull final String deviceAddress, @NonNull final HBHeartRateListener listener) {
        HBLogger.v(TAG, String.format("addHeartRateServiceListener deviceAddress: %s", deviceAddress));
        HBServiceHandler handler = supportedServices.get(HBHeartRateServiceHandler.SERVICE_UUID);
        HBHeartRateListenerHandler listenerHandler = new HBHeartRateListenerHandler(callBackHandler, listener);
        handler.addServiceListener(deviceAddress, listenerHandler);
    }

    public void removeHeartRateServiceListener(@NonNull final String deviceAddress) {
        HBLogger.v(TAG, String.format("removeHeartRateServiceListener deviceAddress: %s", deviceAddress));
        HBServiceHandler handler = supportedServices.get(HBHeartRateServiceHandler.SERVICE_UUID);
        handler.removeServiceListener(deviceAddress);
    }

    public void getBodySensorLocation(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("getBodySensorLocation deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBHeartRateServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBHeartRateServiceHandler) handler).getBodySensorLocation(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    public void resetEnergyExpended(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("resetEnergyExpended deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBHeartRateServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBHeartRateServiceHandler) handler).resetEnergyExpended(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    //endregion

    //region Blood Pressure Service

    public void addBloodPressureServiceListener(@NonNull final String deviceAddress, @NonNull final HBBloodPressureListener listener) {
        HBLogger.v(TAG, String.format("addBloodPressureServiceListener deviceAddress: %s", deviceAddress));
        HBServiceHandler handler = supportedServices.get(HBBloodPressureServiceHandler.SERVICE_UUID);
        HBBloodPressureListenerHandler listenerHandler = new HBBloodPressureListenerHandler(callBackHandler, listener);
        handler.addServiceListener(deviceAddress, listenerHandler);
    }

    public void removeBloodPressureServiceListener(@NonNull final String deviceAddress) {
        HBLogger.v(TAG, String.format("removeBloodPressureServiceListener deviceAddress: %s", deviceAddress));
        HBServiceHandler handler = supportedServices.get(HBBloodPressureServiceHandler.SERVICE_UUID);
        handler.removeServiceListener(deviceAddress);
    }

    public void getBloodPressureFeature(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("getBloodPressureFeature deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBBloodPressureServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBBloodPressureServiceHandler) handler).getBloodPressureFeature(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    public void writeToBlsRACP(@NonNull String deviceAddress, HBRecordProcedure filterProcedure) {
        //HBLogger.v(TAG, String.format("setUser deviceAddress: %s, userIndex: %d, consentCodeL %d", deviceAddress, userIndex, consentCode));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBBloodPressureServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBBloodPressureServiceHandler) handler).writeToBlsRACP(device, filterProcedure);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    //endregion

    //region Physical Activity Monitor Service

    public void addPhysicalActivityMonitorServiceListener(@NonNull final String deviceAddress, @NonNull final HBPhysicalActivityMonitorListener listener) {
        HBLogger.v(TAG, String.format("addPhysicalActivityMonitorServiceListener deviceAddress: %s", deviceAddress));
        HBServiceHandler handler = supportedServices.get(HBPhysicalActivityMonitorServiceHandler.SERVICE_UUID);
        HBPhysicalActivityMonitorListenerHandler listenerHandler = new HBPhysicalActivityMonitorListenerHandler(callBackHandler, listener);
        handler.addServiceListener(deviceAddress, listenerHandler);
    }

    public void removePhysicalActivityMonitorServiceListener(@NonNull final String deviceAddress) {
        HBLogger.v(TAG, String.format("removePhysicalActivityMonitorServiceListener deviceAddress: %s", deviceAddress));
        HBServiceHandler handler = supportedServices.get(HBPhysicalActivityMonitorServiceHandler.SERVICE_UUID);
        handler.removeServiceListener(deviceAddress);
    }

    public void getPhysicalActivityMonitorFeature(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("getPhysicalActivityMonitorFeature deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBPhysicalActivityMonitorServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBPhysicalActivityMonitorServiceHandler) handler).getPamFeature(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    public void writeToPamCP(@NonNull String deviceAddress, HBPhysicalActivityMonitorControlPoint filterProcedure) {
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBPhysicalActivityMonitorServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBPhysicalActivityMonitorServiceHandler) handler).writeToPamCP(device, filterProcedure);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    //endregion

    //region Device Time Service

    public void addDeviceTimeServiceListener(@NonNull final String deviceAddress, @NonNull final HBDeviceTimeListener listener) {
        HBLogger.v(TAG, String.format("addDeviceTimeServiceListener deviceAddress: %s", deviceAddress));
        HBServiceHandler handler = supportedServices.get(HBDeviceTimeServiceHandler.SERVICE_UUID);
        HBDeviceTimeListenerHandler listenerHandler = new HBDeviceTimeListenerHandler(callBackHandler, listener);
        handler.addServiceListener(deviceAddress, listenerHandler);
    }

    public void removeDeviceTimeServiceListener(@NonNull final String deviceAddress) {
        HBLogger.v(TAG, String.format("removeDeviceTimeServiceListener deviceAddress: %s", deviceAddress));
        HBServiceHandler handler = supportedServices.get(HBDeviceTimeServiceHandler.SERVICE_UUID);
        handler.removeServiceListener(deviceAddress);
    }

    public void getDeviceTimeFeature(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("getDeviceTimeFeature deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBDeviceTimeServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBDeviceTimeServiceHandler) handler).getDeviceTimeFeature(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    public void getDeviceTimeParameter(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("getDeviceTimeFeature deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBDeviceTimeServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBDeviceTimeServiceHandler) handler).getDeviceTimeParameter(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    public void getDeviceTimeCharacteristic(@NonNull String deviceAddress) {
        HBLogger.v(TAG, String.format("getDeviceTimeCharacteristic deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBDeviceTimeServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBDeviceTimeServiceHandler) handler).getDeviceTimeCharacteristic(device);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    /**
     * Propose a time update from the client
     *
     * @param deviceAddress Address of the device
     * @param operand   Object containing the proposed time update
     */
    public void proposeTimeUpdate(@NonNull String deviceAddress, HBTimeUpdateOperand operand) {
        HBLogger.v(TAG, String.format("proposeTimeUpdate deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBDeviceTimeServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBDeviceTimeServiceHandler) handler).proposeTimeUpdate(device, operand);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    /**
     * Propose a time update from the client
     *
     * @param deviceAddress Address of the device
     * @param operand   Object containing the proposed time update
     */
    public void forceTimeUpdate(@NonNull String deviceAddress, HBTimeUpdateOperand operand) {
        HBLogger.v(TAG, String.format("forceTimeUpdate deviceAddress: %s", deviceAddress));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBDeviceTimeServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBDeviceTimeServiceHandler) handler).forceTimeUpdate(device, operand);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    public void writeToDtsRACP(@NonNull String deviceAddress, HBRecordProcedure filterProcedure) {
        //HBLogger.v(TAG, String.format("setUser deviceAddress: %s, userIndex: %d, consentCodeL %d", deviceAddress, userIndex, consentCode));
        HBDevice device = connectedHbDevices.get(deviceAddress);
        HBServiceHandler handler = getServiceHandler(device, HBDeviceTimeServiceHandler.SERVICE_UUID);
        if (handler != null) {
            ((HBDeviceTimeServiceHandler) handler).writeToDtsRACP(device, filterProcedure);
        } else {
            onReadWriteError(deviceAddress);
        }
    }

    // end region

    //region Private methods

    /**
     * Start device discovery on a specific primary service UUID
     *
     * @param serviceUUID Service to scan for
     */
    private void discoverBLEDevices(String deviceAddress, UUID serviceUUID, HBConnectionListener listener) {
        HBLogger.v(TAG, String.format("discoverBLEDevices address: %s", deviceAddress));

        if (!this.bluetoothAdapter.isEnabled()) {
            HBLogger.i(TAG, String.format("Discovery failed, BLE disabled"));
            listener.connectFailed(deviceAddress, serviceUUID);
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            HBLogger.i(TAG, String.format("Discovery failed, no location permission"));
            listener.connectFailed(deviceAddress, serviceUUID);
            return;
        }

        if (this.scanMode != HBScanMode.IDLE) {
            HBLogger.i(TAG, String.format("Scanner mode not IDLE, scanMode: %s", this.scanMode));
            listener.connectFailed(deviceAddress, serviceUUID);
            return;
        }

        updateScanMode(HBScanMode.SCANNING);

        connectionListenerHandler = new HBConnectionListenerHandler(callBackHandler, listener);
        connectDeviceAddress = deviceAddress;
        connectPrimaryServiceUUID = serviceUUID;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bluetoothScanner = bluetoothAdapter.getBluetoothLeScanner();
            if (bluetoothScanner != null) {
                ScanSettings settings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();

                ScanFilter filter = new ScanFilter.Builder()
                        .setServiceUuid(new ParcelUuid(serviceUUID))
                        .build();
                List<ScanFilter> filters = new ArrayList();
                filters.add(filter);

                HBLogger.v(TAG, String.format("discoverBLEDevices startScan"));
                bluetoothScanner.startScan(filters, settings, scanCallback);
                return;
            }
        } else {
            HBLogger.v(TAG, String.format("discoverBLEDevices startLeScan"));
            bluetoothAdapter.startLeScan(new UUID[]{serviceUUID}, leScanCallback);
            return;
        }

        HBLogger.i(TAG, String.format("Start scanning failed"));

        updateScanMode(HBScanMode.IDLE);
        listener.connectFailed(deviceAddress, serviceUUID);
    }

    /**
     * Set scan timeout timer, timeout time is {@code CONNECT_TIMEOUT}.
     * If timeout is executed the scan is stopped and connect failed.
     */
    private void setTimeoutTimer(final UUID serviceUUID) {
        HBLogger.v(TAG, String.format("setTimeoutTimer"));
        if (timeoutRunnable != null) {
            timeoutHandler.removeCallbacks(timeoutRunnable);
        }

        this.timeoutRunnable = new Runnable() {
            @Override
            public void run() {
                stopScanning();
                updateScanMode(HBScanMode.IDLE);
                if (connectionListenerHandler != null) {
                    connectionListenerHandler.connectFailed(connectDeviceAddress, serviceUUID);
                    connectionListenerHandler = null;
                }
                timeoutRunnable = null;
            }
        };

        timeoutHandler.postDelayed(timeoutRunnable, CONNECT_TIMEOUT);
    }

    /**
     * Cancel the scan timeout timer
     */
    private void cancelTimeoutTimer() {
        HBLogger.v(TAG, String.format("cancelTimeoutTimer"));
        if (timeoutRunnable != null) {
            timeoutHandler.removeCallbacks(timeoutRunnable);
            timeoutRunnable = null;
        }
    }

    /**
     * Stop with device discovery on a specific primary service
     */
    private void stopScanning() {
        HBLogger.i(TAG, String.format("stopScanning"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bluetoothScanner = bluetoothAdapter.getBluetoothLeScanner();
            if (bluetoothScanner != null) {
                bluetoothScanner.stopScan(scanCallback);
            }
        } else {
            bluetoothAdapter.stopLeScan(leScanCallback);
        }
    }

    /**
     * Connect with the discovered device.
     * If the deviceAddress is set, the found device address must be equal.
     *
     * @param device Discovered device
     * @return Connect successful
     */
    private boolean isCorrectDevice(BluetoothDevice device, HBScanRecord scanRecord) {
        if (this.scanMode != HBScanMode.SCANNING) {
            HBLogger.v(TAG, String.format("isCorrectDevice not scanning, address %s", device.getAddress()));
            return false;
        }

        if (connectDeviceAddress != null && !device.getAddress().equals(connectDeviceAddress)) {
            HBLogger.v(TAG, String.format("isCorrectDevice wrong address, address %s, requested: %s", device.getAddress(), connectDeviceAddress));
            return false;
        }

        if (connectionListenerHandler != null) {
            if (!connectionListenerHandler.discoveredDeviceIsCorrect(connectDeviceAddress, connectPrimaryServiceUUID, scanRecord)) {
                HBLogger.v(TAG, String.format("isCorrectDevice rejected by handler, address %s", device.getAddress()));
                return false;
            }
        } else {
            HBLogger.v(TAG, String.format("isCorrectDevice handler null, address %s", device.getAddress()));
            return false;
        }

        HBLogger.i(TAG, String.format("Found corrent device with address %s", device.getAddress()));

        return true;
    }

    /**
     * Connect with the found device
     * If the device is already connected, then don't create a new HBDevice
     *
     * @param device BLE device to connect with
     */
    private void connect(BluetoothDevice device) {
        HBLogger.i(TAG, String.format("Connect with %s", device.getAddress()));
        connectDeviceAddress = null;

        if (!connectedHbDevices.containsKey(device.getAddress())) {
            updateScanMode(HBScanMode.CONNECTING);

            HBDevice hbDevice = new HBDevice(context, device, connectPrimaryServiceUUID, Collections.unmodifiableMap(supportedServices), deviceListener);
            connectedHbDevices.put(hbDevice.getAddress(), hbDevice);
            hbDevice.connect(true);
        } else {
            HBLogger.v(TAG, String.format("Already connected with %s", device.getAddress()));
            updateScanMode(HBScanMode.IDLE);

            if (connectionListenerHandler != null) {
                connectionListenerHandler.connected(device.getAddress(), connectPrimaryServiceUUID);
                connectionListenerHandler = null;
            }
        }
    }

    /**
     * Update the scanmode of central and set timers
     *
     * @param scanMode New scanmode of central
     */
    private void updateScanMode(HBScanMode scanMode) {
        this.scanMode = scanMode;
        HBLogger.i(TAG, String.format("updateScanMode scanMode: %s", scanMode));

        switch (scanMode) {
            case IDLE:
                cancelTimeoutTimer();
                break;
            case SCANNING:
                setTimeoutTimer(connectPrimaryServiceUUID);
                break;
            case CONNECTING:
                setTimeoutTimer(connectPrimaryServiceUUID);
                break;
        }
    }

    /**
     * Create the map of supported services by HaraldLib
     */
    private void initialiseServices() {
        this.supportedServices.put(HBHealthThermometerServiceHandler.SERVICE_UUID, new HBHealthThermometerServiceHandler(context));
        this.supportedServices.put(HBCurrentTimeServiceHandler.SERVICE_UUID, new HBCurrentTimeServiceHandler(context));
        this.supportedServices.put(HBNordicDFUServiceHandler.SERVICE_UUID, new HBNordicDFUServiceHandler(context));
        this.supportedServices.put(HBBatteryServiceHandler.SERVICE_UUID, new HBBatteryServiceHandler(context));
        this.supportedServices.put(HBDeviceInformationServiceHandler.SERVICE_UUID, new HBDeviceInformationServiceHandler(context));
        this.supportedServices.put(HBWeightScaleServiceHandler.SERVICE_UUID, new HBWeightScaleServiceHandler(context));
        this.supportedServices.put(HBUserDataServiceHandler.SERVICE_UUID, new HBUserDataServiceHandler(context));
        this.supportedServices.put(HBPhilipsExtensionServiceHandler.SERVICE_UUID, new HBPhilipsExtensionServiceHandler(context));
        this.supportedServices.put(HBHeartRateServiceHandler.SERVICE_UUID, new HBHeartRateServiceHandler(context));
        this.supportedServices.put(HBBloodPressureServiceHandler.SERVICE_UUID, new HBBloodPressureServiceHandler(context));
        this.supportedServices.put(HBPhysicalActivityMonitorServiceHandler.SERVICE_UUID, new HBPhysicalActivityMonitorServiceHandler(context));
        this.supportedServices.put(HBDeviceTimeServiceHandler.SERVICE_UUID, new HBDeviceTimeServiceHandler(context));
    }

    /**
     * Get the service handler based on the device and supported services
     * If device supports the given service UUID then return the handler
     *
     * @param device      Device that must support the service
     * @param serviceUUID Service for which the handler is requested
     * @return Service handler
     */
    private HBServiceHandler getServiceHandler(HBDevice device, UUID serviceUUID) {
        if (device != null && device.supportsService(serviceUUID)) {
            HBServiceHandler handler = supportedServices.get(serviceUUID);
            return handler;
        }
        return null;
    }

    private void removeDisconnectedDevice(HBDevice hbDevice) {
        connectedHbDevices.remove(hbDevice.getAddress());
    }

    /**
     * Throw read write error to listener on address
     *
     * @param deviceAddress Address where the read write occured
     */
    private void onReadWriteError(String deviceAddress) {
        HBLogger.i(TAG, String.format("onReadWriteError deviceAddress: %s", deviceAddress));
        HBDeviceConnectionListener listener = getDeviceConnectionListener(deviceAddress);
        if (listener != null) {
            listener.onReadWriteError(deviceAddress);
        }
    }

    /**
     * Get the device connection listener based on the device address
     *
     * @param address Device address to find listener for
     * @return device connection listener, null if not found
     */
    private HBDeviceConnectionListener getDeviceConnectionListener(String address) {
        return this.deviceConnectionListeners.get(address);
    }

    //endregion
}
