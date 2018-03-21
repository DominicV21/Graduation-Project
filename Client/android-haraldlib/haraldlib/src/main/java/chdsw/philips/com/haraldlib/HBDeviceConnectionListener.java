/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib;

/**
 * A callback to receive information from connected devices.
 * Callbacks are initiated by calling the public methods of {@link HBCentral} for a specific device.
 * This interface contains the callbacks of all supported services.
 */
public interface HBDeviceConnectionListener {

    /**
     * The device disconnected.
     *
     * @param deviceAddress Address of the device.
     */
    void disconnected(final String deviceAddress);

    /**
     * Read/write request on the device failed.
     *
     * @param deviceAddress Address of the device.
     */
    void onReadWriteError(final String deviceAddress);
}
