/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.deviceinformation;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import java.util.UUID;

import chdsw.philips.com.haraldlib.HBDevice;
import chdsw.philips.com.haraldlib.HBLogger;
import chdsw.philips.com.haraldlib.HBReadCommand;
import chdsw.philips.com.haraldlib.services.HBServiceHandler;
import chdsw.philips.com.haraldlib.services.deviceinformation.object.HBPnPID;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT16;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;
import static chdsw.philips.com.haraldlib.services.HBDataParser.*;

public class HBDeviceInformationServiceHandler extends HBServiceHandler<HBDeviceInformationListenerHandler> {
    public static final String TAG = "HBDeviceInformationServiceHandler";
    public static final UUID SERVICE_UUID =
            UUID.fromString("0000180A-0000-1000-8000-00805f9b34fb");
    private static final UUID MANUFACTURER_NAME_CHARACTERISTIC_UUID =
            UUID.fromString("00002A29-0000-1000-8000-00805f9b34fb");
    private static final UUID MODEL_NUMBER_CHARACTERISTIC_UUID =
            UUID.fromString("00002A24-0000-1000-8000-00805f9b34fb");
    private static final UUID SERIAL_NUMBER_CHARACTERISTIC_UUID =
            UUID.fromString("00002A25-0000-1000-8000-00805f9b34fb");
    private static final UUID HARDWARE_REVISION_CHARACTERISTIC_UUID =
            UUID.fromString("00002A27-0000-1000-8000-00805f9b34fb");
    private static final UUID FIRMWARE_REVISION_CHARACTERISTIC_UUID =
            UUID.fromString("00002A26-0000-1000-8000-00805f9b34fb");
    private static final UUID SOFTWARE_REVISION_CHARACTERISTIC_UUID =
            UUID.fromString("00002A28-0000-1000-8000-00805f9b34fb");
    private static final UUID SYSTEM_ID_CHARACTERISTIC_UUID =
            UUID.fromString("00002A23-0000-1000-8000-00805f9b34fb");
    private static final UUID REGULATORY_CERTIFICATION_DATA_LIST_CHARACTERISTIC_UUID =
            UUID.fromString("00002A2A-0000-1000-8000-00805f9b34fb");
    private static final UUID PNP_ID_CHARACTERISTIC_UUID =
            UUID.fromString("00002A50-0000-1000-8000-00805f9b34fb");

    /**
     * Construct new healt thermometer service handler.
     *
     * @param context Application context.
     */
    public HBDeviceInformationServiceHandler(Context context) {
        super(context);
    }

    /**
     * Process characteristic value update in service handler.
     *
     * @param deviceAddress  Device address which received characteristic value update.
     * @param characteristic Characteristic that received value update.
     */
    @Override
    public void characteristicValueUpdated(String deviceAddress,
                                           BluetoothGattCharacteristic characteristic) {
        HBLogger.v(TAG, String.format("characteristicValueUpdated device: %s, characteristic: %s", deviceAddress, characteristic.getUuid()));
        UUID characteristicUUID = characteristic.getUuid();

        if (characteristicUUID.equals(MANUFACTURER_NAME_CHARACTERISTIC_UUID)) {
            getManufacturerNameValue(deviceAddress, characteristic);
        }

        if (characteristicUUID.equals(MODEL_NUMBER_CHARACTERISTIC_UUID)) {
            getModelNumberValue(deviceAddress, characteristic);
        }

        if (characteristicUUID.equals(SERIAL_NUMBER_CHARACTERISTIC_UUID)) {
            getSerialNumberValue(deviceAddress, characteristic);
        }

        if (characteristicUUID.equals(HARDWARE_REVISION_CHARACTERISTIC_UUID)) {
            getHardwareRevisionValue(deviceAddress, characteristic);
        }

        if (characteristicUUID.equals(FIRMWARE_REVISION_CHARACTERISTIC_UUID)) {
            getFirmwareRevisionValue(deviceAddress, characteristic);
        }

        if (characteristicUUID.equals(SOFTWARE_REVISION_CHARACTERISTIC_UUID)) {
            getSoftwareRevisionValue(deviceAddress, characteristic);
        }

        if (characteristicUUID.equals(SYSTEM_ID_CHARACTERISTIC_UUID)) {
            getSystemIDValue(deviceAddress, characteristic);
        }

        if (characteristicUUID.equals(REGULATORY_CERTIFICATION_DATA_LIST_CHARACTERISTIC_UUID)) {
            getRegulatoryCertificationDataListValue(deviceAddress, characteristic);
        }

        if (characteristicUUID.equals(PNP_ID_CHARACTERISTIC_UUID)) {
            getPnPIDValue(deviceAddress, characteristic);
        }
    }

