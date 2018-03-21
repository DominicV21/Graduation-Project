/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.userdata;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import chdsw.philips.com.haraldlib.HBDevice;
import chdsw.philips.com.haraldlib.HBLogger;
import chdsw.philips.com.haraldlib.HBReadCommand;
import chdsw.philips.com.haraldlib.HBWriteCommand;
import chdsw.philips.com.haraldlib.services.HBServiceHandler;
import chdsw.philips.com.haraldlib.services.userdata.data.HBUserData;
import chdsw.philips.com.haraldlib.services.userdata.data.HBUserDataCalendar;
import chdsw.philips.com.haraldlib.services.userdata.data.HBUserDataFiveZoneHearthRateLimits;
import chdsw.philips.com.haraldlib.services.userdata.data.HBUserDataGender;
import chdsw.philips.com.haraldlib.services.userdata.data.HBUserDataNumber;
import chdsw.philips.com.haraldlib.services.userdata.data.HBUserDataSportType;
import chdsw.philips.com.haraldlib.services.userdata.data.HBUserDataString;
import chdsw.philips.com.haraldlib.services.userdata.data.HBUserDataThreeZoneHearthRateLimits;
import chdsw.philips.com.haraldlib.services.userdata.data.HBUserDataTwoZoneHearthRateLimits;
import chdsw.philips.com.haraldlib.services.userdata.object.HBFiveZoneHearthRateLimits;
import chdsw.philips.com.haraldlib.services.userdata.object.HBGender;
import chdsw.philips.com.haraldlib.services.userdata.object.HBRegisteredUser;
import chdsw.philips.com.haraldlib.services.userdata.object.HBSportType;
import chdsw.philips.com.haraldlib.services.userdata.object.HBThreeZoneHearthRateLimits;
import chdsw.philips.com.haraldlib.services.userdata.object.HBTwoZoneHearthRateLimits;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT16;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT32;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;
import static chdsw.philips.com.haraldlib.services.HBDataParser.createValue;
import static chdsw.philips.com.haraldlib.services.HBDataParser.getDate;
import static chdsw.philips.com.haraldlib.services.HBDataParser.getTypeLen;
import static chdsw.philips.com.haraldlib.services.HBDataParser.mergeArrays;

public class HBUserDataServiceHandler extends HBServiceHandler<HBUserDataListenerHandler> {
    public static final String TAG = "HBUserDataServiceHandler";
    public static final UUID SERVICE_UUID =
            UUID.fromString("0000181C-0000-1000-8000-00805f9b34fb");

    private static final UUID USER_CONTROL_POINT_CHARACTERISTIC_UUID =
            UUID.fromString("00002A9F-0000-1000-8000-00805f9b34fb");

    private static final UUID REGISTERED_USER_CHARACTERISTIC_UUID =
            UUID.fromString("00007F00-0000-1000-8000-00805f9b34fb");

    private int numberOfUsers;
    private int sequenceNumberInProgress = 0;
    private int databaseChangeIncrement = 0;
    private Map<Integer, HBRegisteredUser> registeredUsers;

    private static final byte UDS_CP_REGISTER_NEW_USER              = 0x01;
    private static final byte UDS_CP_CONSENT                        = 0x02;
    private static final byte UDS_CP_DELETE_USER_DATA               = 0x03;
    private static final byte UDS_CP_LIST_ALL_USERS                 = 0x04;
    private static final byte UDS_CP_DELETE_USERS                   = 0x05;
    private static final byte UDS_CP_RESPONSE                       = 0x20;

    private static final byte UDS_CP_RESP_VALUE_SUCCESS             = 0x01;
    private static final byte UDS_CP_RESP_OP_CODE_NOT_SUPPORTED     = 0x02;
    private static final byte UDS_CP_RESP_INVALID_PARAMETER         = 0x03;
    private static final byte UDS_CP_RESP_OPERATION_FAILED          = 0x04;
    private static final byte UDS_CP_RESP_USER_NOT_AUTHORIZED       = 0x05;


    private static final int UDS_DELETE_ALL_USERS                   = 0xFF;

    private static final int BLOOD_PRESSURE_SERVICE_UUID            = 0x1810;
    private static final int INTERMEDIATE_CUFF_PRESSURE_SERVICE_UUID = 0x2A36;

