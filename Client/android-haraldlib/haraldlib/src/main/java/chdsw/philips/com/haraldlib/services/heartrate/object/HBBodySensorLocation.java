/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.heartrate.object;

/**
 * Enum that contains all body sensor locations and their corresponding values as specified here:
 * https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.body_sensor_location.xml
 */
public enum HBBodySensorLocation {
    Other(0),
    Chest(1),
    Wrist(2),
    Finger(3),
    Hand(4),
    EarLobe(5),
    Foot(6);

    /**
     * Construct new enum with value.
     *
     * @param value Value of the enum
     * @return Value of HBBodySensorLocation.
     */
    HBBodySensorLocation(int value)
    {
        this.value = value;
    }

    private int value;

    /**
     * Get the value of the HBBodySensorLocation.
     *
     * @return Value of HBBodySensorLocation.
     */
    public int getValue()
    {
        return value;
    }

    /**
     * Get the enum based on the value.
     *
     * @param value Value to find HBBodySensorLocation for.
     * @return Found HBBodySensorLocation.
     */
    public static HBBodySensorLocation fromValue(int value) {
        for(HBBodySensorLocation type : values()) {
            if(type.getValue() == value)
                return type;
        }
        return null;
    }
}
