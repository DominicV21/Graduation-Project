/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.healththermometer.object;

import java.util.Date;

/**
 * Temperature object that contains measurement information as described in
 * https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.temperature_measurement.xml
 */
public class HBTemperatureMeasurement {
    private HBTemperatureUnit unit;
    private float temperatureValue;
    private Date timestamp;
    private HBTemperatureType type;

    /**
     * Get the {@link HBTemperatureUnit} of the measurement
     * @return The measurement unit
     */
    public HBTemperatureUnit getUnit() {
        return this.unit;
    }

    /**
     * Set the {@link HBTemperatureUnit} of the measurement
     * @param unit Value of the temperature unit
     */
    public void setUnit(HBTemperatureUnit unit) {
        this.unit = unit;
    }

    /**
     * Get the temperature value of the measurement
     * @return The temperature value
     */
    public float getTemperatureValue() {
        return this.temperatureValue;
    }

    /**
     * Set the temperature value of the measurement
     * @param temperatureValue Value of the temperature
     */
    public void setTemperatureValue(float temperatureValue) {
        this.temperatureValue = temperatureValue;
    }

    /**
     * Get the timestamp of the measurement
     * @return The timestamp of the measurement
     */
    public Date getTimestamp() {
        return this.timestamp;
    }

    /**
     * Set the timestamp of the measurement
     * @param timestamp Value of the timestamp
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Get the {@link HBTemperatureType} of the measurement
     * @return The measurement type
     */
    public HBTemperatureType getType() {
        return this.type;
    }

    /**
     * Set the {@link HBTemperatureType} of the measurement
     * @param type Value of the temperature type
     */
    public void setType(HBTemperatureType type) {
        this.type = type;
    }
}
