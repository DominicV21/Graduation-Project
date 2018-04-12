/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class HBPhysicalActivityMonitorSaid {
    private int Flags = -1;
    private int SessionID = -1;
    private int SubSessionID = -1;
    private Long RelativeTimestamp = -1L;
    private Long SequenceNumber = -1L;
    private Long VisibleLightLevel = -1L;
    private Long UVLightLevel = -1L;
    private Long IRLightLevel = -1L;
    private int SleepStage = -1;
    private int SleepingHeartRate = -1;

    //to be defined in the specification
    private boolean flag1;
    private boolean flag2;
    private boolean flag3;
    private boolean flag4;
    private boolean flag5;

    private static final int BIT1 = 0x01;
    private static final int BIT2 = 0x02;
    private static final int BIT3 = 0x04;
    private static final int BIT4 = 0x08;
    private static final int BIT5 = 0x10;



    public HBPhysicalActivityMonitorSaid(byte[] data){
        int offset = 0;
        ByteBuffer buffer;
        Flags = ((data[offset + 1] & 0xFF) << 8) | (data[offset] & 0xFF);
        offset += 2;

        flag1 = ((Flags & BIT1) == BIT1);
        flag2 = ((Flags & BIT2) == BIT2);
        flag3 = ((Flags & BIT3) == BIT3);
        flag4 = ((Flags & BIT4) == BIT4);
        flag5 = ((Flags & BIT5) == BIT5);

        SessionID = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
        offset+=2;

        SubSessionID = ((data[offset + 1] & 0xFF) << 8) | (data[offset] & 0xFF);
        offset+=2;

        byte[] dataForLong2 = {0, 0, 0, 0, 0, 0, 0, 0};
        System.arraycopy(data, offset, dataForLong2, 0, 4);
        buffer = ByteBuffer.wrap(dataForLong2, 0, 8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        RelativeTimestamp = buffer.getLong();
        offset += 4;

        byte[] dataForLong3 = {0, 0, 0, 0, 0, 0, 0, 0};
        System.arraycopy(data, offset, dataForLong3, 0, 4);
        buffer = ByteBuffer.wrap(dataForLong3, 0, 8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        SequenceNumber = buffer.getLong();
        offset += 4;

        if(flag1) {
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 3);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            VisibleLightLevel = buffer.getLong();
            offset += 3;
        }
        if(flag2){
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 3);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            UVLightLevel = buffer.getLong();
            offset += 3;
        }
        if(flag3){
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 3);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            IRLightLevel = buffer.getLong();
            offset += 3;
        }
        if(flag4) {
            SleepStage = data[offset++];
        }
        if(flag5){
            SleepingHeartRate = data[offset++];
        }
    }

    public int getFlags(){return Flags;}
    public int  getSessionID(){return SessionID;}
    public int  getSubSessionID(){return SubSessionID;}
    public Long getRelativeTimestamp(){return RelativeTimestamp;}
    public Long getSequenceNumber(){return SequenceNumber;}
    public Long getVisibleLightLevel(){return VisibleLightLevel;}
    public Long getUVLightLevel(){return UVLightLevel;}
    public Long getIRLightLevel(){return IRLightLevel;}
    public int getSleepStage(){return SleepStage;}
    public int getSleepingHeartRate(){return SleepingHeartRate;}
}
