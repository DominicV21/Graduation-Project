/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.physicalactivitymonitor;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.support.v4.content.res.TypedArrayUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;
import java.math.BigInteger;
import java.nio.ByteOrder;
import java.text.Format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import chdsw.philips.com.haraldlib.HBDevice;
import chdsw.philips.com.haraldlib.HBLogger;
import chdsw.philips.com.haraldlib.HBReadCommand;
import chdsw.philips.com.haraldlib.HBWriteCommand;
import chdsw.philips.com.haraldlib.services.HBServiceHandler;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorControlPoint;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorFeature;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorGaid;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorSessDescriptor;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_FLOAT;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_SINT16;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT16;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT32;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;
import static chdsw.philips.com.haraldlib.services.HBDataParser.createValue;
import static chdsw.philips.com.haraldlib.services.HBDataParser.getTypeLen;

public class HBPhysicalActivityMonitorServiceHandler extends HBServiceHandler<HBPhysicalActivityMonitorListenerHandler> {

    public static final String TAG = "HBPhysicalActivityMonitorServiceHandler";
    public static final UUID SERVICE_UUID =
            UUID.fromString("00007F10-0000-1000-8000-00805f9b34fb");
    private static final UUID PHYSICAL_ACTIVITY_MONITOR_FEATURE_CHARACTERISTIC_UUID =
            UUID.fromString("00007F11-0000-1000-8000-00805f9b34fb");
    private static final UUID PHYSICAL_ACTIVITY_MONITOR_GAID_CHARACTERISTIC_UUID =
            UUID.fromString("00007F12-0000-1000-8000-00805f9b34fb");
    private static final UUID PHYSICAL_ACTIVITY_MONITOR_GASD_CHARACTERISTIC_UUID =
            UUID.fromString("00007F13-0000-1000-8000-00805f9b34fb");
    private static final UUID PHYSICAL_ACTIVITY_MONITOR_CRAID_CHARACTERISTIC_UUID =
            UUID.fromString("00007F14-0000-1000-8000-00805f9b34fb");
    private static final UUID PHYSICAL_ACTIVITY_MONITOR_CRASD_CHARACTERISTIC_UUID =
            UUID.fromString("00007F15-0000-1000-8000-00805f9b34fb");
    private static final UUID PHYSICAL_ACTIVITY_MONITOR_SCAS_CHARACTERISTIC_UUID =
            UUID.fromString("00007F16-0000-1000-8000-00805f9b34fb");
    private static final UUID PHYSICAL_ACTIVITY_MONITOR_SAID_CHARACTERISTIC_UUID =
            UUID.fromString("00007F17-0000-1000-8000-00805f9b34fb");
    private static final UUID PHYSICAL_ACTIVITY_MONITOR_SASD_CHARACTERISTIC_UUID =
            UUID.fromString("00007F18-0000-1000-8000-00805f9b34fb");
    private static final UUID PHYSICAL_ACTIVITY_MONITOR_CONTROL_POINT_CHARACTERISTIC_UUID =
            UUID.fromString("00007F1E-0000-1000-8000-00805f9b34fb");
    private static final UUID PHYSICAL_ACTIVITY_MONITOR_CURR_SESS_CHARACTERISTIC_UUID =
            UUID.fromString("00007F1F-0000-1000-8000-00805f9b34fb");
    private static final UUID PHYSICAL_ACTIVITY_MONITOR_SESS_DESCR_CHARACTERISTIC_UUID =
            UUID.fromString("00007F20-0000-1000-8000-00805f9b34fb");

    private HBPhysicalActivityMonitorFeature pamFeature;
    private byte[] firstMessageBuffer;
    private byte[] totalDataBuffer;
    private boolean receivedAllMessages;

    public HBPhysicalActivityMonitorServiceHandler(Context context) {
        super(context);
    }

