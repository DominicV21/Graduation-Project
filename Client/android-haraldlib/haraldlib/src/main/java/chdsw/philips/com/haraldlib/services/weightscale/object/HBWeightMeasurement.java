/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

/**
 * Weight measurement object that contains weight measurement as described in
 * https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.weight_measurement.xml
 */
package chdsw.philips.com.haraldlib.services.weightscale.object;

import java.util.Date;

public class HBWeightMeasurement {
    private int weight;
    private HBMeasurementUnit measurementUnit;
    private Date timestamp;
    private int userID;
    private int BMI;
    private int height;
    private HBWeightScaleFeature weightScaleFeature;

    /**
     * Get the weight value
     *
     * @return The weight value
     */
    public int getWeight() {
        return this.weight;
    }

    /**
     * Set the weight value
     *
     * @param weight Value of the weight
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * Get the actual weight based on the weight resolution
     *
     * @return Actual weight
     */
    public float getActualWeight() {
        if (weightScaleFeature != null)
            return ((float) this.weight) * this.weightScaleFeature.getWeightMeasurementResolutionValue(measurementUnit);
        else
            return this.weight;
    }

    /**
     * Get the measurement unit
     *
     * @return The measurement unit value
     */
    public HBMeasurementUnit getMeasurementUnit() {
        return this.measurementUnit;
    }

    /**
     * Set the measurement unit
     *
     * @param measurementUnit Value of the measurement unit
     */
    public void setMeasurementUnit(HBMeasurementUnit measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    /**
     * Get the timestamp of the measurement
     *
     * @return The timestamp of the measurement
     */
    public Date getTimestamp() {
        return this.timestamp;
    }

    /**
     * Set the timestamp of the measurement
     *
     * @param timestamp Value of the timestamp
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Get the user ID
     *
     * @return The user ID value
     */
    public int getUserID() {
        return this.userID;
    }

    /**
     * Set the user ID
     *
     * @param userID Value of the user ID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * Get the BMI
     *
     * @return The BMI value
     */
    public int getBMI() {
        return this.BMI;
    }

    /**
     * Set the BMI
     *
     * @param BMI Value of the BMI
     */
    public void setBMI(int BMI) {
        this.BMI = BMI;
    }

    /**
     * Get the height
     *
     * @return The height value
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Set the height
     *
     * @param height Value of the height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Get the actual height based on the height resolution
     *
     * @return Actual height
     */
    public float getActualHeight() {
        if (weightScaleFeature != null)
            return ((float) this.height) * this.weightScaleFeature.getHeightMeasurementResolutionValue(measurementUnit);
        else
            return this.height;
    }

    /**
     * Set the weight scale feature containing the resolutions
     *
     * @param weightScaleFeature Weight scale feature
     */
    public void setHBWeightScaleFeature(HBWeightScaleFeature weightScaleFeature) {
        this.weightScaleFeature = weightScaleFeature;
    }
}
