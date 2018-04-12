/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import chdsw.philips.com.haraldlib.HBLogger;

public class HBPhysicalActivityMonitorGaid {
    private Long Flags = -1L;
    private int SessionID = 0;
    private int SubSessionID = 0;
    private Long RelativeTimestamp = -1L;
    private Long SequenceNumber = -1L;
    private int NormalWalkingEnergyExpenditurePerHour = -1;
    private int IntensityEnergyExpenditurePerHour = -1;
    private int TotalEnergyExpenditurePerHour = -1;
    private int FatBurnedPerHour = -1;
    private int MetabolicEquivalent = -1;
    private int Speed = -1;
    private int MotionCadence = -1;
    private int Elevation = -1;
    private int ActivityCountPerMinute = -1;
    private int ActivityLevel = -1;
    private int ActivityType = -1;

    private boolean normalWalkingEnergyExpenditurePerHourPresent;
    private boolean intensityEnergyExpenditurePerHourPresent;
    private boolean totalEnergyExpenditurePerHourPresent;
    private boolean fatBurnedPerHourPresent;
    private boolean metabolicEquivalentPresent;
    private boolean speedPresent;
    private boolean motionCadencePresent;
    private boolean elevationPresent;
    private boolean activityCountPerMinutePresent;
    private boolean activityLevelPresent;
    private boolean activityTypePresent;

    private static final int BIT1 = 0x01;
    private static final int BIT2 = 0x02;
    private static final int BIT3 = 0x04;
    private static final int BIT4 = 0x08;
    private static final int BIT5 = 0x10;
    private static final int BIT6 = 0x20;
    private static final int BIT7 = 0x40;
    private static final int BIT8 = 0x80;
    private static final int BIT9 = 0x100;
    private static final int BIT10 = 0x200;
    private static final int BIT11 = 0x400;

    public HBPhysicalActivityMonitorGaid(byte[] data){
        int offset = 0;
        ByteBuffer buffer;
        byte[] dataForLong1 = {0, 0, 0, 0, 0, 0, 0, 0};
        System.arraycopy(data, offset, dataForLong1, 0, 3);
        buffer = ByteBuffer.wrap(dataForLong1, 0, 8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        Flags = buffer.getLong();
        offset += 3;


        normalWalkingEnergyExpenditurePerHourPresent = ((Flags & BIT1) == BIT1);
        intensityEnergyExpenditurePerHourPresent = ((Flags & BIT2) == BIT2);
        totalEnergyExpenditurePerHourPresent = ((Flags & BIT3) == BIT3);
        fatBurnedPerHourPresent = ((Flags & BIT4) == BIT4);
        metabolicEquivalentPresent = ((Flags & BIT5) == BIT5);
        speedPresent = ((Flags & BIT6) == BIT6);
        motionCadencePresent = ((Flags & BIT7) == BIT7);
        elevationPresent = ((Flags & BIT8) == BIT8);
        activityCountPerMinutePresent = ((Flags & BIT9) == BIT9);
        activityLevelPresent = ((Flags & BIT10) == BIT10);
        activityTypePresent = ((Flags & BIT11) == BIT11);

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

        if(normalWalkingEnergyExpenditurePerHourPresent)
        {
            NormalWalkingEnergyExpenditurePerHour = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
            offset += 2;
        }
        if(intensityEnergyExpenditurePerHourPresent)
        {
            IntensityEnergyExpenditurePerHour = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
            offset += 2;
        }
        if(totalEnergyExpenditurePerHourPresent)
        {
            TotalEnergyExpenditurePerHour = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
            offset += 2;
        }
        if(fatBurnedPerHourPresent)
        {
            FatBurnedPerHour = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
            offset += 2;
        }
        if(metabolicEquivalentPresent)
        {
            MetabolicEquivalent = data[offset++];
        }
        if(speedPresent)
        {
            Speed = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
            offset += 2;
        }
        if(motionCadencePresent)
        {
            MotionCadence = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
            offset += 2;
        }
        if(elevationPresent)
        {
            Elevation = (((data[offset + 2] & 0xFF) << 16) | ((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
            offset += 3;
        }
        if(activityCountPerMinutePresent)
        {
            ActivityCountPerMinute = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
            offset += 2;
        }
        if(activityLevelPresent)
        {
            ActivityLevel = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
            offset += 2;
        }
        if(activityTypePresent)
        {
            ActivityType = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
            offset += 2;
        }
    }




    public Long getFlags() { return Flags; }
    public int getSessionID() { return SessionID; }
    public int getSubSessionID() { return SubSessionID; }
    public Long getRelativeTimestamp() {return RelativeTimestamp; }
    public Long getSequenceNumber() {return SequenceNumber; }
    public int getNormalWalkingEnergyExpenditurePerHour() {return NormalWalkingEnergyExpenditurePerHour; }
    public int getIntensityEnergyExpenditurePerHour() {return IntensityEnergyExpenditurePerHour; }
    public int getTotalEnergyExpenditurePerHour() {return TotalEnergyExpenditurePerHour; }
    public int getFatBurnedPerHour() {return FatBurnedPerHour; }
    public int getMetabolicEquivalent() {return MetabolicEquivalent; }
    public int getSpeed() {return Speed; }
    public int getMotionCadence() {return MotionCadence; }
    public int getElevation() {return Elevation; }
    public int getActivityCountPerMinute() {return ActivityCountPerMinute; }
    public int getActivityLevel() {return ActivityLevel; }
    public int getActivityType() {return ActivityType; }

    public void setFlags(Long Flags){this.Flags=Flags;}
    public void setSessionID(int SessionID){this.SessionID=SessionID;}
    public void setSubSessionID(int SubSessionID){this.SubSessionID=SubSessionID;}
    public void setRelativeTimestamp(Long RelativeTimestamp){this.RelativeTimestamp=RelativeTimestamp;}
    public void setSequenceNumber(Long SequenceNumber){this.SequenceNumber=SequenceNumber;}
    public void setNormalWalkingEnergyExpenditurePerHour(int NormalWalkingEnergyExpenditurePerHour){this.NormalWalkingEnergyExpenditurePerHour=NormalWalkingEnergyExpenditurePerHour;}
    public void setIntensityEnergyExpenditurePerHour(int IntensityEnergyExpenditurePerHour){this.IntensityEnergyExpenditurePerHour=IntensityEnergyExpenditurePerHour;}
    public void setTotalEnergyExpenditurePerHour(int TotalEnergyExpenditurePerHour){this.TotalEnergyExpenditurePerHour=TotalEnergyExpenditurePerHour;}
    public void setFatBurnedPerHour(int FatBurnedPerHour){this.FatBurnedPerHour=FatBurnedPerHour;}
    public void setMetabolicEquivalent(int MetabolicEquivalent){this.MetabolicEquivalent=MetabolicEquivalent;}
    public void setSpeed(int Speed){this.Speed=Speed;}
    public void setMotionCadence(int MotionCadence){this.MotionCadence=MotionCadence;}
    public void setElevation(int Elevation){this.Elevation=Elevation;}
    public void setActivityCountPerMinute(int ActivityCountPerMinute){this.ActivityCountPerMinute=ActivityCountPerMinute;}
    public void setActivityLevel(int ActivityLevel){this.ActivityLevel=ActivityLevel;}
    public void setActivityType(int ActivityType){this.ActivityType=ActivityType;}

}
