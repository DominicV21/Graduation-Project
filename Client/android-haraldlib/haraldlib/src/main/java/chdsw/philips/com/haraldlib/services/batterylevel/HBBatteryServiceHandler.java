/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.batterylevel;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import java.util.UUID;

import chdsw.philips.com.haraldlib.HBDevice;
import chdsw.philips.com.haraldlib.HBLogger;
import chdsw.philips.com.haraldlib.HBReadCommand;
import chdsw.philips.com.haraldlib.services.HBServiceHandler;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;

public class HBBatteryServiceHandler extends HBServiceHandler<HBBatteryListenerHandler> {
    public static final String TAG = "HBBatteryServiceHandler";
    public static final UUID SERVICE_UUID =
            UUID.fromString("0000180F-0000-1000-8000-00805f9b34fb");
    private static final UUID BATTERY_LEVEL_CHARACTERISTIC_UUID =
            UUID.fromString("00002A19-0000-1000-8000-00805f9b34fb");

    /**
     * Construct new current time service handler.
     *
     * @param context Application context.
     */
    public HBBatteryServiceHandler(Context context) {
        super(context);
    }

    /**
     * Process characteristic value update in service handler.
     *
     * @param deviceAddress  Device address which received characteristic value update.
     * @param characteristic Characteristic that received value update.
     */
    @Override
    public void characteristicValueUpdated(String deviceAddress,
                                           BluetoothGattCharacteristic characteristic) {
        UUID characteristicUUID = characteristic.getUuid();
        HBLogger.v(TAG, String.format("characteristicValueUpdated device: %s, characteristic: %s", deviceAddress, characteristic.getUuid()));

        if (characteristicUUID.equals(BATTERY_LEVEL_CHARACTERISTIC_UUID)) {
            getBatteryLevelValue(deviceAddress, characteristic);
        }
    }

    /**
     * Process characteristic error response in service handler.
     *
     * @param deviceAddress  Device address which received characteristic value update.
     * @param characteristic Characteristic that received value update.
     * @param status         Value of the error response
     */
    public void characteristicErrorResponse(String deviceAddress,
                                                     BluetoothGattCharacteristic characteristic, int status) {
        HBLogger.v(TAG, String.format("characteristicErrorResponse device: %s, characteristic: %s, status: %d", deviceAddress, characteristic.getUuid(), status));
    }

    /**
     * Get the battery level from the bluetooth device
     *
     * @param device Device to execute characteristic read on
     */
    public void getBatteryLevel(HBDevice device) {
        HBLogger.v(TAG, String.format("getBatteryLevel device: %s", device.getAddress()));
        device.readCharacteristic(new HBReadCommand(SERVICE_UUID,
                BATTERY_LEVEL_CHARACTERISTIC_UUID));
    }

    /**
     * Get the battery level value from the characteristic.
     *
     * @param deviceAddress  Address of the device that the value is received from
     * @param characteristic Characteristic containing the value
     */
    private void getBatteryLevelValue(String deviceAddress,
                                      BluetoothGattCharacteristic characteristic) {
        int batteryLevel = characteristic.getIntValue(FORMAT_UINT8, 0);
        HBLogger.v(TAG, String.format("getBatteryLevelValue device: %s, batteryLevel: %d", deviceAddress, batteryLevel));

        HBBatteryListener listener = getServiceListener(deviceAddress);
        if (listener != null)
            listener.onBatteryLevel(deviceAddress, batteryLevel);
    }
}
