/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.currenttime.object;

/**
 * Enum that contains all DST offsets as specified here:
 * https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.dst_offset.xml
 */
public enum HBSTDOffset {
    StandardTime(0, 0.0),
    HalfAnHourDaylightTime(2, 0.5),
    DaylightTime(4, 1.0),
    DoubleDaylightTime(8, 2.0);

    /**
     * Construct new enum with value.
     *
     * @param value Value of the enum
     * @param offset DST Offset of the enum
     */
    HBSTDOffset(int value, double offset)
    {
        this.value = value;
        this.offset = offset;
    }

    private int value;
    private double offset;

    /**
     * Get the value of the HBSTDOffset.
     *
     * @return Value of HBSTDOffset.
     */
    public int getValue()
    {
        return value;
    }

    /**
     * Get the offset of the HBSTDOffset.
     *
     * @return Offset of HBSTDOffset.
     */
    public double getOffset()
    {
        return offset;
    }

    /**
     * Get the enum based on the value.
     *
     * @param value Value to find HBSTDOffset for.
     * @return Found HBSTDOffset.
     */
    public static HBSTDOffset fromValue(int value) {
        for(HBSTDOffset type : values()) {
            if(type.getValue() == value)
                return type;
        }
        return null;
    }
}
