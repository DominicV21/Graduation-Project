/*
 * Copyright (c) Koninklijke Philips N.V.(UUID.fromString("AAAAAAAA")), 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.userdata;

import java.util.UUID;

/**
 * Enum that contains all user data characteristics as specified here:
 * https://www.bluetooth.com/specifications/assigned-numbers/user-data-service-characteristics
 */
public enum HBUserDataType {
    FIRST_NAME(UUID.fromString("00002A8A-0000-1000-8000-00805f9b34fb")),
    LAST_NAME(UUID.fromString("00002A90-0000-1000-8000-00805f9b34fb")),
    EMAIL_ADDRESS(UUID.fromString("00002A87-0000-1000-8000-00805f9b34fb")),
    AGE(UUID.fromString("00002A80-0000-1000-8000-00805f9b34fb")),
    DATE_OF_BIRTH(UUID.fromString("00002A85-0000-1000-8000-00805f9b34fb")),
    GENDER(UUID.fromString("00002A8C-0000-1000-8000-00805f9b34fb")),
    WEIGHT(UUID.fromString("00002A98-0000-1000-8000-00805f9b34fb")),
    HEIGHT(UUID.fromString("00002A8E-0000-1000-8000-00805f9b34fb")),
    VO2_MAX(UUID.fromString("00002A96-0000-1000-8000-00805f9b34fb")),
    HEARTH_RATE_MAX(UUID.fromString("00002A8D-0000-1000-8000-00805f9b34fb")),
    RESTING_HEARTH_RATE(UUID.fromString("00002A92-0000-1000-8000-00805f9b34fb")),
    MAX_RECOMMENDED_HEARTH_RATE(UUID.fromString("00002A91-0000-1000-8000-00805f9b34fb")),
    AEROBIC_THRESHOLD(UUID.fromString("00002A7F-0000-1000-8000-00805f9b34fb")),
    ANAEROBIC_THRESHOLD(UUID.fromString("00002A83-0000-1000-8000-00805f9b34fb")),
    SPORT_TYPE(UUID.fromString("00002A93-0000-1000-8000-00805f9b34fb")),
    DATE_OF_THRESHOLD(UUID.fromString("00002A86-0000-1000-8000-00805f9b34fb")),
    WAIST_CIRCUMFERENCE(UUID.fromString("00002A97-0000-1000-8000-00805f9b34fb")),
    HIP_CIRCUMFERENCE(UUID.fromString("00002A8F-0000-1000-8000-00805f9b34fb")),
    FAT_BURN_LOWER_LIMIT(UUID.fromString("00002A88-0000-1000-8000-00805f9b34fb")),
    FAT_BURN_UPPER_LIMIT(UUID.fromString("00002A89-0000-1000-8000-00805f9b34fb")),
    AEROBIC_HEARTH_RATE_LOWER_LIMIT(UUID.fromString("00002A7E-0000-1000-8000-00805f9b34fb")),
    AEROBIC_HEARTH_RATE_UPPER_LIMIT(UUID.fromString("00002A84-0000-1000-8000-00805f9b34fb")),
    ANAEROBIC_HEARTH_RATE_LOWER_LIMIT(UUID.fromString("00002A81-0000-1000-8000-00805f9b34fb")),
    ANAEROBIC_HEARTH_RATE_UPPER_LIMIT(UUID.fromString("00002A82-0000-1000-8000-00805f9b34fb")),
    FIVE_ZONE_HEARTH_RATE_LIMITS(UUID.fromString("00002A8B-0000-1000-8000-00805f9b34fb")),
    THREE_ZONE_HEARTH_RATE_LIMITS(UUID.fromString("00002A94-0000-1000-8000-00805f9b34fb")),
    TWO_ZONE_HEARTH_RATE_LIMITS(UUID.fromString("00002A95-0000-1000-8000-00805f9b34fb")),
    DATABASE_CHANGE_INCREMENT(UUID.fromString("00002A99-0000-1000-8000-00805f9b34fb")),
    USER_INDEX(UUID.fromString("00002A9A-0000-1000-8000-00805f9b34fb")),
    LANGUAGE(UUID.fromString("00002AA2-0000-1000-8000-00805f9b34fb"));

    HBUserDataType(UUID characteristicUUID) {
        this.characteristicUUID = characteristicUUID;
    }

    private UUID characteristicUUID;

    public UUID getCharacteristicUUID() {
        return characteristicUUID;
    }

    public static HBUserDataType fromCharacteristicUUID(UUID characteristicUUID) {
        for (HBUserDataType type : values()) {
            if (type.getCharacteristicUUID().equals(characteristicUUID))
                return type;
        }
        return null;
    }
}