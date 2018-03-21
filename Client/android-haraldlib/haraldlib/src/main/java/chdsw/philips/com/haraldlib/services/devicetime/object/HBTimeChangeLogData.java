package chdsw.philips.com.haraldlib.services.devicetime.object;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import chdsw.philips.com.haraldlib.HBLogger;
import chdsw.philips.com.haraldlib.services.bloodpressure.object.Sfloat;

public class HBTimeChangeLogData {
    private int sequenceNumber;
    private int e2eCrc;

    private HBEventLogType eventLogType;
    private byte[] eventLogFlagsField = new byte[3];
    private long baseTime;
    private byte dtStatus;

    private Sfloat accumulatedRtcDrift;
    private int daysRemainingForSyncLoss;
    private short rtcTimeFaultCounter;
    private short timeZone;
    private short dstOffset;
    private HBTimeSource timeSource;
    private HBTimeAccuracy timeAccuracy;
    private long baseTimeOldValue;
    private int baseTimeSecondFractions;
    private int secondFractionsOldValue;
    private long userTime;
    private long userTimeOldValue;
    private Sfloat maximumDriftLimitOldValue;
    private int maxDaysUntilSyncLossOldValue;
    private Sfloat rtcResolutionOldValue;
    private Sfloat nonLoggedTimeAdjustmentLimitOldValue;
    private int displayedValuesOldValue;

    private byte[] field;

    private boolean accumulatedRTCDriftIncluded = false;
    private boolean daysUntilSyncLossIncluded = false;
    private boolean rTCTimeFaultCounterIncluded = false;
    private boolean timeZoneIncluded = false;
    private boolean dSTOffsetIncluded = false;
    private boolean timeSourceIncluded = false;
    private boolean timeAccuracyIncluded = false;
    private boolean baseTimeOldValueIncluded = false;
    private boolean baseTimeSecondFractionsIncluded = false;
    private boolean secondFractionsOldValueIncluded = false;
    private boolean userTimeIncluded = false;
    private boolean userTimeOldValueIncluded = false;
    private boolean maximumDriftLimitOldValueIncluded = false;
    private boolean maxDaysUntilSyncLossOldValueIncluded = false;
    private boolean rTCResolutionOldValueIncluded = false;
    private boolean nonLoggedTimeAdjustmentLimitOldValueIncluded = false;
    private boolean displayedFormatsOldValueFormatsIncluded = false;

    private final static int accumulatedRTCDriftPosition = 0x001;
    private final static int daysUntilSyncLossPosition = 0x002;
    private final static int rTCTimeFaultCounterPosition = 0x004;
    private final static int timeZonePosition = 0x008;
    private final static int dSTOffsetPosition = 0x010;
    private final static int timeSourcePosition = 0x020;
    private final static int timeAccuracyPosition = 0x040;
    private final static int baseTimeOldValuePosition = 0x080;
    private final static int baseTimeSecondFractionsPosition = 0x100;
    private final static int secondFractionsOldValuePosition = 0x200;
    private final static int userTimePosition = 0x400;
    private final static int userTimeOldValuePosition = 0x800;
    private final static int maximumDriftLimitOldValuePosition = 0x1000;
    private final static int maxDaysUntilSyncLossOldValuePosition = 0x2000;
    private final static int rTCResolutionOldValuePosition = 0x4000;
    private final static int nonLoggedTimeAdjustmentLimitOldValuePosition = 0x8000;
    private final static int displayedFormatsOldValueFormatsPosition = 0x10000;

    String TAG = "HBTimeChangeLogData";

    public HBTimeChangeLogData(int sequenceNumber, int e2eCrc) {
        this.sequenceNumber = sequenceNumber;
        this.e2eCrc = e2eCrc;
        this.field = new byte[0];
    }

    public HBTimeChangeLogData(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
        this.field = new byte[0];
    }