    /**
     * Construct new user data service handler.
     *
     * @param context Application context.
     */
    public HBUserDataServiceHandler(Context context) {
        super(context);
        this.registeredUsers = new HashMap<>();
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
        HBUserDataType type = HBUserDataType.fromCharacteristicUUID(characteristicUUID);

        if (type != null) {
            HBLogger.v(TAG, String.format("characteristicValueUpdated device: %s, type: %s", deviceAddress, type));
            HBUserData data = null;

            switch (type) {
                case FIRST_NAME:
                case LAST_NAME:
                case EMAIL_ADDRESS:
                case LANGUAGE:
                    data = new HBUserDataString(type, characteristic.getStringValue(0));
                    break;
                case AGE:
                case VO2_MAX:
                case HEARTH_RATE_MAX:
                case RESTING_HEARTH_RATE:
                case MAX_RECOMMENDED_HEARTH_RATE:
                case AEROBIC_THRESHOLD:
                case ANAEROBIC_THRESHOLD:
                case FAT_BURN_LOWER_LIMIT:
                case FAT_BURN_UPPER_LIMIT:
                case AEROBIC_HEARTH_RATE_LOWER_LIMIT:
                case AEROBIC_HEARTH_RATE_UPPER_LIMIT:
                case ANAEROBIC_HEARTH_RATE_LOWER_LIMIT:
                case ANAEROBIC_HEARTH_RATE_UPPER_LIMIT:
                case USER_INDEX:
                    data = new HBUserDataNumber(type, characteristic.getIntValue(FORMAT_UINT8, 0));
                    break;
                case DATE_OF_BIRTH:
                case DATE_OF_THRESHOLD:
                    data = new HBUserDataCalendar(type, getDate(characteristic, 0));
                    break;
                case GENDER:
                    HBGender gender = HBGender.fromValue(characteristic.getIntValue(FORMAT_UINT8, 0));
                    data = new HBUserDataGender(gender);
                    break;
                case WEIGHT:
                    int weight = characteristic.getIntValue(FORMAT_UINT16, 0);
                    float actualWeight = ((float) weight) * 0.005f;
                    data = new HBUserDataNumber(type, actualWeight);
                    break;
                case HEIGHT:
                case WAIST_CIRCUMFERENCE:
                case HIP_CIRCUMFERENCE:
                    int value = characteristic.getIntValue(FORMAT_UINT16, 0);
                    float actualValue = ((float) value) * 0.01f;
                    data = new HBUserDataNumber(type, actualValue);
                    break;
                case SPORT_TYPE:
                    HBSportType sportType = HBSportType.fromValue(characteristic.getIntValue(FORMAT_UINT8, 0));
                    data = new HBUserDataSportType(sportType);
                    break;
                case FIVE_ZONE_HEARTH_RATE_LIMITS:
                    data = getFiveZoneHeartRateLimits(characteristic);
                    break;
                case THREE_ZONE_HEARTH_RATE_LIMITS:
                    data = getThreeZoneHeartRateLimits(characteristic);
                    break;
                case TWO_ZONE_HEARTH_RATE_LIMITS:
                    data = getTwoZoneHeartRateLimits(characteristic);
                    break;
                case DATABASE_CHANGE_INCREMENT:
                    data = new HBUserDataNumber(type, characteristic.getIntValue(FORMAT_UINT32, 0));
                    break;
            }

            HBUserDataListenerHandler listener = getServiceListener(deviceAddress);
            if (listener != null)
                listener.onUserData(deviceAddress, data);
        } else {
            if (characteristicUUID.equals(USER_CONTROL_POINT_CHARACTERISTIC_UUID)) {
                getUserControlPointValue(deviceAddress, characteristic);
            }
            if(characteristicUUID.equals(REGISTERED_USER_CHARACTERISTIC_UUID)) {
                getRegisteredUserValue(deviceAddress, characteristic);
            }
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

        HBUserDataListenerHandler listener = getServiceListener(deviceAddress);
        if (listener != null) {
            listener.onInvalidRequest(deviceAddress, status);
        }
    }

    /**
     * Register a new user on the device with given consent code
     *
     * @param device      Device to execute characteristic write on
     * @param consentCode Consent code of the new user
     */
    public void registerUser(HBDevice device, int consentCode) {
        HBLogger.v(TAG, String.format("registerUser device: %s, consentCode: %d", device.getAddress(), consentCode));
        byte[] opCodeData = new byte[]{UDS_CP_REGISTER_NEW_USER};
        byte[] consentCodeData = createValue(consentCode, FORMAT_UINT16);
        byte[] data = mergeArrays(opCodeData, consentCodeData);

        device.writeCharacteristic(new HBWriteCommand(SERVICE_UUID,
                USER_CONTROL_POINT_CHARACTERISTIC_UUID, data));
    }

    /**
     * Set the current user on the device
     *
     * @param device      Device to execute characteristic write on
     * @param userIndex   User index of the user
     * @param consentCode Consent code of the user
     */
    public void setUser(HBDevice device, int userIndex, int consentCode) {
        HBLogger.v(TAG, String.format("setUser device: %s, userIndex: %d, consentCode: %d", device.getAddress(), userIndex, consentCode));
        byte[] opCodeData = new byte[]{UDS_CP_CONSENT};
        byte[] userIndexData = createValue(userIndex, FORMAT_UINT8);
        byte[] consentCodeData = createValue(consentCode, FORMAT_UINT16);
        byte[] data = mergeArrays(opCodeData, userIndexData, consentCodeData);

        device.writeCharacteristic(new HBWriteCommand(SERVICE_UUID,
                USER_CONTROL_POINT_CHARACTERISTIC_UUID, data));
    }

    /**
     * Delete the current user from the device
     *
     * @param device Device to execute characteristic write on
     */
    public void deleteCurrentUser(HBDevice device) {
        HBLogger.v(TAG, String.format("deleteCurrentUser device: %s", device.getAddress()));
        byte[] opCodeData = new byte[]{UDS_CP_DELETE_USER_DATA};

        device.writeCharacteristic(new HBWriteCommand(SERVICE_UUID,
                USER_CONTROL_POINT_CHARACTERISTIC_UUID, opCodeData));
    }

    /**
     * Get user data of a specific type from the device
     *
     * @param device Device to execute characteristic read on
     * @param type   Type of user data to receive
     */
    public void getUserData(HBDevice device, HBUserDataType type) {
        HBLogger.v(TAG, String.format("getUserData device: %s, type: %s", device.getAddress(), type));
        device.readCharacteristic(new HBReadCommand(SERVICE_UUID, type.getCharacteristicUUID()));
    }

    /**
     * Set user data of a specific type on the device
     *
     * @param device Device to execute characteristic write on
     * @param data   Data to write to device
     */
    public void setUserData(HBDevice device, HBUserData data) {
        HBLogger.v(TAG, String.format("setUserData device: %s, type: %s", device.getAddress(), data.getUserDataType()));
        device.writeCharacteristic(new HBWriteCommand(SERVICE_UUID,
                data.getUserDataType().getCharacteristicUUID(), data.getData()));
    }


    /**
     * Get all users registered on the device
     *
     * @param device Device to execute characteristic read on
     */
    public void getAllUsers(HBDevice device) {
        HBLogger.v(TAG, String.format("getAllUsers device: %s", device.getAddress()));
        byte[] opCodeData = new byte[]{UDS_CP_LIST_ALL_USERS};

        device.writeCharacteristic(new HBWriteCommand(SERVICE_UUID,
                USER_CONTROL_POINT_CHARACTERISTIC_UUID, opCodeData));
    }

    /**
     * Delete a user from the device with specific user index
     *
     * @param device    Device to execute characteristic write on
     * @param userIndex Index of the user
     */
    public void deleteUser(HBDevice device, int userIndex) {
        HBLogger.v(TAG, String.format("deleteUser device: %s, userIndex: %d", device.getAddress(), userIndex));
        byte[] opCodeData = new byte[]{UDS_CP_DELETE_USERS};
        byte[] userIndexData = createValue(userIndex, FORMAT_UINT8);
        byte[] data = mergeArrays(opCodeData, userIndexData);

        device.writeCharacteristic(new HBWriteCommand(SERVICE_UUID,
                USER_CONTROL_POINT_CHARACTERISTIC_UUID, data));
    }

    /**
     * Delete all users from the device
     *
     * @param device Device to execute characteristic write on
     */
    public void deleteAllUsers(HBDevice device) {
        HBLogger.v(TAG, String.format("deleteAllUsers device: %s", device.getAddress()));
        byte[] opCodeData = new byte[]{UDS_CP_DELETE_USERS};
        byte[] userIndexData = createValue(UDS_DELETE_ALL_USERS, FORMAT_UINT8);
        byte[] data = mergeArrays(opCodeData, userIndexData);

        device.writeCharacteristic(new HBWriteCommand(SERVICE_UUID,
                USER_CONTROL_POINT_CHARACTERISTIC_UUID, data));
    }

    /**
     * Get the five zone hearth rate limits from the characteristic
     *
     * @param characteristic Characteristic that contains the value
     * @return Five zone hearth rate limits user data
     */
    private HBUserData getFiveZoneHeartRateLimits(BluetoothGattCharacteristic characteristic) {
        int offset = 0;
        int veryLightToLightLimit = characteristic.getIntValue(FORMAT_UINT8, offset);
        offset += getTypeLen(FORMAT_UINT8);
        int lightToModerateLimit = characteristic.getIntValue(FORMAT_UINT8, offset);
        offset += getTypeLen(FORMAT_UINT8);
        int moderateToHardLimit = characteristic.getIntValue(FORMAT_UINT8, offset);
        offset += getTypeLen(FORMAT_UINT8);
        int hardToMaximumLimit = characteristic.getIntValue(FORMAT_UINT8, offset);

        HBFiveZoneHearthRateLimits limits = new HBFiveZoneHearthRateLimits(veryLightToLightLimit,
                lightToModerateLimit, moderateToHardLimit, hardToMaximumLimit);

        return new HBUserDataFiveZoneHearthRateLimits(limits);
    }

    /**
     * Get the three zone hearth rate limits from the characteristic
     *
     * @param characteristic Characteristic that contains the value
     * @return Three zone hearth rate limits user data
     */
    private HBUserData getThreeZoneHeartRateLimits(BluetoothGattCharacteristic characteristic) {
        int offset = 0;
        int lightToModerateLimit = characteristic.getIntValue(FORMAT_UINT8, offset);
        offset += getTypeLen(FORMAT_UINT8);
        int moderateToHardLimit = characteristic.getIntValue(FORMAT_UINT8, offset);

        HBThreeZoneHearthRateLimits limits = new HBThreeZoneHearthRateLimits(lightToModerateLimit,
                moderateToHardLimit);

        return new HBUserDataThreeZoneHearthRateLimits(limits);
    }

    /**
     * Get the two zone hearth rate limits from the characteristic
     *
     * @param characteristic Characteristic that contains the value
     * @return Two zone hearth rate limits user data
     */
    private HBUserData getTwoZoneHeartRateLimits(BluetoothGattCharacteristic characteristic) {
        int fatBurnFitnessLimit = characteristic.getIntValue(FORMAT_UINT8, 0);

        HBTwoZoneHearthRateLimits limits = new HBTwoZoneHearthRateLimits(fatBurnFitnessLimit);

        return new HBUserDataTwoZoneHearthRateLimits(limits);
    }

    /**
     * Retrieve the user control point response
     *
     * @param deviceAddress  Address of the device that gave the response
     * @param characteristic Characteristic that contains response value
     */
    private void getUserControlPointValue(String deviceAddress,
                                          BluetoothGattCharacteristic characteristic) {
        HBLogger.v(TAG, String.format("getUserControlPointValue device: %s", deviceAddress));

        HBUserDataListenerHandler listener = getServiceListener(deviceAddress);
        if (listener != null) {
            int offset = 0;
            byte responseCode = characteristic.getValue()[offset];

            if ((responseCode == UDS_CP_RESPONSE)) {
                offset += getTypeLen(FORMAT_UINT8);
                byte requestOpCode = characteristic.getValue()[offset];
                offset += getTypeLen(FORMAT_UINT8);
                byte responseValue = characteristic.getValue()[offset];
                offset += getTypeLen(FORMAT_UINT8);

                HBLogger.v(TAG, String.format("responseCode: %d, requestOpCode: %d, responseValue: %d", responseCode, requestOpCode, responseValue));

                if(responseValue == UDS_CP_RESP_VALUE_SUCCESS) {
                    switch (requestOpCode) {
                        case UDS_CP_REGISTER_NEW_USER:
                            int userIndex = characteristic.getIntValue(FORMAT_UINT8, offset);
                            offset += getTypeLen(FORMAT_UINT8);
                            listener.onUserRegistered(deviceAddress, userIndex);
                            break;
                        case UDS_CP_CONSENT:
                            listener.onUserSet(deviceAddress);
                            break;
                        case UDS_CP_DELETE_USER_DATA:
                            listener.onUserDeleted(deviceAddress, -1);
                            break;
                        case UDS_CP_LIST_ALL_USERS:
                            numberOfUsers = characteristic.getIntValue(FORMAT_UINT8, offset);
                            if (numberOfUsers == 0) {
                                listener.onRegisteredUsers(deviceAddress, new ArrayList<HBRegisteredUser>());
                            }
                            break;
                        case UDS_CP_DELETE_USERS:
                            int index = characteristic.getIntValue(FORMAT_UINT8, offset);
                            offset += getTypeLen(FORMAT_UINT8);
                            listener.onUserDeleted(deviceAddress, index);

                            break;
                    }
                } else {
                    byte[] response = new byte[4];
                    response[0] = responseCode;
                    response[1] = requestOpCode;
                    response[2] = responseValue;
                    response[3] = 0;
                    ByteBuffer buffer = ByteBuffer.wrap(response);

                    listener.onInvalidRequest(deviceAddress, buffer.getInt());
                }
            }
        }
    }

    /**
     * Retrieve the registered user response
     *
     * @param deviceAddress  Address of the device that gave the response
     * @param characteristic Characteristic that contains response value
     */
    private void getRegisteredUserValue(String deviceAddress,
                                          BluetoothGattCharacteristic characteristic) {
        HBLogger.v(TAG, String.format("getRegisteredUser device: %s", deviceAddress));

        if (numberOfUsers > 0) {
            int userNameTotalParts = 0;
            int userNamePart = 0;
            String userFirstName = "";

            int offset = 0;
            int flags = characteristic.getIntValue(FORMAT_UINT8, offset);
            offset += getTypeLen(FORMAT_UINT8);
            int userIndex = characteristic.getIntValue(FORMAT_UINT8, offset);
            offset += getTypeLen(FORMAT_UINT8);

            boolean namePresent = ((flags & 0x01) == 0x01);
            boolean nameTruncated = ((flags & 0x02) == 0x02);


            if(namePresent) {
                int userNameData = characteristic.getIntValue(FORMAT_UINT8, offset);
                offset += getTypeLen(FORMAT_UINT8);

                userNameTotalParts = userNameData & 0x0F;
                userNamePart = userNameData >> 4;

                userFirstName = characteristic.getStringValue(offset);
            }


            if (registeredUsers.containsKey(userIndex)) {
                if(namePresent) {
                    registeredUsers.get(userIndex).addFirstNamePart(userNamePart, userFirstName);
                }
            } else {
                if(namePresent) {
                    HBRegisteredUser hbRegisteredUser = new HBRegisteredUser(userIndex, userNameTotalParts, flags);
                    hbRegisteredUser.addFirstNamePart(userNamePart, userFirstName);
                    registeredUsers.put(userIndex, hbRegisteredUser);
                } else {
                    HBRegisteredUser hbRegisteredUser = new HBRegisteredUser(userIndex, flags);
                    registeredUsers.put(userIndex, hbRegisteredUser);
                }
            }

            if (registeredUsers.size() >= numberOfUsers) {
                boolean isComplete = true;
                for (HBRegisteredUser registeredUser : registeredUsers.values()) {
                    isComplete &= registeredUser.isComplete();
                }

                if (isComplete) {

                    HBUserDataListenerHandler listener = getServiceListener(deviceAddress);
                    if (listener != null) {
                        listener.onRegisteredUsers(deviceAddress, new ArrayList<>(registeredUsers.values()));
                    }
                    numberOfUsers = 0;
                    registeredUsers.clear();
                }
            }
        }
    }
}