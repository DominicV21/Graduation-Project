/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib;

/**
 * Interface between {@link HBCentral} and a {@link HBDevice}.
 * <p/>
 * The {@link HBDevice} sends status updates to {@link HBCentral}.
 */
public interface HBDeviceListener {

    /**
     * {@link HBDevice} has successfully connected.
     *
     * @param hbDevice {@link HBDevice} that connected.
     */
    void connected(HBDevice hbDevice);

    /**
     * Connecting with {@link HBDevice} has failed.
     *
     * @param hbDevice {@link HBDevice} of which connect failed.
     */
    void connectFailed(HBDevice hbDevice);

    /**
     * {@link HBDevice} has disconnected.
     *
     * @param hbDevice {@link HBDevice} that disconnected.
     */
    void disconnected(HBDevice hbDevice);

    /**
     * Read/write request failed on {@link HBDevice}.
     *
     * @param hbDevice {@link HBDevice} of which read/write failed.
     */
    void onReadWriteError(HBDevice hbDevice);
}