    /**
     * Process characteristic error response in service handler.
     *
     * @param deviceAddress  Device address which received characteristic value update.
     * @param characteristic Characteristic that received value update.
     * @param status         Value of the error response
     */
    public void characteristicErrorResponse(String deviceAddress,
                                            BluetoothGattCharacteristic characteristic, int status) {
        HBLogger.v(TAG, String.format("characteristicErrorResponse device: %s, characteristic: %s, status: %d", deviceAddress, characteristic.getUuid(), status));
    }

    /**
     * Get the manufacturer name from the bluetooth device
     *
     * @param device Device to execute characteristic read on
     */
    public void getManufacturerName(HBDevice device) {
        HBLogger.v(TAG, String.format("getManufacturerName device: %s", device.getAddress()));
        device.readCharacteristic(new HBReadCommand(SERVICE_UUID,
                MANUFACTURER_NAME_CHARACTERISTIC_UUID));
    }

    /**
     * Get the model number from the bluetooth device
     *
     * @param device Device to execute characteristic read on
     */
    public void getModelNumber(HBDevice device) {
        HBLogger.v(TAG, String.format("getModelNumber device: %s", device.getAddress()));
        device.readCharacteristic(new HBReadCommand(SERVICE_UUID,
                MODEL_NUMBER_CHARACTERISTIC_UUID));
    }

    /**
     * Get the serial number from the bluetooth device
     *
     * @param device Device to execute characteristic read on
     */
    public void getSerialNumber(HBDevice device) {
        HBLogger.v(TAG, String.format("getSerialNumber device: %s", device.getAddress()));
        device.readCharacteristic(new HBReadCommand(SERVICE_UUID,
                SERIAL_NUMBER_CHARACTERISTIC_UUID));
    }

    /**
     * Get the hardware revision from the bluetooth device
     *
     * @param device Device to execute characteristic read on
     */
    public void getHardwareRevision(HBDevice device) {
        HBLogger.v(TAG, String.format("getHardwareRevision device: %s", device.getAddress()));
        device.readCharacteristic(new HBReadCommand(SERVICE_UUID,
                HARDWARE_REVISION_CHARACTERISTIC_UUID));
    }

    /**
     * Get the firmware revision from the bluetooth device
     *
     * @param device Device to execute characteristic read on
     */
    public void getFirmwareRevision(HBDevice device) {
        HBLogger.v(TAG, String.format("getFirmwareRevision device: %s", device.getAddress()));
        device.readCharacteristic(new HBReadCommand(SERVICE_UUID,
                FIRMWARE_REVISION_CHARACTERISTIC_UUID));
    }

    /**
     * Get the software revision from the bluetooth device
     *
     * @param device Device to execute characteristic read on
     */
    public void getSoftwareRevision(HBDevice device) {
        HBLogger.v(TAG, String.format("getSoftwareRevision device: %s", device.getAddress()));
        device.readCharacteristic(new HBReadCommand(SERVICE_UUID,
                SOFTWARE_REVISION_CHARACTERISTIC_UUID));
    }

