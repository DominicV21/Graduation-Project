package chdsw.philips.com.haraldlib.services.bloodpressure.object;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import chdsw.philips.com.haraldlib.HBLogger;

public class HBEnhancedBloodPressureMeasurement {
    private Byte flags = 0;
    private Sfloat systolic;        /* unit depends on flag */
    private Sfloat diastolic;       /* unit depends on flag */
    private Sfloat map;             /* unit depends on flag */
    private long baseTime;
    private Sfloat pulseRate;
    private int userID = 0;
    private int measurementStatus = 0;
    private long userFacingTime;

    private HBBloodPressureUnit unit;

    private boolean timeStampFlag;
    private boolean pulseRateFlag;
    private boolean userIDFlag;
    private boolean measurementStatusFlag;
    private boolean userFacingTimeFlag;


    private static final int BIT1 = 0x01;
    private static final int BIT2 = 0x02;
    private static final int BIT3 = 0x04;
    private static final int BIT4 = 0x08;
    private static final int BIT5 = 0x10;
    private static final int BIT6 = 0x20;

    public HBEnhancedBloodPressureMeasurement(byte[] data){
        int offset = 0;
        byte[] sfloatData = new byte[2];
        ByteBuffer buffer;
        byte flags = data[offset];
        offset++;

        unit = HBBloodPressureUnit.fromValue(flags & BIT1);
        timeStampFlag = ((flags & BIT2) == BIT2);
        pulseRateFlag = ((flags & BIT3) == BIT3);
        userIDFlag = ((flags & BIT4) == BIT4);
        measurementStatusFlag = ((flags & BIT5) == BIT5);
        userFacingTimeFlag = ((flags & BIT6) == BIT6);


        sfloatData[0] = data[offset];
        sfloatData[1] = data[offset + 1];
        systolic = new Sfloat(sfloatData);
        offset += 2;

        sfloatData[0] = data[offset];
        sfloatData[1] = data[offset + 1];
        this.diastolic = new Sfloat(sfloatData);
        offset += 2;

        sfloatData[0] = data[offset];
        sfloatData[1] = data[offset + 1];
        this.map = new Sfloat(sfloatData);
        offset += 2;

        if(timeStampFlag) {
            byte[] dataForLong = { 0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong, 0, 4);

            buffer = ByteBuffer.wrap(dataForLong, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            baseTime = buffer.getLong();
            offset += 4;
        }

        if(pulseRateFlag) {
            sfloatData[0] = data[offset];
            sfloatData[1] = data[offset + 1];
            pulseRate = new Sfloat(sfloatData);
            offset += 2;
        }

        if(userIDFlag) {
            userID = data[offset];
            offset++;
        }

        if(measurementStatusFlag) {
            HBLogger.v("parser", "MeasurementStatusFlag available and measurement status is parsed");
            HBLogger.v("parser", "Offset is " + offset + " and the length of the data is " + data.length );
            buffer = ByteBuffer.wrap(data, offset, 2);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            measurementStatus = buffer.getShort();
            HBLogger.v("parser", "measurementStatus is " + measurementStatus);

            offset += 2;
        }

        if(userFacingTimeFlag) {
            byte[] dataForLong = {0, 0, 0, 0, 0, 0, 0, 0};
            System.arraycopy(data, offset, dataForLong, 0, 4);

            buffer = ByteBuffer.wrap(dataForLong, 0, 8);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            userFacingTime = buffer.getLong();
            offset += 4;
        }
    }

    public Sfloat getSystolic() {
        return systolic;
    }

    public Sfloat getDiastolic() {
        return diastolic;
    }

    public Sfloat getMap() {
        return map;
    }

    public long getBaseTime() {
        return baseTime;
    }

    public Sfloat getPulseRate() {
        return pulseRate;
    }

    public int getUserID() {
        return userID;
    }

    public int getMeasurementStatus() {
        return measurementStatus;
    }

    public long getUserFacingTime() {
        return userFacingTime;
    }

    public HBBloodPressureUnit getUnit() {
        return unit;
    }

    public boolean isTimeStampFlag() {
        return timeStampFlag;
    }

    public boolean isPulseRateFlag() {
        return pulseRateFlag;
    }

    public boolean isUserIDFlag() {
        return userIDFlag;
    }

    public boolean isMeasurementStatusFlag() {
        return measurementStatusFlag;
    }

    public boolean isUserFacingTimeFlag() {
        return userFacingTimeFlag;
    }
}
