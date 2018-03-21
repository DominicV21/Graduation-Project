/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.bloodpressure.object;

public enum HBBloodPressureUnit {
    mmHg(0),
    kPa(1);

    HBBloodPressureUnit(int value)
    {
        this.value = value;
    }

    private int value;

    public int getValue()
    {
        return value;
    }

    public static HBBloodPressureUnit fromValue(int value) {
        for(HBBloodPressureUnit type : values()) {
            if(type.getValue() == value)
                return type;
        }
        return null;
    }
}
