/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.bloodpressure.object;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Calendar;

public class HBIntermediateCuffPressure {
    private Byte flags = 0;
    private Sfloat currentPressure;        /* unit depends on flag */
    private Calendar baseTime;
    private Sfloat pulseRate;
    private int userID = 0;
    private int measurementStatus = 0;
    private long userFacingTime;

    /* Flag values */
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

    public HBIntermediateCuffPressure(byte[] data) {
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
        currentPressure = new Sfloat(sfloatData);
        offset += 2;

        // Skip the 4 bytes where the NaN values are
        offset += 4;

        if(timeStampFlag) {
            int year, month, day, hour, minute, second;
            buffer = ByteBuffer.wrap(data, offset, 2);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            year = buffer.getShort();           // this.field[offset + 1] << 8 | this.field[offset];
            offset += 2;
            month = data[offset];
            offset++;
            day = data[offset];
            offset++;
            hour = data[offset];
            offset++;
            minute = data[offset];
            offset++;
            second = data[offset];
            offset++;
            baseTime.set(year, month, day, hour, minute, second);
        }

        if(pulseRateFlag) {
            //buffer = ByteBuffer.wrap(data, offset, 2);
            //buffer.order(ByteOrder.LITTLE_ENDIAN);
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
            buffer = ByteBuffer.wrap(data, offset, 2);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            measurementStatus = buffer.getShort();
            offset+=2;
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

    public Sfloat getCurrentPressure() {
        return currentPressure;
    }

    public Calendar getBaseTime() {
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

    public long getUserFacingTime() {
        return userFacingTime;
    }
}
