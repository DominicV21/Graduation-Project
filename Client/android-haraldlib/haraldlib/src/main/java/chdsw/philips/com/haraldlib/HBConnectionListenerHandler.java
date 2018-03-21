/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib;

import android.bluetooth.le.ScanRecord;
import android.os.Handler;

import java.util.UUID;

/**
 * Callback handler that passed each callback call onto the right handler.
 */
public class HBConnectionListenerHandler implements HBConnectionListener {
    private Handler handler;
    private HBConnectionListener listener;

    /**
     * Constructs the callback handler with a handler and the callback interface
     *
     * @param handler Handler to use on the callbacks
     */
    public HBConnectionListenerHandler(Handler handler, HBConnectionListener listener) {
        this.handler = handler;
        this.listener = listener;
    }

    /**
     * Pass the connect call onto the handler
     *
     * @param deviceAddress Address of the device.
     * @param primaryService Primary service to connect with.
     */
    @Override
    public void connected(final String deviceAddress, final UUID primaryService) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.connected(deviceAddress, primaryService);
            }
        });
    }

    /**
     * Pass the connectFailed call onto the handler
     *
     * @param deviceAddress Address of the device.
     * @param primaryService Primary service to connect with.
     */
    @Override
    public void connectFailed(final String deviceAddress, final UUID primaryService) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.connectFailed(deviceAddress, primaryService);
            }
        });
    }

    /**
     * Pass the discoveredDeviceIsCorrect call onto the handler
     *
     * @param deviceAddress Address of the device.
     * @param primaryService Primary service to connect with.
     * @param scanRecord Advertisement data of the device
     *
     * @return Is device to connect with
     */
    @Override
    public boolean discoveredDeviceIsCorrect(final String deviceAddress, final UUID primaryService, final HBScanRecord scanRecord) {
        return listener.discoveredDeviceIsCorrect(deviceAddress, primaryService, scanRecord);
    }

    /**
     * Check if the listener is the same as the handlers listener
     *
     * @param listener Listener to compare
     * @return Lister equality
     */
    public boolean equalsListener(HBConnectionListener listener) {
        return this.listener.equals(listener);
    }
}
