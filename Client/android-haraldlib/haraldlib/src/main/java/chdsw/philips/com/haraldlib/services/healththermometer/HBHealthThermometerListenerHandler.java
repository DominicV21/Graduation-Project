/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.healththermometer;

import android.os.Handler;

import chdsw.philips.com.haraldlib.services.HBServiceListener;
import chdsw.philips.com.haraldlib.services.HBServiceListenerHandler;
import chdsw.philips.com.haraldlib.services.healththermometer.object.HBTemperatureMeasurement;
import chdsw.philips.com.haraldlib.services.healththermometer.object.HBTemperatureType;

/**
 * Callback handler that passed each callback call onto the right handler.
 */
public class HBHealthThermometerListenerHandler implements HBHealthThermometerListener, HBServiceListenerHandler {
    private Handler handler;
    private HBHealthThermometerListener listener;

    /**
     * Constructs the callback handler with a handler and the callback interface
     *
     * @param handler Handler to use on the callbacks
     */
    public HBHealthThermometerListenerHandler(Handler handler, HBHealthThermometerListener listener) {
        this.handler = handler;
        this.listener = listener;
    }

    /**
     * Pass the onTemperatureMeasurement call onto the handler
     *
     * @param deviceAddress          Address of the device.
     * @param measurementTemperature Temperature measurement.
     */
    @Override
    public void onTemperatureMeasurement(final String deviceAddress, final HBTemperatureMeasurement measurementTemperature) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onTemperatureMeasurement(deviceAddress, measurementTemperature);
            }
        });
    }

    /**
     * Pass the onMeasurementInterval call onto the handler
     *
     * @param deviceAddress Address of the device.
     * @param interval      Interval measurement of the device.
     */
    @Override
    public void onMeasurementInterval(final String deviceAddress, final int interval) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onMeasurementInterval(deviceAddress, interval);
            }
        });
    }

    /**
     * Pass the onIntermediateTemperature call onto the handler
     *
     * @param deviceAddress           Address of the device.
     * @param intermediateTemperature Intermediate temperature.
     */
    @Override
    public void onIntermediateTemperature(final String deviceAddress, final HBTemperatureMeasurement intermediateTemperature) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onIntermediateTemperature(deviceAddress, intermediateTemperature);
            }
        });
    }

    /**
     * Pass the onTemperatureType call onto the handler
     *
     * @param deviceAddress Address of the device.
     * @param type          {@link HBTemperatureType} of the device.
     */
    @Override
    public void onTemperatureType(final String deviceAddress, final HBTemperatureType type) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onTemperatureType(deviceAddress, type);
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
