/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.heartrate.object;

/**
 * Heart rate object that contains measurement information as described in
 * https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.heart_rate_measurement.xml
 */
public class HBHeartRateMeasurement {
    private int heartRateMeasurementValue;
    private int energyExpended;
    private int RRInterval;
    private HBSensorContactFeature sensorContactStatus;

    public int getHeartRateMeasurementValue() {
        return heartRateMeasurementValue;
    }

    public void setHeartRateMeasurementValue(int heartRateMeasurementValue) {
        this.heartRateMeasurementValue = heartRateMeasurementValue;
    }

    public HBSensorContactFeature getSensorContactStatus() {
        return sensorContactStatus;
    }

    public void setSensorContactStatus(HBSensorContactFeature sensorContactStatus) {
        this.sensorContactStatus = sensorContactStatus;
    }

    public int getEnergyExpended() {
        return energyExpended;
    }

    public void setEnergyExpended(int energyExpended) {
        this.energyExpended = energyExpended;
    }

    public int getRRInterval() {
        return RRInterval;
    }

    public void setRRInterval(int RRInterval) {
        this.RRInterval = RRInterval;
    }
}
