/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class HBPhysicalActivityMonitorGasd {
    private Long Flags = 0L;
    private int SessionID = -1;
    private int SubSessionID = -1;
    private Long RelativeTimestamp = -1L;
    private Long SequenceNumber = -1L;
    private Long NormalWalkingEnergyExpenditure = -1L;
    private Long IntensityEnergyExpenditure = -1L;
    private Long TotalEnergyExpenditure = -1L;
    private int FatBurned = -1;
    private int MinimumMetabolicEquivalent = -1;
    private int MaximumMetabolicEquivalent = -1;
    private int AverageMetabolicEquivalent = -1;
    private Long Distance = -1L;
    private int MinimumSpeed = -1;
    private int MaximumSpeed = -1;
    private int AverageSpeed = -1;
    private Long DurationOfNormalWalkingEpisodes = -1L;
    private Long DurationOfIntensityWalkingEpisodes = -1L;
    private int MinimumMotionCadence = -1;
    private int MaximumMotionCadence = -1;
    private int AverageMotionCadence = -1;
    private int Floors = -1;
    private Long PositiveElevationGain = -1L;
    private Long NegativeElevationGain = -1L;
    private Long ActivityCount = -1L;
    private int MinimumActivityLevel = -1;
    private int MaximumActivityLevel = -1;
    private int AverageActivityLevel = -1;
    private int AverageActivityType = -1;
    private Long WornDuration = -1L;

    private boolean normalWalkingEnergyExpenditurePresent;
    private boolean intensityEnergyExpenditurePresent;
    private boolean totalEnergyExpenditurePresent;
    private boolean fatburnedpresent;
    private boolean minimumMetabolicEquivalentPresent;
    private boolean maximumMetabolicEquivalentPresent;
    private boolean averageMetabolicEquivalentPresent;
    private boolean distancePresent;
    private boolean minimumSpeedPresent;
    private boolean maximumSpeedPresent;
    private boolean averageSpeedPresent;
    private boolean durationOfPormalWalkingEpisodesPresent;
    private boolean durationOfIntensityWalkingEpisodesPresent;
    private boolean minimumMotionCadencePresent;
    private boolean maximumMotionCadencePresent;
    private boolean averageMotionCadencePresent;
    private boolean floorsPresent;
    private boolean positiveElevationGainPresent;
    private boolean negativeElevationGainPresent;
    private boolean activityCountPresent;
    private boolean minimumActivityLevelPresent;
    private boolean maximumActivityLevelPresent;
    private boolean averageActivityLevelPresent;
    private boolean averageActivityTypePresent;
    private boolean wornDurationPresent;

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


    public HBPhysicalActivityMonitorGasd(byte[] data){
        int offset = 0;
        ByteBuffer buffer;
        byte[] dataForLong = {0, 0, 0, 0, 0, 0, 0, 0};
        System.arraycopy(data, offset, dataForLong, 0, 4);
        buffer = ByteBuffer.wrap(dataForLong, 0, 8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        Flags = buffer.getLong();
        offset += 4;

        normalWalkingEnergyExpenditurePresent = ((Flags & BIT1) == BIT1);
        intensityEnergyExpenditurePresent = ((Flags & BIT2) == BIT2);
        totalEnergyExpenditurePresent = ((Flags & BIT3) == BIT3);
        fatburnedpresent = ((Flags & BIT4) == BIT4);
        minimumMetabolicEquivalentPresent = ((Flags & BIT5) == BIT5);
        maximumMetabolicEquivalentPresent = ((Flags & BIT6) == BIT6);
        averageMetabolicEquivalentPresent = ((Flags & BIT7) == BIT7);
        distancePresent = ((Flags & BIT8) == BIT8);
        minimumSpeedPresent = ((Flags & BIT9) == BIT9);
        maximumSpeedPresent = ((Flags & BITA) == BITA);
        averageSpeedPresent = ((Flags & BITB) == BITB);
        durationOfPormalWalkingEpisodesPresent = ((Flags & BITC) == BITC);
        durationOfIntensityWalkingEpisodesPresent = ((Flags & BITD) == BITD);
        minimumMotionCadencePresent = ((Flags & BITE) == BITE);
        maximumMotionCadencePresent = ((Flags & BITF) == BITF);
        averageMotionCadencePresent = ((Flags & BITG) == BITG);
        floorsPresent = ((Flags & BITH) == BITH);
        positiveElevationGainPresent = ((Flags & BITI) == BITI);
        negativeElevationGainPresent = ((Flags & BITJ) == BITJ);
        activityCountPresent = ((Flags & BITK) == BITK);
        minimumActivityLevelPresent = ((Flags & BITL) == BITL);
        maximumActivityLevelPresent = ((Flags & BITM) == BITM);
        averageActivityLevelPresent = ((Flags & BITN) == BITN);
        averageActivityTypePresent = ((Flags & BITO) == BITO);
        wornDurationPresent = ((Flags & BITP) == BITP);

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

        if(normalWalkingEnergyExpenditurePresent) {
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 4);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            NormalWalkingEnergyExpenditure = buffer.getLong();
            offset += 4;
        }
        if(intensityEnergyExpenditurePresent){
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 4);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            IntensityEnergyExpenditure = buffer.getLong();
            offset += 4;
        }
        if(totalEnergyExpenditurePresent){
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 4);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            TotalEnergyExpenditure = buffer.getLong();
            offset += 4;
        }
        if(fatburnedpresent) {
            FatBurned = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
            offset += 2;
        }
        if(minimumMetabolicEquivalentPresent){
            MinimumMetabolicEquivalent = data[offset++];
        }
        if(maximumMetabolicEquivalentPresent){
            MaximumMetabolicEquivalent = data[offset++];
        }
        if(averageMetabolicEquivalentPresent){
            AverageMetabolicEquivalent = data[offset++];
        }
        if(distancePresent){
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 3);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            Distance = buffer.getLong();
            offset += 3;
        }
        if(minimumSpeedPresent){
            MinimumSpeed = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
            offset += 2;
        }
        if(maximumSpeedPresent){
            MaximumSpeed = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
            offset += 2;
        }
        if(averageSpeedPresent){
            AverageSpeed = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
            offset += 2;
        }
        if(durationOfPormalWalkingEpisodesPresent){
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 3);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            DurationOfNormalWalkingEpisodes = buffer.getLong();
            offset += 3;
        }
        if(durationOfIntensityWalkingEpisodesPresent){
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 3);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            DurationOfIntensityWalkingEpisodes = buffer.getLong();
            offset += 3;
        }
        if(minimumMotionCadencePresent){
            MinimumMotionCadence = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
            offset += 2;
        }
        if(maximumMotionCadencePresent){
            MaximumMotionCadence = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
            offset += 2;
        }
        if(averageMotionCadencePresent){
            AverageMotionCadence = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
            offset += 2;
        }
        if(floorsPresent){
            Floors = data[offset++];
        }
        if(positiveElevationGainPresent){
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 3);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            PositiveElevationGain = buffer.getLong();
            offset += 3;
        }
        if(negativeElevationGainPresent){
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 3);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            NegativeElevationGain = buffer.getLong();
            offset += 3;
        }
        if(activityCountPresent){
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 4);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            ActivityCount = buffer.getLong();
            offset += 4;
        }
        if(minimumActivityLevelPresent){
            MinimumActivityLevel = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
            offset += 2;
        }
        if(maximumActivityLevelPresent){
            MaximumActivityLevel = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
            offset += 2;
        }
        if(averageActivityLevelPresent){
            AverageActivityLevel = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
            offset += 2;
        }
        if(averageActivityTypePresent){
            AverageActivityType = (((data[offset + 1] & 0xFF) << 8) | (((data[offset]) & 0xFF)));
            offset += 2;
        }
        if(wornDurationPresent){
            byte[] dataForLong4 = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong4, 0, 3);
            buffer = ByteBuffer.wrap(dataForLong4, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            WornDuration = buffer.getLong();
            offset += 3;
        }
    }

    public Long getFlags(){return Flags;}
    public int  getSessionID(){return SessionID;}
    public int  getSubSessionID(){return SubSessionID;}
    public Long getRelativeTimestamp(){return RelativeTimestamp;}
    public Long getSequenceNumber(){return SequenceNumber;}
    public Long getNormalWalkingEnergyExpenditure(){return NormalWalkingEnergyExpenditure;}
    public Long getIntensityEnergyExpenditure(){return IntensityEnergyExpenditure;}
    public Long getTotalEnergyExpenditure(){return TotalEnergyExpenditure;}
    public int  getFatBurned(){return FatBurned;}
    public int  getMinimumMetabolicEquivalent(){return MinimumMetabolicEquivalent;}
    public int  getMaximumMetabolicEquivalent(){return MaximumMetabolicEquivalent;}
    public int  getAverageMetabolicEquivalent(){return AverageMetabolicEquivalent;}
    public Long getDistance(){return Distance;}
    public int  getMinimumSpeed(){return MinimumSpeed;}
    public int  getMaximumSpeed(){return MaximumSpeed;}
    public int  getAverageSpeed(){return AverageSpeed;}
    public Long getDurationOfNormalWalkingEpisodes(){return DurationOfNormalWalkingEpisodes;}
    public Long getDurationOfIntensityWalkingEpisodes(){return DurationOfIntensityWalkingEpisodes;}
    public int  getMinimumMotionCadence(){return MinimumMotionCadence;}
    public int  getMaximumMotionCadence(){return MaximumMotionCadence;}
    public int  getAverageMotionCadence(){return AverageMotionCadence;}
    public int  getFloors(){return Floors;}
    public Long getPositiveElevationGain(){return PositiveElevationGain;}
    public Long getNegativeElevationGain(){return NegativeElevationGain;}
    public Long getActivityCount(){return ActivityCount;}
    public int  getMinimumActivityLevel(){return MinimumActivityLevel;}
    public int  getMaximumActivityLevel(){return MaximumActivityLevel;}
    public int  getAverageActivityLevel(){return AverageActivityLevel;}
    public int  getAverageActivityType(){return AverageActivityType;}
    public Long getWornDuration(){return WornDuration;}

    /*
    public void setFlags(Long Flags) {this.=;}
    public void setSessionID(int SessionID) {this.=;}
    public void setSubSessionID(int SubSessionID) {this.=;}
    public void setRelativeTimestamp(Long RelativeTimestamp) {this.=;}
    public void setSequenceNumber(Long SequenceNumber) {this.=;}
    public void setNormalWalkingEnergyExpenditure(Long NormalWalkingEnergyExpenditure) {this.=;}
    public void setIntensityEnergyExpenditure(Long IntensityEnergyExpenditure) {this.=;}
    public void setTotalEnergyExpenditure(Long TotalEnergyExpenditure) {this.=;}
    public void setFatBurned(int FatBurned) {this.=;}
    public void setMinimumMetabolicEquivalent(int          ) {this.=;}
    public void setMaximumMetabolicEquivalent(int          ) {this.=;}
    public void setAverageMetabolicEquivalent(int          ) {this.=;}
    public void setDistance(Long                            ) {this.=;}
    public void setMinimumSpeed(int                        ) {this.=;}
    public void setMaximumSpeed(int                        ) {this.=;}
    public void setAverageSpeed(int                        ) {this.=;}
    public void setDurationOfNormalWalkingEpisodes(Long     ) {this.=;}
    public void setDurationOfIntensityWalkingEpisodes(Long  ) {this.=;}
    public void setMinimumMotionCadence(int                ) {this.=;}
    public void setMaximumMotionCadence(int                ) {this.=;}
    public void setAverageMotionCadence(int                ) {this.=;}
    public void setFloors(int                              ) {this.=;}
    public void setPositiveElevationGain(Long               ) {this.=;}
    public void setNegativeElevationGain(Long               ) {this.=;}
    public void setActivityCount(Long                       ) {this.=;}
    public void setMinimumActivityLevel(int                ) {this.=;}
    public void setMaximumActivityLevel(int                ) {this.=;}
    public void setAverageActivityLevel(int                ) {this.=;}
    public void setAverageActivityType(int                ) {this.=;}
    public void setWornDuration(Long                        ) {this.=;}*/

}
