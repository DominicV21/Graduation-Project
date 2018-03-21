/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.philipsextension.object;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;
import static chdsw.philips.com.haraldlib.services.HBDataParser.createValue;
import static chdsw.philips.com.haraldlib.services.HBDataParser.mergeArrays;

public class HBAthlete {
    private boolean isAthlete;
    private int athleticLevel;

    /**
     * Construct new athelte object
     *
     * @param isAthlete Is athelete value
     * @param athleticLevel Atheletic level value
     */
    public HBAthlete(boolean isAthlete, int athleticLevel) {
        this.isAthlete = isAthlete;
        this.athleticLevel = athleticLevel;
    }

    /**
     * Get the is athelete value
     *
     * @return Is athelete value
     */
    public boolean isAthlete() {
        return isAthlete;
    }

    /**
     * Get the athletic level value
     *
     * @return Atheletic level value
     */
    public int getAthleticLevel() {
        return athleticLevel;
    }

    /**
     * Get the byte[] value representation of HBAthlete
     *
     * @return Value of HBAthlete
     */
    public byte[] getValue() {
        byte[] isAthleteValue = createValue(isAthlete ? 1 : 0, FORMAT_UINT8);
        byte[] athleticLevelValue = createValue(athleticLevel, FORMAT_UINT8);

        byte[] valueArray = mergeArrays(isAthleteValue, athleticLevelValue);

        return valueArray;
    }
}
