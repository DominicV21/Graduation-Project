/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.physicalactivitymonitor;

import android.os.Handler;

import chdsw.philips.com.haraldlib.services.HBServiceListener;
import chdsw.philips.com.haraldlib.services.HBServiceListenerHandler;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorControlPoint;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorFeature;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorGaid;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorGasd;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorCraid;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorCrasd;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorScasd;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorSaid;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorSasd;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorCurrSess;
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
    public void onPamGasd(final String deviceAddress, final HBPhysicalActivityMonitorGasd pamGasd) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onPamGasd(deviceAddress, pamGasd);
            }
        });
    }

    @Override
    public void onPamCraid(final String deviceAddress, final HBPhysicalActivityMonitorCraid pamCraid) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onPamCraid(deviceAddress, pamCraid);
            }
        });
    }

    @Override
    public void onPamCrasd(final String deviceAddress, final HBPhysicalActivityMonitorCrasd pamCrasd) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onPamCrasd(deviceAddress, pamCrasd);
            }
        });
    }

    @Override
    public void onPamScasd(final String deviceAddress, final HBPhysicalActivityMonitorScasd pamScasd) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onPamScasd(deviceAddress, pamScasd);
            }
        });
    }

    @Override
    public void onPamSaid(final String deviceAddress, final HBPhysicalActivityMonitorSaid pamSaid) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onPamSaid(deviceAddress, pamSaid);
            }
        });
    }

    @Override
    public void onPamSasd(final String deviceAddress, final HBPhysicalActivityMonitorSasd pamSasd) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onPamSasd(deviceAddress, pamSasd);
            }
        });
    }

    @Override
    public void onPamCP(final String deviceAddress, final HBPhysicalActivityMonitorControlPoint pamCP) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onPamCP(deviceAddress, pamCP);
            }
        });
    }

    @Override
    public void onPamCurrSess(final String deviceAddress, final HBPhysicalActivityMonitorCurrSess pamCurrSess) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onPamCurrSess(deviceAddress, pamCurrSess);
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
