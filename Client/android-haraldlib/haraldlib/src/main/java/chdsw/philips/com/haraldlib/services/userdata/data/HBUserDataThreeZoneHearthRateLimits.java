/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.userdata.data;

import chdsw.philips.com.haraldlib.services.userdata.object.HBThreeZoneHearthRateLimits;
import chdsw.philips.com.haraldlib.services.userdata.HBUserDataType;

public class HBUserDataThreeZoneHearthRateLimits implements HBUserData<HBThreeZoneHearthRateLimits> {
    private HBThreeZoneHearthRateLimits value;

    /**
     * Create a new user data object for a three zone hearth rate limits value
     *
     * @param value Three zone hearth rate limits value
     */
    public HBUserDataThreeZoneHearthRateLimits(HBThreeZoneHearthRateLimits value) {
        this.value = value;
    }

    /**
     * Get the user data type
     *
     * @return User data type
     */
    @Override
    public HBUserDataType getUserDataType() {
        return HBUserDataType.THREE_ZONE_HEARTH_RATE_LIMITS;
    }

    /**
     * Get the byte[] value of the three zone hearth rate limits value
     *
     * @return Byte[] of the three zone hearth rate limits value
     */
    @Override
    public byte[] getData() {
        return value.getValue();
    }

    /**
     * Get the three zone hearth rate limits value
     *
     * @return Three zone hearth rate limits value
     */
    @Override
    public HBThreeZoneHearthRateLimits getValue() {
        return this.value;
    }
}
