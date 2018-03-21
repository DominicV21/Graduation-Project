/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.physicalactivitymonitor;

import java.util.List;

import chdsw.philips.com.haraldlib.services.HBServiceListener;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorFeature;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorGaid;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorSessDescriptor;

public interface HBPhysicalActivityMonitorListener extends HBServiceListener {

    void onPamFeature(final String deviceAddress, HBPhysicalActivityMonitorFeature pamFeature);

    void onPamGaid(final String deviceAddress, final HBPhysicalActivityMonitorGaid pamGaid);

    void onPamSessDescriptor(final String deviceAddress, final HBPhysicalActivityMonitorSessDescriptor pamSessDescriptor);

}
