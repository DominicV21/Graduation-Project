package chdsw.philips.com.haraldlib.services.devicetime;

import java.util.List;

import chdsw.philips.com.haraldlib.services.HBServiceListener;
import chdsw.philips.com.haraldlib.services.devicetime.object.HBDeviceTimeCharacteristic;
import chdsw.philips.com.haraldlib.services.devicetime.object.HBDeviceTimeFeature;
import chdsw.philips.com.haraldlib.services.devicetime.object.HBDeviceTimeParameter;
import chdsw.philips.com.haraldlib.services.devicetime.object.HBTimeChangeLogData;


public interface HBDeviceTimeListener extends HBServiceListener {

    /**
     * On dtFeature response received from the device
     *
     * @param deviceAddress Address of the device.
     * @param deviceTimeFeature The HBDeviceTimeFeature object
     */
    void onDeviceTimeFeatureReported(final String deviceAddress, final HBDeviceTimeFeature deviceTimeFeature);

    /**
     * On dtFeature response received from the device
     *
     * @param deviceAddress Address of the device.
     * @param deviceTimeParameter The HBDeviceTimeParameter object
     */
    void onDeviceTimeParameterReported(final String deviceAddress, final HBDeviceTimeParameter deviceTimeParameter);

    /**
     * On device time characteristic response received from the device
     *
     * @param deviceAddress Address of the device.
     * @param deviceTimeCharacteristic The HBDeviceTimeCharacteristic object
     */
    void onDeviceTimeCharacteristicReported(final String deviceAddress, final HBDeviceTimeCharacteristic deviceTimeCharacteristic);

    /**
     * On Device Time Control Point response received from the device
     *
     * @param deviceAddress Address of the device.
     * @param data List of registered users
     */
    void onDeviceTimeControlPointReported(final String deviceAddress, final byte[] data);

    /**
     * On Time change log data received from the device
     *
     * @param deviceAddress Address of the device.
     * @param records List of registered users
     */
    void onRecordsReported(final String deviceAddress, List<HBTimeChangeLogData> records);

    /**
     * Pass the onRecordAccessControlPointReported call onto the handler
     *
     * @param deviceAddress Address of the device.
     * @param data List of registered users
     */
    void onRecordAccessControlPointReported(final String deviceAddress, final byte[] data);

    /**
     * Invalid request on the device on the user data service
     *
     * @param deviceAddress Address of the device.
     * @param responseValue Response of the request
     */
    void onInvalidRequest(final String deviceAddress, final int responseValue );
}
