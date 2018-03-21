/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.physicalactivitymonitor;

import android.os.Handler;

import chdsw.philips.com.haraldlib.services.HBServiceListener;
import chdsw.philips.com.haraldlib.services.HBServiceListenerHandler;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorFeature;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorGaid;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorSessDescriptor;

/**
 * Callback handler that passed each callback call onto the right handler.
 */
public class HBPhysicalActivityMonitorListenerHandler implements HBPhysicalActivityMonitorListener, HBServiceListenerHandler {
    private Handler handler;
    private HBPhysicalActivityMonitorListener listener;

    /**
     * Constructs the callback handler with a handler and the callback interface
     *
     * @param handler Handler to use on the callbacks
     */
    public HBPhysicalActivityMonitorListenerHandler(Handler handler, HBPhysicalActivityMonitorListener listener) {
        this.handler = handler;
        this.listener = listener;
    }

    public void onPamFeature(final String deviceAddress, final HBPhysicalActivityMonitorFeature pamFeature) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onPamFeature(deviceAddress, pamFeature);
            }
        });
    }

    @Override
    public void onPamGaid(final String deviceAddress, final HBPhysicalActivityMonitorGaid pamGaid) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onPamGaid(deviceAddress, pamGaid);
            }
        });
    }

    @Override
    public void onPamSessDescriptor(final String deviceAddress, final HBPhysicalActivityMonitorSessDescriptor pamSessDescriptor) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onPamSessDescriptor(deviceAddress, pamSessDescriptor);
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
