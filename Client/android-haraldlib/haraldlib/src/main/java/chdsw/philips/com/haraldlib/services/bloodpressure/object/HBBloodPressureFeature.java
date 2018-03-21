/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.bloodpressure.object;

public class HBBloodPressureFeature {
    private boolean bodyMovementFlag;
    private boolean cuffFitFlag;
    private boolean irregularPulseflag;
    private boolean pulseRateFlag;
    private boolean measurementPositionFlag;
    private boolean multipleBondsFlag;
    private boolean e2eCrcFlag;
    private boolean userDataServiceFlag;
    private boolean baseOffsetFlag;

    private static final int BODYMOVEMENT_POSITION = 0x01;
    private static final int CUFFFIT_POSITION = 0x02;
    private static final int IRREGULARPULSE_POSITION = 0x04;
    private static final int PULSERATE_POSITION = 0x08;
    private static final int MEASUREMENTPOSITION_POSITION = 0x10;
    private static final int MULTIPLEBOND_POSITION = 0x20;
    private static final int CRC_POSITION = 0x40;
    private static final int USERDATASERVICE_POSITION = 0x80;
    private static final int BASEOFFSET_POSITION = 0x0100;

    public HBBloodPressureFeature(int flags) {
            bodyMovementFlag = ((flags & BODYMOVEMENT_POSITION) == BODYMOVEMENT_POSITION);
            cuffFitFlag = ((flags & CUFFFIT_POSITION) == CUFFFIT_POSITION);
            irregularPulseflag = ((flags & IRREGULARPULSE_POSITION) == IRREGULARPULSE_POSITION);
            pulseRateFlag = ((flags & PULSERATE_POSITION) == PULSERATE_POSITION);
            measurementPositionFlag = ((flags & MEASUREMENTPOSITION_POSITION) == MEASUREMENTPOSITION_POSITION);
            multipleBondsFlag = ((flags & MULTIPLEBOND_POSITION) == MULTIPLEBOND_POSITION);
            e2eCrcFlag = ((flags & CRC_POSITION) == CRC_POSITION);
            userDataServiceFlag = ((flags & USERDATASERVICE_POSITION) == USERDATASERVICE_POSITION);
            baseOffsetFlag = ((flags & BASEOFFSET_POSITION) == BASEOFFSET_POSITION);
    }

    public boolean getBodyMovementFlag() {
        return bodyMovementFlag;
    }

    public boolean getCuffFitFlag() {
        return cuffFitFlag;
    }

    public boolean getIrregularPulseflag() {
        return irregularPulseflag;
    }

    public boolean getPulseRateFlag() {
        return pulseRateFlag;
    }

    public boolean getMeasurementPositionFlag() {
        return measurementPositionFlag;
    }

    public boolean getMultipleBondsFlag() {
        return multipleBondsFlag;
    }

    public boolean getE2eCrcFlag() {
        return e2eCrcFlag;
    }

    public boolean getUserDataServiceFlag() {
        return userDataServiceFlag;
    }

    public boolean getBaseOffsetFlag() {
        return baseOffsetFlag;
    }
}
