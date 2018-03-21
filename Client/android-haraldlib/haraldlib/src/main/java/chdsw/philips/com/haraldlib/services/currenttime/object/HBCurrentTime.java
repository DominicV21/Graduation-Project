/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.currenttime.object;

import java.util.Date;

/**
 * Reference time information object that contains local time information as described in
 * https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.current_time.xml
 */
public class HBCurrentTime {
    private Date dateTime;
    private int dayOfWeek;
    private int fractions256;
    private int adjustReason;

    /**
     * Get the date time of the current time
     * @return The date time
     */
    public Date getDateTime() {
        return this.dateTime;
    }

    /**
     * Set the date time of the current time
     * @param dateTime Value of the date time
     */
    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Get the day of week of the current time
     * @return The day of week
     */
    public int getDayOfWeek() {
        return this.dayOfWeek;
    }

    /**
     * Set the day of week of the current time
     * @param dayOfWeek Value of the day of week
     */
    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * Get the fractions 256 of the current time
     * @return The fractions 256
     */
    public int getFractions256() {
        return this.fractions256;
    }

    /**
     * Set the fractions 256 of the current time
     * @param fractions256 Value of the fractions 256
     */
    public void setFractions256(int fractions256) {
        this.fractions256 = fractions256;
    }

    /**
     * Get the adjust reason of the current time
     * @return The adjust reason
     */
    public int getAdjustReason() {
        return this.adjustReason;
    }

    /**
     * Set the adjust reason of the current time
     * @param adjustReason Value of the adjust reason
     */
    public void setAdjustReason(int adjustReason) {
        this.adjustReason = adjustReason;
    }

}
