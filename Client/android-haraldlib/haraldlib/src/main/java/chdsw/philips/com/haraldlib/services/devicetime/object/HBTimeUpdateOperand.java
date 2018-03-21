package chdsw.philips.com.haraldlib.services.devicetime.object;

public class HBTimeUpdateOperand {
    private int timeUpdateFlags;
    private long baseTime;
    private int baseTimeSecondFractions;
    private byte timeZone;
    private byte dstOffset;
    private byte timeSource;
    private byte timeAccuracy;

    private boolean secondFractionsIncluded;

    public HBTimeUpdateOperand(int timeUpdateFlags, long baseTime, byte timeZone, byte dstOffset, byte timeSource, byte timeAccuracy) {
        this.timeUpdateFlags = timeUpdateFlags;
        this.baseTime = baseTime;
        this.timeZone = timeZone;
        this.dstOffset = dstOffset;
        this.timeSource = timeSource;
        this.timeAccuracy = timeAccuracy;
        secondFractionsIncluded = false;
    }

    public HBTimeUpdateOperand(int timeUpdateFlags, long baseTime, byte timeZone, byte dstOffset, byte timeSource, byte timeAccuracy, int baseTimeSecondFractions) {
        this.timeUpdateFlags = timeUpdateFlags;
        this.baseTime = baseTime;
        this.timeZone = timeZone;
        this.dstOffset = dstOffset;
        this.timeSource = timeSource;
        this.timeAccuracy = timeAccuracy;
        secondFractionsIncluded = true;
        this.baseTimeSecondFractions =  baseTimeSecondFractions;
    }

    public byte[] getOperandData() {
        byte data[] = new byte[10];
        int offset = 0;

        if(secondFractionsIncluded) {
            data = new byte[12];
        }

        data[offset++] = (byte) (timeUpdateFlags & 0xFF);
        data[offset++] = (byte) ((timeUpdateFlags >> 8) & 0xFF);

        data[offset++] = (byte) (baseTime & 0xFF);
        data[offset++] = (byte) ((baseTime >> 8) & 0xFF);
        data[offset++] = (byte) ((baseTime >> 16) & 0xFF);
        data[offset++] = (byte) ((baseTime >> 24) & 0xFF);

        if(secondFractionsIncluded) {
            data[offset++] = (byte) (baseTimeSecondFractions & 0xFF);
            data[offset++] = (byte) ((baseTimeSecondFractions >> 8) & 0xFF);
        }

        data[offset++] = timeZone;
        data[offset++] = dstOffset;
        data[offset++] = timeSource;
        data[offset++] = timeAccuracy;

        return data;
    }
}