    private void parseField() {
        HBLogger.v(TAG, "parseField started! ");
        // sequence number is removed from the fields, so take that into account
        int offset = 0;
        ByteBuffer buffer;

        eventLogType = HBEventLogType.fromValue((int) field[offset]);
        offset++;

        for(int i = 0; i < 3; i++) {
            eventLogFlagsField[i] = field[offset];
            offset++;
        }

        parseEventLogFlagsField();

        byte[] dataForLong = { 0, 0, 0, 0, 0, 0, 0, 0};
        System.arraycopy(field, offset, dataForLong, 0, 4);

        buffer = ByteBuffer.wrap(dataForLong, 0, 8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        baseTime = buffer.getLong();
        offset += 4;

        dtStatus = field[offset];
        offset++;

        if(accumulatedRTCDriftIncluded) {
            accumulatedRtcDrift = getSfloat(field, offset);
            offset += 2;
        }
        if(daysUntilSyncLossIncluded) {
            daysRemainingForSyncLoss = getUint16(field, offset);
            offset += 2;
        }
        if(rTCTimeFaultCounterIncluded) {
            rtcTimeFaultCounter = getUint8(field, offset);
            offset += 1;
        }
        if(timeZoneIncluded) {
            timeZone = getUint8(field, offset);
            offset += 1;
        }
        if(dSTOffsetIncluded) {
            dstOffset = getUint8(field, offset);
            offset += 1;
        }
        if(timeSourceIncluded) {
            timeSource = HBTimeSource.fromValue((int) field[offset]);
            offset += 1;
        }
        if(timeAccuracyIncluded) {
            timeAccuracy = HBTimeAccuracy.fromValue((int) field[offset]);
            offset += 1;
        }
        if(baseTimeOldValueIncluded) {
            baseTimeOldValue = getUint32(field, offset);
            offset += 4;
        }
        if(baseTimeSecondFractionsIncluded) {
            baseTimeSecondFractions = getUint16(field, offset);
            offset += 2;
        }
        if(secondFractionsOldValueIncluded) {
            secondFractionsOldValue = getUint16(field, offset);
            offset += 2;
        }
        if(userTimeIncluded) {
            userTime = getUint32(field, offset);
            offset += 4;
        }
        if(userTimeOldValueIncluded) {
            userTimeOldValue = getUint32(field, offset);
            offset += 4;
        }
        if(maximumDriftLimitOldValueIncluded) {
            maximumDriftLimitOldValue = getSfloat(field, offset);
            offset += 2;
        }
        if(maxDaysUntilSyncLossOldValueIncluded) {
            maxDaysUntilSyncLossOldValue = getUint16(field, offset);
            offset += 2;
        }
        if(rTCResolutionOldValueIncluded) {
            rtcResolutionOldValue = getSfloat(field, offset);
            offset += 2;
        }
        if(nonLoggedTimeAdjustmentLimitOldValueIncluded) {
            nonLoggedTimeAdjustmentLimitOldValue = getSfloat(field, offset);
            offset += 2;
        }
        if(displayedFormatsOldValueFormatsIncluded) {
            displayedValuesOldValue = getUint16(field, offset);
            offset += 2;
        }
    }

    private void parseEventLogFlagsField() {
        accumulatedRTCDriftIncluded = (eventLogFlagsField[0] & accumulatedRTCDriftPosition) == accumulatedRTCDriftPosition;
        daysUntilSyncLossIncluded = (eventLogFlagsField[0] & daysUntilSyncLossPosition) == daysUntilSyncLossPosition;
        rTCTimeFaultCounterIncluded = (eventLogFlagsField[0] & rTCTimeFaultCounterPosition) == rTCTimeFaultCounterPosition;
        timeZoneIncluded = (eventLogFlagsField[0] & timeZonePosition) == timeZonePosition;
        dSTOffsetIncluded = (eventLogFlagsField[0] & dSTOffsetPosition) == dSTOffsetPosition;
        timeSourceIncluded = (eventLogFlagsField[0] & timeSourcePosition)         == timeSourcePosition;
        timeAccuracyIncluded = (eventLogFlagsField[0] & timeAccuracyPosition)       == timeAccuracyPosition;
        baseTimeOldValueIncluded = (eventLogFlagsField[0] & baseTimeOldValuePosition)   == baseTimeOldValuePosition;
        baseTimeSecondFractionsIncluded = (eventLogFlagsField[1] & (baseTimeSecondFractionsPosition >> 8)) == baseTimeSecondFractionsPosition >> 8;
        secondFractionsOldValueIncluded = (eventLogFlagsField[1] & (secondFractionsOldValuePosition >> 8)) == secondFractionsOldValuePosition >> 8;
        userTimeIncluded = (eventLogFlagsField[1] & (userTimePosition >> 8)) == userTimePosition >> 8;
        userTimeOldValueIncluded = (eventLogFlagsField[1] & (userTimeOldValuePosition >> 8)) == userTimeOldValuePosition >> 8;
        maximumDriftLimitOldValueIncluded = (eventLogFlagsField[1] & (maximumDriftLimitOldValuePosition >> 8)) == maximumDriftLimitOldValuePosition >> 8;
        maxDaysUntilSyncLossOldValueIncluded = (eventLogFlagsField[1] & (maxDaysUntilSyncLossOldValuePosition >> 8)) == maxDaysUntilSyncLossOldValuePosition >> 8;
        rTCResolutionOldValueIncluded = (eventLogFlagsField[1] & (rTCResolutionOldValuePosition >> 8)) == rTCResolutionOldValuePosition >> 8;
        nonLoggedTimeAdjustmentLimitOldValueIncluded = (eventLogFlagsField[1] & (nonLoggedTimeAdjustmentLimitOldValuePosition >> 8)) == nonLoggedTimeAdjustmentLimitOldValuePosition >> 8;
        displayedFormatsOldValueFormatsIncluded = (eventLogFlagsField[2] & (displayedFormatsOldValueFormatsPosition >> 16)) == displayedFormatsOldValueFormatsPosition >> 16;
    }


    public void addFieldPart(boolean finalPart, byte[] fieldPart) {
        byte[] newField = new byte[this.field.length + fieldPart.length];
        System.arraycopy(this.field,0, newField, 0,this.field.length);
        System.arraycopy(fieldPart, 0, newField, this.field.length, fieldPart.length);
        this.field = newField;

        if(finalPart) {
            parseField();
        }
    }

    private short getUint8(byte[] data, int offset) {
        return data[offset];
    }

    private int getUint16(byte[] data, int offset) {
        ByteBuffer buffer = ByteBuffer.wrap(data, offset, 2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getShort();
    }

    private long getUint32(byte[] data, int offset) {
        byte[] dataForLong = { 0, 0, 0, 0, 0, 0, 0, 0};
        System.arraycopy(data, offset, dataForLong, 0, 4);

        ByteBuffer buffer = ByteBuffer.wrap(dataForLong, 0, 8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getLong();
    }

    private Sfloat getSfloat(byte[] data, int offset) {
        byte[] sfloatData = new byte[2];
        sfloatData[0] = data[offset];
        sfloatData[1] = data[offset + 1];
        return new Sfloat(sfloatData);
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public HBEventLogType getEventLogType() {
        return eventLogType;
    }

    public long getBaseTime() {
        return baseTime;
    }

    public int getE2eCrc() {
        return e2eCrc;
    }

    public byte[] getField() {
        return field;
    }

    public byte getDtStatus() {
        return dtStatus;
    }

    public Sfloat getAccumulatedRtcDrift() {
        return accumulatedRtcDrift;
    }

    public int getDaysRemainingForSyncLoss() {
        return daysRemainingForSyncLoss;
    }

    public short getRtcTimeFaultCounter() {
        return rtcTimeFaultCounter;
    }

    public short getTimeZone() {
        return timeZone;
    }

    public short getDstOffset() {
        return dstOffset;
    }

    public HBTimeSource getTimeSource() {
        return timeSource;
    }

    public HBTimeAccuracy getTimeAccuracy() {
        return timeAccuracy;
    }

    public long getBaseTimeOldValue() {
        return baseTimeOldValue;
    }

    public int getBaseTimeSecondFractions() {
        return baseTimeSecondFractions;
    }

    public int getSecondFractionsOldValue() {
        return secondFractionsOldValue;
    }

    public long getUserTime() {
        return userTime;
    }

    public long getUserTimeOldValue() {
        return userTimeOldValue;
    }

    public Sfloat getMaximumDriftLimitOldValue() {
        return maximumDriftLimitOldValue;
    }

    public int getMaxDaysUntilSyncLossOldValue() {
        return maxDaysUntilSyncLossOldValue;
    }

    public Sfloat getRtcResolutionOldValue() {
        return rtcResolutionOldValue;
    }

    public Sfloat getNonLoggedTimeAdjustmentLimitOldValue() {
        return nonLoggedTimeAdjustmentLimitOldValue;
    }

    public int getDisplayedValuesOldValue() {
        return displayedValuesOldValue;
    }
}
