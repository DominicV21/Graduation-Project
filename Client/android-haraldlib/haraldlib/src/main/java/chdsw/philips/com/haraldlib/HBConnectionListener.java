/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib;

import android.bluetooth.le.ScanRecord;

import java.util.UUID;

/**
 * A callback to receive connection information from new connection request.
 */
public interface HBConnectionListener {

    /**
     * Successfully connected with the device.
     *
     * @param deviceAddress Address of the device.
     * @param primaryService Primary service to connect with.
     */
    void connected(final String deviceAddress, final UUID primaryService);

    /**
     * Connecting with the device has failed.
     *
     * @param deviceAddress Address of the device.
     * @param primaryService Primary service to connect with.
     */
    void connectFailed(final String deviceAddress, final UUID primaryService);

    /**
     * Verify if the found device if correct based on the advertisement data
     *
     * @param deviceAddress Address of the device.
     * @param primaryService Primary service to connect with.
     * @param scanRecord Advertisement data of the device
     *
     * @return Is device to connect with
     */
    boolean discoveredDeviceIsCorrect(final String deviceAddress, final UUID primaryService, final HBScanRecord scanRecord);
}
