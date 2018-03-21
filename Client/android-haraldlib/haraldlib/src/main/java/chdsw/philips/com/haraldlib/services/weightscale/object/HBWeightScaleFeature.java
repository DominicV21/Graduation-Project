/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

/**
 * Weight scale object that contains weight scale feature as described in
 * https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.weight_scale_feature.xml
 */
package chdsw.philips.com.haraldlib.services.weightscale.object;

public class HBWeightScaleFeature {
    private boolean timeStampSupported;
    private boolean multipleUsersSupported;
    private boolean bmiSupported;
    private int weightMeasurementResolution;
    private int heightMeasurementResolution;

    /**
     * Get the time stamp supported
     *
     * @return The support for time stamp
     */
    public boolean getTimeStampSupported() {
        return this.timeStampSupported;
    }

    /**
     * Set the time stamp support
     *
     * @param timeStampSupported Value of the time stamp support
     */
    public void setTimeStampSupported(boolean timeStampSupported) {
        this.timeStampSupported = timeStampSupported;
    }

    /**
     * Get the multiple users support
     *
     * @return The support for multiple users
     */
    public boolean getMultipleUsersSupported() {
        return this.multipleUsersSupported;
    }

    /**
     * Set the time stamp supports support
     *
     * @param multipleUsersSupported Value of the multiple users support
     */
    public void setMultipleUsersSupported(boolean multipleUsersSupported) {
        this.multipleUsersSupported = multipleUsersSupported;
    }

    /**
     * Get the BMI support
     *
     * @return The support for BMI
     */
    public boolean getBMISupported() {
        return this.bmiSupported;
    }

    /**
     * Set the BMI support
     *
     * @param bmiSupported Value of the BMI support
     */
    public void setBMISupported(boolean bmiSupported) {
        this.bmiSupported = bmiSupported;
    }

    /**
     * Get the weight measurement resolution
     *
     * @return The weight measurement resolution value
     */
    public int getWeightMeasurementResolution() {
        return this.weightMeasurementResolution;
    }

    /**
     * Set the weight measurement resolution
     *
     * @param weightMeasurementResolution Value of the weight measurement resolution
     */
    public void setWeightMeasurementResolution(int weightMeasurementResolution) {
        this.weightMeasurementResolution = weightMeasurementResolution;
    }

    /**
     * Get the height measurement resolution
     *
     * @return The height measurement resolution value
     */
    public int getHeightMeasurementResolution() {
        return this.heightMeasurementResolution;
    }

    /**
     * Set the  height measurement resolution
     *
     * @param heightMeasurementResolution Value of the  height measurement resolution
     */
    public void setHeightMeasurementResolution(int heightMeasurementResolution) {
        this.heightMeasurementResolution = heightMeasurementResolution;
    }

    /**
     * Parse the weight resolution to a value
     *
     * @param measurementUnit Unit of the measurements
     *
     * @return Weight resolution value
     */
    public float getWeightMeasurementResolutionValue(HBMeasurementUnit measurementUnit) {
        switch (this.weightMeasurementResolution) {
            case 1:
                return (measurementUnit == HBMeasurementUnit.SI ? 0.5f : 1.0f);
            case 2:
                return (measurementUnit == HBMeasurementUnit.SI ? 0.2f : 0.5f);
            case 3:
                return (measurementUnit == HBMeasurementUnit.SI ? 0.1f : 0.2f);
            case 4:
                return (measurementUnit == HBMeasurementUnit.SI ? 0.05f : 0.1f);
            case 5:
                return (measurementUnit == HBMeasurementUnit.SI ? 0.02f : 0.05f);
            case 6:
                return (measurementUnit == HBMeasurementUnit.SI ? 0.01f : 0.02f);
            case 7:
                return (measurementUnit == HBMeasurementUnit.SI ? 0.005f : 0.01f);
        }
        return 1.0f;
    }

    /**
     * Parse the height resolution to a value
     *
     * @param measurementUnit Unit of the measurements
     *
     * @return Height resolution value
     */
    public float getHeightMeasurementResolutionValue(HBMeasurementUnit measurementUnit) {
        switch (this.heightMeasurementResolution) {
            case 1:
                return (measurementUnit == HBMeasurementUnit.SI ? 0.01f : 1.0f);
            case 2:
                return (measurementUnit == HBMeasurementUnit.SI ? 0.005f : 0.5f);
            case 3:
                return (measurementUnit == HBMeasurementUnit.SI ? 0.001f : 0.1f);
        }
        return 1.0f;
    }
}
