/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.physicalactivitymonitor;

import chdsw.philips.com.haraldlib.services.HBServiceListener;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorControlPoint;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorFeature;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorGaid;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorGasd;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorCraid;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorCrasd;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorScasd;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorSaid;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorSasd;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorCurrSess;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorSessDescriptor;

public interface HBPhysicalActivityMonitorListener extends HBServiceListener {

    void onPamFeature(final String deviceAddress, HBPhysicalActivityMonitorFeature pamFeature);

    void onPamGaid(final String deviceAddress, final HBPhysicalActivityMonitorGaid pamGaid);

    void onPamGasd(final String deviceAddress, final HBPhysicalActivityMonitorGasd pamGasd);

    void onPamCraid(final String deviceAddress, final HBPhysicalActivityMonitorCraid pamCraid);

    void onPamCrasd(final String deviceAddress, final HBPhysicalActivityMonitorCrasd pamCrasd);

    void onPamScasd(final String deviceAddress, final HBPhysicalActivityMonitorScasd pamScasd);

    void onPamSaid(final String deviceAddress, final HBPhysicalActivityMonitorSaid pamSaid);

    void onPamSasd(final String deviceAddress, final HBPhysicalActivityMonitorSasd pamSasd);

    void onPamCP(final String deviceAddress, final HBPhysicalActivityMonitorControlPoint pamCP);

    void onPamCurrSess(final String deviceAddress, final HBPhysicalActivityMonitorCurrSess pamCurrSess);

    void onPamSessDescriptor(final String deviceAddress, final HBPhysicalActivityMonitorSessDescriptor pamSessDescriptor);

}
