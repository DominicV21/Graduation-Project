package chdsw.philips.com.haraldlib.services.devicetime;

import android.os.Handler;

import java.util.List;

import chdsw.philips.com.haraldlib.services.HBServiceListener;
import chdsw.philips.com.haraldlib.services.HBServiceListenerHandler;
import chdsw.philips.com.haraldlib.services.devicetime.object.HBDeviceTimeCharacteristic;
import chdsw.philips.com.haraldlib.services.devicetime.object.HBDeviceTimeFeature;
import chdsw.philips.com.haraldlib.services.devicetime.object.HBDeviceTimeParameter;
import chdsw.philips.com.haraldlib.services.devicetime.object.HBTimeChangeLogData;

public class HBDeviceTimeListenerHandler implements HBDeviceTimeListener, HBServiceListenerHandler {
    private Handler handler;
    private HBDeviceTimeListener listener;

    /**
     * Constructs the callback handler with a handler and the callback interface
     *
     * @param handler Handler to use on the callbacks
     */
    public HBDeviceTimeListenerHandler(Handler handler, HBDeviceTimeListener listener) {
        this.handler = handler;
        this.listener = listener;
    }

    /**
     * On dtFeature response received from the device
     *
     * @param deviceAddress Address of the device.
     * @param deviceTimeFeature The HBDeviceTimeFeature object
     */
    public void onDeviceTimeFeatureReported(final String deviceAddress, final HBDeviceTimeFeature deviceTimeFeature) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onDeviceTimeFeatureReported(deviceAddress, deviceTimeFeature);
            }
        });
    }

    /**
     * On dtFeature response received from the device
     *
     * @param deviceAddress Address of the device.
     * @param deviceTimeParameter The HBDeviceTimeParameter object
     */
    public void onDeviceTimeParameterReported(final String deviceAddress, final HBDeviceTimeParameter deviceTimeParameter) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onDeviceTimeParameterReported(deviceAddress, deviceTimeParameter);
            }
        });
    }

    /**
     * On device time characteristic response received from the device
     *
     * @param deviceAddress Address of the device.
     * @param deviceTimeCharacteristic The HBDeviceTimeCharacteristic object
     */
    public void onDeviceTimeCharacteristicReported(final String deviceAddress, final HBDeviceTimeCharacteristic deviceTimeCharacteristic) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onDeviceTimeCharacteristicReported(deviceAddress, deviceTimeCharacteristic);
            }
        });
    }

    /**
     * On Record access control point response received from the device
     *
     * @param deviceAddress Address of the device.
     * @param data List of registered users
     */
    public void onDeviceTimeControlPointReported(final String deviceAddress, final byte[] data) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onDeviceTimeControlPointReported(deviceAddress, data);
            }
        });
    }

    /**
     * On Time change log data received from the device
     *
     * @param deviceAddress Address of the device.
     * @param records List of registered users
     */
    public void onRecordsReported(final String deviceAddress, final List<HBTimeChangeLogData> records) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onRecordsReported(deviceAddress, records);
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
