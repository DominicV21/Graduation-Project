/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.deviceinformation;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.List;
import java.util.UUID;

import chdsw.philips.com.haraldlib.HBDevice;
import chdsw.philips.com.haraldlib.HBReadCommand;
import chdsw.philips.com.haraldlib.services.deviceinformation.object.HBPnPID;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT16;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class HBDeviceInformationServiceHandlerTest {
    private HBDeviceInformationServiceHandler handler;

    @Mock
    private Context context;

    @Mock
    private HBDeviceInformationListenerHandler listener;

    @Before
    public void setUp()  {
        initMocks(this);

        handler = new HBDeviceInformationServiceHandler(context);
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
    public void onManufacturerNameValueUpdatedTest() {
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A29-0000-1000-8000-00805f9b34fb"));
        when(characteristic.getStringValue(0)).thenReturn("value");

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        verify(listener).onManufacturerName("0.0.0.0", "value");
    }

    @Test
    public void onModelNumberValueUpdatedTest() {
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A24-0000-1000-8000-00805f9b34fb"));
        when(characteristic.getStringValue(0)).thenReturn("value");

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        verify(listener).onModelNumber("0.0.0.0", "value");
    }

    @Test
    public void onSerialNumberValueUpdatedTest() {
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A25-0000-1000-8000-00805f9b34fb"));
        when(characteristic.getStringValue(0)).thenReturn("value");

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        verify(listener).onSerialNumber("0.0.0.0", "value");
    }

    @Test
    public void onHardwareRevisionValueUpdatedTest() {
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A27-0000-1000-8000-00805f9b34fb"));
        when(characteristic.getStringValue(0)).thenReturn("value");

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        verify(listener).onHardwareRevision("0.0.0.0", "value");
    }

    @Test
    public void onFirmwareRevisionValueUpdatedTest() {
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A26-0000-1000-8000-00805f9b34fb"));
        when(characteristic.getStringValue(0)).thenReturn("value");

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        verify(listener).onFirmwareRevision("0.0.0.0", "value");
    }

    @Test
    public void onSoftwareRevisionValueUpdatedTest() {
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A28-0000-1000-8000-00805f9b34fb"));
        when(characteristic.getStringValue(0)).thenReturn("value");

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        verify(listener).onSoftwareRevision("0.0.0.0", "value");
    }

    @Test
    public void onSystemIDValueUpdatedTest() {
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A23-0000-1000-8000-00805f9b34fb"));
        byte[] value = new byte[] { 1, 2 };
        when(characteristic.getValue()).thenReturn(value);

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        verify(listener).onSystemID("0.0.0.0", value);
    }

    @Test
    public void onRegulatoryCertificationDataListValueUpdatedTest() {
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A2A-0000-1000-8000-00805f9b34fb"));
        byte[] value = new byte[] { 1, 2 };
        when(characteristic.getValue()).thenReturn(value);

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        verify(listener).onRegulatoryCertificationDataList("0.0.0.0", value);
    }

    @Test
    public void onPnPIDValueUpdatedTest() {
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A50-0000-1000-8000-00805f9b34fb"));

        when(characteristic.getIntValue(FORMAT_UINT8, 0)).thenReturn(10);
        when(characteristic.getIntValue(FORMAT_UINT16, 1)).thenReturn(20);
        when(characteristic.getIntValue(FORMAT_UINT16, 3)).thenReturn(30);
        when(characteristic.getIntValue(FORMAT_UINT16, 5)).thenReturn(40);

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        ArgumentCaptor<HBPnPID> valueCaptor = ArgumentCaptor.forClass(HBPnPID.class);
        verify(listener).onPnPID(anyString(), valueCaptor.capture());
        List<HBPnPID> captured = valueCaptor.getAllValues();
        assertEquals(10, captured.get(0).getVendorIDSource());
        assertEquals(20, captured.get(0).getVendorID());
        assertEquals(30, captured.get(0).getProductID());
        assertEquals(40, captured.get(0).getProductVersion());
    }

    @Test
    public void getManufacturerNameTest() {
        HBDevice device = mock(HBDevice.class);

        handler.getManufacturerName(device);

        ArgumentCaptor<HBReadCommand> commandCaptor = ArgumentCaptor.forClass(HBReadCommand.class);
        verify(device).readCharacteristic(commandCaptor.capture());

        List<HBReadCommand> capturedCommands = commandCaptor.getAllValues();
        assertEquals(UUID.fromString("0000180A-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getServiceUUID());
        assertEquals(UUID.fromString("00002A29-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getCharacteristicUUID());
    }

    @Test
    public void getModelNumberTest() {
        HBDevice device = mock(HBDevice.class);

        handler.getModelNumber(device);

        ArgumentCaptor<HBReadCommand> commandCaptor = ArgumentCaptor.forClass(HBReadCommand.class);
        verify(device).readCharacteristic(commandCaptor.capture());

        List<HBReadCommand> capturedCommands = commandCaptor.getAllValues();
        assertEquals(UUID.fromString("0000180A-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getServiceUUID());
        assertEquals(UUID.fromString("00002A24-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getCharacteristicUUID());
    }

    @Test
    public void getSerialNumberTest() {
        HBDevice device = mock(HBDevice.class);

        handler.getSerialNumber(device);

        ArgumentCaptor<HBReadCommand> commandCaptor = ArgumentCaptor.forClass(HBReadCommand.class);
        verify(device).readCharacteristic(commandCaptor.capture());

        List<HBReadCommand> capturedCommands = commandCaptor.getAllValues();
        assertEquals(UUID.fromString("0000180A-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getServiceUUID());
        assertEquals(UUID.fromString("00002A25-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getCharacteristicUUID());
    }

    @Test
    public void getHardwareRevisionTest() {
        HBDevice device = mock(HBDevice.class);

        handler.getHardwareRevision(device);

        ArgumentCaptor<HBReadCommand> commandCaptor = ArgumentCaptor.forClass(HBReadCommand.class);
        verify(device).readCharacteristic(commandCaptor.capture());

        List<HBReadCommand> capturedCommands = commandCaptor.getAllValues();
        assertEquals(UUID.fromString("0000180A-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getServiceUUID());
        assertEquals(UUID.fromString("00002A27-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getCharacteristicUUID());
    }

    @Test
    public void getFirmwareRevisionTest() {
        HBDevice device = mock(HBDevice.class);

        handler.getFirmwareRevision(device);

        ArgumentCaptor<HBReadCommand> commandCaptor = ArgumentCaptor.forClass(HBReadCommand.class);
        verify(device).readCharacteristic(commandCaptor.capture());

        List<HBReadCommand> capturedCommands = commandCaptor.getAllValues();
        assertEquals(UUID.fromString("0000180A-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getServiceUUID());
        assertEquals(UUID.fromString("00002A26-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getCharacteristicUUID());
    }

    @Test
    public void getSoftwareRevisionTest() {
        HBDevice device = mock(HBDevice.class);

        handler.getSoftwareRevision(device);

        ArgumentCaptor<HBReadCommand> commandCaptor = ArgumentCaptor.forClass(HBReadCommand.class);
        verify(device).readCharacteristic(commandCaptor.capture());

        List<HBReadCommand> capturedCommands = commandCaptor.getAllValues();
        assertEquals(UUID.fromString("0000180A-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getServiceUUID());
        assertEquals(UUID.fromString("00002A28-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getCharacteristicUUID());
    }

    @Test
    public void getSystemIDTest() {
        HBDevice device = mock(HBDevice.class);

        handler.getSystemID(device);

        ArgumentCaptor<HBReadCommand> commandCaptor = ArgumentCaptor.forClass(HBReadCommand.class);
        verify(device).readCharacteristic(commandCaptor.capture());

        List<HBReadCommand> capturedCommands = commandCaptor.getAllValues();
        assertEquals(UUID.fromString("0000180A-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getServiceUUID());
        assertEquals(UUID.fromString("00002A23-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getCharacteristicUUID());
    }

    @Test
    public void getRegulatoryCertificationDataListTest() {
        HBDevice device = mock(HBDevice.class);

        handler.getRegulatoryCertificationDataList(device);

        ArgumentCaptor<HBReadCommand> commandCaptor = ArgumentCaptor.forClass(HBReadCommand.class);
        verify(device).readCharacteristic(commandCaptor.capture());

        List<HBReadCommand> capturedCommands = commandCaptor.getAllValues();
        assertEquals(UUID.fromString("0000180A-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getServiceUUID());
        assertEquals(UUID.fromString("00002A2A-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getCharacteristicUUID());
    }

    @Test
    public void getPnPIDTest() {
        HBDevice device = mock(HBDevice.class);

        handler.getPnPID(device);

        ArgumentCaptor<HBReadCommand> commandCaptor = ArgumentCaptor.forClass(HBReadCommand.class);
        verify(device).readCharacteristic(commandCaptor.capture());

        List<HBReadCommand> capturedCommands = commandCaptor.getAllValues();
        assertEquals(UUID.fromString("0000180A-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getServiceUUID());
        assertEquals(UUID.fromString("00002A50-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getCharacteristicUUID());
    }


}