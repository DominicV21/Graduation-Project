/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.weightscale;

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
import chdsw.philips.com.haraldlib.services.weightscale.object.HBMeasurementUnit;
import chdsw.philips.com.haraldlib.services.weightscale.object.HBWeightMeasurement;
import chdsw.philips.com.haraldlib.services.weightscale.object.HBWeightScaleFeature;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT16;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class HBWeightScaleServiceHandlerTest {
    private HBWeightScaleServiceHandler handler;

    @Mock
    private Context context;

    @Mock
    private HBWeightScaleListenerHandler listener;

    @Before
    public void setUp()  {
        initMocks(this);

        handler = new HBWeightScaleServiceHandler(context);
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
    public void onWeightScaleFeatureValueUpdatedTest() {
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A9E-0000-1000-8000-00805f9b34fb"));

        when(characteristic.getValue()).thenReturn(new byte[] {
                Byte.parseByte("0111111", 2),
                Byte.parseByte("0000011", 2),
                Byte.parseByte("0000000", 2),
                Byte.parseByte("000000", 2)
                 });

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        ArgumentCaptor<HBWeightScaleFeature> captor = ArgumentCaptor.forClass(HBWeightScaleFeature.class);
        verify(listener).onWeightScaleFeature(anyString(), captor.capture());

        List<HBWeightScaleFeature> capturedMeasurements = captor.getAllValues();
        assertEquals(true, capturedMeasurements.get(0).getTimeStampSupported());
        assertEquals(true, capturedMeasurements.get(0).getMultipleUsersSupported());
        assertEquals(true, capturedMeasurements.get(0).getBMISupported());
        assertEquals(7, capturedMeasurements.get(0).getWeightMeasurementResolution());
        assertEquals(3, capturedMeasurements.get(0).getHeightMeasurementResolution());
    }

    @Test
    public void onWeightMeasurementValueUpdatedTest() {
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A9D-0000-1000-8000-00805f9b34fb"));

        GregorianCalendar calendar = new GregorianCalendar(2001, 6, 29, 12, 1, 30);
        HBWeightMeasurement measurement = new HBWeightMeasurement();
        measurement.setWeight(25);
        measurement.setMeasurementUnit(HBMeasurementUnit.Imperial);
        measurement.setTimestamp(calendar.getTime());
        measurement.setUserID(1);
        measurement.setBMI(50);
        measurement.setHeight(100);

        when(characteristic.getValue()).thenReturn(new byte[] { 0b1111 });
        when(characteristic.getIntValue(FORMAT_UINT16, 1)).thenReturn(measurement.getWeight());

        when(characteristic.getIntValue(FORMAT_UINT16, 3)).thenReturn(calendar.get(GregorianCalendar.YEAR));
        when(characteristic.getIntValue(FORMAT_UINT8, 5)).thenReturn(calendar.get(GregorianCalendar.MONTH));
        when(characteristic.getIntValue(FORMAT_UINT8, 6)).thenReturn(calendar.get(GregorianCalendar.DAY_OF_MONTH));
        when(characteristic.getIntValue(FORMAT_UINT8, 7)).thenReturn(calendar.get(GregorianCalendar.HOUR_OF_DAY));
        when(characteristic.getIntValue(FORMAT_UINT8, 8)).thenReturn(calendar.get(GregorianCalendar.MINUTE));
        when(characteristic.getIntValue(FORMAT_UINT8, 9)).thenReturn(calendar.get(GregorianCalendar.SECOND));

        when(characteristic.getIntValue(FORMAT_UINT8, 10)).thenReturn(measurement.getUserID());
        when(characteristic.getIntValue(FORMAT_UINT16, 11)).thenReturn(measurement.getBMI());
        when(characteristic.getIntValue(FORMAT_UINT16, 13)).thenReturn(measurement.getHeight());

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        ArgumentCaptor<HBWeightMeasurement> captor = ArgumentCaptor.forClass(HBWeightMeasurement.class);
        verify(listener).onWeightMeasurement(anyString(), captor.capture());

        List<HBWeightMeasurement> capturedMeasurements = captor.getAllValues();
        assertEquals(measurement.getWeight(), capturedMeasurements.get(0).getWeight());
        assertEquals(measurement.getMeasurementUnit(), capturedMeasurements.get(0).getMeasurementUnit());
        assertEquals(measurement.getTimestamp(), capturedMeasurements.get(0).getTimestamp());
        assertEquals(measurement.getUserID(), capturedMeasurements.get(0).getUserID());
        assertEquals(measurement.getBMI(), capturedMeasurements.get(0).getBMI());
        assertEquals(measurement.getHeight(), capturedMeasurements.get(0).getHeight());
    }

    @Test
    public void onWeightMeasurementValueUpdatedUnitSiAndNullTest() {
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A9D-0000-1000-8000-00805f9b34fb"));

        HBWeightMeasurement measurement = new HBWeightMeasurement();
        measurement.setWeight(25);
        measurement.setMeasurementUnit(HBMeasurementUnit.SI);

        when(characteristic.getValue()).thenReturn(new byte[] { 0b0000 });
        when(characteristic.getIntValue(FORMAT_UINT16, 1)).thenReturn(measurement.getWeight());

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        ArgumentCaptor<HBWeightMeasurement> captor = ArgumentCaptor.forClass(HBWeightMeasurement.class);
        verify(listener).onWeightMeasurement(anyString(), captor.capture());

        List<HBWeightMeasurement> capturedMeasurements = captor.getAllValues();
        assertEquals(measurement.getWeight(), capturedMeasurements.get(0).getWeight());
        assertEquals(measurement.getMeasurementUnit(), capturedMeasurements.get(0).getMeasurementUnit());
        assertEquals(measurement.getTimestamp(), capturedMeasurements.get(0).getTimestamp());
        assertEquals(measurement.getUserID(), capturedMeasurements.get(0).getUserID());
        assertEquals(measurement.getBMI(), capturedMeasurements.get(0).getBMI());
        assertEquals(measurement.getHeight(), capturedMeasurements.get(0).getHeight());
    }

    @Test
    public void getWeightScaleFeatureTest() {
        HBDevice device = mock(HBDevice.class);

        handler.getWeightScaleFeature(device);

        ArgumentCaptor<HBReadCommand> commandCaptor = ArgumentCaptor.forClass(HBReadCommand.class);
        verify(device).readCharacteristic(commandCaptor.capture());

        List<HBReadCommand> capturedCommands = commandCaptor.getAllValues();
        assertEquals(UUID.fromString("0000181D-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getServiceUUID());
        assertEquals(UUID.fromString("00002A9E-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getCharacteristicUUID());
    }

}