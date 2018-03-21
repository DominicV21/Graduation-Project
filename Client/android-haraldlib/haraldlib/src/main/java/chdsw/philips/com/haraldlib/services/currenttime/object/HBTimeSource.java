/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.currenttime.object;

/**
 * Enum that contains all Time sources as specified here:
 * https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.time_source.xml
 */
public enum HBTimeSource {
    Unknown(0),
    NetworkTimeProtocol(1),
    GPS(2),
    RadioTimeSignal(3),
    Manual(4),
    AtomicClock(5),
    CellularNetwork(6);

    /**
     * Construct new enum with value.
     *
     * @param value Value of the enum
     */
    HBTimeSource(int value)
    {
        this.value = value;
    }

    private int value;

    /**
     * Get the value of the HBTimeSource.
     *
     * @return Value of HBTimeSource.
     */
    public int getValue()
    {
        return value;
    }

    /**
     * Get the enum based on the value.
     *
     * @param value Value to find HBTimeSource for.
     * @return Found HBTimeSource.
     */
    public static HBTimeSource fromValue(int value) {
        for(HBTimeSource type : values()) {
            if(type.getValue() == value)
                return type;
        }
        return null;
    }
}
