/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import chdsw.philips.com.haraldlib.HBLogger;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_SINT16;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_SINT32;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_SINT8;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT16;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT32;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;

/**
 * General parsing methods to create byte[] values from given values
 */
public class HBDataParser {
    public static final String TAG = "HBDataParser";

    /**
     * Create byte[] value from Integer.
     *
     * @param value      Value to create byte[] from.
     * @param formatType The format type used to interpret the characteristic value.
     * @return Byte array representation of the value.
     */
    public static byte[] createValue(int value, int formatType) {
        HBLogger.v(TAG, String.format("createValue: value: %d, formatType: %d", value, formatType));

        int offset = 0;
        int len = getTypeLen(formatType);
        byte[] valueArray = new byte[len];

        switch (formatType) {
            case FORMAT_SINT8:
                value = intToSignedBits(value, 8);
            case FORMAT_UINT8:
                valueArray[offset] = (byte) (value & 0xFF);
                break;

            case FORMAT_SINT16:
                value = intToSignedBits(value, 16);
            case FORMAT_UINT16:
                valueArray[offset++] = (byte) (value & 0xFF);
                valueArray[offset] = (byte) ((value >> 8) & 0xFF);
                break;

            case FORMAT_SINT32:
                value = intToSignedBits(value, 32);
            case FORMAT_UINT32:
                valueArray[offset++] = (byte) (value & 0xFF);
                valueArray[offset++] = (byte) ((value >> 8) & 0xFF);
                valueArray[offset++] = (byte) ((value >> 16) & 0xFF);
                valueArray[offset] = (byte) ((value >> 24) & 0xFF);
                break;
        }

        HBLogger.v(TAG, String.format("createValue: valueArray: %s", Arrays.toString(valueArray)));

        return valueArray;
    }

    /**
     * Create byte[] value from String.
     *
     * @param value Value to create byte[] from.
     * @return Byte array representation of the value.
     */
    public static byte[] createValue(String value) {
        HBLogger.v(TAG, String.format("createValue: value: %s", value));
        return value.getBytes();
    }

    /**
     * Create byte[] value from Calendar.
     *
     * @param value Value to create byte[] from.
     * @return Byte array representation of the value.
     */
    public static byte[] createDateValue(Calendar value) {
        HBLogger.v(TAG, String.format("createDateValue: value: %s", value.toString()));

        byte[] year = createValue(value.get(Calendar.YEAR), FORMAT_UINT16);
        byte[] month = createValue(value.get(Calendar.MONTH), FORMAT_UINT8);
        byte[] day = createValue(value.get(Calendar.DATE), FORMAT_UINT8);

        byte[] valueArray = mergeArrays(year, month, day);

        HBLogger.v(TAG, String.format("createDateValue: valueArray: %s", Arrays.toString(valueArray)));

        return valueArray;
    }

    public static byte[] createDateTimeValue(Calendar value) {
        HBLogger.v(TAG, String.format("createDateTimeValue: value: %s", value.toString()));

        byte[] dateValue = createDateValue(value);

        byte[] hour = createValue(value.get(Calendar.HOUR_OF_DAY), FORMAT_UINT8);
        byte[] minute = createValue(value.get(Calendar.MINUTE), FORMAT_UINT8);
        byte[] seconds = createValue(value.get(Calendar.SECOND), FORMAT_UINT8);

        byte[] valueArray = mergeArrays(dateValue, hour, minute, seconds);

        HBLogger.v(TAG, String.format("createDateTimeValue: valueArray: %s", Arrays.toString(valueArray)));

        return valueArray;
    }

    /**
     * Returns the size of a give value type.
     */
    public static int getTypeLen(int formatType) {
        HBLogger.v(TAG, String.format("getTypeLen: formatType: %d", formatType));
        return formatType & 0xF;
    }

    /**
     * Get Date from characteristic with offset
     *
     * @param characteristic Characteristic to get value from
     * @param offset         Offset of value
     * @return Parsed date from value
     */
    public static Date getDateTime(BluetoothGattCharacteristic characteristic, int offset) {
        HBLogger.v(TAG, String.format("getDateTime: value: %s, offset: %d", Arrays.toString(characteristic.getValue()), offset));

        int year = characteristic.getIntValue(FORMAT_UINT16, offset);
        offset += getTypeLen(FORMAT_UINT16);
        int month = characteristic.getIntValue(FORMAT_UINT8, offset);
        offset += getTypeLen(FORMAT_UINT8);
        int day = characteristic.getIntValue(FORMAT_UINT8, offset);
        offset += getTypeLen(FORMAT_UINT8);
        int hour = characteristic.getIntValue(FORMAT_UINT8, offset);
        offset += getTypeLen(FORMAT_UINT8);
        int min = characteristic.getIntValue(FORMAT_UINT8, offset);
        offset += getTypeLen(FORMAT_UINT8);
        int sec = characteristic.getIntValue(FORMAT_UINT8, offset);

        GregorianCalendar calendar = new GregorianCalendar(year, month, day, hour, min, sec);

        HBLogger.v(TAG, String.format("getDateTime: calendar: %s", calendar));

        return calendar.getTime();
    }

    /**
     * Get length of date object value
     *
     * @return Length
     */
    public static int getDateTimeTypeLen() {
        HBLogger.v(TAG, String.format("getDateTimeTypeLen"));
        return getTypeLen(FORMAT_SINT16) + getTypeLen(FORMAT_UINT8) + getTypeLen(FORMAT_UINT8) +
                getTypeLen(FORMAT_UINT8) + getTypeLen(FORMAT_UINT8) + getTypeLen(FORMAT_UINT8);
    }

    public static Calendar getDate(BluetoothGattCharacteristic characteristic, int offset) {
        HBLogger.v(TAG, String.format("getDateValue: value: %s, offset: %d", Arrays.toString(characteristic.getValue()), offset));

        int year = characteristic.getIntValue(FORMAT_UINT16, offset);
        offset += getTypeLen(FORMAT_UINT16);
        int month = characteristic.getIntValue(FORMAT_UINT8, offset);
        offset += getTypeLen(FORMAT_UINT8);
        int day = characteristic.getIntValue(FORMAT_UINT8, offset);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        HBLogger.v(TAG, String.format("getDateValue: calendar: %s", calendar));

        return calendar;
    }

    /**
     * Get length of date object value
     *
     * @return Length
     */
    public static int getDateTypeLen() {
        HBLogger.v(TAG, String.format("getDateTypeLen"));
        return getTypeLen(FORMAT_SINT16) + getTypeLen(FORMAT_UINT8) + getTypeLen(FORMAT_UINT8);
    }

    /**
     * Merge multiple arrays intro one array
     *
     * @param arrays Arrays to merge
     * @return Merge array
     */
    public static byte[] mergeArrays(byte[]... arrays) {
        HBLogger.v(TAG, String.format("mergeArrays size: %d", arrays.length));

        int size = 0;
        for (byte[] array : arrays) {
            size += array.length;
        }

        byte[] merged = new byte[size];
        int index = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, merged, index, array.length);
            index += array.length;
        }

        return merged;
    }

    /**
     * Convert an integer into the signed bits of a given length.
     */
    private static int intToSignedBits(int i, int size) {
        if (i < 0) {
            i = (1 << size - 1) + (i & ((1 << size - 1) - 1));
        }
        return i;
    }
}