/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.userdata.data;

import chdsw.philips.com.haraldlib.services.userdata.HBUserDataType;
import chdsw.philips.com.haraldlib.services.userdata.object.HBTwoZoneHearthRateLimits;

public class HBUserDataTwoZoneHearthRateLimits implements HBUserData<HBTwoZoneHearthRateLimits> {
    private HBTwoZoneHearthRateLimits value;

    /**
     * Create a new user data object for a two zone hearth rate limits value
     *
     * @param value Two zone hearth rate limits value
     */
    public HBUserDataTwoZoneHearthRateLimits(HBTwoZoneHearthRateLimits value) {
        this.value = value;
    }

    /**
     * Get the user data type
     *
     * @return User data type
     */
    @Override
    public HBUserDataType getUserDataType() {
        return HBUserDataType.TWO_ZONE_HEARTH_RATE_LIMITS;
    }

    /**
     * Get the byte[] value of the two zone hearth rate limits value
     *
     * @return Byte[] of the two zone hearth rate limits value
     */
    @Override
    public byte[] getData() {
        return value.getValue();
    }

    /**
     * Get the two zone hearth rate limits value
     *
     * @return Two zone hearth rate limits value
     */
    @Override
    public HBTwoZoneHearthRateLimits getValue() {
        return this.value;
    }
}
