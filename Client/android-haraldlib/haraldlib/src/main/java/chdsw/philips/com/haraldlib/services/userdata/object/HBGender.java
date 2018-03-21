/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.userdata.object;

/**
 * Enum that contains all genders as specified here:
 * https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.gender.xml
 */
public enum HBGender {
    Male(1),
    Female(2),
    Unspecified(3);

    HBGender(int value)
    {
        this.value = value;
    }

    private int value;

    public int getValue()
    {
        return value;
    }

    public static HBGender fromValue(int value) {
        for(HBGender type : values()) {
            if(type.getValue() == value)
                return type;
        }
        return null;
    }
}
