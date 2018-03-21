/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object;

public class HBPhysicalActivityMonitorSessDescriptor {
    private byte Flags;
    private int SessionID;
    private int SessionStartBaseTime = -1;
    private int SessionStartTimeOffset = -1;
    private int SessionEndBaseTime = -1;
    private int SessionEndTimeOffset = -1;
    private int SubSessionID;
    private int SubSessionStartBaseTime = -1;
    private int SubSessionStartTimeOffset = -1;
    private int SubSessionEndBaseTime = -1;
    private int SubSessionEndTimeOffset = -1;
    private int PredominantActivityType = -1;

    private boolean describesSessionFlag;
    private boolean sessionSubSessionRunningFlag;
    private boolean deletedSessionFlag;

    private static final int BIT1 = 0x01;
    private static final int BIT2 = 0x02;
    private static final int BIT3 = 0x04;

    public HBPhysicalActivityMonitorSessDescriptor(byte[] data){
        int offset = 0; //starts at 0 because Session Descriptor does not have a header

        Flags = data[offset++];
        describesSessionFlag = ((Flags & BIT1) == BIT1);
        sessionSubSessionRunningFlag = ((Flags & BIT2) == BIT2);
        deletedSessionFlag = ((Flags & BIT3) == BIT3);

        SessionID = data[offset];
        offset += 2;

        if(describesSessionFlag){
            SessionStartBaseTime = data[offset];
            offset += 4;
            SessionStartTimeOffset = data[offset];
            offset += 2;
            if(!sessionSubSessionRunningFlag){
                SessionEndBaseTime = data[offset];
                offset += 4;
                SessionEndTimeOffset = data[offset];
                offset += 2;
            }
        }
        else{
            SubSessionID = data[offset];
            offset += 2;
            SubSessionStartBaseTime = data[offset];
            offset += 4;
            SubSessionStartTimeOffset = data[offset];
        }

    }




    public byte getFlags() { return Flags; }
    public int getSessionID() { return SessionID; }




    public int getSubSessionID() { return SubSessionID; }


    public void setFlags(byte Flags){this.Flags=Flags;}
    public void setSessionID(int SessionID){this.SessionID=SessionID;}
    public void setSubSessionID(int SubSessionID){this.SubSessionID=SubSessionID;}
}
