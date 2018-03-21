/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.deviceinformation;

import android.os.Handler;

import chdsw.philips.com.haraldlib.services.HBServiceListener;
import chdsw.philips.com.haraldlib.services.HBServiceListenerHandler;
import chdsw.philips.com.haraldlib.services.deviceinformation.object.HBPnPID;

/**
 * Callback handler that passed each callback call onto the right handler.
 */
public class HBDeviceInformationListenerHandler implements HBDeviceInformationListener, HBServiceListenerHandler {
    private Handler handler;
    private HBDeviceInformationListener listener;

    /**
     * Constructs the callback handler with a handler and the callback interface
     *
     * @param handler Handler to use on the callbacks
     */
    public HBDeviceInformationListenerHandler(Handler handler, HBDeviceInformationListener listener) {
        this.handler = handler;
        this.listener = listener;
    }

    /**
     * Pass the onManufacturerName call onto the handler
     *
     * @param deviceAddress    Address of the device.
     * @param manufacturerName Manufacturer name of the device.
     */
    @Override
    public void onManufacturerName(final String deviceAddress, final String manufacturerName) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onManufacturerName(deviceAddress, manufacturerName);
            }
        });
    }

    /**
     * Pass the onModelNumber call onto the handler
     *
     * @param deviceAddress Address of the device.
     * @param modelNumber   Model number of the device.
     */
    @Override
    public void onModelNumber(final String deviceAddress, final String modelNumber) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onModelNumber(deviceAddress, modelNumber);
            }
        });
    }

    /**
     * Pass the onSerialNumber call onto the handler
     *
     * @param deviceAddress Address of the device.
     * @param serialNumber  Serial number of the device.
     */
    @Override
    public void onSerialNumber(final String deviceAddress, final String serialNumber) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onSerialNumber(deviceAddress, serialNumber);
            }
        });
    }

    /**
     * Pass the onHardwareRevision call onto the handler
     *
     * @param deviceAddress    Address of the device.
     * @param hardwareRevision Hardware revision of the device.
     */
    @Override
    public void onHardwareRevision(final String deviceAddress, final String hardwareRevision) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onHardwareRevision(deviceAddress, hardwareRevision);
            }
        });
    }

    /**
     * Pass the onFirmwareRevision call onto the handler
     *
     * @param deviceAddress    Address of the device.
     * @param firmwareRevision Firmware revision of the device.
     */
    @Override
    public void onFirmwareRevision(final String deviceAddress, final String firmwareRevision) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onFirmwareRevision(deviceAddress, firmwareRevision);
            }
        });
    }

    /**
     * Pass the onSoftwareRevision call onto the handler
     *
     * @param deviceAddress    Address of the device.
     * @param softwareRevision Software revision of the device.
     */
    @Override
    public void onSoftwareRevision(final String deviceAddress, final String softwareRevision) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onSoftwareRevision(deviceAddress, softwareRevision);
            }
        });
    }

    /**
     * Pass the onSystemID call onto the handler
     *
     * @param deviceAddress Address of the device.
     * @param systemID      System ID of the device.
     */
    @Override
    public void onSystemID(final String deviceAddress, final byte[] systemID) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onSystemID(deviceAddress, systemID);
            }
        });
    }

    /**
     * Pass the onRegulatoryCertificationDataList call onto the handler
     *
     * @param deviceAddress                   Address of the device.
     * @param regulatoryCertificationDataList Regulatory certification data list  of the device.
     */
    @Override
    public void onRegulatoryCertificationDataList(final String deviceAddress, final byte[] regulatoryCertificationDataList) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onRegulatoryCertificationDataList(deviceAddress, regulatoryCertificationDataList);
            }
        });
    }

    /**
     * Pass the onPnPID call onto the handler
     *
     * @param deviceAddress Address of the device.
     * @param pnpID         PnP ID of the device.
     */
    @Override
    public void onPnPID(final String deviceAddress, final HBPnPID pnpID) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onPnPID(deviceAddress, pnpID);
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
