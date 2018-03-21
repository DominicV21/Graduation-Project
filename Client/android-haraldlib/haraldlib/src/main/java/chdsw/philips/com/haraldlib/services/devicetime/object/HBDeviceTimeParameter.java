package chdsw.philips.com.haraldlib.services.devicetime.object;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import chdsw.philips.com.haraldlib.services.bloodpressure.object.Sfloat;

public class HBDeviceTimeParameter {
    private Sfloat maximumDriftLimit;
    private int maxDaysUntilSyncLoss;
    private Sfloat rtcResolution;
    private Sfloat nonLoggedTimeAdjustmentLimit;
    private int displayedFormats;

    private boolean maximumDriftLimitIncluded;
    private boolean maxDaysUntilSyncLossIncluded;
    private boolean nonLoggedTimeAdjustmentLimitIncluded;
    private boolean displayedFormatsIncluded;

    public HBDeviceTimeParameter(byte[] data, HBDeviceTimeFeature feature) {
        int offset = 0;
        byte[] sfloatData = new byte[2];
        ByteBuffer buffer;

        maximumDriftLimitIncluded = false;
        maxDaysUntilSyncLossIncluded = false;
        nonLoggedTimeAdjustmentLimitIncluded = false;
        displayedFormatsIncluded = false;

        if(feature.isRtcDriftTrackingFlag()) {
            sfloatData[0] = data[offset];
            sfloatData[1] = data[offset + 1];
            maximumDriftLimit = new Sfloat(sfloatData);
            maximumDriftLimitIncluded = true;
            offset += 2;

            buffer = ByteBuffer.wrap(data, offset, 2);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            maxDaysUntilSyncLoss = buffer.getShort();
            maxDaysUntilSyncLossIncluded = true;
            offset += 2;
        }

        sfloatData[0] = data[offset];
        sfloatData[1] = data[offset + 1];
        rtcResolution = new Sfloat(sfloatData);
        offset += 2;

        if(feature.isTimeChangeLoggingFlag()) {
            sfloatData[0] = data[offset];
            sfloatData[1] = data[offset + 1];
            nonLoggedTimeAdjustmentLimit = new Sfloat(sfloatData);
            nonLoggedTimeAdjustmentLimitIncluded = true;
            offset += 2;
        }

        if(feature.isDisplayedFormatsSupportedFlag()) {
            buffer = ByteBuffer.wrap(data, offset, 2);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            displayedFormats = buffer.getShort();
            displayedFormatsIncluded = true;
            offset += 2;
        }
    }

    public Sfloat getMaximumDriftLimit() {
        return maximumDriftLimit;
    }

    public int getMaxDaysUntilSyncLoss() {
        return maxDaysUntilSyncLoss;
    }

    public Sfloat getRtcResolution() {
        return rtcResolution;
    }

    public Sfloat getNonLoggedTimeAdjustmentLimit() {
        return nonLoggedTimeAdjustmentLimit;
    }

    public int getDisplayedFormats() {
        return displayedFormats;
    }

    public boolean isMaximumDriftLimitIncluded() {
        return maximumDriftLimitIncluded;
    }

    public boolean isMaxDaysUntilSyncLossIncluded() {
        return maxDaysUntilSyncLossIncluded;
    }

    public boolean isNonLoggedTimeAdjustmentLimitIncluded() {
        return nonLoggedTimeAdjustmentLimitIncluded;
    }

    public boolean isDisplayedFormatsIncluded() {
        return displayedFormatsIncluded;
    }
}
