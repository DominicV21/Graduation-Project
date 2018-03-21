/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.philipsextension;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import chdsw.philips.com.haraldlib.HBDevice;
import chdsw.philips.com.haraldlib.HBLogger;
import chdsw.philips.com.haraldlib.HBReadCommand;
import chdsw.philips.com.haraldlib.HBWriteCommand;
import chdsw.philips.com.haraldlib.services.HBServiceHandler;
import chdsw.philips.com.haraldlib.services.philipsextension.data.HBExtendedUserData;
import chdsw.philips.com.haraldlib.services.philipsextension.data.HBExtendedUserDataAthlete;
import chdsw.philips.com.haraldlib.services.philipsextension.data.HBExtendedUserDataNumber;
import chdsw.philips.com.haraldlib.services.philipsextension.object.HBAthlete;
import chdsw.philips.com.haraldlib.services.philipsextension.object.HBRegisteredUser;

import static android.bluetooth.BluetoothGattCharacteristic.*;
import static chdsw.philips.com.haraldlib.services.HBDataParser.createValue;
import static chdsw.philips.com.haraldlib.services.HBDataParser.getTypeLen;
import static chdsw.philips.com.haraldlib.services.HBDataParser.mergeArrays;

public class HBPhilipsExtensionServiceHandler extends HBServiceHandler<HBPhilipsExtensionListenerHandler> {
    public static final String TAG = "HBPhilipsExtensionServiceHandler";
    public static final UUID SERVICE_UUID =
            UUID.fromString("00000001-0000-0000-0000-000000000000");
    private static final UUID EXTENDED_USER_CONTROL_POINT_CHARACTERISTIC_UUID =
            UUID.fromString("00000002-0000-0000-0000-000000000000");
    private static final UUID REGISTERED_USER_CHARACTERISTIC_UUID =
            UUID.fromString("00000003-0000-0000-0000-000000000000");

    private int numberOfUsers;
    private Map<Integer, HBRegisteredUser> registeredUsers;

