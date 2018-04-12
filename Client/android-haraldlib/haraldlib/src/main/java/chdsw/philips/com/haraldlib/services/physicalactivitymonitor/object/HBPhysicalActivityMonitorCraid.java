/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class HBPhysicalActivityMonitorCraid {
    private int Flags;
    private int SessionID = 0;
    private int SubSessionID = 0;
    private Long RelativeTimestamp = -1L;
    private Long SequenceNumber = -1L;
    private int VO2MAX = -1;
    private int HeartRate = -1;
    private int PulseInterBeatInterval = -1;
    private int RestingHeartRate = -1;
    private int HeartRateVariability = -1;
    private int RespirationRate = -1;
    private int RestingRespirationRate = -1;


    //Flags field needs to be described in the specification
    private boolean flagOne;
    private boolean flagTwo;
    private boolean flagThree;
    private boolean flagFour;
    private boolean flagFive;
    private boolean flagSix;
    private boolean flagSeven;

    private static final int BIT1 = 0x01;
    private static final int BIT2 = 0x02;
    private static final int BIT3 = 0x04;
    private static final int BIT4 = 0x08;
    private static final int BIT5 = 0x10;
    private static final int BIT6 = 0x20;
    private static final int BIT7 = 0x40;
    private static final int BIT8 = 0x80;

    public HBPhysicalActivityMonitorCraid(byte[] data){
        int offset = 0;
        ByteBuffer buffer;
        Flags = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
        offset += 2;

        // Replace Flag with the appropriate flags (look at HBPhysicalActivityMonitorGaid for example)
        flagOne = ((Flags & BIT1) == BIT1);
        flagTwo = ((Flags & BIT2) == BIT2);
        flagThree = ((Flags & BIT3) == BIT3);
        flagFour = ((Flags & BIT4) == BIT4);
        flagFive = ((Flags & BIT5) == BIT5);
        flagSix = ((Flags & BIT6) == BIT6);
        flagSeven = ((Flags & BIT7) == BIT7);

        SessionID = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
        offset+=2;

        SubSessionID = ((data[offset + 1] & 0xFF) << 8) | (data[offset] & 0xFF);
        offset+=2;

        byte[] dataForLong = {0, 0, 0, 0, 0, 0, 0, 0};
        System.arraycopy(data, offset, dataForLong, 0, 4);
        buffer = ByteBuffer.wrap(dataForLong, 0, 8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        RelativeTimestamp = buffer.getLong();
        offset += 4;

        byte[] dataForLong2 = {0, 0, 0, 0, 0, 0, 0, 0};
        System.arraycopy(data, offset, dataForLong2, 0, 4);
        buffer = ByteBuffer.wrap(dataForLong2, 0, 8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        SequenceNumber = buffer.getLong();
        offset += 4;

        if(flagOne)
        {
            VO2MAX = data[offset++];
        }
        if(flagTwo)
        {
            HeartRate = data[offset++];
        }
        if(flagThree)
        {
            PulseInterBeatInterval = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
            offset += 2;
        }
        if(flagFour)
        {
            RestingHeartRate = data[offset++];
        }
        if(flagFive)
        {
            HeartRateVariability = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
        }
        if(flagSix)
        {
            RespirationRate = data[offset++];
        }
        if(flagSeven)
        {
            RestingRespirationRate = data[offset++];
        }
    }




    public int getFlags() { return Flags; }
    public int getSessionID() { return SessionID; }
    public int getSubSessionID() { return SubSessionID; }
    public Long getRelativeTimestamp() {return RelativeTimestamp; }
    public Long getSequenceNumber() {return SequenceNumber; }
    public int getVO2Max() {return VO2MAX; }
    public int getHeartRate() {return HeartRate; }
    public int getPulseInterBeatInterval() {return PulseInterBeatInterval; }
    public int getRestingHeartRate() {return RestingHeartRate; }
    public int getHeartRateVariability() {return HeartRateVariability; }
    public int getRespirationRate() {return RespirationRate; }
    public int getRestingRespirationRate() {return RestingRespirationRate; }

    public void setFlags(int Flags){this.Flags=Flags;}
    public void setSessionID(int SessionID){this.SessionID=SessionID;}
    public void setSubSessionID(int SubSessionID){this.SubSessionID=SubSessionID;}
    public void setRelativeTimestamp(Long RelativeTimestamp){this.RelativeTimestamp=RelativeTimestamp;}
    public void setSequenceNumber(Long SequenceNumber){this.SequenceNumber=SequenceNumber;}
    public void setVO2Max                (int VO2MAX) {this.VO2MAX=VO2MAX;}
    public void setHeartRate             (int HeartRate) {this.HeartRate=HeartRate;}
    public void setPulseInterBeatInterval(int PulseInterBeatInterval) {this.PulseInterBeatInterval=PulseInterBeatInterval;}
    public void setRestingHeartRate      (int RestingHeartRate) {this.RestingHeartRate=RestingHeartRate;}
    public void setHeartRateVariability  (int HeartRateVariability) {this.HeartRateVariability=HeartRateVariability;}
    public void setRespirationRate       (int RespirationRate) {this.RespirationRate=RespirationRate;}
    public void setRestingRespirationRate(int RestingRespirationRate) {this.RestingRespirationRate=RestingRespirationRate;}


}