    /**
     * Get the system ID from the bluetooth device
     *
     * @param device Device to execute characteristic read on
     */
    public void getSystemID(HBDevice device) {
        HBLogger.v(TAG, String.format("getSystemID device: %s", device.getAddress()));
        device.readCharacteristic(new HBReadCommand(SERVICE_UUID,
                SYSTEM_ID_CHARACTERISTIC_UUID));
    }

    /**
     * Get the regulatory certification data list from the bluetooth device
     *
     * @param device Device to execute characteristic read on
     */
    public void getRegulatoryCertificationDataList(HBDevice device) {
        HBLogger.v(TAG, String.format("getRegulatoryCertificationDataList device: %s", device.getAddress()));
        device.readCharacteristic(new HBReadCommand(SERVICE_UUID,
                REGULATORY_CERTIFICATION_DATA_LIST_CHARACTERISTIC_UUID));
    }

    /**
     * Get the PnP ID from the bluetooth device
     *
     * @param device Device to execute characteristic read on
     */
    public void getPnPID(HBDevice device) {
        HBLogger.v(TAG, String.format("getPnPID device: %s", device.getAddress()));
        device.readCharacteristic(new HBReadCommand(SERVICE_UUID,
                PNP_ID_CHARACTERISTIC_UUID));
    }

    /**
     * Get the manufacturer name value from the characteristic.
     *
     * @param deviceAddress  Address of the device that the value is received from
     * @param characteristic Characteristic containing the value
     */
    private void getManufacturerNameValue(String deviceAddress,
                                          BluetoothGattCharacteristic characteristic) {
        String manufacturerName = characteristic.getStringValue(0);
        HBLogger.v(TAG, String.format("getManufacturerNameValue device: %s, manufacturerName: %s", deviceAddress, manufacturerName));
        HBDeviceInformationListener listener = getServiceListener(deviceAddress);
        if (listener != null)
            listener.onManufacturerName(deviceAddress, manufacturerName);
    }

    /**
     * Get the model number value from the characteristic.
     *
     * @param deviceAddress  Address of the device that the value is received from
     * @param characteristic Characteristic containing the value
     */
    private void getModelNumberValue(String deviceAddress,
                                     BluetoothGattCharacteristic characteristic) {
        String modelNumber = characteristic.getStringValue(0);
        HBLogger.v(TAG, String.format("getModelNumberValue device: %s, modelNumber: %s", deviceAddress, modelNumber));
        HBDeviceInformationListener listener = getServiceListener(deviceAddress);
        if (listener != null)
            listener.onModelNumber(deviceAddress, modelNumber);
    }

    /**
     * Get the serial number value from the characteristic.
     *
     * @param deviceAddress  Address of the device that the value is received from
     * @param characteristic Characteristic containing the value
     */
    private void getSerialNumberValue(String deviceAddress,
                                      BluetoothGattCharacteristic characteristic) {
        String serialNumber = characteristic.getStringValue(0);
        HBLogger.v(TAG, String.format("getSerialNumberValue device: %s, serialNumber: %s", deviceAddress, serialNumber));
        HBDeviceInformationListener listener = getServiceListener(deviceAddress);
        if (listener != null)
            listener.onSerialNumber(deviceAddress, serialNumber);
    }

    /**
     * Get the hardware revision value from the characteristic.
     *
     * @param deviceAddress  Address of the device that the value is received from
     * @param characteristic Characteristic containing the value
     */
    private void getHardwareRevisionValue(String deviceAddress,
                                          BluetoothGattCharacteristic characteristic) {
        String hardwareRevision = characteristic.getStringValue(0);
        HBLogger.v(TAG, String.format("getHardwareRevisionValue device: %s, hardwareRevision: %s", deviceAddress, hardwareRevision));
        HBDeviceInformationListener listener = getServiceListener(deviceAddress);
        if (listener != null)
            listener.onHardwareRevision(deviceAddress, hardwareRevision);
    }

