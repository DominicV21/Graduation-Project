/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib;

import java.util.UUID;

/**
 * Contains write command information to execute on the bluetooth device
 */
public class HBWriteCommand extends HBCommand {
    private byte[] value;

    /**
     * Constructs a new write command for a specific service and characteristic.
     *
     * @param serviceUUID Service UUID.
     * @param characteristicUUID Characteristic UUID.
     * @param value Value to write to characteristic
     */
    public HBWriteCommand(UUID serviceUUID, UUID characteristicUUID, byte[] value) {
        super(serviceUUID, characteristicUUID);
        this.value = value;
    }

    /**
     * Get the value that has to be written to characteristic
     *
     * @return Value for characteristic
     */
    public byte[] getValue() {
        return value;
    }
}
