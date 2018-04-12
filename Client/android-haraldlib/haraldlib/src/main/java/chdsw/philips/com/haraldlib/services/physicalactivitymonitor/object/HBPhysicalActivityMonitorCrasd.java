/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class HBPhysicalActivityMonitorCrasd {
    private Long Flags;
    private int SessionID = 0;
    private int SubSessionID = 0;
    private Long RelativeTimestamp = -1L;
    private Long SequenceNumber = -1L;
    private Long	TimeInHeartRateZone1            = -1L;
    private Long	TimeInHeartRateZone2            = -1L;
    private Long	TimeInHeartRateZone3            = -1L;
    private Long	TimeInHeartRateZone4            = -1L;
    private Long	TimeInHeartRateZone5            = -1L;
    private int	MinimumVO2Max                   = -1;
    private int	MaximumVO2Max                   = -1;
    private int	AverageVO2Max                   = -1;
    private int	MinimumHeartRate                = -1;
    private int	MaximumHeartRate                = -1;
    private int	AverageHeartRate                = -1;
    private int	MinimumPulseInterbeatInterval   = -1;
    private int	MaximumPulseInterbeatInterval   = -1;
    private int	AveragePulseInterbeatInterval   = -1;
    private int	MinimumRestingHeartRate         = -1;
    private int	MaximumRestingHeartRate         = -1;
    private int	AverageRestingHeartRate         = -1;
    private int	MinimumHeartRateVariability     = -1;
    private int	MaximumHeartRateVariability     = -1;
    private int	AverageHeartRateVariability     = -1;
    private int	MinimumRespirationRate          = -1;
    private int	MaximumRespirationRate          = -1;
    private int	AverageRespirationRate          = -1;
    private int	MinimumRestingRespirationRate   = -1;
    private int	MaximumRestingRespirationRate   = -1;
    private int	AverageRestingRespirationRate   = -1;
    private Long	WornDuration                    = -1L;


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
    private boolean flag21;
    private boolean flag22;
    private boolean flag23;
    private boolean flag24;
    private boolean flag25;
    private boolean flag26;
    private boolean flag27;

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
    private static final int BITL = 0x100000;
    private static final int BITM = 0x200000;
    private static final int BITN = 0x400000;
    private static final int BITO = 0x800000;
    private static final int BITP = 0x1000000;
    private static final int BITQ = 0x2000000;
    private static final int BITR = 0x4000000;

    public HBPhysicalActivityMonitorCrasd(byte[] data) {
        int offset = 0;
        ByteBuffer buffer;
        byte[] dataForLong5 = {0, 0, 0, 0, 0, 0, 0, 0};
        System.arraycopy(data, offset, dataForLong5, 0, 4);
        buffer = ByteBuffer.wrap(dataForLong5, 0, 8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        Flags = buffer.getLong();
        offset += 4;


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
        flag21 = ((Flags & BITL) == BITL);
        flag22 = ((Flags & BITM) == BITM);
        flag23 = ((Flags & BITN) == BITN);
        flag24 = ((Flags & BITO) == BITO);
        flag25 = ((Flags & BITP) == BITP);
        flag26 = ((Flags & BITQ) == BITQ);
        flag27 = ((Flags & BITR) == BITR);

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
            TimeInHeartRateZone1 = buffer.getLong();
            offset += 3;
        }
        if (flag2) {
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 3);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            TimeInHeartRateZone2 = buffer.getLong();
            offset += 3;
        }
        if (flag3) {
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 3);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            TimeInHeartRateZone3 = buffer.getLong();
            offset += 3;
        }
        if (flag4) {
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 3);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            TimeInHeartRateZone4 = buffer.getLong();
            offset += 3;
        }
        if (flag5) {
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 3);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            TimeInHeartRateZone5 = buffer.getLong();
            offset += 3;
        }
        if (flag6) {
            MinimumVO2Max = data[offset++];
        }
        if (flag7) {
            MaximumVO2Max = data[offset++];
        }
        if (flag8) {
            AverageVO2Max  = data[offset++];
        }
        if (flag9) {
            MinimumHeartRate  = data[offset++];
        }
        if (flag10) {
            MaximumHeartRate  = data[offset++];
        }
        if (flag11) {
            AverageHeartRate  = data[offset++];
        }
        if (flag12) {
            MinimumPulseInterbeatInterval = ((data[offset + 1] & 0xFF) << 8) | (data[offset] & 0xFF);
            offset += 2;
        }
        if (flag13) {
            MaximumPulseInterbeatInterval = ((data[offset + 1] & 0xFF) << 8) | (data[offset] & 0xFF);
            offset += 2;
        }
        if (flag14) {
            AveragePulseInterbeatInterval = ((data[offset + 1] & 0xFF) << 8) | (data[offset] & 0xFF);
            offset += 2;
        }
        if (flag15) {
            MinimumRestingHeartRate = data[offset++];
        }
        if (flag16) {
            MaximumRestingHeartRate = data[offset++];
        }
        if (flag17) {
            AverageRestingHeartRate = data[offset++];
        }
        if (flag18) {
            MinimumHeartRateVariability = ((data[offset + 1] & 0xFF) << 8) | (data[offset] & 0xFF);
            offset += 2;
        }
        if (flag19) {
            MaximumHeartRateVariability = ((data[offset + 1] & 0xFF) << 8) | (data[offset] & 0xFF);
            offset += 2;
        }
        if (flag20) {
            AverageHeartRateVariability = ((data[offset + 1] & 0xFF) << 8) | (data[offset] & 0xFF);
            offset += 2;
        }
        if (flag21) {
            MinimumRespirationRate = data[offset++];
        }
        if (flag22) {
            MaximumRespirationRate = data[offset++];
        }
        if (flag23) {
            AverageRespirationRate = data[offset++];
        }
        if (flag24) {
            MinimumRestingRespirationRate = data[offset++];
        }
        if (flag25) {
            MaximumRestingRespirationRate = data[offset++];
        }
        if (flag26) {
            AverageRestingRespirationRate = data[offset++];
        }
        if (flag27) {
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
    public  Long	getTimeInHeartRateZone1         (){ return TimeInHeartRateZone1         ;}
    public  Long	getTimeInHeartRateZone2         (){ return TimeInHeartRateZone2         ;}
    public  Long	getTimeInHeartRateZone3         (){ return TimeInHeartRateZone3         ;}
    public  Long	getTimeInHeartRateZone4         (){ return TimeInHeartRateZone4         ;}
    public  Long	getTimeInHeartRateZone5         (){ return TimeInHeartRateZone5         ;}
    public  int	getMinimumVO2Max                (){ return MinimumVO2Max                ;}
    public  int	getMaximumVO2Max                (){ return MaximumVO2Max                ;}
    public  int	getAverageVO2Max                (){ return AverageVO2Max                ;}
    public  int	getMinimumHeartRate             (){ return MinimumHeartRate             ;}
    public  int	getMaximumHeartRate             (){ return MaximumHeartRate             ;}
    public  int	getAverageHeartRate             (){ return AverageHeartRate             ;}
    public  int	getMinimumPulseInterbeatInterval(){ return MinimumPulseInterbeatInterval;}
    public  int	getMaximumPulseInterbeatInterval(){ return MaximumPulseInterbeatInterval;}
    public  int	getAveragePulseInterbeatInterval(){ return AveragePulseInterbeatInterval;}
    public  int	getMinimumRestingHeartRate      (){ return MinimumRestingHeartRate      ;}
    public  int	getMaximumRestingHeartRate      (){ return MaximumRestingHeartRate      ;}
    public  int	getAverageRestingHeartRate      (){ return AverageRestingHeartRate      ;}
    public  int	getMinimumHeartRateVariability  (){ return MinimumHeartRateVariability  ;}
    public  int	getMaximumHeartRateVariability  (){ return MaximumHeartRateVariability  ;}
    public  int	getAverageHeartRateVariability  (){ return AverageHeartRateVariability  ;}
    public  int	getMinimumRespirationRate       (){ return MinimumRespirationRate       ;}
    public  int	getMaximumRespirationRate       (){ return MaximumRespirationRate       ;}
    public  int	getAverageRespirationRate       (){ return AverageRespirationRate       ;}
    public  int	getMinimumRestingRespirationRate(){ return MinimumRestingRespirationRate;}
    public  int	getMaximumRestingRespirationRate(){ return MaximumRestingRespirationRate;}
    public  int	getAverageRestingRespirationRate(){ return AverageRestingRespirationRate;}
    public  Long	getWornDuration                 (){ return WornDuration                 ;}

    public void setFlags(Long Flags){this.Flags=Flags;}
    public void setSessionID(int SessionID){this.SessionID=SessionID;}
    public void setSubSessionID(int SubSessionID){this.SubSessionID=SubSessionID;}
    public void setRelativeTimestamp(Long RelativeTimestamp){this.RelativeTimestamp=RelativeTimestamp;}
    public void setSequenceNumber(Long SequenceNumber){this.SequenceNumber=SequenceNumber;}
    public void setTimeInHeartRateZone1         (Long	 TimeInHeartRateZone1         )      {this.TimeInHeartRateZone1         = TimeInHeartRateZone1         ;}
    public void setTimeInHeartRateZone2         (Long	 TimeInHeartRateZone2         )      {this.TimeInHeartRateZone2         = TimeInHeartRateZone2         ;}
    public void setTimeInHeartRateZone3         (Long	 TimeInHeartRateZone3         )      {this.TimeInHeartRateZone3         = TimeInHeartRateZone3         ;}
    public void setTimeInHeartRateZone4         (Long	 TimeInHeartRateZone4         )      {this.TimeInHeartRateZone4         = TimeInHeartRateZone4         ;}
    public void setTimeInHeartRateZone5         (Long	 TimeInHeartRateZone5         )      {this.TimeInHeartRateZone5         = TimeInHeartRateZone5         ;}
    public void settMinimumVO2Max                (int	 MinimumVO2Max                )      {this.MinimumVO2Max                = MinimumVO2Max                ;}
    public void settMaximumVO2Max                (int	 MaximumVO2Max                )      {this.MaximumVO2Max                = MaximumVO2Max                ;}
    public void settAverageVO2Max                (int	 AverageVO2Max                )      {this.AverageVO2Max                = AverageVO2Max                ;}
    public void settMinimumHeartRate             (int	 MinimumHeartRate             )      {this.MinimumHeartRate             = MinimumHeartRate             ;}
    public void settMaximumHeartRate             (int	 MaximumHeartRate             )      {this.MaximumHeartRate             = MaximumHeartRate             ;}
    public void settAverageHeartRate             (int	 AverageHeartRate             )      {this.AverageHeartRate             = AverageHeartRate             ;}
    public void setgerMinimumPulseInterbeatInterval(int MinimumPulseInterbeatInterval	){this.MinimumPulseInterbeatInterval= MinimumPulseInterbeatInterval;}
    public void setgerMaximumPulseInterbeatInterval(int MaximumPulseInterbeatInterval	){this.MaximumPulseInterbeatInterval= MaximumPulseInterbeatInterval;}
    public void setgerAveragePulseInterbeatInterval(int AveragePulseInterbeatInterval	){this.AveragePulseInterbeatInterval= AveragePulseInterbeatInterval;}
    public void settMinimumRestingHeartRate      (int	 MinimumRestingHeartRate      )      {this.MinimumRestingHeartRate      = MinimumRestingHeartRate      ;}
    public void settMaximumRestingHeartRate      (int	 MaximumRestingHeartRate      )      {this.MaximumRestingHeartRate      = MaximumRestingHeartRate      ;}
    public void settAverageRestingHeartRate      (int	 AverageRestingHeartRate      )      {this.AverageRestingHeartRate      = AverageRestingHeartRate      ;}
    public void setgerMinimumHeartRateVariability  (int MinimumHeartRateVariability  	){this.MinimumHeartRateVariability  = MinimumHeartRateVariability  ;}
    public void setgerMaximumHeartRateVariability  (int MaximumHeartRateVariability  	){this.MaximumHeartRateVariability  = MaximumHeartRateVariability  ;}
    public void setgerAverageHeartRateVariability  (int AverageHeartRateVariability  	){this.AverageHeartRateVariability  = AverageHeartRateVariability  ;}
    public void settMinimumRespirationRate       (int	 MinimumRespirationRate       )      {this.MinimumRespirationRate       = MinimumRespirationRate       ;}
    public void settMaximumRespirationRate      (int	 MaximumRespirationRate      )       {this.MaximumRespirationRate       = MaximumRespirationRate      ;}
    public void settAverageRespirationRate       (int	 AverageRespirationRate       )      {this.AverageRespirationRate       = AverageRespirationRate       ;}
    public void settMinimumRestingRespirationRate(int	 MinimumRestingRespirationRate)      {this.MinimumRestingRespirationRate= MinimumRestingRespirationRate;}
    public void settMaximumRestingRespirationRate(int	 MaximumRestingRespirationRate)      {this.MaximumRestingRespirationRate= MaximumRestingRespirationRate;}
    public void settAverageRestingRespirationRate(int	 AverageRestingRespirationRate)      {this.AverageRestingRespirationRate= AverageRestingRespirationRate;}
    public void setWornDuration                 (Long	 WornDuration                 )      {this.WornDuration                 = WornDuration                 ;}


}
