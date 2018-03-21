/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.currenttime;

import android.os.Handler;

import chdsw.philips.com.haraldlib.services.HBServiceListener;
import chdsw.philips.com.haraldlib.services.HBServiceListenerHandler;
import chdsw.philips.com.haraldlib.services.currenttime.object.HBCurrentTime;
import chdsw.philips.com.haraldlib.services.currenttime.object.HBLocalTimeInformation;
import chdsw.philips.com.haraldlib.services.currenttime.object.HBReferenceTimeInformation;

/**
 * Callback handler that passed each callback call onto the right handler.
 */
public class HBCurrentTimeListenerHandler implements HBCurrentTimeListener, HBServiceListenerHandler {
    private Handler handler;
    private HBCurrentTimeListener listener;

    /**
     * Constructs the callback handler with a handler and the callback interface
     *
     * @param handler Handler to use on the callbacks
     */
    public HBCurrentTimeListenerHandler(Handler handler, HBCurrentTimeListener listener) {
        this.handler = handler;
        this.listener = listener;
    }

    /**
     * Pass the onCurrentTime call onto the handler
     *
     * @param deviceAddress Address of the device.
     * @param currentTime   {@link HBCurrentTime} of the device.
     */
    @Override
    public void onCurrentTime(final String deviceAddress, final HBCurrentTime currentTime) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onCurrentTime(deviceAddress, currentTime);
            }
        });
    }

    /**
     * Pass the onLocalTimeInformation call onto the handler
     *
     * @param deviceAddress        Address of the device.
     * @param localTimeInformation {@link HBLocalTimeInformation} of the device.
     */
    @Override
    public void onLocalTimeInformation(final String deviceAddress, final HBLocalTimeInformation localTimeInformation) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onLocalTimeInformation(deviceAddress, localTimeInformation);
            }
        });
    }

    /**
     * Pass the onReferenceTimeInformation call onto the handler
     *
     * @param deviceAddress            Address of the device.
     * @param referenceTimeInformation {@link HBReferenceTimeInformation} of the device.
     */
    @Override
    public void onReferenceTimeInformation(final String deviceAddress, final HBReferenceTimeInformation referenceTimeInformation) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onReferenceTimeInformation(deviceAddress, referenceTimeInformation);
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
