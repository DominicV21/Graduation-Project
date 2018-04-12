/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class HBPhysicalActivityMonitorSessDescriptor {

    private byte Flags;
    private int SessionID = -1;
    private Long SessionStartBaseTime = -1L;
    private int SessionStartTimeOffset = -1;
    private Long SessionEndBaseTime = -1L;
    private int SessionEndTimeOffset = -1;
    private int SubSessionID = -1;
    private Long SubSessionStartBaseTime = -1L;
    private int SubSessionStartTimeOffset = -1;
    private Long SubSessionEndBaseTime = -1L;
    private int SubSessionEndTimeOffset = -1;

    private boolean describesSessionFlag;
    private boolean sessionSubSessionRunningFlag;
    private boolean deletedSessionFlag;

    private static final int BIT1 = 0x01;
    private static final int BIT2 = 0x02;
    private static final int BIT3 = 0x04;

    public HBPhysicalActivityMonitorSessDescriptor(byte[] data){
        int offset = 0; //starts at 0 because Session Descriptor does not have a header
        ByteBuffer buffer;

        Flags = data[offset++];
        describesSessionFlag = ((Flags & BIT1) == BIT1);
        sessionSubSessionRunningFlag = ((Flags & BIT2) == BIT2);
        deletedSessionFlag = ((Flags & BIT3) == BIT3);

        SessionID = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
        offset += 2;

        if(describesSessionFlag){
            byte[] dataForLong = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong, 0, 4);
            buffer = ByteBuffer.wrap(dataForLong, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            SessionStartBaseTime = buffer.getLong();
            offset += 4;
            SessionStartTimeOffset = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
            offset += 2;
            if(!sessionSubSessionRunningFlag){
                byte[] dataForLong2 = {0, 0, 0, 0, 0, 0, 0, 0};
                System.arraycopy(data, offset, dataForLong2, 0, 4);
                buffer = ByteBuffer.wrap(dataForLong2, 0, 8);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                SessionEndBaseTime = buffer.getLong();
                offset += 4;
                SessionEndTimeOffset = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
                offset += 2;
            }
        }
        else{
            SubSessionID = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
            offset += 2;
            byte[] dataForLong = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong, 0, 4);
            buffer = ByteBuffer.wrap(dataForLong, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            SubSessionStartBaseTime = buffer.getLong();
            offset += 4;
            SubSessionStartTimeOffset = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
            offset += 2;
            if(!sessionSubSessionRunningFlag){
                byte[] dataForLong2 = {0, 0, 0, 0, 0, 0, 0, 0};
                System.arraycopy(data, offset, dataForLong2, 0, 4);
                buffer = ByteBuffer.wrap(dataForLong2, 0, 8);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                SubSessionEndBaseTime = buffer.getLong();
                offset += 4;
                SubSessionEndTimeOffset = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
                offset += 2;
            }
        }
    }




    public byte getFlags() { return Flags; }
    public int getSessionID() { return SessionID; }
    public Long getSessionStartBaseTime() { return SessionStartBaseTime; }
    public int getSessionStartTimeOffset() {return SessionStartTimeOffset; }
    public Long getSessionEndBaseTime() { return SessionEndBaseTime; }
    public int getSessionEndTimeOffset() { return  SessionEndTimeOffset; }
    public int getSubSessionID() { return SubSessionID; }
    public Long getSubSessionStartBaseTime() { return SubSessionStartBaseTime; };
    public int getSubSessionStartTimeOffset() { return SubSessionStartTimeOffset; };
    public Long getSubSessionEndBaseTime() { return SubSessionEndBaseTime; };
    public int getSubSessionEndTimeOffset() { return SubSessionEndTimeOffset; };

    public void setFlags(byte Flags){this.Flags=Flags;}
    public void setSessionID(int SessionID){this.SessionID=SessionID; }
    public void setSessionStartBaseTime(Long SessionStartBaseTime ) { this.SessionStartBaseTime = SessionStartBaseTime; }
    public void setSessionStartTimeOffset(int SessionStartTimeOffset) { this.SessionStartTimeOffset = SessionStartTimeOffset; }
    public void setSessionEndBaseTime(Long SessionEndBaseTime) { this.SessionEndBaseTime = SessionStartBaseTime; }
    public void setSessionEndTimeOffset(int SessionEndTimeOffset) { this.SessionEndTimeOffset = SessionEndTimeOffset; }
    public void setSubSessionID(int SubSessionID){this.SubSessionID=SubSessionID;}
    public void setSubSessionStartBaseTime(Long SubSessionStartBaseTime) { this.SubSessionStartBaseTime = SubSessionStartBaseTime; }
    public void setSubSessionStartTimeOffset(int SubSessionStartTimeOffset) { this.SubSessionStartTimeOffset = SubSessionStartTimeOffset; }
    public void setSubSessionEndBaseTime(Long SubSessionEndBaseTime) { this.SubSessionEndBaseTime = SubSessionEndBaseTime; }
    public void setSubSessionEndTimeOffset(int SubSessionEndTimeOffset) { this.SubSessionEndTimeOffset = SubSessionEndTimeOffset; }
}
