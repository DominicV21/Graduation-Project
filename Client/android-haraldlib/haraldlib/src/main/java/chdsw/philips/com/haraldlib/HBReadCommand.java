/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib;

import java.util.UUID;

/**
 * Contains read command information to execute on the bluetooth device
 */
public class HBReadCommand extends HBCommand {

    /**
     * Constructs a new read command for a specific service and characteristic.
     *
     * @param serviceUUID Service UUID.
     * @param characteristicUUID Characteristic UUID.
     */
    public HBReadCommand(UUID serviceUUID, UUID characteristicUUID) {
        super(serviceUUID, characteristicUUID);
    }

}
