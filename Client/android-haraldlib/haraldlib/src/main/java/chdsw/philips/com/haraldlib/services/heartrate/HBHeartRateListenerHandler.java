/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.heartrate;

import android.os.Handler;

import chdsw.philips.com.haraldlib.services.HBServiceListener;
import chdsw.philips.com.haraldlib.services.HBServiceListenerHandler;
import chdsw.philips.com.haraldlib.services.heartrate.object.HBBodySensorLocation;
import chdsw.philips.com.haraldlib.services.heartrate.object.HBHeartRateMeasurement;

/**
 * Callback handler that passed each callback call onto the right handler.
 */
public class HBHeartRateListenerHandler implements HBHeartRateListener, HBServiceListenerHandler {
    private Handler handler;
    private HBHeartRateListener listener;

    /**
     * Constructs the callback handler with a handler and the callback interface
     *
     * @param handler Handler to use on the callbacks
     */
    public HBHeartRateListenerHandler(Handler handler, HBHeartRateListener listener) {
        this.handler = handler;
        this.listener = listener;
    }

    @Override
    public void onHeartRateMeasurement(final String deviceAddress, final HBHeartRateMeasurement hbHeartRateMeasurement) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onHeartRateMeasurement(deviceAddress, hbHeartRateMeasurement);
            }
        });
    }

    @Override
    public void onBodySensorLocation(final String deviceAddress, final HBBodySensorLocation hbBodySensorLocation) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onBodySensorLocation(deviceAddress, hbBodySensorLocation);
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
