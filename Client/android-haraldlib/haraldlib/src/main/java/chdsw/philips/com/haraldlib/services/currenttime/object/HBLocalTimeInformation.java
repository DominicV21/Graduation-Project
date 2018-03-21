/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.currenttime.object;

/**
 * Reference time information object that contains local time information as described in
 * https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.local_time_information.xml
 */
public class HBLocalTimeInformation {
    private int timeZone;
    private HBSTDOffset STDOffset;

    /**
     * Get the timeZone of the local time information
     * @return The timeZone
     */
    public int getTimeZone() {
        return this.timeZone;
    }

    /**
     * Set the timeZone of the local time information
     * @param timeZone Value of the timeZone
     */
    public void setTimeZone(int timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * Get the {@link HBSTDOffset} of the local time information
     * @return The STD offset
     */
    public HBSTDOffset getSTDOffset() {
        return this.STDOffset;
    }

    /**
     * Set the {@link HBSTDOffset} of the local time information
     * @param STDOffset Value of the STD offset
     */
    public void setSTDOffset(HBSTDOffset STDOffset) {
        this.STDOffset = STDOffset;
    }
}
