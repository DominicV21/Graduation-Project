/*
 * Copyright (c) Koninklijke Philips N.V.(UUID.fromString("AAAAAAAA")), 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.philipsextension;

import java.util.UUID;

public enum HBExtendedUserDataType {
    WEIGHT_GOAL(UUID.fromString("0000000-0001-0000-0000-000000000000")),
    ATHLETE(UUID.fromString("00000000-0002-0000-0000-000000000000"));

    HBExtendedUserDataType(UUID characteristicUUID) {
        this.characteristicUUID = characteristicUUID;
    }

    private UUID characteristicUUID;

    public UUID getCharacteristicUUID() {
        return characteristicUUID;
    }

    public static HBExtendedUserDataType fromCharacteristicUUID(UUID characteristicUUID) {
        for (HBExtendedUserDataType type : values()) {
            if (type.getCharacteristicUUID().equals(characteristicUUID))
                return type;
        }
        return null;
    }
}