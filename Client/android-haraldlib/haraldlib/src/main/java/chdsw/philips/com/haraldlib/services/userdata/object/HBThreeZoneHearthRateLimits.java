/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.userdata.object;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;
import static chdsw.philips.com.haraldlib.services.HBDataParser.createValue;
import static chdsw.philips.com.haraldlib.services.HBDataParser.mergeArrays;

/**
 * Object that contains all three zone hearth rate limits as specified here:
 * https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.three_zone_heart_rate_limits.xml
 */
public class HBThreeZoneHearthRateLimits {
    private int lightToModerateLimit;
    private int moderateToHardLimit;

    /**
     * Construct new three zone hearth rate limits object
     *
     * @param lightToModerateLimit Light to moderate heart rate limit
     * @param moderateToHardLimit Moderate to hard heart rate limit
     */
    public HBThreeZoneHearthRateLimits(int lightToModerateLimit, int moderateToHardLimit) {
        this.lightToModerateLimit = lightToModerateLimit;
        this.moderateToHardLimit = moderateToHardLimit;
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
     * Get the byte[] value representation of the three zone hearth rate limits
     *
     * @return Value of the three zone hearth rate limits
     */
    public byte[] getValue() {
        byte[] limit1 = createValue(lightToModerateLimit, FORMAT_UINT8);
        byte[] limit2 = createValue(moderateToHardLimit, FORMAT_UINT8);

        byte[] valueArray = mergeArrays(limit1, limit2);

        return valueArray;
    }
}
