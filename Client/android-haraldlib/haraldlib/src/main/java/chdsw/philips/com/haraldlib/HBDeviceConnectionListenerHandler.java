/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib;

import android.os.Handler;

import chdsw.philips.com.haraldlib.services.HBServiceListener;
import chdsw.philips.com.haraldlib.services.HBServiceListenerHandler;

/**
 * Callback handler that passed each callback call onto the right handler.
 */
public class HBDeviceConnectionListenerHandler implements HBDeviceConnectionListener, HBServiceListenerHandler {
    private Handler handler;
    private HBDeviceConnectionListener listener;

    /**
     * Constructs the callback handler with a handler and the callback interface
     *
     * @param handler Handler to use on the callbacks
     */
    public HBDeviceConnectionListenerHandler(Handler handler, HBDeviceConnectionListener listener) {
        this.handler = handler;
        this.listener = listener;
    }

    /**
     * Pass the disconnected call onto the handler
     *
     * @param deviceAddress Address of the device.
     */
    @Override
    public void disconnected(final String deviceAddress) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.disconnected(deviceAddress);
            }
        });
    }

    /**
     * Pass the onReadWriteError call onto the handler
     *
     * @param deviceAddress Address of the device.
     */
    @Override
    public void onReadWriteError(final String deviceAddress) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onReadWriteError(deviceAddress);
            }
        });
    }

    /**
     * Check if the listener is the same as the handlers listener
     *
     * @param listener Listener to compare
     * @return Lister equality
     */
    @Override
    public boolean equalsListener(HBServiceListener listener) {
        return this.listener.equals(listener);
    }
}
