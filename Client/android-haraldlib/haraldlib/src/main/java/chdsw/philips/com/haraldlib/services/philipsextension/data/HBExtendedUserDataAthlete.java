/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.philipsextension.data;

import chdsw.philips.com.haraldlib.services.philipsextension.HBExtendedUserDataType;
import chdsw.philips.com.haraldlib.services.philipsextension.object.HBAthlete;

public class HBExtendedUserDataAthlete implements HBExtendedUserData<HBAthlete> {
    private HBAthlete value;

    /**
     * Create a new user data object for a gender value
     *
     * @param value Gender value
     */
    public HBExtendedUserDataAthlete(HBAthlete value) {
        this.value = value;
    }

    /**
     * Get the user data type
     *
     * @return User data type
     */
    @Override
    public HBExtendedUserDataType getExtendedUserDataType() {
        return HBExtendedUserDataType.ATHLETE;
    }

    /**
     * Get the byte[] value of the gender value
     *
     * @return Byte[] of the gender value
     */
    @Override
    public byte[] getData() {
        return value.getValue();
    }

    /**
     * Get the gender value
     *
     * @return Gender value
     */
    @Override
    public HBAthlete getValue() {
        return this.value;
    }
}
