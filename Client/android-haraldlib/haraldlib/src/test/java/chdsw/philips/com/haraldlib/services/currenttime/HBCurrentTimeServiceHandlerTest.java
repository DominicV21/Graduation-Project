/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.currenttime;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import chdsw.philips.com.haraldlib.BuildConfig;
import chdsw.philips.com.haraldlib.HBDevice;
import chdsw.philips.com.haraldlib.HBReadCommand;
import chdsw.philips.com.haraldlib.HBWriteCommand;
import chdsw.philips.com.haraldlib.services.currenttime.object.HBCurrentTime;
import chdsw.philips.com.haraldlib.services.currenttime.object.HBLocalTimeInformation;
import chdsw.philips.com.haraldlib.services.currenttime.object.HBReferenceTimeInformation;
import chdsw.philips.com.haraldlib.services.currenttime.object.HBTimeSource;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_SINT16;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_SINT32;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_SINT8;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT16;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT32;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;
import static chdsw.philips.com.haraldlib.services.currenttime.object.HBSTDOffset.DaylightTime;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class HBCurrentTimeServiceHandlerTest {
    private HBCurrentTimeServiceHandler handler;

    @Mock
    private Context context;

    @Mock
    private HBCurrentTimeListenerHandler listener;

    @Before
    public void setUp() {
        initMocks(this);

        handler = new HBCurrentTimeServiceHandler(context);
    }

    @Test
    public void addServiceListenerTest() {
        handler.addServiceListener("0.0.0.0", listener);
        assertEquals(handler.getServiceListener("0.0.0.0"),listener);
    }

    @Test
    public void removeServiceListenerTest() {
        handler.addServiceListener("0.0.0.0", listener);
        assertEquals(listener, handler.getServiceListener("0.0.0.0"));
        handler.removeServiceListener("0.0.0.0");
        assertEquals(handler.getServiceListener("0.0.0.0"), null);
    }

    @Test
    public void onCurrentTimeValueUpdatedTest() {
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A2B-0000-1000-8000-00805f9b34fb"));

        GregorianCalendar calendar = new GregorianCalendar(2001, 6, 29, 12, 1, 30);
        when(characteristic.getIntValue(FORMAT_UINT16, 0)).thenReturn(calendar.get(GregorianCalendar.YEAR));
        when(characteristic.getIntValue(FORMAT_UINT8, 2)).thenReturn(calendar.get(GregorianCalendar.MONTH));
        when(characteristic.getIntValue(FORMAT_UINT8, 3)).thenReturn(calendar.get(GregorianCalendar.DAY_OF_MONTH));
        when(characteristic.getIntValue(FORMAT_UINT8, 4)).thenReturn(calendar.get(GregorianCalendar.HOUR_OF_DAY));
        when(characteristic.getIntValue(FORMAT_UINT8, 5)).thenReturn(calendar.get(GregorianCalendar.MINUTE));
        when(characteristic.getIntValue(FORMAT_UINT8, 6)).thenReturn(calendar.get(GregorianCalendar.SECOND));

        when(characteristic.getIntValue(FORMAT_UINT8, 7)).thenReturn(1);
        when(characteristic.getIntValue(FORMAT_UINT8, 8)).thenReturn(2);
        when(characteristic.getIntValue(FORMAT_UINT8, 9)).thenReturn(3);

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        ArgumentCaptor<HBCurrentTime> captor = ArgumentCaptor.forClass(HBCurrentTime.class);
        verify(listener).onCurrentTime(anyString(), captor.capture());

        HBCurrentTime currentTime = captor.getAllValues().get(0);
        assertEquals(calendar.getTime(), currentTime.getDateTime());
        assertEquals(1, currentTime.getDayOfWeek());
        assertEquals(2, currentTime.getFractions256());
        assertEquals(3, currentTime.getAdjustReason());
    }

    @Test
    public void onLocalTimeInformationValueUpdatedTest() {
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A0F-0000-1000-8000-00805f9b34fb"));

        when(characteristic.getIntValue(FORMAT_SINT8, 0)).thenReturn(1);
        when(characteristic.getIntValue(FORMAT_UINT8, 1)).thenReturn(DaylightTime.getValue());

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        ArgumentCaptor<HBLocalTimeInformation> captor = ArgumentCaptor.forClass(HBLocalTimeInformation.class);
        verify(listener).onLocalTimeInformation(anyString(), captor.capture());

        HBLocalTimeInformation localTimeInformation = captor.getAllValues().get(0);
        assertEquals(1, localTimeInformation.getTimeZone());
        assertEquals(DaylightTime, localTimeInformation.getSTDOffset());
    }

    @Test
    public void onReferenceTimeInformationValueUpdatedTest() {
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A14-0000-1000-8000-00805f9b34fb"));

        when(characteristic.getValue()).thenReturn(new byte[] { (byte) HBTimeSource.GPS.getValue() } );
        when(characteristic.getIntValue(FORMAT_UINT8, 1)).thenReturn(1);
        when(characteristic.getIntValue(FORMAT_UINT8, 2)).thenReturn(2);
        when(characteristic.getIntValue(FORMAT_UINT8, 3)).thenReturn(3);

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        ArgumentCaptor<HBReferenceTimeInformation> captor = ArgumentCaptor.forClass(HBReferenceTimeInformation.class);
        verify(listener).onReferenceTimeInformation(anyString(), captor.capture());

        HBReferenceTimeInformation referenceTimeInformation = captor.getAllValues().get(0);
        assertEquals(HBTimeSource.GPS, referenceTimeInformation.getTimeSource());
        assertEquals(1, referenceTimeInformation.getAccuracy());
        assertEquals(2, referenceTimeInformation.getDaysSinceUpdate());
        assertEquals(3, referenceTimeInformation.getHoursSinceUpdate());
    }

    @Test
    public void getCurrentTimeTest() {
        HBDevice device = mock(HBDevice.class);

        handler.getCurrentTime(device);

        ArgumentCaptor<HBReadCommand> commandCaptor = ArgumentCaptor.forClass(HBReadCommand.class);
        verify(device).readCharacteristic(commandCaptor.capture());

        List<HBReadCommand> capturedCommands = commandCaptor.getAllValues();
        assertEquals(UUID.fromString("00001805-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getServiceUUID());
        assertEquals(UUID.fromString("00002A2B-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getCharacteristicUUID());
    }

    @Test
    public void setCurrentTimeTest() {
        HBDevice device = mock(HBDevice.class);
        HBCurrentTime currentTime = new HBCurrentTime();
        Date now = new Date();
        currentTime.setDateTime(now);
        currentTime.setDayOfWeek(1);
        currentTime.setFractions256(2);
        currentTime.setAdjustReason(3);
        handler.setCurrentTime(device, currentTime);

        ArgumentCaptor<HBWriteCommand> commandCaptor = ArgumentCaptor.forClass(HBWriteCommand.class);
        verify(device).writeCharacteristic(commandCaptor.capture());

        List<HBWriteCommand> capturedCommands = commandCaptor.getAllValues();
        assertEquals(UUID.fromString("00001805-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getServiceUUID());
        assertEquals(UUID.fromString("00002A2B-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getCharacteristicUUID());


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        byte[] value = new byte[10];

        byte[] year = createValue(calendar.get(Calendar.YEAR), FORMAT_UINT16, 0);
        System.arraycopy(year, 0, value, 0, 2);
        byte[] month = createValue(calendar.get(Calendar.MONTH), FORMAT_UINT8, 0);
        System.arraycopy(month, 0, value, 2, 1);
        byte[] day = createValue(calendar.get(Calendar.DAY_OF_MONTH), FORMAT_UINT8, 0);
        System.arraycopy(day, 0, value, 3, 1);
        byte[] hour = createValue(calendar.get(Calendar.HOUR_OF_DAY), FORMAT_UINT8, 0);
        System.arraycopy(hour, 0, value, 4, 1);
        byte[] minute = createValue(calendar.get(Calendar.MINUTE), FORMAT_UINT8, 0);
        System.arraycopy(minute, 0, value, 5, 1);
        byte[] second = createValue(calendar.get(Calendar.SECOND), FORMAT_UINT8, 0);
        System.arraycopy(second, 0, value, 6, 1);

        byte[] dayweek = createValue(1, FORMAT_UINT8, 0);
        System.arraycopy(dayweek, 0, value, 7, 1);
        byte[] fraction = createValue(2, FORMAT_UINT8, 0);
        System.arraycopy(fraction, 0, value, 8, 1);
        byte[] adjust = createValue(3, FORMAT_UINT8, 0);
        System.arraycopy(adjust, 0, value, 9, 1);


        assertArrayEquals(value, capturedCommands.get(0).getValue());
    }

    @Test
    public void getLocalTimeInformationTest() {
        HBDevice device = mock(HBDevice.class);

        handler.getLocalTimeInformation(device);

        ArgumentCaptor<HBReadCommand> commandCaptor = ArgumentCaptor.forClass(HBReadCommand.class);
        verify(device).readCharacteristic(commandCaptor.capture());

        List<HBReadCommand> capturedCommands = commandCaptor.getAllValues();
        assertEquals(UUID.fromString("00001805-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getServiceUUID());
        assertEquals(UUID.fromString("00002A0F-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getCharacteristicUUID());
    }

    @Test
    public void setLocalTimeInformationTest() {
        HBDevice device = mock(HBDevice.class);
        HBLocalTimeInformation localTimeInformation = new HBLocalTimeInformation();
        localTimeInformation.setTimeZone(1);
        localTimeInformation.setSTDOffset(DaylightTime);

        handler.setLocalTimeInformation(device, localTimeInformation);

        ArgumentCaptor<HBWriteCommand> commandCaptor = ArgumentCaptor.forClass(HBWriteCommand.class);
        verify(device).writeCharacteristic(commandCaptor.capture());

        List<HBWriteCommand> capturedCommands = commandCaptor.getAllValues();
        assertEquals(UUID.fromString("00001805-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getServiceUUID());
        assertEquals(UUID.fromString("00002A0F-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getCharacteristicUUID());

        byte[] value = new byte[2];

        byte[] timezone = createValue(1, FORMAT_SINT8, 0);
        System.arraycopy(timezone, 0, value, 0, 1);
        byte[] dst = createValue(DaylightTime.getValue(), FORMAT_UINT8, 0);
        System.arraycopy(dst, 0, value, 1, 1);

        assertArrayEquals(value, capturedCommands.get(0).getValue());
    }

    @Test
    public void getReferenceTimeInformationTest() {
        HBDevice device = mock(HBDevice.class);

        handler.getReferenceTimeInformation(device);

        ArgumentCaptor<HBReadCommand> commandCaptor = ArgumentCaptor.forClass(HBReadCommand.class);
        verify(device).readCharacteristic(commandCaptor.capture());

        List<HBReadCommand> capturedCommands = commandCaptor.getAllValues();
        assertEquals(UUID.fromString("00001805-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getServiceUUID());
        assertEquals(UUID.fromString("00002A14-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getCharacteristicUUID());
    }

    private byte[] createValue(int value, int formatType, int offset) {
        int len = offset + getTypeLen(formatType);
        byte[] mValue = new byte[len];

        switch (formatType) {
            case FORMAT_SINT8:
                value = intToSignedBits(value, 8);
            case FORMAT_UINT8:
                mValue[offset] = (byte) (value & 0xFF);
                break;

            case FORMAT_SINT16:
                value = intToSignedBits(value, 16);
            case FORMAT_UINT16:
                mValue[offset++] = (byte) (value & 0xFF);
                mValue[offset] = (byte) ((value >> 8) & 0xFF);
                break;

            case FORMAT_SINT32:
                value = intToSignedBits(value, 32);
            case FORMAT_UINT32:
                mValue[offset++] = (byte) (value & 0xFF);
                mValue[offset++] = (byte) ((value >> 8) & 0xFF);
                mValue[offset++] = (byte) ((value >> 16) & 0xFF);
                mValue[offset] = (byte) ((value >> 24) & 0xFF);
                break;
        }

        return mValue;
    }

    private int getTypeLen(int formatType) {
        return formatType & 0xF;
    }

    private int intToSignedBits(int i, int size) {
        if (i < 0) {
            i = (1 << size - 1) + (i & ((1 << size - 1) - 1));
        }
        return i;
    }
}