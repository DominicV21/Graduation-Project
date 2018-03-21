/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.philipsextension;

import java.util.List;

import chdsw.philips.com.haraldlib.services.HBServiceListener;
import chdsw.philips.com.haraldlib.services.philipsextension.data.HBExtendedUserData;
import chdsw.philips.com.haraldlib.services.philipsextension.object.HBRegisteredUser;

/**
 * A callback to receive information from the philips extension service
 */
public interface HBPhilipsExtensionListener extends HBServiceListener {
    /**
     * Extended user data value read from the device
     *
     * @param deviceAddress Address of the device.
     * @param data Externded user data object contain the value of a user data type
     */
    void onExtendedUserData(final String deviceAddress, HBExtendedUserData data);

    /**
     * User delete on the device
     *
     * @param deviceAddress Address of the device.
     */
    void onUserDeleted(final String deviceAddress);

    /**
     * On registered users received from the device
     *
     * @param deviceAddress Address of the device.
     * @param users List of registered users
     */
    void onRegisteredUsers(final String deviceAddress, List<HBRegisteredUser> users);

    /**
     * Invalid request on the device on the philips extension service
     *
     * @param deviceAddress Address of the device.
     * @param responseValue Response of the request
     */
    void onInvalidRequest(final String deviceAddress, final int responseValue);
}
