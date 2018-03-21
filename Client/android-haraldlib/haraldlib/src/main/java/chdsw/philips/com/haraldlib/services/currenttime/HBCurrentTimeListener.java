/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.currenttime;

import chdsw.philips.com.haraldlib.services.HBServiceListener;
import chdsw.philips.com.haraldlib.services.currenttime.object.HBCurrentTime;
import chdsw.philips.com.haraldlib.services.currenttime.object.HBLocalTimeInformation;
import chdsw.philips.com.haraldlib.services.currenttime.object.HBReferenceTimeInformation;

/**
 * A callback to receive information from the current time service
 */
public interface HBCurrentTimeListener extends HBServiceListener {
    /**
     * Current time of the device.
     *
     * @param deviceAddress Address of the device.
     * @param currentTime Current time of the device.
     */
    void onCurrentTime(final String deviceAddress, final HBCurrentTime currentTime);

    /**
     * Local time information of the device
     *
     * @param deviceAddress Address of the device.
     * @param localTimeInformation Local time information of the device
     */
    void onLocalTimeInformation(final String deviceAddress, final HBLocalTimeInformation localTimeInformation);

    /**
     * Reference time information of the device
     *
     * @param deviceAddress Address of the device.
     * @param referenceTimeInformation Reference time information of the device
     */
    void onReferenceTimeInformation(final String deviceAddress, final HBReferenceTimeInformation referenceTimeInformation);

}
