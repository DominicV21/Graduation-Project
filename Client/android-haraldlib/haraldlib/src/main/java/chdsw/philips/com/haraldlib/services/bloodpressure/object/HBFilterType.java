/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.bloodpressure.object;

public enum HBFilterType {
    SequenceNumber(1),
    BaseTime(2),
    UserTime(3);

    HBFilterType(int value)
    {
        this.value = value;
    }

    private int value;

    public int getValue()
    {
        return value;
    }

    public static HBFilterType fromValue(int value) {
        for(HBFilterType type : values()) {
            if(type.getValue() == value)
                return type;
        }
        return null;
    }
}
