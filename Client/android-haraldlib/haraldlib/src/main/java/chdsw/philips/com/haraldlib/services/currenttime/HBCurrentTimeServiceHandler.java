/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.currenttime;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import chdsw.philips.com.haraldlib.HBDevice;
import chdsw.philips.com.haraldlib.HBLogger;
import chdsw.philips.com.haraldlib.HBReadCommand;
import chdsw.philips.com.haraldlib.HBWriteCommand;
import chdsw.philips.com.haraldlib.services.HBServiceHandler;
import chdsw.philips.com.haraldlib.services.currenttime.object.HBCurrentTime;
import chdsw.philips.com.haraldlib.services.currenttime.object.HBLocalTimeInformation;
import chdsw.philips.com.haraldlib.services.currenttime.object.HBReferenceTimeInformation;
import chdsw.philips.com.haraldlib.services.currenttime.object.HBSTDOffset;
import chdsw.philips.com.haraldlib.services.currenttime.object.HBTimeSource;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_SINT8;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;
import static chdsw.philips.com.haraldlib.services.HBDataParser.createDateTimeValue;
import static chdsw.philips.com.haraldlib.services.HBDataParser.createValue;
import static chdsw.philips.com.haraldlib.services.HBDataParser.getDateTime;
import static chdsw.philips.com.haraldlib.services.HBDataParser.getDateTimeTypeLen;
import static chdsw.philips.com.haraldlib.services.HBDataParser.getTypeLen;
import static chdsw.philips.com.haraldlib.services.HBDataParser.mergeArrays;

public class HBCurrentTimeServiceHandler extends HBServiceHandler<HBCurrentTimeListenerHandler> {
    public static final String TAG = "HBCurrentTimeServiceHandler";
    public static final UUID SERVICE_UUID =
            UUID.fromString("00001805-0000-1000-8000-00805f9b34fb");
    private static final UUID CURRENT_TIME_CHARACTERISTIC_UUID =
            UUID.fromString("00002A2B-0000-1000-8000-00805f9b34fb");
    private static final UUID LOCAL_TIME_INFORMATION_CHARACTERISTIC_UUID =
            UUID.fromString("00002A0F-0000-1000-8000-00805f9b34fb");
    private static final UUID REFERENCE_TIME_INFORMATION_CHARACTERISTIC_UUID =
            UUID.fromString("00002A14-0000-1000-8000-00805f9b34fb");

