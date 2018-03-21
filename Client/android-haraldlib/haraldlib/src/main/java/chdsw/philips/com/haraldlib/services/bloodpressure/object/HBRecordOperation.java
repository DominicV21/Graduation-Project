/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.bloodpressure.object;

public enum HBRecordOperation {
    ReportRecords(1),
    Delete(2),
    Abort(3),
    ReportNumbers(4);

    HBRecordOperation(int value) {
        this.value = value;
    }

    private int value;

    public int getValue()
    {
        return value;
    }

    public static HBRecordOperation fromValue(int value) {
        for(HBRecordOperation type : values()) {
            if(type.getValue() == value)
                return type;
        }
        return null;
    }
}
