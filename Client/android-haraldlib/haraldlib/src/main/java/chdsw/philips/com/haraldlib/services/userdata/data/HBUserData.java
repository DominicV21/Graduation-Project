/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.userdata.data;

import chdsw.philips.com.haraldlib.services.userdata.HBUserDataType;

/**
 * User data that is used to read and write user data service
 */
public interface HBUserData<T> {

    /**
     * Get the user data type
     *
     * @return User data type
     */
    HBUserDataType getUserDataType();

    /**
     * Get the byte[] value of the user data
     *
     * @return Byte[] of the data
     */
    byte[] getData();

    /**
     * Get the value of the user data
     *
     * @return Value of the data
     */
    T getValue();
}
