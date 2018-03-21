/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import chdsw.philips.com.haraldlib.HBLogger;

public class HBPhysicalActivityMonitorGaid {
    private byte[] Flags;
    private int SessionID = 0;
    private int SubSessionID = 0;
    private int RelativeTimestamp;
    private int SequenceNumber;
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

    public HBPhysicalActivityMonitorGaid(byte[] data){
        int offset = 1; //starts at 1 because 0 is a message header
        ByteBuffer buffer;
        Flags = new byte[3];
        Flags[0] |= data[offset];
        Flags[1] |= (data[offset + 1]);
        Flags[2] |= (data[offset + 2]);
        offset += 3;

        normalWalkingEnergyExpenditurePerHourPresent = ((Flags[0] & BIT1) == BIT1);
        intensityEnergyExpenditurePerHourPresent = ((Flags[0] & BIT2) == BIT2);
        totalEnergyExpenditurePerHourPresent = ((Flags[0] & BIT3) == BIT3);
        fatBurnedPerHourPresent = ((Flags[0] & BIT4) == BIT4);
        metabolicEquivalentPresent = ((Flags[0] & BIT5) == BIT5);
        speedPresent = ((Flags[0] & BIT6) == BIT6);
        motionCadencePresent = ((Flags[0] & BIT7) == BIT7);
        elevationPresent = ((Flags[0] & BIT8) == BIT8);
        activityCountPerMinutePresent = ((Flags[1] & BIT1) == BIT1);
        activityLevelPresent = ((Flags[1] & BIT2) == BIT2);
        activityTypePresent = ((Flags[1] & BIT3) == BIT3);

        //buffer = ByteBuffer.wrap(data, offset, 2);
        //buffer.order(ByteOrder.LITTLE_ENDIAN);
        SessionID = (((data[offset] & 0xFF) << 8) | (((data[offset + 1]) & 0xFF)));
        offset+=2;

        SubSessionID = ((data[offset] & 0xFF) << 8) | (data[offset + 1] & 0xFF);
        offset+=2;

        /*
        int mySessionID = Integer.parseInt(pamControlPointGetEndedSessionID.getText().toString());
        int mySubSessionID = Integer.parseInt(pamControlPointGetEndedSubSessionID.getText().toString());
        int swappedSessionID = 0;
        int swappedSubSessionID = 0;

        swappedSessionID = ((byte)(mySessionID & 0xFF) << 8) + (byte)((mySessionID >> 8) & 0xFF);
        swappedSubSessionID = ((byte)(mySubSessionID & 0xFF) << 8) + (byte)((mySessionID >> 8) & 0xFF);*/

        int test = ( (data[offset + 3] << 24) | (data[offset + 2] << 16) | (data[offset + 1] << 8) | data[offset]);
        RelativeTimestamp = test;
        offset += 4;

        byte[] dataForLong2 = {0, 0, 0, 0, 0, 0, 0, 0};
        System.arraycopy(data, offset, dataForLong2, 0, 4);
        buffer = ByteBuffer.wrap(dataForLong2, 0, 8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        SequenceNumber = buffer.getInt();
        offset += 4;

        if(normalWalkingEnergyExpenditurePerHourPresent)
        {
            NormalWalkingEnergyExpenditurePerHour = data[offset] + data[offset + 1];
            offset += 2;
        }
        if(intensityEnergyExpenditurePerHourPresent)
        {
            IntensityEnergyExpenditurePerHour = data[offset];
            offset += 2;
        }
        if(totalEnergyExpenditurePerHourPresent)
        {
            TotalEnergyExpenditurePerHour = data[offset];
            offset += 2;
        }
        if(fatBurnedPerHourPresent)
        {
            FatBurnedPerHour = data[offset];
            offset += 2;
        }
        if(metabolicEquivalentPresent)
        {
            MetabolicEquivalent = data[offset++];
        }
        if(speedPresent)
        {
            Speed = data[offset];
            offset += 2;
        }
        if(motionCadencePresent)
        {
            MotionCadence = data[offset];
            offset += 2;
        }
        if(elevationPresent)
        {
            Elevation = data[offset] | (data[offset + 1] << 8) + (data[offset + 2] << 16);
            offset += 3;
        }
        if(activityCountPerMinutePresent)
        {
            ActivityCountPerMinute = data[offset];
            offset += 2;
        }
        if(activityLevelPresent)
        {
            ActivityLevel = data[offset];
            offset += 2;
        }
        if(activityTypePresent)
        {
            ActivityType = data[offset];
            offset += 2;
        }
    }




    public byte[] getFlags() { return Flags; }
    public int getSessionID() { return SessionID; }
    public int getSubSessionID() { return SubSessionID; }
    public int getRelativeTimestamp() {return RelativeTimestamp; }
    public int getSequenceNumber() {return SequenceNumber; }
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

    public void setFlags(byte[] Flags){this.Flags=Flags;}
    public void setSessionID(int SessionID){this.SessionID=SessionID;}
    public void setSubSessionID(int SubSessionID){this.SubSessionID=SubSessionID;}
    public void setRelativeTimestamp(int RelativeTimestamp){this.RelativeTimestamp=RelativeTimestamp;}
    public void setSequenceNumber(int SequenceNumber){this.SequenceNumber=SequenceNumber;}
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
