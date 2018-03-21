/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.healththermometer;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import java.util.Date;
import java.util.UUID;

import chdsw.philips.com.haraldlib.HBDevice;
import chdsw.philips.com.haraldlib.HBLogger;
import chdsw.philips.com.haraldlib.HBReadCommand;
import chdsw.philips.com.haraldlib.HBWriteCommand;
import chdsw.philips.com.haraldlib.services.HBServiceHandler;
import chdsw.philips.com.haraldlib.services.healththermometer.object.HBTemperatureMeasurement;
import chdsw.philips.com.haraldlib.services.healththermometer.object.HBTemperatureType;
import chdsw.philips.com.haraldlib.services.healththermometer.object.HBTemperatureUnit;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_FLOAT;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT16;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;
import static chdsw.philips.com.haraldlib.services.HBDataParser.*;

public class HBHealthThermometerServiceHandler extends HBServiceHandler<HBHealthThermometerListenerHandler> {
    public static final String TAG = "HBHealthThermometerServiceHandler";
    public static final UUID SERVICE_UUID =
            UUID.fromString("00001809-0000-1000-8000-00805f9b34fb");
    private static final UUID TEMPERATURE_MEASUREMENT_CHARACTERISTIC_UUID =
            UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb");
    private static final UUID TEMPERATURE_TYPE_CHARACTERISTIC_UUID =
            UUID.fromString("00002A1D-0000-1000-8000-00805f9b34fb");
    private static final UUID INTERMEDIATE_TEMPERATURE_CHARACTERISTIC_UUID =
            UUID.fromString("00002A1E-0000-1000-8000-00805f9b34fb");
    private static final UUID MEASUREMENT_INTERVAL_CHARACTERISTIC_UUID =
            UUID.fromString("00002A21-0000-1000-8000-00805f9b34fb");

