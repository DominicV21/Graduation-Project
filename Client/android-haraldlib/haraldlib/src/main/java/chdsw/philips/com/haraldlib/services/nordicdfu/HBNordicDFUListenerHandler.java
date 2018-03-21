/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.nordicdfu;

import android.os.Handler;

import chdsw.philips.com.haraldlib.HBConnectionListener;
import chdsw.philips.com.haraldlib.services.HBServiceListener;
import chdsw.philips.com.haraldlib.services.HBServiceListenerHandler;

/**
 * Callback handler that passed each callback call onto the right handler.
 */
public class HBNordicDFUListenerHandler implements HBNordicDFUListener, HBServiceListenerHandler {
    private Handler handler;
    private HBNordicDFUListener listener;

    /**
     * Constructs the callback handler with a handler and the callback interface
     *
     * @param handler Handler to use on the callbacks
     */
    public HBNordicDFUListenerHandler(Handler handler, HBNordicDFUListener listener) {
        this.handler = handler;
        this.listener = listener;
    }

    /**
     * Pass the onOtaProgressChanged call onto the handler
     *
     * @param deviceAddress Address of the device.
     * @param sendFileSize  Send size of the file
     * @param totalFileSize Total size of the file
     */
    @Override
    public void onOTAProgressChanged(final String deviceAddress, final float sendFileSize, final float totalFileSize) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onOTAProgressChanged(deviceAddress, sendFileSize, totalFileSize);
            }
        });
    }

    /**
     * Pass the onOtaCompleted call onto the handler
     *
     * @param deviceAddress Address of the device.
     */
    @Override
    public void onOTACompleted(final String deviceAddress) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onOTACompleted(deviceAddress);
            }
        });
    }

    /**
     * Pass the onOtaAborted call onto the handler
     *
     * @param deviceAddress Address of the device.
     */
    @Override
    public void onOTAAborted(final String deviceAddress) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onOTAAborted(deviceAddress);
            }
        });
    }

    /**
     * Pass the onOtaError call onto the handler
     *
     * @param deviceAddress Address of the device.
     * @param errorCode     Code of the error
     * @param message       Message of the error
     */
    @Override
    public void onOTAError(final String deviceAddress, final int errorCode, final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onOTAError(deviceAddress, errorCode, message);
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
