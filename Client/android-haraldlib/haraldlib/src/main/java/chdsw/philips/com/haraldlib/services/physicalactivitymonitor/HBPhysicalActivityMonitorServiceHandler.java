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
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorCurrSess;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorFeature;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorGaid;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorGasd;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorCraid;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorCrasd;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorScasd;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorSaid;
import chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object.HBPhysicalActivityMonitorSasd;
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
    private static final UUID PHYSICAL_ACTIVITY_MONITOR_SCASD_CHARACTERISTIC_UUID =
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
    private byte[] secondMessageBuffer;
    private byte[] thirdMessageBuffer;
    private byte[] totalDataBuffer;
    private boolean receivedAllMessages;
    private int nrOfMessages;
    private int currentMessage = 1;

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

        if (    characteristicUUID.equals(PHYSICAL_ACTIVITY_MONITOR_GAID_CHARACTERISTIC_UUID) ||
                characteristicUUID.equals(PHYSICAL_ACTIVITY_MONITOR_GASD_CHARACTERISTIC_UUID) ||
                characteristicUUID.equals(PHYSICAL_ACTIVITY_MONITOR_CRAID_CHARACTERISTIC_UUID) ||
                characteristicUUID.equals(PHYSICAL_ACTIVITY_MONITOR_CRASD_CHARACTERISTIC_UUID) ||
                characteristicUUID.equals(PHYSICAL_ACTIVITY_MONITOR_SCASD_CHARACTERISTIC_UUID) ||
                characteristicUUID.equals(PHYSICAL_ACTIVITY_MONITOR_SAID_CHARACTERISTIC_UUID) ||
                characteristicUUID.equals(PHYSICAL_ACTIVITY_MONITOR_SASD_CHARACTERISTIC_UUID))
        {
            HBLogger.v(TAG, String.format("%s", characteristic.getUuid()));
            getPamValue(deviceAddress, characteristic, characteristicUUID);
        }

        if(characteristicUUID.equals(PHYSICAL_ACTIVITY_MONITOR_CONTROL_POINT_CHARACTERISTIC_UUID)) {
            HBLogger.v(TAG, "PHYSICAL_ACTIVITY_MONITOR_CONTROL_POINT_CHARACTERISTIC_UUID");
            HBLogger.v(TAG, String.format("getCPValue,  device: %s, data: %s ", deviceAddress, Arrays.toString(characteristic.getValue())));
            HBPhysicalActivityMonitorControlPoint hbPhysicalActivityMonitorControlPoint = new HBPhysicalActivityMonitorControlPoint(characteristic.getValue());
            HBPhysicalActivityMonitorListener listener = getServiceListener(deviceAddress);
            if (listener != null) {
                listener.onPamCP(deviceAddress, hbPhysicalActivityMonitorControlPoint);
            }
        }

        if(characteristicUUID.equals(PHYSICAL_ACTIVITY_MONITOR_CURR_SESS_CHARACTERISTIC_UUID)) {
            HBLogger.v(TAG, "PHYSICAL_ACTIVITY_MONITOR_CURR_SESS_CHARACTERISTIC_UUID");
            HBLogger.v(TAG, String.format("getCurrentSession,  device: %s, data: %s ", deviceAddress, Arrays.toString(characteristic.getValue())));
            HBPhysicalActivityMonitorCurrSess hbPhysicalActivityMonitorCurrSess = new HBPhysicalActivityMonitorCurrSess(characteristic.getValue());
            HBPhysicalActivityMonitorListener listener = getServiceListener(deviceAddress);
            if (listener != null) {
                listener.onPamCurrSess(deviceAddress, hbPhysicalActivityMonitorCurrSess);
            }
        }

        if(characteristicUUID.equals(PHYSICAL_ACTIVITY_MONITOR_SESS_DESCR_CHARACTERISTIC_UUID))
        {
            HBLogger.v(TAG, "PHYSICAL_ACTIVITY_MONITOR_SESS_DESCR_CHARACTERISTIC_UUID");
            HBLogger.v(TAG, String.format("getPamSessDescriptor,  device: %s, data: %s ", deviceAddress, Arrays.toString(characteristic.getValue())));
            HBPhysicalActivityMonitorSessDescriptor hbPhysicalActivityMonitorSessDescriptor = new HBPhysicalActivityMonitorSessDescriptor(characteristic.getValue());
            HBPhysicalActivityMonitorListener listener = getServiceListener(deviceAddress);
            if (listener != null) {
                listener.onPamSessDescriptor(deviceAddress, hbPhysicalActivityMonitorSessDescriptor);
            }
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

    private void getPamValue(String deviceAddress, BluetoothGattCharacteristic characteristic, UUID characteristicUUID){
        HBLogger.v(TAG, String.format("getPamValue,  device: %s, data: %s ", deviceAddress, Arrays.toString(characteristic.getValue())));

        receivedAllMessages = false;
        int HeaderPos = 0;
        byte[] messageData = characteristic.getValue();

        boolean firstMessage = false;

        if((messageData[HeaderPos] & 0x80) == 0x80)
        {
            firstMessage = true;
            nrOfMessages  = (messageData[HeaderPos] & 0x7F) + 1;
            HBLogger.v(TAG, String.format("Check message numbers, message %d of %s This is the first Message", currentMessage, nrOfMessages ));
        }
        else
        {
            currentMessage = nrOfMessages - (messageData[HeaderPos] & 0x7F);
            HBLogger.v(TAG, String.format("Check message numbers, message %d of %s ", currentMessage, nrOfMessages ));
        }

        if(firstMessage && nrOfMessages == 1)
        {
            totalDataBuffer = new byte[messageData.length];
            System.arraycopy(messageData, 1, totalDataBuffer, 0, messageData.length - 1);
            receivedAllMessages = true;
        }
        else if(nrOfMessages > 1){
            //SRCPOS is 1 since we do not need the header anymore we will not copy it to the parsing method
            if (currentMessage == 1) {
                HBLogger.v(TAG, String.format("msg 1"));
                firstMessageBuffer = new byte[messageData.length];
                System.arraycopy(messageData, 1, firstMessageBuffer, 0, messageData.length - 1);
            }
            if (currentMessage == 2) {
                HBLogger.v(TAG, String.format("msg 2"));
                secondMessageBuffer = new byte[messageData.length];
                System.arraycopy(messageData, 1, secondMessageBuffer, 0, messageData.length - 1);
            }
            if (currentMessage == 3) {
                HBLogger.v(TAG, String.format("msg 3"));
                thirdMessageBuffer = new byte[messageData.length];
                System.arraycopy(messageData, 1, thirdMessageBuffer, 0, messageData.length - 1);
            }
            //if a Data Characteristic is going to exceed the total length of 80 and the minimum ATTMTU size is still at 23
            //you have to ad an extra message buffer here.

            if (currentMessage == nrOfMessages) {
                totalDataBuffer = new byte[(((nrOfMessages - 1) * firstMessageBuffer.length) + messageData.length) - nrOfMessages];
                System.arraycopy(firstMessageBuffer, 0, totalDataBuffer, 0, firstMessageBuffer.length - 1);
                System.arraycopy(secondMessageBuffer, 0, totalDataBuffer, firstMessageBuffer.length - 1, secondMessageBuffer.length - 1);
                if(nrOfMessages >= 3){System.arraycopy(thirdMessageBuffer, 0, totalDataBuffer, firstMessageBuffer.length + secondMessageBuffer.length - 2, thirdMessageBuffer.length - 1);}
                if(nrOfMessages == 4){System.arraycopy(messageData, 1, totalDataBuffer, firstMessageBuffer.length + secondMessageBuffer.length + thirdMessageBuffer.length - 3, messageData.length - 1);}
                receivedAllMessages = true;
                HBLogger.v(TAG, String.format("sizeof Data in the message (no headers) %d (this should be Terminal 'CHARACTERISTIC' presentdatalength - 1) ", totalDataBuffer.length));
            }
        }

        if(receivedAllMessages){
            nrOfMessages = 0;
            currentMessage = 1;

            if(characteristicUUID.equals(PHYSICAL_ACTIVITY_MONITOR_GAID_CHARACTERISTIC_UUID)) {

                HBPhysicalActivityMonitorGaid hbPhysicalActivityMonitorGaid = new HBPhysicalActivityMonitorGaid(totalDataBuffer);
                HBPhysicalActivityMonitorListener listener = getServiceListener(deviceAddress);
                if (listener != null) {
                    listener.onPamGaid(deviceAddress, hbPhysicalActivityMonitorGaid);
                }
            }
            if(characteristicUUID.equals(PHYSICAL_ACTIVITY_MONITOR_GASD_CHARACTERISTIC_UUID))
            {
                HBPhysicalActivityMonitorGasd hbPhysicalActivityMonitorGasd = new HBPhysicalActivityMonitorGasd(totalDataBuffer);
                HBPhysicalActivityMonitorListener listener = getServiceListener(deviceAddress);
                if (listener != null) {
                    listener.onPamGasd(deviceAddress, hbPhysicalActivityMonitorGasd);
                }
            }
            if(characteristicUUID.equals(PHYSICAL_ACTIVITY_MONITOR_CRAID_CHARACTERISTIC_UUID))
            {
                HBPhysicalActivityMonitorCraid hbPhysicalActivityMonitorCraid = new HBPhysicalActivityMonitorCraid(totalDataBuffer);
                HBPhysicalActivityMonitorListener listener = getServiceListener(deviceAddress);
                if (listener != null) {
                    listener.onPamCraid(deviceAddress, hbPhysicalActivityMonitorCraid);
                }
            }
            if(characteristicUUID.equals(PHYSICAL_ACTIVITY_MONITOR_CRASD_CHARACTERISTIC_UUID))
            {
                HBPhysicalActivityMonitorCrasd hbPhysicalActivityMonitorCrasd = new HBPhysicalActivityMonitorCrasd(totalDataBuffer);
                HBPhysicalActivityMonitorListener listener = getServiceListener(deviceAddress);
                if (listener != null) {
                    listener.onPamCrasd(deviceAddress, hbPhysicalActivityMonitorCrasd);
                }
            }
            if(characteristicUUID.equals(PHYSICAL_ACTIVITY_MONITOR_SCASD_CHARACTERISTIC_UUID))
            {
                HBPhysicalActivityMonitorScasd hbPhysicalActivityMonitorScasd = new HBPhysicalActivityMonitorScasd(totalDataBuffer);
                HBPhysicalActivityMonitorListener listener = getServiceListener(deviceAddress);
                if (listener != null) {
                    listener.onPamScasd(deviceAddress, hbPhysicalActivityMonitorScasd);
                }
            }
            if(characteristicUUID.equals(PHYSICAL_ACTIVITY_MONITOR_SAID_CHARACTERISTIC_UUID))
            {
                HBPhysicalActivityMonitorSaid hbPhysicalActivityMonitorSaid = new HBPhysicalActivityMonitorSaid(totalDataBuffer);
                HBPhysicalActivityMonitorListener listener = getServiceListener(deviceAddress);
                if (listener != null) {
                    listener.onPamSaid(deviceAddress, hbPhysicalActivityMonitorSaid);
                }
            }
            if(characteristicUUID.equals(PHYSICAL_ACTIVITY_MONITOR_SASD_CHARACTERISTIC_UUID))
            {
                HBPhysicalActivityMonitorSasd hbPhysicalActivityMonitorSasd = new HBPhysicalActivityMonitorSasd(totalDataBuffer);
                HBPhysicalActivityMonitorListener listener = getServiceListener(deviceAddress);
                if (listener != null) {
                    listener.onPamSasd(deviceAddress, hbPhysicalActivityMonitorSasd);
                }
            }
        }
    }

    public void getPamFeature(HBDevice device) {
        HBLogger.v(TAG, String.format("getPamFeature device: %s", device.getAddress()));
        device.readCharacteristic(new HBReadCommand(SERVICE_UUID, PHYSICAL_ACTIVITY_MONITOR_FEATURE_CHARACTERISTIC_UUID));
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

        if (filterProcedure.getOperation().numberOfParameters() == 2) {
            data = new byte[3];
            data[0] = opCodeData;
            data[1] = (byte) (filterProcedure.getParameter1() & 0xff);
            data[2] = (byte) (filterProcedure.getParameter2() & 0xff);
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
}
