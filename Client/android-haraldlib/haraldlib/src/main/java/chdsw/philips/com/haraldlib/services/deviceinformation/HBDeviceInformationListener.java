/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.deviceinformation;

import chdsw.philips.com.haraldlib.services.HBServiceListener;
import chdsw.philips.com.haraldlib.services.deviceinformation.object.HBPnPID;

/**
 * A callback to receive information from the device information service
 */
public interface HBDeviceInformationListener extends HBServiceListener {
    /**
     * Manufacturer name read from device
     *
     * @param deviceAddress    Address of the device.
     * @param manufacturerName Manufacturer name of the device.
     */
    void onManufacturerName(final String deviceAddress, final String manufacturerName);

    /**
     * Model number read from device
     *
     * @param deviceAddress Address of the device.
     * @param modelNumber   Model number of the device.
     */
    void onModelNumber(final String deviceAddress, final String modelNumber);

    /**
     * Serial number read from device
     *
     * @param deviceAddress Address of the device.
     * @param serialNumber  Serial number of the device.
     */
    void onSerialNumber(final String deviceAddress, final String serialNumber);

    /**
     * Hardware revision read from device
     *
     * @param deviceAddress    Address of the device.
     * @param hardwareRevision Hardware revision of the device.
     */
    void onHardwareRevision(final String deviceAddress, final String hardwareRevision);

    /**
     * Firmware revision read from device
     *
     * @param deviceAddress    Address of the device.
     * @param firmwareRevision Firmware revision of the device.
     */
    void onFirmwareRevision(final String deviceAddress, final String firmwareRevision);

    /**
     * Software revision read from device
     *
     * @param deviceAddress    Address of the device.
     * @param softwareRevision Software revision of the device.
     */
    void onSoftwareRevision(final String deviceAddress, final String softwareRevision);

    /**
     * System ID read from device
     *
     * @param deviceAddress Address of the device.
     * @param systemID      System ID of the device.
     */
    void onSystemID(final String deviceAddress, final byte[] systemID);

    /**
     * Regulatory certification data list read from device
     *
     * @param deviceAddress                   Address of the device.
     * @param regulatoryCertificationDataList Regulatory certification data list  of the device.
     */
    void onRegulatoryCertificationDataList(final String deviceAddress, final byte[] regulatoryCertificationDataList);

    /**
     * PnP ID read from device
     *
     * @param deviceAddress Address of the device.
     * @param pnpID         PnP ID of the device.
     */
    void onPnPID(final String deviceAddress, final HBPnPID pnpID);
}
