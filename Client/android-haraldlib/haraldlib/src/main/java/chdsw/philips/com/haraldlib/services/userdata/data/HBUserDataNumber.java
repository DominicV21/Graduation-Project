/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.userdata.data;

import chdsw.philips.com.haraldlib.services.userdata.HBUserDataType;

import static android.bluetooth.BluetoothGattCharacteristic.*;
import static chdsw.philips.com.haraldlib.services.HBDataParser.createValue;

public class HBUserDataNumber implements HBUserData<Number> {
    private HBUserDataType type;
    private Number value;

    /**
     * Create a new user data object for a number value
     *
     * @param type Type of user data
     * @param value Nummber value
     */
    public HBUserDataNumber(HBUserDataType type, Number value) {
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
     * Get the byte[] value of the number value
     *
     * @return Byte[] of the number value
     */
    @Override
    public byte[] getData() {
        switch (type) {
            case AGE:
            case VO2_MAX:
            case HEARTH_RATE_MAX:
            case RESTING_HEARTH_RATE:
            case MAX_RECOMMENDED_HEARTH_RATE:
            case AEROBIC_THRESHOLD:
            case ANAEROBIC_THRESHOLD:
            case FAT_BURN_LOWER_LIMIT:
            case FAT_BURN_UPPER_LIMIT:
            case AEROBIC_HEARTH_RATE_LOWER_LIMIT:
            case AEROBIC_HEARTH_RATE_UPPER_LIMIT:
            case ANAEROBIC_HEARTH_RATE_LOWER_LIMIT:
            case ANAEROBIC_HEARTH_RATE_UPPER_LIMIT:
                return createValue(value.intValue(), FORMAT_UINT8);
            case WEIGHT:
                float actualWeight = value.floatValue();
                int weight = (int) (actualWeight / 0.005f);
                return createValue(weight, FORMAT_UINT16);
            case HEIGHT:
            case WAIST_CIRCUMFERENCE:
            case HIP_CIRCUMFERENCE:
                float actualValue = value.floatValue();
                int valueResolution = (int) (actualValue / 0.01f);
                return createValue(valueResolution, FORMAT_UINT16);
            case DATABASE_CHANGE_INCREMENT:
                return createValue(value.intValue(), FORMAT_UINT32);
        }

        return new byte[0];
    }

    /**
     * Get the number value
     *
     * @return Number value
     */
    @Override
    public Number getValue() {
        return this.value;
    }
}
