/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.userdata.object;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;
import static chdsw.philips.com.haraldlib.services.HBDataParser.createValue;
import static chdsw.philips.com.haraldlib.services.HBDataParser.mergeArrays;

/**
 * Object that contains all five zone hearth rate limits as specified here:
 * https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.five_zone_heart_rate_limits.xml
 */
public class HBFiveZoneHearthRateLimits {
    private int veryLightToLightLimit;
    private int lightToModerateLimit;
    private int moderateToHardLimit;
    private int hardToMaximumLimit;

    /**
     * Construct new five zone hearth rate limits object
     *
     * @param veryLightToLightLimit Very light to light heart rate limit
     * @param lightToModerateLimit Light to moderate heart rate limit
     * @param moderateToHardLimit Moderate to hard heart rate limit
     * @param hardToMaximumLimit Hard to maximum heart rate limit
     */
    public HBFiveZoneHearthRateLimits(int veryLightToLightLimit, int lightToModerateLimit, int moderateToHardLimit, int hardToMaximumLimit) {
        this.veryLightToLightLimit = veryLightToLightLimit;
        this.lightToModerateLimit = lightToModerateLimit;
        this.moderateToHardLimit = moderateToHardLimit;
        this.hardToMaximumLimit = hardToMaximumLimit;
    }

    /**
     * Get the very light to light heart rate limit
     *
     * @return The very light to light heart rate limit value
     */
    public int getVeryLightToLightLimit() {
        return veryLightToLightLimit;
    }

    /**
     * Get the light to moderate heart rate limit
     *
     * @return The light to moderate heart rate limit value
     */
    public int getLightToModerateLimit() {
        return lightToModerateLimit;
    }

    /**
     * Get the moderate to hard heart rate limit
     *
     * @return The moderate to hard heart rate limit value
     */
    public int getModerateToHardLimit() {
        return moderateToHardLimit;
    }

    /**
     * Get the hard to maximum heart rate limit
     *
     * @return The hard to maximum heart rate limit value
     */
    public int getHardToMaximumLimit() {
        return hardToMaximumLimit;
    }

    /**
     * Get the byte[] value representation of the five zone hearth rate limits
     *
     * @return Value of the five zone hearth rate limits
     */
    public byte[] getValue() {
        byte[] limit1 = createValue(veryLightToLightLimit, FORMAT_UINT8);
        byte[] limit2 = createValue(lightToModerateLimit, FORMAT_UINT8);
        byte[] limit3 = createValue(moderateToHardLimit, FORMAT_UINT8);
        byte[] limit4 = createValue(hardToMaximumLimit, FORMAT_UINT8);

        byte[] valueArray = mergeArrays(limit1, limit2, limit3, limit4);

        return valueArray;
    }
}
