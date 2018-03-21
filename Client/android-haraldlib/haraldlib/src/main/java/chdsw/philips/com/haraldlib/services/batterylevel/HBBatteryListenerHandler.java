/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.batterylevel;

import android.os.Handler;

import chdsw.philips.com.haraldlib.services.HBServiceListener;
import chdsw.philips.com.haraldlib.services.HBServiceListenerHandler;

/**
 * Callback handler that passed each callback call onto the right handler.
 */
public class HBBatteryListenerHandler implements HBBatteryListener, HBServiceListenerHandler {
    private Handler handler;
    private HBBatteryListener listener;

    /**
     * Constructs the callback handler with a handler and the callback interface
     *
     * @param handler Handler to use on the callbacks
     */
    public HBBatteryListenerHandler(Handler handler, HBBatteryListener listener) {
        this.handler = handler;
        this.listener = listener;
    }

    /**
     * Pass the onBatteryLevel call onto the handler
     *
     * @param deviceAddress Address of the device.
     * @param batteryLevel Battery level of the device.
     */
    @Override
    public void onBatteryLevel(final String deviceAddress, final int batteryLevel) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onBatteryLevel(deviceAddress, batteryLevel);
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
