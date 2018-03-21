/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.currenttime.object;

/**
 * Reference time information object that contains reference time information as described in
 * https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.reference_time_information.xml
 */
public class HBReferenceTimeInformation {
    private HBTimeSource timeSource;
    private int accuracy;
    private int daysSinceUpdate;
    private int hoursSinceUpdate;

    /**
     * Get the {@link HBTimeSource} of the reference time information
     * @return The time source
     */
    public HBTimeSource getTimeSource() {
        return this.timeSource;
    }

    /**
     * Set the {@link HBTimeSource} of the reference time information
     * @param timeSource Value of the time source
     */
    public void setTimeSource(HBTimeSource timeSource) {
        this.timeSource = timeSource;
    }

    /**
     * Get the accuracy of the reference time information
     * @return The accuracy
     */
    public int getAccuracy() {
        return this.accuracy;
    }

    /**
     * Set the accuracy of the reference time information
     * @param accuracy Value of the accuracy
     */
    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    /**
     * Get the days since update of the reference time information
     * @return The days since update
     */
    public int getDaysSinceUpdate() {
        return this.daysSinceUpdate;
    }

    /**
     * Set the days since update of the reference time information
     * @param daysSinceUpdate Value of the days since update
     */
    public void setDaysSinceUpdate(int daysSinceUpdate) {
        this.daysSinceUpdate = daysSinceUpdate;
    }

    /**
     * Get the hours since update of the reference time information
     * @return The hours since update
     */
    public int getHoursSinceUpdate() {
        return this.hoursSinceUpdate;
    }

    /**
     * Set the hours since update of the reference time information
     * @param hoursSinceUpdate Value of the hours since update
     */
    public void setHoursSinceUpdate(int hoursSinceUpdate) {
        this.hoursSinceUpdate = hoursSinceUpdate;
    }
}
