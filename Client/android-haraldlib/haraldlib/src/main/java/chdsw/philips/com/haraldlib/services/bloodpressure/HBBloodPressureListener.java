/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.bloodpressure;

import java.util.List;

import chdsw.philips.com.haraldlib.services.HBServiceListener;
import chdsw.philips.com.haraldlib.services.bloodpressure.object.HBBloodPressureFeature;
import chdsw.philips.com.haraldlib.services.bloodpressure.object.HBBloodPressureMeasurement;
import chdsw.philips.com.haraldlib.services.bloodpressure.object.HBBloodPressureRecord;
import chdsw.philips.com.haraldlib.services.bloodpressure.object.HBEnhancedBloodPressureMeasurement;
import chdsw.philips.com.haraldlib.services.bloodpressure.object.HBEnhancedIntermediateCuffPressure;
import chdsw.philips.com.haraldlib.services.bloodpressure.object.HBIntermediateCuffPressure;


public interface HBBloodPressureListener extends HBServiceListener {

    /**
     * On Record access control point response received from the device
     *
     * @param deviceAddress Address of the device.
     * @param bloodPressureMeasurement The HBBloodPressureMeasurement Object
     */
    void onBloodPressureMeasurementReported(final String deviceAddress, HBBloodPressureMeasurement bloodPressureMeasurement);

    /**
     * On Record access control point response received from the device
     *
     * @param deviceAddress Address of the device.
     * @param intermediateCuffPressure The HBIntermediateCuffPressure object
     */
    void onIntermediateCuffPressureReported(final String deviceAddress, HBIntermediateCuffPressure intermediateCuffPressure);

    /**
     * On Record access control point response received from the device
     *
     * @param deviceAddress Address of the device.
     * @param bloodPressureFeature The HBBloodPressureFeature object
     */
    void onBloodPressureFeatureReported(final String deviceAddress, HBBloodPressureFeature bloodPressureFeature);

    /**
     * On Record access control point response received from the device
     *
     * @param deviceAddress Address of the device.
     * @param data List of registered users
     */
    void onRecordAccessControlPointReported(final String deviceAddress, final byte[] data);

    /**
     * On registered users received from the device
     *
     * @param deviceAddress Address of the device.
     * @param records List of registered users
     */
    void onRecordsReported(final String deviceAddress, List<HBBloodPressureRecord> records);

    /**
     * On Record access control point response received from the device
     *
     * @param deviceAddress Address of the device.
     * @param enhancedBloodPressureMeasurement The HBBloodPressureMeasurement Object
     */
    void onEnhancedBloodPressureMeasurementReported(final String deviceAddress, HBEnhancedBloodPressureMeasurement enhancedBloodPressureMeasurement);

    /**
     * On Record access control point response received from the device
     *
     * @param deviceAddress Address of the device.
     * @param enhancedIntermediateCuffPressure The HBBloodPressureMeasurement Object
     */
    void onEnhancedIntermediateCuffPressureReported(final String deviceAddress, HBEnhancedIntermediateCuffPressure enhancedIntermediateCuffPressure);

    /**
     * Invalid request on the device on the user data service
     *
     * @param deviceAddress Address of the device.
     * @param responseValue Response of the request
     */
    void onInvalidRequest(final String deviceAddress, final int responseValue );
}
