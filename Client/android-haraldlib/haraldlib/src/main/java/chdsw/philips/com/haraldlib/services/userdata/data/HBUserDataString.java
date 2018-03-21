/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.userdata.data;

import chdsw.philips.com.haraldlib.services.userdata.HBUserDataType;

import static chdsw.philips.com.haraldlib.services.HBDataParser.createValue;

public class HBUserDataString implements HBUserData<String> {
    private HBUserDataType type;
    private String value;

    /**
     * Create a new user data object for a string value
     *
     * @param type Type of user data
     * @param value String value
     */
    public HBUserDataString(HBUserDataType type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Get the user data type
     *
     * @return User data type
     */
    @Override
    public HBUserDataType getUserDataType() {
        return this.type;
    }

    /**
     * Get the byte[] value of the string value
     *
     * @return Byte[] of the string value
     */
    @Override
    public byte[] getData() {
        return createValue(value);
    }

    /**
     * Get the string value
     *
     * @return String value
     */
    @Override
    public String getValue() {
        return this.value;
    }
}
