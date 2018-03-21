/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.userdata.data;

import chdsw.philips.com.haraldlib.services.userdata.object.HBFiveZoneHearthRateLimits;
import chdsw.philips.com.haraldlib.services.userdata.HBUserDataType;

public class HBUserDataFiveZoneHearthRateLimits implements HBUserData<HBFiveZoneHearthRateLimits> {
    private HBFiveZoneHearthRateLimits value;

    /**
     * Create a new user data object for a five zone hearth rate limits value
     *
     * @param value Five zone hearth rate limits value
     */
    public HBUserDataFiveZoneHearthRateLimits(HBFiveZoneHearthRateLimits value) {
        this.value = value;
    }

    /**
     * Get the user data type
     *
     * @return User data type
     */
    @Override
    public HBUserDataType getUserDataType() {
        return HBUserDataType.FIVE_ZONE_HEARTH_RATE_LIMITS;
    }

    /**
     * Get the byte[] value of the five zone hearth rate limits value
     *
     * @return Byte[] of the five zone hearth rate limits value
     */
    @Override
    public byte[] getData() {
        return value.getValue();
    }

    /**
     * Get the five zone hearth rate limits value
     *
     * @return Five zone hearth rate limits value
     */
    @Override
    public HBFiveZoneHearthRateLimits getValue() {
        return this.value;
    }
}