    /**
     * Construct new healt thermometer service handler.
     *
     * @param context Application context.
     */
    public HBHealthThermometerServiceHandler(Context context) {
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

        if (characteristicUUID.equals(TEMPERATURE_MEASUREMENT_CHARACTERISTIC_UUID)) {
            getTemperatureMeasurementValue(deviceAddress, characteristic);
        }

        if (characteristicUUID.equals(TEMPERATURE_TYPE_CHARACTERISTIC_UUID)) {
            getTemperatureTypeValue(deviceAddress, characteristic);
        }

        if (characteristicUUID.equals(INTERMEDIATE_TEMPERATURE_CHARACTERISTIC_UUID)) {
            getIntermediateTemperatureValue(deviceAddress, characteristic);
        }

        if (characteristicUUID.equals(MEASUREMENT_INTERVAL_CHARACTERISTIC_UUID)) {
            getMeasurementIntervalValue(deviceAddress, characteristic);
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
     * Get the temperature type from the bluetooth device
     *
     * @param device Device to execute characteristic read on
     */
    public void getTemperatureType(HBDevice device) {
        HBLogger.v(TAG, String.format("getTemperatureType device: %s", device.getAddress()));
        device.readCharacteristic(new HBReadCommand(SERVICE_UUID,
                TEMPERATURE_TYPE_CHARACTERISTIC_UUID));
    }

    /**
     * Get the measurement interval from the bluetooth device
     *
     * @param device Device to execute characteristic read on
     */
    public void getMeasurementInterval(HBDevice device) {
        HBLogger.v(TAG, String.format("getMeasurementInterval device: %s", device.getAddress()));
        device.readCharacteristic(new HBReadCommand(SERVICE_UUID,
                MEASUREMENT_INTERVAL_CHARACTERISTIC_UUID));
    }

    /**
     * Set the measurement interval to the bluetooth device
     *
     * @param device   Device to execute characteristic write on
     * @param interval Interval value to write
     */
    public void setMeasurementInterval(HBDevice device, int interval) {
        HBLogger.v(TAG, String.format("setMeasurementInterval device: %s, interval: %d", device.getAddress(), interval));
        byte[] value = createValue(interval, FORMAT_UINT16);

        device.writeCharacteristic(new HBWriteCommand(SERVICE_UUID,
                MEASUREMENT_INTERVAL_CHARACTERISTIC_UUID, value));
    }

    /**
     * Get the temperature measurement value from the characteristic.
     *
     * @param deviceAddress  Address of the device that the value is received from
     * @param characteristic Characteristic containing the value
     */
    private void getTemperatureMeasurementValue(String deviceAddress,
                                                BluetoothGattCharacteristic characteristic) {
        HBLogger.v(TAG, String.format("getTemperatureMeasurementValue device: %s", deviceAddress));
        HBHealthThermometerListener listener = getServiceListener(deviceAddress);
        if (listener != null)
            listener.onTemperatureMeasurement(deviceAddress, getTemperature(characteristic));
    }

    /**
     * Get the temperature type value from the characteristic.
     *
     * @param deviceAddress  Address of the device that the value is received from
     * @param characteristic Characteristic containing the value
     */
    private void getTemperatureTypeValue(String deviceAddress,
                                         BluetoothGattCharacteristic characteristic) {
        HBLogger.v(TAG, String.format("getTemperatureTypeValue device: %s", deviceAddress));
        int value = characteristic.getIntValue(FORMAT_UINT8, 0);
        HBTemperatureType type = HBTemperatureType.fromValue(value);
        HBHealthThermometerListener listener = getServiceListener(deviceAddress);
        if (listener != null)
            listener.onTemperatureType(deviceAddress, type);
    }

    /**
     * Get the intermediate temperature value from the characteristic.
     *
     * @param deviceAddress  Address of the device that the value is received from
     * @param characteristic Characteristic containing the value
     */
    private void getIntermediateTemperatureValue(String deviceAddress,
                                                 BluetoothGattCharacteristic characteristic) {
        HBLogger.v(TAG, String.format("getIntermediateTemperatureValue device: %s", deviceAddress));
        HBHealthThermometerListener listener = getServiceListener(deviceAddress);
        if (listener != null)
            listener.onIntermediateTemperature(deviceAddress, getTemperature(characteristic));
    }

    /**
     * Get the measurement interval value from the characteristic.
     *
     * @param deviceAddress  Address of the device that the value is received from
     * @param characteristic Characteristic containing the value
     */
    private void getMeasurementIntervalValue(String deviceAddress,
                                             BluetoothGattCharacteristic characteristic) {
        int interval = characteristic.getIntValue(FORMAT_UINT16, 0);
        HBLogger.v(TAG, String.format("getMeasurementIntervalValue device: %s, interval: %d", deviceAddress, interval));
        HBHealthThermometerListener listener = getServiceListener(deviceAddress);
        if (listener != null)
            listener.onMeasurementInterval(deviceAddress, interval);
    }

    /**
     * Parse the characteristic value to a {@link HBTemperatureMeasurement} object
     * The parsing occurs using the BLE specification:
     * https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.temperature_measurement.xml
     *
     * @param characteristic Characteristic containing the temperature value
     * @return New {@link HBTemperatureMeasurement} object
     */
    private HBTemperatureMeasurement getTemperature(BluetoothGattCharacteristic characteristic) {
        byte[] value = characteristic.getValue();

        HBTemperatureMeasurement temperature = new HBTemperatureMeasurement();

        int offset = 1;
        float temperatureValue = characteristic.getFloatValue(FORMAT_FLOAT, offset);
        offset += getTypeLen(FORMAT_FLOAT);
        temperature.setTemperatureValue(temperatureValue);

        switch ((value[0] >> 0) & 1) {
            case 0:
            default:
                //Temperature Measurement Value in units of Celsius
                temperature.setUnit(HBTemperatureUnit.Celsius);
                break;
            case 1:
                //Temperature Measurement Value in units of Fahrenheit
                temperature.setUnit(HBTemperatureUnit.Fahrenheit);
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
                temperature.setTimestamp(timestamp);
                break;
        }

        switch ((value[0] >> 2) & 1) {
            case 0:
            default:
                //Temperature Type field not present
                break;
            case 1:
                //Temperature Type field present
                int typeValue = characteristic.getIntValue(FORMAT_UINT8, offset);
                HBTemperatureType type = HBTemperatureType.fromValue(typeValue);
                temperature.setType(type);
                break;
        }

        return temperature;
    }
}
