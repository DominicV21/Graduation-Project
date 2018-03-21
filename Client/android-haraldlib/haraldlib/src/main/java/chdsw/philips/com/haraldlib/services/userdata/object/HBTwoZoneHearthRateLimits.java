/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.userdata.object;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;
import static chdsw.philips.com.haraldlib.services.HBDataParser.createValue;

/**
 * Object that contains all two zone hearth rate limits as specified here:
 * https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.two_zone_heart_rate_limits.xml
 */
public class HBTwoZoneHearthRateLimits {
    private int fatBurnFitnessLimit;

    /**
     * Construct new two zone hearth rate limits object
     *
     * @param fatBurnFitnessLimit Fat burn fitness heart rate limit
     */
    public HBTwoZoneHearthRateLimits(int fatBurnFitnessLimit) {
        this.fatBurnFitnessLimit = fatBurnFitnessLimit;
    }

    /**
     * Get the fat burn fitness heart rate limit
     *
     * @return The fat burn fitness heart rate limit value
     */
    public int getFatBurnFitnessLimit() {
        return fatBurnFitnessLimit;
    }

    /**
     * Get the byte[] value representation of the two zone hearth rate limits
     *
     * @return Value of the two zone hearth rate limits
     */
    public byte[] getValue() {
        byte[] valueArray = createValue(fatBurnFitnessLimit, FORMAT_UINT8);

        return valueArray;
    }
}
