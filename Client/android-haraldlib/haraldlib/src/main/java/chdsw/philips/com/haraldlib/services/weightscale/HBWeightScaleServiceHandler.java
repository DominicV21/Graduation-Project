/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.weightscale;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import java.util.Date;
import java.util.UUID;

import chdsw.philips.com.haraldlib.HBDevice;
import chdsw.philips.com.haraldlib.HBLogger;
import chdsw.philips.com.haraldlib.HBReadCommand;
import chdsw.philips.com.haraldlib.services.HBServiceHandler;
import chdsw.philips.com.haraldlib.services.weightscale.object.HBMeasurementUnit;
import chdsw.philips.com.haraldlib.services.weightscale.object.HBWeightMeasurement;
import chdsw.philips.com.haraldlib.services.weightscale.object.HBWeightScaleFeature;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT16;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;
import static chdsw.philips.com.haraldlib.services.HBDataParser.*;

public class HBWeightScaleServiceHandler extends HBServiceHandler<HBWeightScaleListenerHandler> {
    public static final String TAG = "HBWeightScaleServiceHandler";
    public static final UUID SERVICE_UUID =
            UUID.fromString("0000181D-0000-1000-8000-00805f9b34fb");
    private static final UUID WEIGHT_SCALE_FEATURE_CHARACTERISTIC_UUID =
            UUID.fromString("00002A9E-0000-1000-8000-00805f9b34fb");
    private static final UUID WEIGHT_MEASUREMENT_CHARACTERISTIC_UUID =
            UUID.fromString("00002A9D-0000-1000-8000-00805f9b34fb");

    /**
     * Construct new health thermometer service handler.
     *
     * @param context Application context.
     */
    public HBWeightScaleServiceHandler(Context context) {
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
        HBLogger.v(TAG, String.format("characteristicValueUpdated device: %s, characteristic: %s", deviceAddress, characteristic.getUuid()));
        UUID characteristicUUID = characteristic.getUuid();

        if (characteristicUUID.equals(WEIGHT_SCALE_FEATURE_CHARACTERISTIC_UUID)) {
            getWeightScaleFeatureValue(deviceAddress, characteristic);
        }

        if (characteristicUUID.equals(WEIGHT_MEASUREMENT_CHARACTERISTIC_UUID)) {
            getWeightMeasurementValue(deviceAddress, characteristic);
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
     * Get the weight scale feature from the bluetooth device
     *
     * @param device Device to execute characteristic read on
     */
    public void getWeightScaleFeature(HBDevice device) {
        HBLogger.v(TAG, String.format("getWeightScaleFeature device: %s", device.getAddress()));
        device.readCharacteristic(new HBReadCommand(SERVICE_UUID,
                WEIGHT_SCALE_FEATURE_CHARACTERISTIC_UUID));
    }

    /**
     * Get the weight scale feature value from the characteristic.
     *
     * @param deviceAddress  Address of the device that the value is received from
     * @param characteristic Characteristic containing the value
     */
    private void getWeightScaleFeatureValue(String deviceAddress,
                                            BluetoothGattCharacteristic characteristic) {
        HBLogger.v(TAG, String.format("getWeightScaleFeatureValue device: %s", deviceAddress));
        byte[] value = characteristic.getValue();

        HBWeightScaleFeature weightScaleFeature = new HBWeightScaleFeature();

        String bitValue = "";
        String byteValue = Integer.toBinaryString(value[3]);
        bitValue += ("0000000" + byteValue).substring(byteValue.length());
        byteValue = Integer.toBinaryString(value[2]);
        bitValue += ("0000000" + byteValue).substring(byteValue.length());
        byteValue = Integer.toBinaryString(value[1]);
        bitValue += ("0000000" + byteValue).substring(byteValue.length());
        byteValue = Integer.toBinaryString(value[0]);
        bitValue += ("0000000" + byteValue).substring(byteValue.length());
        int unsignedValue = Integer.parseInt(bitValue, 2);

        weightScaleFeature.setTimeStampSupported(((unsignedValue >> 0) & 1) == 1);
        weightScaleFeature.setMultipleUsersSupported(((unsignedValue >> 1) & 1) == 1);
        weightScaleFeature.setBMISupported(((unsignedValue >> 2) & 1) == 1);

        weightScaleFeature.setWeightMeasurementResolution((unsignedValue >> 3) & 0b1111);
        weightScaleFeature.setHeightMeasurementResolution((unsignedValue >> 7) & 0b111);

        HBWeightScaleListener listener = getServiceListener(deviceAddress);
        if (listener != null)
            listener.onWeightScaleFeature(deviceAddress, weightScaleFeature);
    }

    /**
     * Get the weight measurement value from the characteristic.
     *
     * @param deviceAddress  Address of the device that the value is received from
     * @param characteristic Characteristic containing the value
     */
    private void getWeightMeasurementValue(String deviceAddress,
                                           BluetoothGattCharacteristic characteristic) {
        HBLogger.v(TAG, String.format("getWeightMeasurementValue device: %s", deviceAddress));
        byte[] value = characteristic.getValue();

        HBWeightMeasurement weightMeasurement = new HBWeightMeasurement();

        int offset = 1;
        int weightValue = characteristic.getIntValue(FORMAT_UINT16, offset);
        offset += getTypeLen(FORMAT_UINT16);
        weightMeasurement.setWeight(weightValue);

        switch ((value[0] >> 0) & 1) {
            case 0:
            default:
                weightMeasurement.setMeasurementUnit(HBMeasurementUnit.SI);
                break;
            case 1:
                weightMeasurement.setMeasurementUnit(HBMeasurementUnit.Imperial);
                break;
        }

        switch ((value[0] >> 1) & 1) {
            case 0:
            default:
                //Time Stamp field not present
                break;
            case 1:
                //Time Stamp field present
                Date timestamp = getDateTime(characteristic, offset);
                offset += getDateTimeTypeLen();
                weightMeasurement.setTimestamp(timestamp);
                break;
        }

        switch ((value[0] >> 2) & 1) {
            case 0:
            default:
                //User ID  field not present
                break;
            case 1:
                //User ID field present
                int userId = characteristic.getIntValue(FORMAT_UINT8, offset);
                offset += getTypeLen(FORMAT_UINT8);
                weightMeasurement.setUserID(userId);
                break;
        }

        switch ((value[0] >> 3) & 1) {
            case 0:
            default:
                //BMI and Height field not present
                break;
            case 1:
                //BMI and Height field present
                int bmi = characteristic.getIntValue(FORMAT_UINT16, offset);
                offset += getTypeLen(FORMAT_UINT16);
                weightMeasurement.setBMI(bmi);

                int height = characteristic.getIntValue(FORMAT_UINT16, offset);
                weightMeasurement.setHeight(height);
                break;
        }

        HBWeightScaleListener listener = getServiceListener(deviceAddress);
        if (listener != null)
            listener.onWeightMeasurement(deviceAddress, weightMeasurement);
    }
}