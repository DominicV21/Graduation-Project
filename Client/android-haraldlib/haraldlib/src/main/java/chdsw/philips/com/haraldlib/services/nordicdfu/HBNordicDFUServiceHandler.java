/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.nordicdfu;

import android.Manifest;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import java.io.File;
import java.util.UUID;

import chdsw.philips.com.haraldlib.HBDevice;
import chdsw.philips.com.haraldlib.HBLogger;
import chdsw.philips.com.haraldlib.R;
import chdsw.philips.com.haraldlib.services.HBServiceHandler;
import no.nordicsemi.android.dfu.DfuProgressListener;
import no.nordicsemi.android.dfu.DfuProgressListenerAdapter;
import no.nordicsemi.android.dfu.DfuServiceInitiator;
import no.nordicsemi.android.dfu.DfuServiceListenerHelper;

import static no.nordicsemi.android.dfu.DfuServiceInitiator.DEFAULT_PRN_VALUE;

public class HBNordicDFUServiceHandler extends HBServiceHandler<HBNordicDFUListenerHandler> {
    public static final String TAG = "HBNordicDFUServiceHandler";
    public static final UUID SERVICE_UUID =
            UUID.fromString("0000FE59-0000-1000-8000-00805f9b34fb");

    private File firmwareFile;

    private final DfuProgressListener mDFUProgressListener = new DfuProgressListenerAdapter() {
        /**
         * The DFU process has started on the device
         * @param deviceAddress Address of the device
         */
        @Override
        public void onDfuProcessStarted(final String deviceAddress) {
            HBNordicDFUListener listener = getServiceListener(deviceAddress);
            if (listener != null)
                listener.onOTAProgressChanged(deviceAddress, 0, firmwareFile.length());
        }

        /**
         * The progress of the DFU process on the device has changed
         * @param deviceAddress Address of the device
         * @param percent Percentage of current part
         * @param speed Speed of the DFU
         * @param avgSpeed Average speed of the DFU
         * @param currentPart Current part
         * @param partsTotal Total parts
         */
        @Override
        public void onProgressChanged(final String deviceAddress, final int percent, final float speed, final float avgSpeed, final int currentPart, final int partsTotal) {
            float realPercentage = (((float) percent) * (1 / ((float) partsTotal))) + (100 * ((((float) currentPart) - 1) / ((float) partsTotal)));
            float sendSize = firmwareFile.length() * (realPercentage / 100.0f);
            HBNordicDFUListener listener = getServiceListener(deviceAddress);
            if (listener != null)
                listener.onOTAProgressChanged(deviceAddress, sendSize, firmwareFile.length());
        }

        /**
         * The DFU process has completed on the device
         * @param deviceAddress Address of the device
         */
        @Override
        public void onDfuCompleted(final String deviceAddress) {
            DfuServiceListenerHelper.unregisterProgressListener(context, mDFUProgressListener);
            HBNordicDFUListener listener = getServiceListener(deviceAddress);
            if (listener != null)
                listener.onOTACompleted(deviceAddress);
        }

        /**
         * The DFU process has aborted on the device
         * @param deviceAddress Address of the device
         */
        @Override
        public void onDfuAborted(final String deviceAddress) {
            DfuServiceListenerHelper.unregisterProgressListener(context, mDFUProgressListener);
            HBNordicDFUListener listener = getServiceListener(deviceAddress);
            if (listener != null)
                listener.onOTAAborted(deviceAddress);
        }

        /**
         * An error occurred during the DFU process on the device
         * @param deviceAddress Address of the device
         * @param error Error code
         * @param errorType Type of error
         * @param message Error message
         */

        @Override
        public void onError(final String deviceAddress, final int error, final int errorType, final String message) {
            DfuServiceListenerHelper.unregisterProgressListener(context, mDFUProgressListener);
            OtaError(deviceAddress, error, message);
        }
    };

    /**
     * Construct new Nordic DFU service handler.
     *
     * @param context Application context.
     */
    public HBNordicDFUServiceHandler(Context context) {
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
        //No callbacks here
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
     * Start the nordic DFU with the firmware file on given device
     *
     * @param device Device to update firmware
     * @param file   Firmware file
     */
    public void startDFU(HBDevice device, File file) {
        HBLogger.v(TAG, String.format("startDFU device: %s, file: %s", device.getAddress(), file.getPath()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            OtaError(device.getAddress(), 0, context.getString(R.string.dfu_no_read_permission));
            return;
        }

        if (!file.exists()) {
            OtaError(device.getAddress(), 0, context.getString(R.string.dfu_file_not_exists));
            return;
        }
        firmwareFile = file;

        final DfuServiceInitiator starter = new DfuServiceInitiator(device.getAddress())
                .setKeepBond(true)
                .setPacketsReceiptNotificationsEnabled(true)
                .setDisableNotification(true)
                .setPacketsReceiptNotificationsValue(DEFAULT_PRN_VALUE);

        starter.setZip(Uri.fromFile(firmwareFile), firmwareFile.getPath());

        DfuServiceListenerHelper.registerProgressListener(context, mDFUProgressListener);

        starter.start(context, HBNordicDFUService.class);
    }

    private void OtaError(String deviceAddress, int error, String message) {
        HBLogger.e(TAG, String.format("OTA error on device: %s, message: %s", deviceAddress, message));
        HBNordicDFUListener listener = getServiceListener(deviceAddress);
        if (listener != null)
            listener.onOTAError(deviceAddress, error, message);
    }
}