    /**
     * Construct new current time service handler.
     *
     * @param context Application context.
     */
    public HBCurrentTimeServiceHandler(Context context) {
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

        if (characteristicUUID.equals(CURRENT_TIME_CHARACTERISTIC_UUID)) {
            getCurrentTimeValue(deviceAddress, characteristic);
        }
        if (characteristicUUID.equals(LOCAL_TIME_INFORMATION_CHARACTERISTIC_UUID)) {
            getLocalTimeInformationValue(deviceAddress, characteristic);
        }

        if (characteristicUUID.equals(REFERENCE_TIME_INFORMATION_CHARACTERISTIC_UUID)) {
            getReferenceTimeInformationValue(deviceAddress, characteristic);
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
     * Get the current time from the bluetooth device
     *
     * @param device Device to execute characteristic read on
     */
    public void getCurrentTime(HBDevice device) {
        HBLogger.v(TAG, String.format("getCurrentTime device: %s", device.getAddress()));
        device.readCharacteristic(new HBReadCommand(SERVICE_UUID,
                CURRENT_TIME_CHARACTERISTIC_UUID));
    }

    /**
     * Write the current time to the bluetooth device
     *
     * @param device Device to execute characteristic write on
     * @param currentTime Current time to write
     */
    public void setCurrentTime(HBDevice device, HBCurrentTime currentTime) {
        HBLogger.v(TAG, String.format("setCurrentTime device: %s", device.getAddress()));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime.getDateTime());

        byte[] datetime = createDateTimeValue(calendar);
        byte[] dayOfWeek = createValue(currentTime.getDayOfWeek(), FORMAT_UINT8);
        byte[] fractions256 = createValue(currentTime.getFractions256(), FORMAT_UINT8);
        byte[] adjustReason = createValue(currentTime.getAdjustReason(), FORMAT_UINT8);

        byte[] value = mergeArrays(datetime, dayOfWeek, fractions256, adjustReason);

        device.writeCharacteristic(new HBWriteCommand(SERVICE_UUID,
                CURRENT_TIME_CHARACTERISTIC_UUID, value));
    }

    /**
     * Get the location time information from the bluetooth device
     *
     * @param device Device to execute characteristic read on
     */
    public void getLocalTimeInformation(HBDevice device) {
        HBLogger.v(TAG, String.format("getLocalTimeInformation device: %s", device.getAddress()));
        device.readCharacteristic(new HBReadCommand(SERVICE_UUID,
                LOCAL_TIME_INFORMATION_CHARACTERISTIC_UUID));
    }

    /**
     * Write the local time information to the bluetooth device
     *
     * @param device Device to execute characteristic write on
     * @param localTimeInformation Local time information to write
     */
    public void setLocalTimeInformation(HBDevice device, HBLocalTimeInformation localTimeInformation) {
        HBLogger.v(TAG, String.format("setLocalTimeInformation device: %s", device.getAddress()));
        byte[] timeZone = createValue(localTimeInformation.getTimeZone(), FORMAT_SINT8);
        byte[] STDOffset = createValue(localTimeInformation.getSTDOffset().getValue(), FORMAT_UINT8);

        byte[] value = mergeArrays(timeZone, STDOffset);

        device.writeCharacteristic(new HBWriteCommand(SERVICE_UUID,
                LOCAL_TIME_INFORMATION_CHARACTERISTIC_UUID, value));
    }

    /**
     * Get the reference time information from the bluetooth device
     *
     * @param device Device to execute characteristic read on
     */
    public void getReferenceTimeInformation(HBDevice device) {
        HBLogger.v(TAG, String.format("getReferenceTimeInformation device: %s", device.getAddress()));
        device.readCharacteristic(new HBReadCommand(SERVICE_UUID,
                REFERENCE_TIME_INFORMATION_CHARACTERISTIC_UUID));
    }

    /**
     * Get the current time value from the characteristic.
     *
     * @param deviceAddress Address of the device that the value is received from
     * @param characteristic Characteristic containing the value
     */
    private void getCurrentTimeValue(String deviceAddress,
                                     BluetoothGattCharacteristic characteristic) {
        HBLogger.v(TAG, String.format("getCurrentTimeValue device: %s", deviceAddress));
        HBCurrentTime currentTime = new HBCurrentTime();
        int offset = 0;

        Date dateTime = getDateTime(characteristic, offset);
        currentTime.setDateTime(dateTime);
        offset += getDateTimeTypeLen();

        int dayOfWeek = characteristic.getIntValue(FORMAT_UINT8, offset);
        currentTime.setDayOfWeek(dayOfWeek);
        offset += getTypeLen(FORMAT_UINT8);

        int fractions256 = characteristic.getIntValue(FORMAT_UINT8, offset);
        currentTime.setFractions256(fractions256);
        offset += getTypeLen(FORMAT_UINT8);

        int adjustReason = characteristic.getIntValue(FORMAT_UINT8, offset);
        currentTime.setAdjustReason(adjustReason);

        HBCurrentTimeListener listener = getServiceListener(deviceAddress);
        if (listener != null)
            listener.onCurrentTime(deviceAddress, currentTime);
    }

    /**
     * Get the local time information value from the characteristic.
     *
     * @param deviceAddress Address of the device that the value is received from
     * @param characteristic Characteristic containing the value
     */
    public void getLocalTimeInformationValue(String deviceAddress,
                                             BluetoothGattCharacteristic characteristic) {
        HBLogger.v(TAG, String.format("getLocalTimeInformationValue device: %s", deviceAddress));
        HBLocalTimeInformation localTimeInformation = new HBLocalTimeInformation();
        int offset = 0;

        int timeZone = characteristic.getIntValue(FORMAT_SINT8, offset);
        localTimeInformation.setTimeZone(timeZone);
        offset += getTypeLen(FORMAT_SINT8);

        int dstOffsetValue = characteristic.getIntValue(FORMAT_UINT8, offset);
        HBSTDOffset SDTOffset = HBSTDOffset.fromValue(dstOffsetValue);
        localTimeInformation.setSTDOffset(SDTOffset);

        HBCurrentTimeListener listener = getServiceListener(deviceAddress);
        if (listener != null)
            listener.onLocalTimeInformation(deviceAddress, localTimeInformation);
    }

    /**
     * Get the reference time information value from the characteristic.
     *
     * @param deviceAddress Address of the device that the value is received from
     * @param characteristic Characteristic containing the value
     */
    public void getReferenceTimeInformationValue(String deviceAddress,
                                                 BluetoothGattCharacteristic characteristic) {
        HBLogger.v(TAG, String.format("getReferenceTimeInformationValue device: %s", deviceAddress));
        HBReferenceTimeInformation referenceTimeInformation = new HBReferenceTimeInformation();
        int offset = 0;

        int timeSourceValue = characteristic.getValue()[0];
        HBTimeSource timeSource = HBTimeSource.fromValue(timeSourceValue);
        referenceTimeInformation.setTimeSource(timeSource);
        offset += 1;

        int accuracy = characteristic.getIntValue(FORMAT_UINT8, offset);
        referenceTimeInformation.setAccuracy(accuracy);
        offset += getTypeLen(FORMAT_UINT8);

        int daysSinceUpdate = characteristic.getIntValue(FORMAT_UINT8, offset);
        referenceTimeInformation.setDaysSinceUpdate(daysSinceUpdate);
        offset += getTypeLen(FORMAT_UINT8);

        int hoursSinceUpdate = characteristic.getIntValue(FORMAT_UINT8, offset);
        referenceTimeInformation.setHoursSinceUpdate(hoursSinceUpdate);

        HBCurrentTimeListener listener = getServiceListener(deviceAddress);
        if (listener != null)
            listener.onReferenceTimeInformation(deviceAddress, referenceTimeInformation);
    }
}
