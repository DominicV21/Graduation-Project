/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.heartrate;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import java.util.UUID;

import chdsw.philips.com.haraldlib.HBDevice;
import chdsw.philips.com.haraldlib.HBLogger;
import chdsw.philips.com.haraldlib.HBReadCommand;
import chdsw.philips.com.haraldlib.HBWriteCommand;
import chdsw.philips.com.haraldlib.services.HBServiceHandler;
import chdsw.philips.com.haraldlib.services.heartrate.object.HBBodySensorLocation;
import chdsw.philips.com.haraldlib.services.heartrate.object.HBHeartRateMeasurement;
import chdsw.philips.com.haraldlib.services.heartrate.object.HBSensorContactFeature;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT16;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;
import static chdsw.philips.com.haraldlib.services.HBDataParser.createValue;
import static chdsw.philips.com.haraldlib.services.HBDataParser.getTypeLen;

public class HBHeartRateServiceHandler extends HBServiceHandler<HBHeartRateListenerHandler> {
    public static final String TAG = "HBHeartRateServiceHandler";
    public static final UUID SERVICE_UUID =
            UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb");
    private static final UUID HEART_RATE_MEASUREMENT_CHARACTERISTIC_UUID =
            UUID.fromString("00002A37-0000-1000-8000-00805f9b34fb");
    private static final UUID BODY_SENSOR_LOCATION_CHARACTERISTIC_UUID =
            UUID.fromString("00002A38-0000-1000-8000-00805f9b34fb");
    private static final UUID HEART_RATE_CONTROL_POINT_CHARACTERISTIC_UUID =
            UUID.fromString("00002A39-0000-1000-8000-00805f9b34fb");

    public HBHeartRateServiceHandler(Context context) {
        super(context);
    }

    @Override
    public void characteristicValueUpdated(String deviceAddress,
                                           BluetoothGattCharacteristic characteristic) {
        HBLogger.v(TAG, String.format("characteristicValueUpdated device: %s, characteristic: %s", deviceAddress, characteristic.getUuid()));
        UUID characteristicUUID = characteristic.getUuid();

        if (characteristicUUID.equals(HEART_RATE_MEASUREMENT_CHARACTERISTIC_UUID)) {
            getHeartRateMeasurementValue(deviceAddress, characteristic);
        }

        if (characteristicUUID.equals(BODY_SENSOR_LOCATION_CHARACTERISTIC_UUID)) {
            getBodySensorLocationValue(deviceAddress, characteristic);
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

    public void getBodySensorLocation(HBDevice device) {
        HBLogger.v(TAG, String.format("getBodySensorLocation device: %s", device.getAddress()));
        device.readCharacteristic(new HBReadCommand(SERVICE_UUID,
                BODY_SENSOR_LOCATION_CHARACTERISTIC_UUID));
    }

    public void resetEnergyExpended(HBDevice device) {
        HBLogger.v(TAG, String.format("resetEnergyExpended device: %s", device.getAddress()));

        byte[] value = createValue(1, FORMAT_UINT8);
        device.writeCharacteristic(new HBWriteCommand(SERVICE_UUID,
                HEART_RATE_CONTROL_POINT_CHARACTERISTIC_UUID, value));
    }

    private void getHeartRateMeasurementValue(String deviceAddress,
                                               BluetoothGattCharacteristic characteristic) {
        HBLogger.v(TAG, String.format("getHeartRateMeasurementValue device: %s", deviceAddress));
        HBHeartRateMeasurement hbHeartRateMeasurement = getHeartRateMeasurement(characteristic);
        HBHeartRateListener listener = getServiceListener(deviceAddress);
        if (listener != null)
            listener.onHeartRateMeasurement(deviceAddress, hbHeartRateMeasurement);
    }

    private void getBodySensorLocationValue(String deviceAddress,
                                            BluetoothGattCharacteristic characteristic) {
        HBLogger.v(TAG, String.format("getBodySensorLocationValue device: %s", deviceAddress));
        int location = characteristic.getIntValue(FORMAT_UINT8, 0);
        HBBodySensorLocation hbBodySensorLocation = HBBodySensorLocation.fromValue(location);
        HBHeartRateListener listener = getServiceListener(deviceAddress);
        if (listener != null)
            listener.onBodySensorLocation(deviceAddress, hbBodySensorLocation);
    }

    private HBHeartRateMeasurement getHeartRateMeasurement(BluetoothGattCharacteristic characteristic) {
        byte[] value = characteristic.getValue();

        HBHeartRateMeasurement hbHeartRateMeasurement = new HBHeartRateMeasurement();

        int offset = 0;

        int heartRate = 0;
        switch ((value[0] >> 0) & 1) {
            case 0:
            default:
                //Heart Rate Value Format is set to UINT8.
                heartRate = characteristic.getIntValue(FORMAT_UINT8, offset);
                offset += getTypeLen(FORMAT_UINT8);
                break;
            case 1:
                //Heart Rate Value Format is set to UINT16.
                heartRate = characteristic.getIntValue(FORMAT_UINT16, offset);
                offset += getTypeLen(FORMAT_UINT16);
                break;
        }
        hbHeartRateMeasurement.setHeartRateMeasurementValue(heartRate);

        switch ((value[0] >> 1) & 1) {
            case 0:
            case 1:
            default:
                //Sensor Contact feature is not supported in the current connection
                hbHeartRateMeasurement.setSensorContactStatus(HBSensorContactFeature.NotSupported);
                break;
            case 2:
                //Sensor Contact feature is supported, but contact is not detected
                hbHeartRateMeasurement.setSensorContactStatus(HBSensorContactFeature.SupportedNoContact);
                break;
            case 3:
                //Sensor Contact feature is supported and contact is detected
                hbHeartRateMeasurement.setSensorContactStatus(HBSensorContactFeature.SupportedAndContact);
                break;
        }

        switch ((value[0] >> 3) & 1) {
            case 0:
            default:
                //Energy Expended field is not present
                break;
            case 1:
                //Energy Expended field is present
                int energyExpended = characteristic.getIntValue(FORMAT_UINT16, offset);
                offset += getTypeLen(FORMAT_UINT16);
                hbHeartRateMeasurement.setEnergyExpended(energyExpended);
                break;
        }

        switch ((value[0] >> 4) & 1) {
            case 0:
            default:
                //RR-Interval values are not present
                break;
            case 1:
                //One or more RR-Interval values are present
                int RRInterval = characteristic.getIntValue(FORMAT_UINT16, offset);
                hbHeartRateMeasurement.setRRInterval(RRInterval);
                break;
        }

        return hbHeartRateMeasurement;
    }
}
