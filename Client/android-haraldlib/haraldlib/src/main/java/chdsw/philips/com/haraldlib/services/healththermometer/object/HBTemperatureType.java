/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.healththermometer.object;

/**
 * Enum that contains all temperature types and their corresponding values as specified here:
 * https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.temperature_type.xml
 */
public enum HBTemperatureType {
    Armpit(1),
    Body(2),
    Ear(3),
    Finger(4),
    GastroIntestinalTract(5),
    Mouth(6),
    Rectum(7),
    Toe(8),
    Tympanum(9);

    /**
     * Construct new enum with value.
     *
     * @param value Value of the enum
     * @return Value of HBTemperatureType.
     */
    HBTemperatureType(int value)
    {
        this.value = value;
    }

    private int value;

    /**
     * Get the value of the HBTemperatureType.
     *
     * @return Value of HBTemperatureType.
     */
    public int getValue()
    {
        return value;
    }

    /**
     * Get the enum based on the value.
     *
     * @param value Value to find HBTemperatureType for.
     * @return Found HBTemperatureType.
     */
    public static HBTemperatureType fromValue(int value) {
        for(HBTemperatureType type : values()) {
            if(type.getValue() == value)
                return type;
        }
        return null;
    }
}
