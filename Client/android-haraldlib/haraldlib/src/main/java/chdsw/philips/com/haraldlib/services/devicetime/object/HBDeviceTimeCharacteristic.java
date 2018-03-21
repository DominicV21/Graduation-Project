package chdsw.philips.com.haraldlib.services.devicetime.object;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import chdsw.philips.com.haraldlib.services.bloodpressure.object.Sfloat;

public class HBDeviceTimeCharacteristic {
    private long baseTime;
    private int baseTimeSecondFractions;
    private short timeZone;
    private short daylightSavingTimeOffset;
    private HBDeviceTimeStatus dtStatus;
    private short rtcTimeFaultCounter;
    private long userTime;
    private Sfloat accumulatedRTCDrift;
    private int daysRemainingForSyncLoss;

    private boolean baseTimeSecondFractionsIncluded;
    private boolean userTimeIncluded;
    private boolean accumulatedRTCDriftIncluded;
    private boolean daysRemainingUntilSyncLossIncluded;


    public HBDeviceTimeCharacteristic(byte[] data, HBDeviceTimeFeature feature) {
        int offset = 0;
        byte[] sfloatData = new byte[2];
        ByteBuffer buffer;

        baseTimeSecondFractionsIncluded = false;
        userTimeIncluded = false;
        accumulatedRTCDriftIncluded = false;
        daysRemainingUntilSyncLossIncluded = false;

        byte[] dataForLong = {0, 0, 0, 0, 0, 0, 0, 0};
        System.arraycopy(data, offset, dataForLong, 0, 4);

        buffer = ByteBuffer.wrap(dataForLong, 0, 8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        baseTime = buffer.getLong();
        offset += 4;

        if(feature.isBaseTimeSecondFractionsSupportedFlag()) {
            buffer = ByteBuffer.wrap(data, offset, 2);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            baseTimeSecondFractions = buffer.getShort();
            baseTimeSecondFractionsIncluded = true;
            offset += 2;
        }

        timeZone = data[offset];
        offset++;
        daylightSavingTimeOffset = data[offset];
        offset++;

        dtStatus = new HBDeviceTimeStatus(data[offset]);
        offset++;
        rtcTimeFaultCounter = data[offset];
        offset++;

        if(feature.isTimeOrDateDisplayedToUserFlag()) {
            for (int i = 0; i < dataForLong.length; i++) {
                dataForLong[i] = 0;
            }
            System.arraycopy(data, offset, dataForLong, 0, 4);

            buffer = ByteBuffer.wrap(dataForLong, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            userTime = buffer.getLong();
            userTimeIncluded = true;
            offset += 4;
        }

        if(feature.isRtcDriftTrackingFlag()) {
            sfloatData[0] = data[offset];
            sfloatData[1] = data[offset + 1];
            accumulatedRTCDrift = new Sfloat(sfloatData);
            accumulatedRTCDriftIncluded = true;
            offset += 2;

            buffer = ByteBuffer.wrap(data, offset, 2);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            daysRemainingForSyncLoss = buffer.getShort();
            daysRemainingUntilSyncLossIncluded = true;
            offset += 2;
        }
    }

    public long getBaseTime() {
        return baseTime;
    }

    public int getBaseTimeSecondFractions() {
        return baseTimeSecondFractions;
    }

    public short getTimeZone() {
        return timeZone;
    }

    public short getDaylightSavingTimeOffset() {
        return daylightSavingTimeOffset;
    }

    public HBDeviceTimeStatus getDtStatus() {
        return dtStatus;
    }

    public short getRtcTimeFaultCounter() {
        return rtcTimeFaultCounter;
    }

    public long getUserTime() {
        return userTime;
    }

    public Sfloat getAccumulatedRTCDrift() {
        return accumulatedRTCDrift;
    }

    public int getDaysRemainingForSyncLoss() {
        return daysRemainingForSyncLoss;
    }

    public boolean isBaseTimeSecondFractionsIncluded() {
        return baseTimeSecondFractionsIncluded;
    }

    public boolean isUserTimeIncluded() {
        return userTimeIncluded;
    }

    public boolean isAccumulatedRTCDriftIncluded() {
        return accumulatedRTCDriftIncluded;
    }

    public boolean isDaysRemainingUntilSyncLossIncluded() {
        return daysRemainingUntilSyncLossIncluded;
    }
}