    /**
     * Construct new philips extension service handler.
     *
     * @param context Application context.
     */
    public HBPhilipsExtensionServiceHandler(Context context) {
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
        HBExtendedUserDataType type = HBExtendedUserDataType.fromCharacteristicUUID(characteristicUUID);

        if (type != null) {
            HBExtendedUserData data = null;

            switch (type) {
                case WEIGHT_GOAL:
                    data = new HBExtendedUserDataNumber(type, characteristic.getIntValue(FORMAT_UINT16, 0));
                    break;
                case ATHLETE:
                    boolean isAthlete = (characteristic.getIntValue(FORMAT_UINT8, 0) == 1);
                    int athleticLevel = characteristic.getIntValue(FORMAT_UINT8, 1);
                    data = new HBExtendedUserDataAthlete(new HBAthlete(isAthlete, athleticLevel));
                    break;
            }

            HBPhilipsExtensionListenerHandler listener = getServiceListener(deviceAddress);
            if (listener != null)
                listener.onExtendedUserData(deviceAddress, data);
        } else {
            if (characteristicUUID.equals(EXTENDED_USER_CONTROL_POINT_CHARACTERISTIC_UUID)) {
                getUserControlPointValue(deviceAddress, characteristic);
            }
            if (characteristicUUID.equals(REGISTERED_USER_CHARACTERISTIC_UUID)) {
                getRegisteredUser(deviceAddress, characteristic);
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
    }

    /**
     * Get all users registered on the device
     *
     * @param device Device to execute characteristic read on
     */
    public void getAllUsers(HBDevice device) {
        HBLogger.v(TAG, String.format("getAllUsers device: %s", device.getAddress()));
        byte[] opCodeData = new byte[]{0x01};

        device.writeCharacteristic(new HBWriteCommand(SERVICE_UUID,
                EXTENDED_USER_CONTROL_POINT_CHARACTERISTIC_UUID, opCodeData));
    }

    /**
     * Delete the current user from the device with specific user index
     *
     * @param device    Device to execute characteristic write on
     * @param userIndex Index of the user
     */
    public void deleteUser(HBDevice device, int userIndex) {
        HBLogger.v(TAG, String.format("deleteUser device: %s, userIndex: %d", device.getAddress(), userIndex));
        byte[] opCodeData = new byte[]{0x02};
        byte[] userIndexData = createValue(userIndex, FORMAT_UINT8);
        byte[] data = mergeArrays(opCodeData, userIndexData);

        device.writeCharacteristic(new HBWriteCommand(SERVICE_UUID,
                EXTENDED_USER_CONTROL_POINT_CHARACTERISTIC_UUID, data));
    }

    /**
     * Delete all users from the device
     *
     * @param device Device to execute characteristic write on
     */
    public void deleteAllUsers(HBDevice device) {
        HBLogger.v(TAG, String.format("deleteAllUsers device: %s", device.getAddress()));
        byte[] opCodeData = new byte[]{0x02};
        byte[] userIndexData = createValue(0xFF, FORMAT_UINT8);
        byte[] data = mergeArrays(opCodeData, userIndexData);

        device.writeCharacteristic(new HBWriteCommand(SERVICE_UUID,
                EXTENDED_USER_CONTROL_POINT_CHARACTERISTIC_UUID, data));
    }

    /**
     * Get extended user data of a specific type from the device
     *
     * @param device Device to execute characteristic read on
     * @param type   Type of extended user data to receive
     */
    public void getExtendedUserData(HBDevice device, HBExtendedUserDataType type) {
        HBLogger.v(TAG, String.format("getExtendedUserData device: %s, type: %s", device.getAddress(), type));
        device.readCharacteristic(new HBReadCommand(SERVICE_UUID, type.getCharacteristicUUID()));
    }

    /**
     * Set extended user data of a specific type on the device
     *
     * @param device Device to execute characteristic write on
     * @param data   Data to write to device
     */
    public void setExtendedUserData(HBDevice device, HBExtendedUserData data) {
        HBLogger.v(TAG, String.format("setExtendedUserData device: %s, type: %s", device.getAddress(), data.getExtendedUserDataType()));
        device.writeCharacteristic(new HBWriteCommand(SERVICE_UUID,
                data.getExtendedUserDataType().getCharacteristicUUID(), data.getData()));
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
        HBPhilipsExtensionListenerHandler listener = getServiceListener(deviceAddress);
        if (listener != null) {
            int offset = 0;
            int requestOpCode = characteristic.getIntValue(FORMAT_UINT8, offset);
            offset += getTypeLen(FORMAT_UINT8);
            int responseCode = characteristic.getIntValue(FORMAT_UINT8, offset);
            offset += getTypeLen(FORMAT_UINT8);

            HBLogger.v(TAG, String.format("requestOpCode: %d, responseCode: %d", requestOpCode, responseCode));

            if (responseCode == 0x01) {
                switch (requestOpCode) {
                    case 0x01:
                        numberOfUsers = characteristic.getIntValue(FORMAT_UINT8, offset);
                        HBLogger.v(TAG, String.format("numberOfUsers: %d", numberOfUsers));
                        break;
                    case 0x02:
                        listener.onUserDeleted(deviceAddress);
                        break;
                }
            } else {
                listener.onInvalidRequest(deviceAddress, responseCode);
            }
        }
    }

    /**
     * Get hte registered user value from the characteristic
     *
     * @param deviceAddress  Address of the device that gave the response
     * @param characteristic Characteristic that contains registered user value
     */
    private void getRegisteredUser(String deviceAddress,
                                   BluetoothGattCharacteristic characteristic) {
        HBLogger.v(TAG, String.format("getRegisteredUser device: %s", deviceAddress));

        if (numberOfUsers > 0) {
            int offset = 0;
            int userIndex = characteristic.getIntValue(FORMAT_UINT8, offset);
            offset += getTypeLen(FORMAT_UINT8);
            int userNameLength = characteristic.getIntValue(FORMAT_UINT8, offset);
            offset += getTypeLen(FORMAT_UINT8);
            int userNamePart = characteristic.getIntValue(FORMAT_UINT8, offset);
            offset += getTypeLen(FORMAT_UINT8);
            String userFirstName = characteristic.getStringValue(offset);

            if (registeredUsers.containsKey(userIndex)) {
                registeredUsers.get(userIndex).addFirstNamePart(userNamePart, userFirstName);
            } else {
                HBRegisteredUser hbRegisteredUser = new HBRegisteredUser(userIndex, userNameLength);
                hbRegisteredUser.addFirstNamePart(userNamePart, userFirstName);
                registeredUsers.put(userIndex, hbRegisteredUser);
            }

            if (registeredUsers.size() >= numberOfUsers) {
                boolean isComplete = true;
                for (HBRegisteredUser registeredUser : registeredUsers.values()) {
                    isComplete &= registeredUser.isComplete();
                }

                if (isComplete) {

                    HBPhilipsExtensionListenerHandler listener = getServiceListener(deviceAddress);
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
