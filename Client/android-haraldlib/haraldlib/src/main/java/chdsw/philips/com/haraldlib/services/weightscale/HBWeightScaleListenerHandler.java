/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.weightscale;

import android.os.Handler;

import chdsw.philips.com.haraldlib.services.HBServiceListener;
import chdsw.philips.com.haraldlib.services.HBServiceListenerHandler;
import chdsw.philips.com.haraldlib.services.weightscale.object.HBWeightMeasurement;
import chdsw.philips.com.haraldlib.services.weightscale.object.HBWeightScaleFeature;

/**
 * Callback handler that passed each callback call onto the right handler.
 */
public class HBWeightScaleListenerHandler implements HBWeightScaleListener, HBServiceListenerHandler {
    private Handler handler;
    private HBWeightScaleListener listener;

    /**
     * Constructs the callback handler with a handler and the callback interface
     *
     * @param handler Handler to use on the callbacks
     */
    public HBWeightScaleListenerHandler(Handler handler, HBWeightScaleListener listener) {
        this.handler = handler;
        this.listener = listener;
    }

    /**
     * Pass the onWeightScaleFeature call onto the handler
     *
     * @param deviceAddress Address of the device.
     * @param weightScaleFeature Weight scale feature of the device.
     */
    @Override
    public void onWeightScaleFeature(final String deviceAddress, final HBWeightScaleFeature weightScaleFeature) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onWeightScaleFeature(deviceAddress, weightScaleFeature);
            }
        });
    }

    /**
     * Pass the onWeightMeasurement call onto the handler
     *
     * @param deviceAddress Address of the device.
     * @param weightMeasurement Weight measurement of the device.
     */
    @Override
    public void onWeightMeasurement(final String deviceAddress, final HBWeightMeasurement weightMeasurement) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onWeightMeasurement(deviceAddress, weightMeasurement);
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