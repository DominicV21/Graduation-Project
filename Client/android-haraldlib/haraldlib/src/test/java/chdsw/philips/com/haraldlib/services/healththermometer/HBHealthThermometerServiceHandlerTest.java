/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.healththermometer;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import chdsw.philips.com.haraldlib.HBDevice;
import chdsw.philips.com.haraldlib.HBReadCommand;
import chdsw.philips.com.haraldlib.HBWriteCommand;
import chdsw.philips.com.haraldlib.services.healththermometer.object.HBTemperatureMeasurement;
import chdsw.philips.com.haraldlib.services.healththermometer.object.HBTemperatureUnit;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_FLOAT;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT16;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;
import static chdsw.philips.com.haraldlib.services.healththermometer.object.HBTemperatureType.GastroIntestinalTract;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class HBHealthThermometerServiceHandlerTest {
    private HBHealthThermometerServiceHandler handler;

    @Mock
    private Context context;

    @Mock
    private HBHealthThermometerListenerHandler listener;

    @Before
    public void setUp()  {
        initMocks(this);

        handler = new HBHealthThermometerServiceHandler(context);
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
    public void onTemperatureMeasurementValueWithFahrenheitAndTimeStampAndTypeUpdatedTest() {
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"));

        when(characteristic.getValue()).thenReturn(new byte[] { 0 });
        when(characteristic.getFloatValue(FORMAT_FLOAT, 1)).thenReturn(37.0f);

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        ArgumentCaptor<HBTemperatureMeasurement> measurementCaptor = ArgumentCaptor.forClass(HBTemperatureMeasurement.class);
        verify(listener).onTemperatureMeasurement(anyString(), measurementCaptor.capture());

        List<HBTemperatureMeasurement> capturedMeasurements = measurementCaptor.getAllValues();
        assertEquals(37.0f, capturedMeasurements.get(0).getTemperatureValue());
        assertEquals(HBTemperatureUnit.Celsius, capturedMeasurements.get(0).getUnit());
        assertEquals(null, capturedMeasurements.get(0).getTimestamp());
        assertEquals(null, capturedMeasurements.get(0).getType());
    }

    @Test
    public void onTemperatureMeasurementValueWithCelsiusAndNoTimeStampAndNoTypeUpdatedTest() {
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"));

        when(characteristic.getValue()).thenReturn(new byte[] { 7 });
        when(characteristic.getFloatValue(FORMAT_FLOAT, 1)).thenReturn(38.0f);

        GregorianCalendar calendar = new GregorianCalendar(2001, 6, 29, 12, 1, 30);
        when(characteristic.getIntValue(FORMAT_UINT16, 5)).thenReturn(calendar.get(GregorianCalendar.YEAR));
        when(characteristic.getIntValue(FORMAT_UINT8, 7)).thenReturn(calendar.get(GregorianCalendar.MONTH));
        when(characteristic.getIntValue(FORMAT_UINT8, 8)).thenReturn(calendar.get(GregorianCalendar.DAY_OF_MONTH));
        when(characteristic.getIntValue(FORMAT_UINT8, 9)).thenReturn(calendar.get(GregorianCalendar.HOUR_OF_DAY));
        when(characteristic.getIntValue(FORMAT_UINT8, 10)).thenReturn(calendar.get(GregorianCalendar.MINUTE));
        when(characteristic.getIntValue(FORMAT_UINT8, 11)).thenReturn(calendar.get(GregorianCalendar.SECOND));

        when(characteristic.getIntValue(FORMAT_UINT8, 12)).thenReturn(GastroIntestinalTract.getValue());

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        ArgumentCaptor<HBTemperatureMeasurement> measurementCaptor = ArgumentCaptor.forClass(HBTemperatureMeasurement.class);
        verify(listener).onTemperatureMeasurement(anyString(), measurementCaptor.capture());

        List<HBTemperatureMeasurement> capturedMeasurements = measurementCaptor.getAllValues();
        assertEquals(38.0f, capturedMeasurements.get(0).getTemperatureValue());
        assertEquals(HBTemperatureUnit.Fahrenheit, capturedMeasurements.get(0).getUnit());
        assertEquals(calendar.getTime(), capturedMeasurements.get(0).getTimestamp());
        assertEquals(GastroIntestinalTract, capturedMeasurements.get(0).getType());
    }

    @Test
    public void onTemperatureTypeValueUpdatedTest() {
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A1D-0000-1000-8000-00805f9b34fb"));
        when(characteristic.getIntValue(FORMAT_UINT8, 0)).thenReturn(5);

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        verify(listener).onTemperatureType("0.0.0.0", GastroIntestinalTract);
    }

    @Test
    public void onIntermediateTemperatureValueUpdatedTest() {
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A1E-0000-1000-8000-00805f9b34fb"));

        when(characteristic.getValue()).thenReturn(new byte[] { 0 });
        when(characteristic.getFloatValue(FORMAT_FLOAT, 1)).thenReturn(37.0f);

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        ArgumentCaptor<HBTemperatureMeasurement> measurementCaptor = ArgumentCaptor.forClass(HBTemperatureMeasurement.class);
        verify(listener).onIntermediateTemperature(anyString(), measurementCaptor.capture());

        List<HBTemperatureMeasurement> capturedMeasurements = measurementCaptor.getAllValues();
        assertEquals(37.0f, capturedMeasurements.get(0).getTemperatureValue());
        assertEquals(HBTemperatureUnit.Celsius, capturedMeasurements.get(0).getUnit());
        assertEquals(null, capturedMeasurements.get(0).getTimestamp());
        assertEquals(null, capturedMeasurements.get(0).getType());
    }

    @Test
    public void onMeasurementIntervalValueUpdatedTest() {
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A21-0000-1000-8000-00805f9b34fb"));
        when(characteristic.getIntValue(FORMAT_UINT16, 0)).thenReturn(1);

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        verify(listener).onMeasurementInterval("0.0.0.0", 1);
    }

    @Test
    public void getTemperatureTypeTest() {
        HBDevice device = mock(HBDevice.class);

        handler.addServiceListener("0.0.0.0", listener);
        handler.getTemperatureType(device);

        ArgumentCaptor<HBReadCommand> commandCaptor = ArgumentCaptor.forClass(HBReadCommand.class);
        verify(device).readCharacteristic(commandCaptor.capture());

        List<HBReadCommand> capturedCommands = commandCaptor.getAllValues();
        assertEquals(UUID.fromString("00001809-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getServiceUUID());
        assertEquals(UUID.fromString("00002A1D-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getCharacteristicUUID());
    }

    @Test
    public void getMeasurementIntervalTest() {
        HBDevice device = mock(HBDevice.class);

        handler.getMeasurementInterval(device);

        ArgumentCaptor<HBReadCommand> commandCaptor = ArgumentCaptor.forClass(HBReadCommand.class);
        verify(device).readCharacteristic(commandCaptor.capture());

        List<HBReadCommand> capturedCommands = commandCaptor.getAllValues();
        assertEquals(UUID.fromString("00001809-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getServiceUUID());
        assertEquals(UUID.fromString("00002A21-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getCharacteristicUUID());
    }

    @Test
    public void setMeasurementIntervalTest() {
        HBDevice device = mock(HBDevice.class);

        handler.setMeasurementInterval(device, 1);

        ArgumentCaptor<HBWriteCommand> commandCaptor = ArgumentCaptor.forClass(HBWriteCommand.class);
        verify(device).writeCharacteristic(commandCaptor.capture());

        List<HBWriteCommand> capturedCommands = commandCaptor.getAllValues();
        assertEquals(UUID.fromString("00001809-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getServiceUUID());
        assertEquals(UUID.fromString("00002A21-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getCharacteristicUUID());
        assertArrayEquals(new  byte[] { 1, 0 }, capturedCommands.get(0).getValue());
    }
}