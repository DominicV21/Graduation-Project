/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.bloodpressure.object;

public enum HBRecordOperator {
    All(1),
    Less(2),
    Great(3),
    Within(4),
    First(5),
    Last(6),
    Equal(7);

    HBRecordOperator(int value)
    {
        this.value = value;
    }

    private int value;

    public int getValue()
    {
        return value;
    }

    public static HBRecordOperator fromValue(int value) {
        for(HBRecordOperator type : values()) {
            if(type.getValue() == value)
                return type;
        }
        return null;
    }

    public boolean usesType() {
        if(this.value == 1 || this.value == 5 || this.value == 6) {
            return false;
        } else {
            return true;
        }
    }

    public int numberOfParameters() {
        if(this.value == 1 || this.value == 5 || this.value == 6) {
            return 0;
        } else if (this.value == 4){
            return 2;
        } else {
            return 1;
        }
    }
}
