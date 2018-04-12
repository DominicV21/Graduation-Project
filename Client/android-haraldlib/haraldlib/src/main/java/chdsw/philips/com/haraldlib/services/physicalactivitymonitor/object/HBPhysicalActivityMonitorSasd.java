/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.TimeoutException;

public class HBPhysicalActivityMonitorSasd {
    private Long Flags = -1L;
    private int SessionID = 0;
    private int SubSessionID = 0;
    private Long RelativeTimestamp = -1L;
    private Long SequenceNumber = -1L;

    private Long	TotalSleepTime = -1L;
    private Long	TotalWakeTime = -1L;
    private Long	TotalBedTime = -1L;
    private int	    NumberOfAwakenings = 0;
    private int	    SleepLatency = 0;
    private int	    SleepEfficiency = 0;
    private int	    SnoozeTime = 0;
    private int	    NumberOfTossNturnEvents = 0;
    private Long	TimeOfAwakeningAfterAlarm = -1L;
    private Long	MinimumVisibleLightLevel = -1L;
    private Long	MaximumVisibleLightLevel = -1L;
    private Long	AverageVisibleLightLevel = -1L;
    private Long	MinimumUVLightLevel = -1L;
    private Long	MaximumUVLightLevel = -1L;
    private Long	AverageUVLightLevel = -1L;
    private Long	MinimumIRLightLevel = -1L;
    private Long	MaximumIRLightLevel = -1L;
    private Long	AverageIRLightLevel = -1L;
    private int	    AverageSleepingHeartRate = 0;
    private Long	WornDuration = -1L;


    //Flags field needs to be described in the specification
    private boolean flag1;
    private boolean flag2;
    private boolean flag3;
    private boolean flag4;
    private boolean flag5;
    private boolean flag6;
    private boolean flag7;
    private boolean flag8;
    private boolean flag9;
    private boolean flag10;
    private boolean flag11;
    private boolean flag12;
    private boolean flag13;
    private boolean flag14;
    private boolean flag15;
    private boolean flag16;
    private boolean flag17;
    private boolean flag18;
    private boolean flag19;
    private boolean flag20;

    private static final int BIT1 = 0x01;
    private static final int BIT2 = 0x02;
    private static final int BIT3 = 0x04;
    private static final int BIT4 = 0x08;
    private static final int BIT5 = 0x10;
    private static final int BIT6 = 0x20;
    private static final int BIT7 = 0x40;
    private static final int BIT8 = 0x80;
    private static final int BIT9 = 0x100;
    private static final int BITA = 0x200;
    private static final int BITB = 0x400;
    private static final int BITC = 0x800;
    private static final int BITD = 0x1000;
    private static final int BITE = 0x2000;
    private static final int BITF = 0x4000;
    private static final int BITG = 0x8000;
    private static final int BITH = 0x10000;
    private static final int BITI = 0x20000;
    private static final int BITJ = 0x40000;
    private static final int BITK = 0x80000;

