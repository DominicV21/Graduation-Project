/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.heartrate;

import chdsw.philips.com.haraldlib.services.HBServiceListener;
import chdsw.philips.com.haraldlib.services.heartrate.object.HBBodySensorLocation;
import chdsw.philips.com.haraldlib.services.heartrate.object.HBHeartRateMeasurement;

public interface HBHeartRateListener extends HBServiceListener {

    void onHeartRateMeasurement(final String deviceAddress, final HBHeartRateMeasurement hbHeartRateMeasurement);

    void onBodySensorLocation(final String deviceAddress, final HBBodySensorLocation hbBodySensorLocation);

}
