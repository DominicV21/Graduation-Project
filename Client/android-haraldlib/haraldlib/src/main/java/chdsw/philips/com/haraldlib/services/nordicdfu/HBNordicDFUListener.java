/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.nordicdfu;

import chdsw.philips.com.haraldlib.services.HBServiceListener;

/**
 * A callback to receive information from the nordic DFU service
 */
public interface HBNordicDFUListener extends HBServiceListener {
    /**
     * OTA progress changed on device
     *
     * @param deviceAddress Address of the device.
     * @param sendFileSize Send size of the file
     * @param totalFileSize Total size of the file
     */
    void onOTAProgressChanged(final String deviceAddress, final float sendFileSize, final float totalFileSize);

    /**
     * OTA completed on device
     *
     * @param deviceAddress Address of the device.
     */
    void onOTACompleted(final String deviceAddress);

    /**
     * OTA aborted on device
     *
     * @param deviceAddress Address of the device.
     */
    void onOTAAborted(final String deviceAddress);

    /**
     * OTA error on device
     *
     * @param deviceAddress Address of the device.
     * @param errorCode Code of the error
     * @param message Message of the error
     */
    void onOTAError(final String deviceAddress, final int errorCode, final String message);
}
