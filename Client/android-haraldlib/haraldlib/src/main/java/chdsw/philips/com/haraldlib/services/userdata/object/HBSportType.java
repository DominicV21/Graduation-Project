/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.userdata.object;

/**
 * Enum that contains all sport types as specified here:
 * https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.sport_type_for_aerobic_and_anaerobic_thresholds.xml
 */
public enum HBSportType {
    Unspecified(0),
    Running(1),
    Cycling(2),
    Rowing(3),
    CrossTraining(4),
    Climbing(5),
    Skiing(6),
    Skating(7),
    ArmExercising(8),
    LowerBodyExercising(9),
    UpperBodyExercising(10),
    WholeBodyExercising(11);

    HBSportType(int value)
    {
        this.value = value;
    }

    private int value;

    public int getValue()
    {
        return value;
    }

    public static HBSportType fromValue(int value) {
        for(HBSportType type : values()) {
            if(type.getValue() == value)
                return type;
        }
        return null;
    }
}