    public HBPhysicalActivityMonitorSasd(byte[] data) {
        int offset = 0;
        ByteBuffer buffer;
        byte[] dataForLong1 = {0, 0, 0, 0, 0, 0, 0, 0};
        System.arraycopy(data, offset, dataForLong1, 0, 3);
        buffer = ByteBuffer.wrap(dataForLong1, 0, 8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        Flags = buffer.getLong();
        offset += 3;

        // Replace Flag with the appropriate flags (to be defined in the specification) (look at HBPhysicalActivityMonitorGaid for example)
        flag1 = ((Flags & BIT1) == BIT1);
        flag2 = ((Flags & BIT2) == BIT2);
        flag3 = ((Flags & BIT3) == BIT3);
        flag4 = ((Flags & BIT4) == BIT4);
        flag5 = ((Flags & BIT5) == BIT5);
        flag6 = ((Flags & BIT6) == BIT6);
        flag7 = ((Flags & BIT7) == BIT7);
        flag8 = ((Flags & BIT8) == BIT8);
        flag9 = ((Flags & BIT9) == BIT9);
        flag10 = ((Flags & BITA) == BITA);
        flag11 = ((Flags & BITB) == BITB);
        flag12 = ((Flags & BITC) == BITC);
        flag13 = ((Flags & BITD) == BITD);
        flag14 = ((Flags & BITE) == BITE);
        flag15 = ((Flags & BITF) == BITF);
        flag16 = ((Flags & BITG) == BITG);
        flag17 = ((Flags & BITH) == BITH);
        flag18 = ((Flags & BITI) == BITI);
        flag19 = ((Flags & BITJ) == BITJ);
        flag20 = ((Flags & BITK) == BITK);


        SessionID = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
        offset += 2;

        SubSessionID = ((data[offset + 1] & 0xFF) << 8) | (data[offset] & 0xFF);
        offset += 2;

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

        if (flag1) {
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 3);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            TotalSleepTime = buffer.getLong();
            offset += 3;
        }
        if (flag2) {
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 3);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            TotalWakeTime = buffer.getLong();
            offset += 3;
        }
        if (flag3) {
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 3);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            TotalBedTime = buffer.getLong();
            offset += 3;
        }
        if (flag4) {
            NumberOfAwakenings = ((data[offset + 1] & 0xFF) << 8) | (data[offset] & 0xFF);
            offset += 2;
        }
        if (flag5) {
            SleepLatency = ((data[offset + 1] & 0xFF) << 8) | (data[offset] & 0xFF);
            offset += 2;
        }
        if (flag6) {
            SleepEfficiency = data[offset++];
        }
        if (flag7) {
            SnoozeTime = ((data[offset + 1] & 0xFF) << 8) | (data[offset] & 0xFF);
            offset += 2;
        }
        if (flag8) {
            NumberOfTossNturnEvents = ((data[offset + 1] & 0xFF) << 8) | (data[offset] & 0xFF);
            offset += 2;
        }
        if (flag9) {
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 3);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            TimeOfAwakeningAfterAlarm = buffer.getLong();
            offset += 3;
        }
        if (flag10) {
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 3);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            MinimumVisibleLightLevel = buffer.getLong();
            offset += 3;
        }
        if (flag11) {
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 3);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            MaximumVisibleLightLevel = buffer.getLong();
            offset += 3;
        }
        if (flag12) {
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 3);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            AverageVisibleLightLevel = buffer.getLong();
            offset += 3;
        }
        if (flag13) {
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 3);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            MinimumUVLightLevel = buffer.getLong();
            offset += 3;
        }
        if (flag14) {
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 3);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            MaximumUVLightLevel = buffer.getLong();
            offset += 3;
        }
        if (flag15) {
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 3);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            AverageUVLightLevel = buffer.getLong();
            offset += 3;
        }
        if (flag16) {
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 3);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            MinimumIRLightLevel = buffer.getLong();
            offset += 3;
        }
        if (flag17) {
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 3);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            MaximumIRLightLevel = buffer.getLong();
            offset += 3;
        }
        if (flag18) {
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 3);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            AverageIRLightLevel = buffer.getLong();
            offset += 3;
        }
        if (flag19) {
            AverageSleepingHeartRate = data[offset++];
        }
        if (flag20) {
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 3);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            WornDuration = buffer.getLong();
            offset += 3;
        }
    }


    public Long getFlags() { return Flags; }
    public int getSessionID() { return SessionID; }
    public int getSubSessionID() { return SubSessionID; }
    public Long getRelativeTimestamp() {return RelativeTimestamp; }
    public Long getSequenceNumber() {return SequenceNumber; }
    public  Long	getTotalSleepTime               (){ return TotalSleepTime; }
    public  Long	getTotalWakeTime                (){ return TotalWakeTime; }
    public  Long	getTotalBedTime                 (){ return TotalBedTime; }
    public  int	    getNumberOfAwakenings           (){ return NumberOfAwakenings; }
    public  int	    getSleepLatency                 (){ return SleepLatency; }
    public  int	    getSleepEfficiency              (){ return SleepEfficiency; }
    public  int	    getSnoozeTime                   (){ return SnoozeTime; }
    public  int	    getNumberOfTossNturnEvents      (){ return NumberOfTossNturnEvents; }
    public  Long	getTimeOfAwakeningAfterAlarm    (){ return TimeOfAwakeningAfterAlarm; }
    public  Long	getMinimumVisibleLightLevel     (){ return MinimumVisibleLightLevel; }
    public  Long	getMaximumVisibleLightLevel     (){ return MaximumVisibleLightLevel; }
    public  Long	getAverageVisibleLightLevel     (){ return AverageVisibleLightLevel; }
    public  Long	getMinimumUVLightLevel          (){ return MinimumUVLightLevel; }
    public  Long	getMaximumUVLightLevel          (){ return MaximumUVLightLevel; }
    public  Long	getAverageUVLightLevel          (){ return AverageUVLightLevel; }
    public  Long	getMinimumIRLightLevel          (){ return MinimumIRLightLevel; }
    public  Long	getMaximumIRLightLevel          (){ return MaximumIRLightLevel; }
    public  Long	getAverageIRLightLevel          (){ return AverageIRLightLevel; }
    public  int	    getAverageSleepingHeartRate     (){ return AverageSleepingHeartRate; }
    public  Long	getWornDuration                 (){ return WornDuration; }
}
