/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib;

import java.util.UUID;

/**
 * Contains command information to execute on the bluetooth device
 */
public abstract class HBCommand {
    private UUID serviceUUID;
    private UUID characteristicUUID;

    /**
     * Constructs a new command for a specific service and characteristic.
     *
     * @param serviceUUID Service UUID.
     * @param characteristicUUID Characteristic UUID.
     */
    public HBCommand(UUID serviceUUID, UUID characteristicUUID) {
        this.serviceUUID = serviceUUID;
        this.characteristicUUID = characteristicUUID;
    }

    /**
     * Get the command's service UUID.
     *
     * @return Service UUID.
     */
    public UUID getServiceUUID() {
        return serviceUUID;
    }

    /**
     * Get the command's characteristic UUID
     *
     * @return Characteristic UUID
     */
    public UUID getCharacteristicUUID() {
        return characteristicUUID;
    }
}
