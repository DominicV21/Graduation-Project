/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.philipsextension;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import chdsw.philips.com.haraldlib.HBDevice;
import chdsw.philips.com.haraldlib.HBReadCommand;
import chdsw.philips.com.haraldlib.HBWriteCommand;
import chdsw.philips.com.haraldlib.services.philipsextension.data.HBExtendedUserData;
import chdsw.philips.com.haraldlib.services.philipsextension.data.HBExtendedUserDataAthlete;
import chdsw.philips.com.haraldlib.services.philipsextension.data.HBExtendedUserDataNumber;
import chdsw.philips.com.haraldlib.services.philipsextension.object.HBRegisteredUser;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT16;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class HBPhilipsExtensionServiceHandlerTest {
    private HBPhilipsExtensionServiceHandler handler;

    @Mock
    private Context context;

    @Mock
    private HBPhilipsExtensionListenerHandler listener;

    @Mock
    private BluetoothGattCharacteristic characteristic;

    @Before
    public void setUp() {
        initMocks(this);

        handler = new HBPhilipsExtensionServiceHandler(context);
    }

    @Test
    public void addServiceListenerTest() {
        handler.addServiceListener("0.0.0.0", listener);
        assertEquals(handler.getServiceListener("0.0.0.0"), listener);
    }

    @Test
    public void removeServiceListenerTest() {
        handler.addServiceListener("0.0.0.0", listener);
        assertEquals(listener, handler.getServiceListener("0.0.0.0"));
        handler.removeServiceListener("0.0.0.0");
        assertEquals(handler.getServiceListener("0.0.0.0"), null);
    }

    @Test
    public void onWeightGoalValueUpdate() {
        int value = 10;
        when(characteristic.getUuid()).thenReturn(UUID.fromString("0000000-0001-0000-0000-000000000000"));
        when(characteristic.getIntValue(FORMAT_UINT16, 0)).thenReturn(value);

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        ArgumentCaptor<HBExtendedUserData> captor = ArgumentCaptor.forClass(HBExtendedUserData.class);
        verify(listener).onExtendedUserData(anyString(), captor.capture());

        List<HBExtendedUserData> capturedMeasurements = captor.getAllValues();
        assertEquals(value, ((HBExtendedUserDataNumber)capturedMeasurements.get(0)).getValue());
        assertEquals(HBExtendedUserDataType.WEIGHT_GOAL, capturedMeasurements.get(0).getExtendedUserDataType());
    }

    @Test
    public void onAthleteValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("0000000-0002-0000-0000-000000000000"));

        when(characteristic.getIntValue(FORMAT_UINT8, 0)).thenReturn(1);
        when(characteristic.getIntValue(FORMAT_UINT8, 1)).thenReturn(2);

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        ArgumentCaptor<HBExtendedUserData> captor = ArgumentCaptor.forClass(HBExtendedUserData.class);
        verify(listener).onExtendedUserData(anyString(), captor.capture());

        List<HBExtendedUserData> capturedMeasurements = captor.getAllValues();
        assertEquals(true, ((HBExtendedUserDataAthlete)capturedMeasurements.get(0)).getValue().isAthlete());
        assertEquals(2, ((HBExtendedUserDataAthlete)capturedMeasurements.get(0)).getValue().getAthleticLevel());
        assertEquals(HBExtendedUserDataType.ATHLETE, capturedMeasurements.get(0).getExtendedUserDataType());
    }

    @Test
    public void onRegisteredUsersTest() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00000002-0000-0000-0000-000000000000"));
        when(characteristic.getIntValue(FORMAT_UINT8, 0)).thenReturn(0x01);
        when(characteristic.getIntValue(FORMAT_UINT8, 1)).thenReturn(0x01);
        when(characteristic.getIntValue(FORMAT_UINT8, 2)).thenReturn(1);

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        String name1 = "Jane";
        String name2 = "Doe";
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00000003-0000-0000-0000-000000000000"));
        when(characteristic.getIntValue(FORMAT_UINT8, 0)).thenReturn(1);
        when(characteristic.getIntValue(FORMAT_UINT8, 1)).thenReturn(name1.length() + name2.length());

        when(characteristic.getIntValue(FORMAT_UINT8, 2)).thenReturn(0);
        when(characteristic.getStringValue(3)).thenReturn(name1);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        when(characteristic.getIntValue(FORMAT_UINT8, 2)).thenReturn(1);
        when(characteristic.getStringValue(3)).thenReturn(name2);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        List<HBRegisteredUser> registeredUsersList = new ArrayList<>();
        Class typeClass = registeredUsersList.getClass();

        ArgumentCaptor<List<HBRegisteredUser>> commandCaptor = ArgumentCaptor.forClass(typeClass);
        verify(listener).onRegisteredUsers(anyString(), commandCaptor.capture());

        List<List<HBRegisteredUser>> capturedCommands = commandCaptor.getAllValues();
        assertEquals(1, capturedCommands.get(0).size());
        assertEquals(name1 + name2, capturedCommands.get(0).get(0).getFirstName());
    }

    @Test
    public void onUserDeletedTest() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00000002-0000-0000-0000-000000000000"));

        when(characteristic.getIntValue(FORMAT_UINT8, 0)).thenReturn(0x02);
        when(characteristic.getIntValue(FORMAT_UINT8, 1)).thenReturn(0x01);

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        verify(listener).onUserDeleted(anyString());
    }

    @Test
    public void onInvalidRequestTest() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00000002-0000-0000-0000-000000000000"));

        when(characteristic.getIntValue(FORMAT_UINT8, 1)).thenReturn(0x02);

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        verify(listener).onInvalidRequest(anyString(), eq(0x02));
    }

    @Test
    public void getAllUsersTest() {
        HBDevice device = mock(HBDevice.class);
        handler.getAllUsers(device);
        verifyWriteCommand(device, UUID.fromString("00000002-0000-0000-0000-000000000000"), new byte[]{0x01});
    }

    @Test
    public void deleteUserTest() {
        HBDevice device = mock(HBDevice.class);
        handler.deleteUser(device, 2);
        verifyWriteCommand(device, UUID.fromString("00000002-0000-0000-0000-000000000000"), new byte[]{0x02, 0x02});
    }

    @Test
    public void deleteAllUsers() {
        HBDevice device = mock(HBDevice.class);
        handler.deleteAllUsers(device);
        verifyWriteCommand(device, UUID.fromString("00000002-0000-0000-0000-000000000000"), new byte[]{0x02, -1});// -1 equals 0xFF
    }

    @Test
    public void getUserDataTest() {
        HBDevice device = mock(HBDevice.class);
        handler.getExtendedUserData(device, HBExtendedUserDataType.WEIGHT_GOAL);
        verifyReadCommand(device, UUID.fromString("00000000-0001-0000-0000-000000000000"));
    }

    @Test
    public void setUserDataTest() {
        HBDevice device = mock(HBDevice.class);
        HBExtendedUserDataNumber data = new HBExtendedUserDataNumber(HBExtendedUserDataType.WEIGHT_GOAL, 5);
        handler.setExtendedUserData(device, data);
        verifyWriteCommand(device, UUID.fromString("00000000-0001-0000-0000-000000000000"), data.getData());
    }

    private void verifyReadCommand(HBDevice device, UUID characteristicUUID) {
        ArgumentCaptor<HBReadCommand> commandCaptor = ArgumentCaptor.forClass(HBReadCommand.class);
        verify(device).readCharacteristic(commandCaptor.capture());

        List<HBReadCommand> capturedCommands = commandCaptor.getAllValues();
        assertEquals(UUID.fromString("00000001-0000-0000-0000-000000000000"), capturedCommands.get(0).getServiceUUID());
        assertEquals(characteristicUUID, capturedCommands.get(0).getCharacteristicUUID());
    }

    private void verifyWriteCommand(HBDevice device, UUID characteristicUUID, byte[] data) {
        ArgumentCaptor<HBWriteCommand> commandCaptor = ArgumentCaptor.forClass(HBWriteCommand.class);
        verify(device).writeCharacteristic(commandCaptor.capture());

        List<HBWriteCommand> capturedCommands = commandCaptor.getAllValues();
        assertEquals(UUID.fromString("00000001-0000-0000-0000-000000000000"), capturedCommands.get(0).getServiceUUID());
        assertEquals(characteristicUUID, capturedCommands.get(0).getCharacteristicUUID());
        assertArrayEquals(data, capturedCommands.get(0).getValue());
    }
}