    @Override
    public void characteristicValueUpdated(String deviceAddress, BluetoothGattCharacteristic characteristic) {
        HBLogger.v(TAG, String.format("characteristicValueUpdated device: %s, characteristic: %s", deviceAddress, characteristic.getUuid()));

        UUID characteristicUUID = characteristic.getUuid();
        if (characteristicUUID.equals(PHYSICAL_ACTIVITY_MONITOR_FEATURE_CHARACTERISTIC_UUID)) {
            HBLogger.v(TAG, "PHYSICAL_ACTIVITY_MONITOR_FEATURE_UUID");
            pamFeature = new HBPhysicalActivityMonitorFeature(characteristic.getIntValue(FORMAT_UINT32, 4), characteristic.getIntValue(FORMAT_UINT32, 0));
            HBPhysicalActivityMonitorListenerHandler listener = getServiceListener(deviceAddress);
            if (listener != null) {
                listener.onPamFeature(deviceAddress, pamFeature);
            }
        }

        if (characteristicUUID.equals(PHYSICAL_ACTIVITY_MONITOR_GAID_CHARACTERISTIC_UUID))
        {
            HBLogger.v(TAG, "PHYSICAL_ACTIVITY_MONITOR_GAID_UUID");
            getPamGaid(deviceAddress, characteristic);
        }

        if(characteristicUUID.equals(PHYSICAL_ACTIVITY_MONITOR_SESS_DESCR_CHARACTERISTIC_UUID))
        {
            HBLogger.v(TAG, "PHYSICAL_ACTIVITY_MONITOR_SESS_DESCR_CHARACTERISTIC_UUID");
            getPamSessDescriptor(deviceAddress, characteristic);
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


    public void getPamFeature(HBDevice device) {
        HBLogger.v(TAG, String.format("getPamFeature device: %s", device.getAddress()));
        device.readCharacteristic(new HBReadCommand(SERVICE_UUID, PHYSICAL_ACTIVITY_MONITOR_FEATURE_CHARACTERISTIC_UUID));
    }

    private void getPamGaid(String deviceAddress, BluetoothGattCharacteristic characteristic){
        HBLogger.v(TAG, String.format("getPamGaid,  device: %s, data: %s ", deviceAddress, Arrays.toString(characteristic.getValue())));

        receivedAllMessages = false;
        int offset = 0;
        byte[] messageData = characteristic.getValue();

        int nrOfMessages = (messageData[offset] & 0x7F) + 1;
        int currentMessage = messageData[offset] & 0x7F;
        boolean firstMessage = false;

        if((messageData[offset] & 0x80) == 0x80)
        {
            firstMessage = true;
        }

        HBLogger.v(TAG, String.format("Check message numbers, message %d of %s ", currentMessage, nrOfMessages ));

        if(firstMessage && nrOfMessages == 1)
        {
            totalDataBuffer = new byte[messageData.length];
            System.arraycopy(messageData, 0, totalDataBuffer, 0, messageData.length);
            receivedAllMessages = true;
        }
        else {
            if (currentMessage == 1) {
                firstMessageBuffer = new byte[messageData.length];
                totalDataBuffer = new byte[firstMessageBuffer.length + messageData.length];
                System.arraycopy(messageData, 0, firstMessageBuffer, 0, messageData.length);
            }

            System.arraycopy(firstMessageBuffer, 0, totalDataBuffer, 0, firstMessageBuffer.length);
            System.arraycopy(messageData, 1, totalDataBuffer, firstMessageBuffer.length, messageData.length - 1); //SRCPOS is 1 because we dont want to send the header of the second message

            if (currentMessage == 0) {
                receivedAllMessages = true;
            }
        }

        if(receivedAllMessages){
            HBPhysicalActivityMonitorGaid hbPhysicalActivityMonitorGaid = new HBPhysicalActivityMonitorGaid(totalDataBuffer);
            HBPhysicalActivityMonitorListener listener = getServiceListener(deviceAddress);
            if (listener != null) {
                listener.onPamGaid(deviceAddress, hbPhysicalActivityMonitorGaid);
            }
        }
    }

    public void writeToPamCP(HBDevice device, HBPhysicalActivityMonitorControlPoint filterProcedure) {
        HBLogger.v(TAG, String.format("writeToPamCP operation: %d, parameter1: ", filterProcedure.getOperation().getValue(), filterProcedure.getParameter1()));

        byte data[] = new byte[2];

        byte opCodeData = (byte) filterProcedure.getOperation().getValue();
        byte Argument;
        byte[] SessionID = { 0, 0 };
        byte[] SubSessionID = { 0, 0 };

        if(filterProcedure.getOperation().numberOfParameters() == 0)
        {
            data = new byte[1];
            data[0] = opCodeData;
        }

        if (filterProcedure.getOperation().numberOfParameters() == 1)
        {
            if(filterProcedure.getOperation().getValue() == 4) {
                data = new byte[2];
                data[0] = opCodeData;

                Argument = (byte) (filterProcedure.getParameter1() & 0xff);
                data[1] = Argument;
            }
            else
            {
                data = new byte[3];
                data[0] = opCodeData;

                SessionID[0] = (byte) (filterProcedure.getParameter1() & 0xff);
                SessionID[1] = (byte) ((filterProcedure.getParameter1() & 0xff00) >> 8);
                data[1] = SessionID[0];
                data[2] = SessionID[1];
            }
        }

        if (filterProcedure.getOperation().numberOfParameters() == 3) {
            data = new byte[6];

            SessionID[1] = (byte) (filterProcedure.getParameter1() & 0xff);
            SessionID[0] = (byte) ((filterProcedure.getParameter1() & 0xff00) >> 8);
            SubSessionID[1] = (byte) (filterProcedure.getParameter2() & 0xff);
            SubSessionID[0] = (byte) ((filterProcedure.getParameter2() & 0xff00) >> 8);
            Argument = (byte) (filterProcedure.getParameter3() & 0xff);

            data[0] = opCodeData;
            data[1] = SessionID[0];
            data[2] = SessionID[1];
            data[3] = SubSessionID[0];
            data[4] = SubSessionID[1];
            data[5] = Argument;
        }


        device.writeCharacteristic(new HBWriteCommand(SERVICE_UUID,
                PHYSICAL_ACTIVITY_MONITOR_CONTROL_POINT_CHARACTERISTIC_UUID, data));
    }

    private void getPamSessDescriptor(String deviceAddress, BluetoothGattCharacteristic characteristic)
    {
        HBLogger.v(TAG, String.format("getPamSessDescriptor,  device: %s, data: %s ", deviceAddress, Arrays.toString(characteristic.getValue())));

        HBPhysicalActivityMonitorSessDescriptor hbPhysicalActivityMonitorSessDescriptor = new HBPhysicalActivityMonitorSessDescriptor(characteristic.getValue());
        HBPhysicalActivityMonitorListener listener = getServiceListener(deviceAddress);
        if (listener != null) {
            listener.onPamSessDescriptor(deviceAddress, hbPhysicalActivityMonitorSessDescriptor);
        }
    }
}