    /**
     * Get the firmware revision value from the characteristic.
     *
     * @param deviceAddress  Address of the device that the value is received from
     * @param characteristic Characteristic containing the value
     */
    private void getFirmwareRevisionValue(String deviceAddress,
                                          BluetoothGattCharacteristic characteristic) {
        String firmwareRevision = characteristic.getStringValue(0);
        HBLogger.v(TAG, String.format("getFirmwareRevisionValue device: %s, firmwareRevision: %s", deviceAddress, firmwareRevision));
        HBDeviceInformationListener listener = getServiceListener(deviceAddress);
        if (listener != null)
            listener.onFirmwareRevision(deviceAddress, firmwareRevision);
    }

    /**
     * Get the software revision value from the characteristic.
     *
     * @param deviceAddress  Address of the device that the value is received from
     * @param characteristic Characteristic containing the value
     */
    private void getSoftwareRevisionValue(String deviceAddress,
                                          BluetoothGattCharacteristic characteristic) {
        String softwareRevision = characteristic.getStringValue(0);
        HBLogger.v(TAG, String.format("getSoftwareRevisionValue device: %s, softwareRevision: %s", deviceAddress, softwareRevision));
        HBDeviceInformationListener listener = getServiceListener(deviceAddress);
        if (listener != null)
            listener.onSoftwareRevision(deviceAddress, softwareRevision);
    }

    /**
     * Get the system ID value from the characteristic.
     *
     * @param deviceAddress  Address of the device that the value is received from
     * @param characteristic Characteristic containing the value
     */
    private void getSystemIDValue(String deviceAddress,
                                  BluetoothGattCharacteristic characteristic) {
        HBLogger.v(TAG, String.format("getSystemIDValue device: %s", deviceAddress));
        byte[] systemID = characteristic.getValue();
        HBDeviceInformationListener listener = getServiceListener(deviceAddress);
        if (listener != null)
            listener.onSystemID(deviceAddress, systemID);
    }

    /**
     * Get the regulatory certification data list value from the characteristic.
     *
     * @param deviceAddress  Address of the device that the value is received from
     * @param characteristic Characteristic containing the value
     */
    private void getRegulatoryCertificationDataListValue(String deviceAddress,
                                                         BluetoothGattCharacteristic characteristic) {
        HBLogger.v(TAG, String.format("getRegulatoryCertificationDataListValue device: %s", deviceAddress));
        byte[] regulatoryCertificationDataList = characteristic.getValue();
        HBDeviceInformationListener listener = getServiceListener(deviceAddress);
        if (listener != null)
            listener.onRegulatoryCertificationDataList(deviceAddress, regulatoryCertificationDataList);
    }

    /**
     * Get the PnP ID value from the characteristic.
     *
     * @param deviceAddress  Address of the device that the value is received from
     * @param characteristic Characteristic containing the value
     */
    private void getPnPIDValue(String deviceAddress,
                               BluetoothGattCharacteristic characteristic) {
        HBLogger.v(TAG, String.format("getPnPIDValue device: %s", deviceAddress));
        HBPnPID pnpID = new HBPnPID();
        int offset = 0;

        int vendorIDSource = characteristic.getIntValue(FORMAT_UINT8, offset);
        pnpID.setVendorIDSource(vendorIDSource);
        offset += getTypeLen(FORMAT_UINT8);

        int vendorID = characteristic.getIntValue(FORMAT_UINT16, offset);
        pnpID.setVendorID(vendorID);
        offset += getTypeLen(FORMAT_UINT16);

        int productID = characteristic.getIntValue(FORMAT_UINT16, offset);
        pnpID.setProductID(productID);
        offset += getTypeLen(FORMAT_UINT16);

        int productVersion = characteristic.getIntValue(FORMAT_UINT16, offset);
        pnpID.setProductVersion(productVersion);

        HBDeviceInformationListener listener = getServiceListener(deviceAddress);
        if (listener != null)
            listener.onPnPID(deviceAddress, pnpID);
    }
}
