/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.weightscale;

import chdsw.philips.com.haraldlib.services.HBServiceListener;
import chdsw.philips.com.haraldlib.services.weightscale.object.HBWeightMeasurement;
import chdsw.philips.com.haraldlib.services.weightscale.object.HBWeightScaleFeature;

/**
 * A callback to receive information from the weight scale service
 */
public interface HBWeightScaleListener extends HBServiceListener {
    /**
     * Weight scale feature read from device
     *
     * @param deviceAddress Address of the device.
     * @param weightScaleFeature Weight scale feature of the device.
     */
    void onWeightScaleFeature(final String deviceAddress, final HBWeightScaleFeature weightScaleFeature);

    /**
     * Weight measurement read from device
     *
     * @param deviceAddress Address of the device.
     * @param weightMeasurement Weight measurement of the device.
     */
    void onWeightMeasurement(final String deviceAddress, final HBWeightMeasurement weightMeasurement);
}
