/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.batterylevel;

import chdsw.philips.com.haraldlib.services.HBServiceListener;

/**
 * A callback to receive information from the battery service
 */
public interface HBBatteryListener extends HBServiceListener {
    /**
     * Battery level read from device
     *
     * @param deviceAddress Address of the device.
     * @param batteryLevel Battery level of the device.
     */
    void onBatteryLevel(final String deviceAddress, final int batteryLevel);
}
