/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

/**
 * Weight scale object that contains weight scale feature as described in
 * https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.weight_scale_feature.xml
 */

package chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object;

public class HBPhysicalActivityMonitorFeature {
    private long flags;

    private boolean multipleUsersFlag;
    private boolean deviceWornFlag;
    private boolean normalWalkingEnergyExpenditureFlag;

    private static final int MULTIPLEUSERS_POSITION = 0x00;
    private static final int DEVICEWORN_POSITION = 0x01;
    private static final int NORMALWALKINGENERGYEXPENDITURE_POSITION = 0x02;

    public HBPhysicalActivityMonitorFeature (long flagshi, long flagslo){
        this.flags =  (flagshi << 32) | flagslo;
        multipleUsersFlag = ((flags & MULTIPLEUSERS_POSITION) == MULTIPLEUSERS_POSITION);
        deviceWornFlag = ((flags & DEVICEWORN_POSITION) == DEVICEWORN_POSITION);
        normalWalkingEnergyExpenditureFlag = ((flags & NORMALWALKINGENERGYEXPENDITURE_POSITION) == NORMALWALKINGENERGYEXPENDITURE_POSITION);
    }

    public boolean getMultipleUsersSupported() {
        return multipleUsersFlag;
    }
    public boolean getDeviceWornSupported() {
        return deviceWornFlag;
    }
    public boolean getNormalWalkingEnergyExpenditureSupported() {
        return normalWalkingEnergyExpenditureFlag;
    }

    public long getFlags() { return  flags; }

}
