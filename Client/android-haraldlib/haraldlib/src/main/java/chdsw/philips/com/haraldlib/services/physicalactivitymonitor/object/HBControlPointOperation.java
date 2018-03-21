/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object;

public enum HBControlPointOperation {
    EnquireSessions(1),
    EnquireSubSessions(2),
    GetEndedSessionData(3),
    StartSessionSubSession(4),
    StopSession(5),
    DeleteSession(6);


    HBControlPointOperation(int value) {this.value = value; }

    private int value;

    public int getValue() { return value; }

    public static HBControlPointOperation fromValue(int value) {
        for(HBControlPointOperation type : values()) {
            if(type.getValue() == value)
                return type;
        }
        return null;
    }

    public int numberOfParameters() {
        if(this.value == 1 || this.value == 5) {
            return 0;
        } else if (this.value == 3){
            return 3;
        } else {
            return 1;
        }
    }
}
