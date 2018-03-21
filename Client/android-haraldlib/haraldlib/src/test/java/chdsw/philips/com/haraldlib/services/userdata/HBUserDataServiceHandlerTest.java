/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.userdata;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import chdsw.philips.com.haraldlib.HBDevice;
import chdsw.philips.com.haraldlib.HBReadCommand;
import chdsw.philips.com.haraldlib.HBWriteCommand;
import chdsw.philips.com.haraldlib.services.userdata.data.HBUserData;
import chdsw.philips.com.haraldlib.services.userdata.data.HBUserDataCalendar;
import chdsw.philips.com.haraldlib.services.userdata.data.HBUserDataFiveZoneHearthRateLimits;
import chdsw.philips.com.haraldlib.services.userdata.data.HBUserDataGender;
import chdsw.philips.com.haraldlib.services.userdata.data.HBUserDataNumber;
import chdsw.philips.com.haraldlib.services.userdata.data.HBUserDataSportType;
import chdsw.philips.com.haraldlib.services.userdata.data.HBUserDataString;
import chdsw.philips.com.haraldlib.services.userdata.data.HBUserDataThreeZoneHearthRateLimits;
import chdsw.philips.com.haraldlib.services.userdata.data.HBUserDataTwoZoneHearthRateLimits;
import chdsw.philips.com.haraldlib.services.userdata.object.HBGender;
import chdsw.philips.com.haraldlib.services.userdata.object.HBSportType;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT16;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT32;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;
import static chdsw.philips.com.haraldlib.services.userdata.object.HBSportType.CrossTraining;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class HBUserDataServiceHandlerTest {
    private HBUserDataServiceHandler handler;

    @Mock
    private Context context;

    @Mock
    private HBUserDataListenerHandler listener;

    @Mock
    private BluetoothGattCharacteristic characteristic;

    @Before
    public void setUp() {
        initMocks(this);

        handler = new HBUserDataServiceHandler(context);
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
    public void onFirstNameValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A8A-0000-1000-8000-00805f9b34fb"));
        verifyStringValue(characteristic, HBUserDataType.FIRST_NAME);
    }

    @Test
    public void onLastNameValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A90-0000-1000-8000-00805f9b34fb"));
        verifyStringValue(characteristic, HBUserDataType.LAST_NAME);
    }

    @Test
    public void onEmailAddressValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A87-0000-1000-8000-00805f9b34fb"));
        verifyStringValue(characteristic, HBUserDataType.EMAIL_ADDRESS);
    }

    @Test
    public void onLanguageValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002AA2-0000-1000-8000-00805f9b34fb"));
        verifyStringValue(characteristic, HBUserDataType.LANGUAGE);
    }

    @Test
    public void onAgeValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A80-0000-1000-8000-00805f9b34fb"));
        verifyIntValue(characteristic, HBUserDataType.AGE, FORMAT_UINT8);
    }

    @Test
    public void onVO2MaxValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A96-0000-1000-8000-00805f9b34fb"));
        verifyIntValue(characteristic, HBUserDataType.VO2_MAX, FORMAT_UINT8);
    }

    @Test
    public void onHearthRateValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A8D-0000-1000-8000-00805f9b34fb"));
        verifyIntValue(characteristic, HBUserDataType.HEARTH_RATE_MAX, FORMAT_UINT8);
    }

    @Test
    public void onRestingHearthRateValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A92-0000-1000-8000-00805f9b34fb"));
        verifyIntValue(characteristic, HBUserDataType.RESTING_HEARTH_RATE, FORMAT_UINT8);
    }

    @Test
    public void onMaxRecommendedHearthRateValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A91-0000-1000-8000-00805f9b34fb"));
        verifyIntValue(characteristic, HBUserDataType.MAX_RECOMMENDED_HEARTH_RATE, FORMAT_UINT8);
    }

    @Test
    public void onAerobicThresholdValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A7F-0000-1000-8000-00805f9b34fb"));
        verifyIntValue(characteristic, HBUserDataType.AEROBIC_THRESHOLD, FORMAT_UINT8);
    }

    @Test
    public void onAnaerobicThresholdValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A83-0000-1000-8000-00805f9b34fb"));
        verifyIntValue(characteristic, HBUserDataType.ANAEROBIC_THRESHOLD, FORMAT_UINT8);
    }

    @Test
    public void onFatBurnLowerValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A88-0000-1000-8000-00805f9b34fb"));
        verifyIntValue(characteristic, HBUserDataType.FAT_BURN_LOWER_LIMIT, FORMAT_UINT8);
    }

    @Test
    public void onFatBurnUpperValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A89-0000-1000-8000-00805f9b34fb"));
        verifyIntValue(characteristic, HBUserDataType.FAT_BURN_UPPER_LIMIT, FORMAT_UINT8);
    }

    @Test
    public void onAerobicHeartRateLowerValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A7E-0000-1000-8000-00805f9b34fb"));
        verifyIntValue(characteristic, HBUserDataType.AEROBIC_HEARTH_RATE_LOWER_LIMIT, FORMAT_UINT8);
    }

    @Test
    public void onAerobicHeartRateUpperValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A84-0000-1000-8000-00805f9b34fb"));
        verifyIntValue(characteristic, HBUserDataType.AEROBIC_HEARTH_RATE_UPPER_LIMIT, FORMAT_UINT8);
    }

    @Test
    public void onAnaerobicHeartRateLowerValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A81-0000-1000-8000-00805f9b34fb"));
        verifyIntValue(characteristic, HBUserDataType.ANAEROBIC_HEARTH_RATE_LOWER_LIMIT, FORMAT_UINT8);
    }

    @Test
    public void onAnaerobicHeartRateUpperValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A82-0000-1000-8000-00805f9b34fb"));
        verifyIntValue(characteristic, HBUserDataType.ANAEROBIC_HEARTH_RATE_UPPER_LIMIT, FORMAT_UINT8);
    }

    @Test
    public void onUserIndexValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A9A-0000-1000-8000-00805f9b34fb"));
        verifyIntValue(characteristic, HBUserDataType.USER_INDEX, FORMAT_UINT8);
    }

    @Test
    public void onDateOfBirthValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A85-0000-1000-8000-00805f9b34fb"));
        verifyDateValue(characteristic, HBUserDataType.DATE_OF_BIRTH);
    }

    @Test
    public void onDateOfThresholdValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A86-0000-1000-8000-00805f9b34fb"));
        verifyDateValue(characteristic, HBUserDataType.DATE_OF_THRESHOLD);
    }

    @Test
    public void onGenderValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A8C-0000-1000-8000-00805f9b34fb"));

        when(characteristic.getIntValue(FORMAT_UINT8, 0)).thenReturn(3);

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        ArgumentCaptor<HBUserData> captor = ArgumentCaptor.forClass(HBUserData.class);
        verify(listener).onUserData(anyString(), captor.capture());

        List<HBUserData> capturedMeasurements = captor.getAllValues();
        assertEquals(HBGender.Unspecified, ((HBUserDataGender)capturedMeasurements.get(0)).getValue());
        assertEquals(HBUserDataType.GENDER, capturedMeasurements.get(0).getUserDataType());

    }

    @Test
    public void onWeightValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A98-0000-1000-8000-00805f9b34fb"));

        when(characteristic.getIntValue(FORMAT_UINT16, 0)).thenReturn((int)(75 / 0.005f));

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        ArgumentCaptor<HBUserData> captor = ArgumentCaptor.forClass(HBUserData.class);
        verify(listener).onUserData(anyString(), captor.capture());

        List<HBUserData> capturedMeasurements = captor.getAllValues();
        assertEquals(75f, ((HBUserDataNumber)capturedMeasurements.get(0)).getValue().floatValue());
        assertEquals(HBUserDataType.WEIGHT, capturedMeasurements.get(0).getUserDataType());

    }

    @Test
    public void onHeightValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A8E-0000-1000-8000-00805f9b34fb"));

        when(characteristic.getIntValue(FORMAT_UINT16, 0)).thenReturn((int)(1.5 / 0.01f));

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        ArgumentCaptor<HBUserData> captor = ArgumentCaptor.forClass(HBUserData.class);
        verify(listener).onUserData(anyString(), captor.capture());

        List<HBUserData> capturedMeasurements = captor.getAllValues();
        assertEquals(1.5f, ((HBUserDataNumber)capturedMeasurements.get(0)).getValue().floatValue());
        assertEquals(HBUserDataType.HEIGHT, capturedMeasurements.get(0).getUserDataType());

    }

    @Test
    public void onWaitsCircumferenceValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A97-0000-1000-8000-00805f9b34fb"));

        when(characteristic.getIntValue(FORMAT_UINT16, 0)).thenReturn((int)(1.5 / 0.01f));

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        ArgumentCaptor<HBUserData> captor = ArgumentCaptor.forClass(HBUserData.class);
        verify(listener).onUserData(anyString(), captor.capture());

        List<HBUserData> capturedMeasurements = captor.getAllValues();
        assertEquals(1.5f, ((HBUserDataNumber)capturedMeasurements.get(0)).getValue().floatValue());
        assertEquals(HBUserDataType.WAIST_CIRCUMFERENCE, capturedMeasurements.get(0).getUserDataType());

    }

    @Test
    public void onHipCircumferenceValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A8F-0000-1000-8000-00805f9b34fb"));

        when(characteristic.getIntValue(FORMAT_UINT16, 0)).thenReturn((int)(1.5 / 0.01f));

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        ArgumentCaptor<HBUserData> captor = ArgumentCaptor.forClass(HBUserData.class);
        verify(listener).onUserData(anyString(), captor.capture());

        List<HBUserData> capturedMeasurements = captor.getAllValues();
        assertEquals(1.5f, ((HBUserDataNumber)capturedMeasurements.get(0)).getValue().floatValue());
        assertEquals(HBUserDataType.HIP_CIRCUMFERENCE, capturedMeasurements.get(0).getUserDataType());

    }

    @Test
    public void onSportTypeValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A93-0000-1000-8000-00805f9b34fb"));

        when(characteristic.getIntValue(FORMAT_UINT8, 0)).thenReturn(4);

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        ArgumentCaptor<HBUserData> captor = ArgumentCaptor.forClass(HBUserData.class);
        verify(listener).onUserData(anyString(), captor.capture());

        List<HBUserData> capturedMeasurements = captor.getAllValues();
        assertEquals(CrossTraining, ((HBUserDataSportType)capturedMeasurements.get(0)).getValue());
        assertEquals(HBUserDataType.SPORT_TYPE, capturedMeasurements.get(0).getUserDataType());

    }

    @Test
    public void onFiveZoneHearthRateLimitsValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A8B-0000-1000-8000-00805f9b34fb"));

        when(characteristic.getIntValue(FORMAT_UINT8, 0)).thenReturn(1);
        when(characteristic.getIntValue(FORMAT_UINT8, 1)).thenReturn(2);
        when(characteristic.getIntValue(FORMAT_UINT8, 2)).thenReturn(3);
        when(characteristic.getIntValue(FORMAT_UINT8, 3)).thenReturn(4);

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        ArgumentCaptor<HBUserData> captor = ArgumentCaptor.forClass(HBUserData.class);
        verify(listener).onUserData(anyString(), captor.capture());

        List<HBUserData> capturedMeasurements = captor.getAllValues();
        assertEquals(1, ((HBUserDataFiveZoneHearthRateLimits)capturedMeasurements.get(0)).getValue().getVeryLightToLightLimit());
        assertEquals(2, ((HBUserDataFiveZoneHearthRateLimits)capturedMeasurements.get(0)).getValue().getLightToModerateLimit());
        assertEquals(3, ((HBUserDataFiveZoneHearthRateLimits)capturedMeasurements.get(0)).getValue().getModerateToHardLimit());
        assertEquals(4, ((HBUserDataFiveZoneHearthRateLimits)capturedMeasurements.get(0)).getValue().getHardToMaximumLimit());
        assertEquals(HBUserDataType.FIVE_ZONE_HEARTH_RATE_LIMITS, capturedMeasurements.get(0).getUserDataType());

    }

    @Test
    public void onThreeZoneHearthRateLimitsValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A94-0000-1000-8000-00805f9b34fb"));

        when(characteristic.getIntValue(FORMAT_UINT8, 0)).thenReturn(1);
        when(characteristic.getIntValue(FORMAT_UINT8, 1)).thenReturn(2);

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        ArgumentCaptor<HBUserData> captor = ArgumentCaptor.forClass(HBUserData.class);
        verify(listener).onUserData(anyString(), captor.capture());

        List<HBUserData> capturedMeasurements = captor.getAllValues();
        assertEquals(1, ((HBUserDataThreeZoneHearthRateLimits)capturedMeasurements.get(0)).getValue().getLightToModerateLimit());
        assertEquals(2, ((HBUserDataThreeZoneHearthRateLimits)capturedMeasurements.get(0)).getValue().getModerateToHardLimit());
        assertEquals(HBUserDataType.THREE_ZONE_HEARTH_RATE_LIMITS, capturedMeasurements.get(0).getUserDataType());

    }

    @Test
    public void onTwoZoneHearthRateLimitsValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A95-0000-1000-8000-00805f9b34fb"));

        when(characteristic.getIntValue(FORMAT_UINT8, 0)).thenReturn(1);

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        ArgumentCaptor<HBUserData> captor = ArgumentCaptor.forClass(HBUserData.class);
        verify(listener).onUserData(anyString(), captor.capture());

        List<HBUserData> capturedMeasurements = captor.getAllValues();
        assertEquals(1, ((HBUserDataTwoZoneHearthRateLimits)capturedMeasurements.get(0)).getValue().getFatBurnFitnessLimit());
        assertEquals(HBUserDataType.TWO_ZONE_HEARTH_RATE_LIMITS, capturedMeasurements.get(0).getUserDataType());

    }

    @Test
    public void onDatabaseChangeIncrementValueUpdate() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A99-0000-1000-8000-00805f9b34fb"));

        when(characteristic.getIntValue(FORMAT_UINT32, 0)).thenReturn(100);

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        ArgumentCaptor<HBUserData> captor = ArgumentCaptor.forClass(HBUserData.class);
        verify(listener).onUserData(anyString(), captor.capture());

        List<HBUserData> capturedMeasurements = captor.getAllValues();
        assertEquals(100, ((HBUserDataNumber)capturedMeasurements.get(0)).getValue());
        assertEquals(HBUserDataType.DATABASE_CHANGE_INCREMENT, capturedMeasurements.get(0).getUserDataType());

    }

    @Test
    public void onUserRegisteredTest() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A9F-0000-1000-8000-00805f9b34fb"));

        when(characteristic.getIntValue(FORMAT_UINT8, 0)).thenReturn(0x01);
        when(characteristic.getIntValue(FORMAT_UINT8, 1)).thenReturn(0x01);
        when(characteristic.getIntValue(FORMAT_UINT8, 2)).thenReturn(3);

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        verify(listener).onUserRegistered(anyString(), eq(3));
    }

    @Test
    public void onUserSetTest() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A9F-0000-1000-8000-00805f9b34fb"));

        when(characteristic.getIntValue(FORMAT_UINT8, 0)).thenReturn(0x01);
        when(characteristic.getIntValue(FORMAT_UINT8, 1)).thenReturn(0x02);

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        verify(listener).onUserSet(anyString());
    }

    @Test
    public void onUserDeletedTest() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A9F-0000-1000-8000-00805f9b34fb"));

        when(characteristic.getIntValue(FORMAT_UINT8, 0)).thenReturn(0x01);
        when(characteristic.getIntValue(FORMAT_UINT8, 1)).thenReturn(0x03);

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        verify(listener).onUserDeleted(anyString(), 1);
    }

    @Test
    public void onInvalidRequestTest() {
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A9F-0000-1000-8000-00805f9b34fb"));

        when(characteristic.getIntValue(FORMAT_UINT8, 0)).thenReturn(0x02);

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        verify(listener).onInvalidRequest(anyString(), eq(0x02));
    }

    @Test
    public void registerUserTest() {
        HBDevice device = mock(HBDevice.class);
        handler.registerUser(device, 1111);
        verifyWriteCommand(device, UUID.fromString("00002A9F-0000-1000-8000-00805f9b34fb"), new byte[]{0x01, 0x57, 0x04});
    }

    @Test
    public void setUserTest() {
        HBDevice device = mock(HBDevice.class);
        handler.setUser(device, 2, 1111);
        verifyWriteCommand(device, UUID.fromString("00002A9F-0000-1000-8000-00805f9b34fb"), new byte[]{0x02, 0x02, 0x57, 0x04});
    }

    @Test
    public void deleteCurrentUserTest() {
        HBDevice device = mock(HBDevice.class);
        handler.deleteCurrentUser(device);
        verifyWriteCommand(device, UUID.fromString("00002A9F-0000-1000-8000-00805f9b34fb"), new byte[]{0x03});
    }

    @Test
    public void getUserDataTest() {
        HBDevice device = mock(HBDevice.class);
        handler.getUserData(device, HBUserDataType.SPORT_TYPE);
        verifyReadCommand(device, UUID.fromString("00002A93-0000-1000-8000-00805f9b34fb"));
    }

    @Test
    public void setUserDataTest() {
        HBDevice device = mock(HBDevice.class);
        HBUserDataSportType data = new HBUserDataSportType(HBSportType.Climbing);
        handler.setUserData(device, data);
        verifyWriteCommand(device, UUID.fromString("00002A93-0000-1000-8000-00805f9b34fb"), data.getData());
    }

    private void verifyReadCommand(HBDevice device, UUID characteristicUUID) {
        ArgumentCaptor<HBReadCommand> commandCaptor = ArgumentCaptor.forClass(HBReadCommand.class);
        verify(device).readCharacteristic(commandCaptor.capture());

        List<HBReadCommand> capturedCommands = commandCaptor.getAllValues();
        assertEquals(UUID.fromString("0000181C-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getServiceUUID());
        assertEquals(characteristicUUID, capturedCommands.get(0).getCharacteristicUUID());
    }

    private void verifyWriteCommand(HBDevice device, UUID characteristicUUID, byte[] data) {
        ArgumentCaptor<HBWriteCommand> commandCaptor = ArgumentCaptor.forClass(HBWriteCommand.class);
        verify(device).writeCharacteristic(commandCaptor.capture());

        List<HBWriteCommand> capturedCommands = commandCaptor.getAllValues();
        assertEquals(UUID.fromString("0000181C-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getServiceUUID());
        assertEquals(characteristicUUID, capturedCommands.get(0).getCharacteristicUUID());
        assertArrayEquals(data, capturedCommands.get(0).getValue());
    }

    private void verifyStringValue(BluetoothGattCharacteristic characteristic, HBUserDataType type) {
        String value = "value";
        when(characteristic.getStringValue(0)).thenReturn(value);

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        ArgumentCaptor<HBUserData> captor = ArgumentCaptor.forClass(HBUserData.class);
        verify(listener).onUserData(anyString(), captor.capture());

        List<HBUserData> capturedMeasurements = captor.getAllValues();
        assertEquals(value, ((HBUserDataString)capturedMeasurements.get(0)).getValue());
        assertEquals(type, capturedMeasurements.get(0).getUserDataType());
    }

    private void verifyIntValue(BluetoothGattCharacteristic characteristic, HBUserDataType type, int formatType) {
        int value = 10;
        when(characteristic.getIntValue(formatType, 0)).thenReturn(value);

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        ArgumentCaptor<HBUserData> captor = ArgumentCaptor.forClass(HBUserData.class);
        verify(listener).onUserData(anyString(), captor.capture());

        List<HBUserData> capturedMeasurements = captor.getAllValues();
        assertEquals(value, ((HBUserDataNumber)capturedMeasurements.get(0)).getValue());
        assertEquals(type, capturedMeasurements.get(0).getUserDataType());
    }

    private void verifyDateValue(BluetoothGattCharacteristic characteristic, HBUserDataType type) {
        Calendar value = Calendar.getInstance();
        value.set(2017, 2, 3);

        when(characteristic.getIntValue(FORMAT_UINT16, 0)).thenReturn(value.get(Calendar.YEAR));
        when(characteristic.getIntValue(FORMAT_UINT8, 2)).thenReturn(value.get(Calendar.MONTH));
        when(characteristic.getIntValue(FORMAT_UINT8, 3)).thenReturn(value.get(Calendar.DATE));

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        ArgumentCaptor<HBUserData> captor = ArgumentCaptor.forClass(HBUserData.class);
        verify(listener).onUserData(anyString(), captor.capture());

        List<HBUserData> capturedMeasurements = captor.getAllValues();
        assertEquals(value.get(Calendar.YEAR), ((HBUserDataCalendar)capturedMeasurements.get(0)).getValue().get(Calendar.YEAR));
        assertEquals(value.get(Calendar.MONTH), ((HBUserDataCalendar)capturedMeasurements.get(0)).getValue().get(Calendar.MONTH));
        assertEquals(value.get(Calendar.DATE), ((HBUserDataCalendar)capturedMeasurements.get(0)).getValue().get(Calendar.DATE));
        assertEquals(type, capturedMeasurements.get(0).getUserDataType());
    }
}