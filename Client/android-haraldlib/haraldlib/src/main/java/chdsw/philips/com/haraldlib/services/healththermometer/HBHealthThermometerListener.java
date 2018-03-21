/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.healththermometer;

import chdsw.philips.com.haraldlib.services.HBServiceListener;
import chdsw.philips.com.haraldlib.services.healththermometer.object.HBTemperatureMeasurement;
import chdsw.philips.com.haraldlib.services.healththermometer.object.HBTemperatureType;

/**
 * A callback to receive information from the health thermometer service
 */
public interface HBHealthThermometerListener extends HBServiceListener {
    /**
     * Received temperature reading of the device.
     *
     * @param deviceAddress Address of the device.
     * @param measurementTemperature Temperature measurement.
     */
    void onTemperatureMeasurement(final String deviceAddress, final HBTemperatureMeasurement measurementTemperature);

    /**
     * Received temperature type of the device.
     *
     * @param deviceAddress Address of the device.
     * @param type {@link HBTemperatureType} of the device.
     */
    void onTemperatureType(final String deviceAddress, final HBTemperatureType type);

    /**
     * Received intermediate temperature of the device.
     *
     * @param deviceAddress Address of the device.
     * @param intermediateTemperature Intermediate temperature.
     */
    void onIntermediateTemperature(final String deviceAddress, final HBTemperatureMeasurement intermediateTemperature);

    /**
     * Received measurement interval of the device.
     *
     * @param deviceAddress Address of the device.
     * @param interval Interval measurement of the device.
     */
    void onMeasurementInterval(final String deviceAddress, final int interval);

}
