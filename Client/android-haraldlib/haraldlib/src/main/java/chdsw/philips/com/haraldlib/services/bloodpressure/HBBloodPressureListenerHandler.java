/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.bloodpressure;

import android.os.Handler;

import java.util.List;

import chdsw.philips.com.haraldlib.services.HBServiceListener;
import chdsw.philips.com.haraldlib.services.HBServiceListenerHandler;
import chdsw.philips.com.haraldlib.services.bloodpressure.object.HBBloodPressureFeature;
import chdsw.philips.com.haraldlib.services.bloodpressure.object.HBBloodPressureMeasurement;
import chdsw.philips.com.haraldlib.services.bloodpressure.object.HBBloodPressureRecord;
import chdsw.philips.com.haraldlib.services.bloodpressure.object.HBEnhancedBloodPressureMeasurement;
import chdsw.philips.com.haraldlib.services.bloodpressure.object.HBEnhancedIntermediateCuffPressure;
import chdsw.philips.com.haraldlib.services.bloodpressure.object.HBIntermediateCuffPressure;

public class HBBloodPressureListenerHandler implements HBBloodPressureListener, HBServiceListenerHandler {
    private Handler handler;
    private HBBloodPressureListener listener;

    /**
     * Constructs the callback handler with a handler and the callback interface
     *
     * @param handler Handler to use on the callbacks
     */
    public HBBloodPressureListenerHandler(Handler handler, HBBloodPressureListener listener) {
        this.handler = handler;
        this.listener = listener;
    }

    /**
     * On Record access control point response received from the device
     *
     * @param deviceAddress Address of the device.
     * @param bloodPressureMeasurement The HBBloodPressureMeasurement Object
     */
    @Override
    public void onBloodPressureMeasurementReported(final String deviceAddress, final HBBloodPressureMeasurement bloodPressureMeasurement) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onBloodPressureMeasurementReported(deviceAddress, bloodPressureMeasurement);
            }
        });
    }

    /**
     * On Record access control point response received from the device
     *
     * @param deviceAddress Address of the device.
     * @param intermediateCuffPressure The HBIntermediateCuffPressure object
     */
    public void onIntermediateCuffPressureReported(final String deviceAddress, final HBIntermediateCuffPressure intermediateCuffPressure) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onIntermediateCuffPressureReported(deviceAddress, intermediateCuffPressure);
            }
        });
    }

    /**
     * On Record access control point response received from the device
     *
     * @param deviceAddress Address of the device.
     * @param bloodPressureFeature The HBBloodPressureFeature object
     */
    public void onBloodPressureFeatureReported(final String deviceAddress, final HBBloodPressureFeature bloodPressureFeature) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onBloodPressureFeatureReported(deviceAddress, bloodPressureFeature);
            }
        });
    }

    /**
     * Pass the onRecordAccessControlPointReported call onto the handler
     *
     * @param deviceAddress Address of the device.
     * @param data List of registered users
     */
    @Override
    public void onRecordAccessControlPointReported(final String deviceAddress, final byte[] data) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onRecordAccessControlPointReported(deviceAddress, data);
            }
        });
    }

    /**
     * Pass the onRecordsReported call onto the handler
     *
     * @param deviceAddress Address of the device.
     * @param records List of registered users
     */
    @Override
    public void onRecordsReported(final String deviceAddress, final List<HBBloodPressureRecord> records) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onRecordsReported(deviceAddress, records);
            }
        });
    }

    /**
     * On Record access control point response received from the device
     *
     * @param deviceAddress Address of the device.
     * @param enhancedBloodPressureMeasurement The HBBloodPressureMeasurement Object
     */
    @Override
    public void onEnhancedBloodPressureMeasurementReported(final String deviceAddress, final HBEnhancedBloodPressureMeasurement enhancedBloodPressureMeasurement) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onEnhancedBloodPressureMeasurementReported(deviceAddress, enhancedBloodPressureMeasurement);
            }
        });
    }

    /**
     * On Record access control point response received from the device
     *
     * @param deviceAddress Address of the device.
     * @param enhancedIntermediateCuffPressure The HBIntermediateCuffPressure object
     */
    public void onEnhancedIntermediateCuffPressureReported(final String deviceAddress, final HBEnhancedIntermediateCuffPressure enhancedIntermediateCuffPressure) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onEnhancedIntermediateCuffPressureReported(deviceAddress, enhancedIntermediateCuffPressure);
            }
        });
    }

    /**
     * Pass the onInvalidRequest call onto the handler
     *
     * @param deviceAddress Address of the device.
     * @param responseValue Response of the request
     */
    @Override
    public void onInvalidRequest(final String deviceAddress, final int responseValue) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onInvalidRequest(deviceAddress, responseValue);
            }
        });

    }

    /**
     * Check if the listener is the same as the handlers listener
     *
     * @param listener Listener to compare
     * @return Lister equality
     */
    @Override
    public boolean equalsListener(HBServiceListener listener) {
        return this.listener.equals(listener);
    }

}
