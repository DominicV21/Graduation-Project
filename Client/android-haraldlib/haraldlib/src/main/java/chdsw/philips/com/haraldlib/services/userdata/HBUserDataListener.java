/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.userdata;

import java.util.List;

import chdsw.philips.com.haraldlib.services.HBServiceListener;
import chdsw.philips.com.haraldlib.services.userdata.data.HBUserData;
import chdsw.philips.com.haraldlib.services.userdata.object.HBRegisteredUser;

/**
 * A callback to receive information from the user date service
 */
public interface HBUserDataListener extends HBServiceListener {
    /**
     * User data value read from the device
     *
     * @param deviceAddress Address of the device.
     * @param data User data object contain the value of a user data type
     */
    void onUserData(final String deviceAddress, HBUserData data);

    /**
     * New user registered on the device
     *
     * @param deviceAddress Address of the device.
     * @param userIndex User index of the registered user
     */
    void onUserRegistered(final String deviceAddress, final int userIndex);

    /**
     * User set on the device
     *
     * @param deviceAddress Address of the device.
     */
    void onUserSet(final String deviceAddress);

    /**
     * User delete on the device
     *
     * @param deviceAddress Address of the device.
     * @param index Index of the deleted user
     */
    void onUserDeleted(final String deviceAddress, int index);

    /**
     * On registered users received from the device
     *
     * @param deviceAddress Address of the device.
     * @param users List of registered users
     */
    void onRegisteredUsers(final String deviceAddress, List<HBRegisteredUser> users);

    /**
     * Invalid request on the device on the user data service
     *
     * @param deviceAddress Address of the device.
     * @param responseValue Response of the request
     */
    void onInvalidRequest(final String deviceAddress, final int responseValue );
}
