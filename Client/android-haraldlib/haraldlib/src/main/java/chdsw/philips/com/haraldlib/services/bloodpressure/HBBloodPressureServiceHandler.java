/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.bloodpressure;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import chdsw.philips.com.haraldlib.HBDevice;
import chdsw.philips.com.haraldlib.HBLogger;
import chdsw.philips.com.haraldlib.HBReadCommand;
import chdsw.philips.com.haraldlib.HBWriteCommand;
import chdsw.philips.com.haraldlib.services.HBServiceHandler;
import chdsw.philips.com.haraldlib.services.bloodpressure.object.HBBloodPressureFeature;
import chdsw.philips.com.haraldlib.services.bloodpressure.object.HBBloodPressureMeasurement;
import chdsw.philips.com.haraldlib.services.bloodpressure.object.HBBloodPressureRecord;
import chdsw.philips.com.haraldlib.services.bloodpressure.object.HBEnhancedBloodPressureMeasurement;
import chdsw.philips.com.haraldlib.services.bloodpressure.object.HBEnhancedIntermediateCuffPressure;
import chdsw.philips.com.haraldlib.services.bloodpressure.object.HBIntermediateCuffPressure;
import chdsw.philips.com.haraldlib.services.bloodpressure.object.HBRecordProcedure;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_SINT16;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;

public class HBBloodPressureServiceHandler extends HBServiceHandler<HBBloodPressureListenerHandler> {

    public static final String TAG = "HBBloodPressureServiceHandler";
    public static final UUID SERVICE_UUID =
            UUID.fromString("00001810-0000-1000-8000-00805f9b34fb");

    private static final UUID BLOOD_PRESSURE_MEASUREMENT_CHARACTERISTIC_UUID =
            UUID.fromString("00002A35-0000-1000-8000-00805f9b34fb");

    private static final UUID INTERMEDIATE_CUFF_PRESSURE_CHARACTERISTIC_UUID =
            UUID.fromString("00002A36-0000-1000-8000-00805f9b34fb");

    private static final UUID BLOOD_PRESSURE_FEATURE_CHARACTERISTIC_UUID =
            UUID.fromString("00002a49-0000-1000-8000-00805f9b34fb");

    private static final UUID RECORD_ACCESS_CONTROL_POINT_CHARACTERISTIC_UUID =
            UUID.fromString("00002A52-0000-1000-8000-00805f9b34fb");

    private static final UUID BLOOD_PRESSURE_RECORD_CHARACTERISTIC_UUID =
            UUID.fromString("00007F01-0000-1000-8000-00805f9b34fb");

    private static final UUID ENHANCED_BLOOD_PRESSURE_MEASUREMENT_CHARACTERISTIC_UUID =
            UUID.fromString("00007F02-0000-1000-8000-00805f9b34fb");

    private static final UUID ENHANCED_INTERMEDIATE_CUFF_PRESSURE_CHARACTERISTIC_UUID =
            UUID.fromString("00007F03-0000-1000-8000-00805f9b34fb");

    private Map<Integer, HBBloodPressureRecord> bloodPressureRecordMap;
    private HBBloodPressureFeature bloodPressureFeature;
    private int sequenceNumberInProgress = 0;

    private static final int[] CRC16_Lookup = {
            0x0000, 0x1189, 0x2312, 0x329b, 0x4624, 0x57ad, 0x6536, 0x74bf,
            0x8c48, 0x9dc1, 0xaf5a, 0xbed3, 0xca6c, 0xdbe5, 0xe97e, 0xf8f7,
            0x1081, 0x0108, 0x3393, 0x221a, 0x56a5, 0x472c, 0x75b7, 0x643e,
            0x9cc9, 0x8d40, 0xbfdb, 0xae52, 0xdaed, 0xcb64, 0xf9ff, 0xe876,
            0x2102, 0x308b, 0x0210, 0x1399, 0x6726, 0x76af, 0x4434, 0x55bd,
            0xad4a, 0xbcc3, 0x8e58, 0x9fd1, 0xeb6e, 0xfae7, 0xc87c, 0xd9f5,
            0x3183, 0x200a, 0x1291, 0x0318, 0x77a7, 0x662e, 0x54b5, 0x453c,
            0xbdcb, 0xac42, 0x9ed9, 0x8f50, 0xfbef, 0xea66, 0xd8fd, 0xc974,
            0x4204, 0x538d, 0x6116, 0x709f, 0x0420, 0x15a9, 0x2732, 0x36bb,
            0xce4c, 0xdfc5, 0xed5e, 0xfcd7, 0x8868, 0x99e1, 0xab7a, 0xbaf3,
            0x5285, 0x430c, 0x7197, 0x601e, 0x14a1, 0x0528, 0x37b3, 0x263a,
            0xdecd, 0xcf44, 0xfddf, 0xec56, 0x98e9, 0x8960, 0xbbfb, 0xaa72,
            0x6306, 0x728f, 0x4014, 0x519d, 0x2522, 0x34ab, 0x0630, 0x17b9,
            0xef4e, 0xfec7, 0xcc5c, 0xddd5, 0xa96a, 0xb8e3, 0x8a78, 0x9bf1,
            0x7387, 0x620e, 0x5095, 0x411c, 0x35a3, 0x242a, 0x16b1, 0x0738,
            0xffcf, 0xee46, 0xdcdd, 0xcd54, 0xb9eb, 0xa862, 0x9af9, 0x8b70,
            0x8408, 0x9581, 0xa71a, 0xb693, 0xc22c, 0xd3a5, 0xe13e, 0xf0b7,
            0x0840, 0x19c9, 0x2b52, 0x3adb, 0x4e64, 0x5fed, 0x6d76, 0x7cff,
            0x9489, 0x8500, 0xb79b, 0xa612, 0xd2ad, 0xc324, 0xf1bf, 0xe036,
            0x18c1, 0x0948, 0x3bd3, 0x2a5a, 0x5ee5, 0x4f6c, 0x7df7, 0x6c7e,
            0xa50a, 0xb483, 0x8618, 0x9791, 0xe32e, 0xf2a7, 0xc03c, 0xd1b5,
            0x2942, 0x38cb, 0x0a50, 0x1bd9, 0x6f66, 0x7eef, 0x4c74, 0x5dfd,
            0xb58b, 0xa402, 0x9699, 0x8710, 0xf3af, 0xe226, 0xd0bd, 0xc134,
            0x39c3, 0x284a, 0x1ad1, 0x0b58, 0x7fe7, 0x6e6e, 0x5cf5, 0x4d7c,
            0xc60c, 0xd785, 0xe51e, 0xf497, 0x8028, 0x91a1, 0xa33a, 0xb2b3,
            0x4a44, 0x5bcd, 0x6956, 0x78df, 0x0c60, 0x1de9, 0x2f72, 0x3efb,
            0xd68d, 0xc704, 0xf59f, 0xe416, 0x90a9, 0x8120, 0xb3bb, 0xa232,
            0x5ac5, 0x4b4c, 0x79d7, 0x685e, 0x1ce1, 0x0d68, 0x3ff3, 0x2e7a,
            0xe70e, 0xf687, 0xc41c, 0xd595, 0xa12a, 0xb0a3, 0x8238, 0x93b1,
            0x6b46, 0x7acf, 0x4854, 0x59dd, 0x2d62, 0x3ceb, 0x0e70, 0x1ff9,
            0xf78f, 0xe606, 0xd49d, 0xc514, 0xb1ab, 0xa022, 0x92b9, 0x8330,
            0x7bc7, 0x6a4e, 0x58d5, 0x495c, 0x3de3, 0x2c6a, 0x1ef1, 0x0f78};

    public HBBloodPressureServiceHandler(Context context) {
        super(context);
        this.bloodPressureRecordMap = new HashMap<>();
    }

    @Override
    public void characteristicValueUpdated(String deviceAddress, BluetoothGattCharacteristic characteristic) {
        HBLogger.v(TAG, String.format("characteristicValueUpdated device: %s, characteristic: %s", deviceAddress, characteristic.getUuid()));

        UUID characteristicUUID = characteristic.getUuid();
        if (characteristicUUID.equals(BLOOD_PRESSURE_MEASUREMENT_CHARACTERISTIC_UUID)) {
            HBLogger.v(TAG, "BLOOD_PRESSURE_MEASUREMENT_CHARACTERISTIC_UUID");

            byte[] data = characteristic.getValue();
            HBBloodPressureMeasurement bloodPressureMeasurement = new HBBloodPressureMeasurement(data);

            HBBloodPressureListenerHandler listener = getServiceListener(deviceAddress);
            if (listener != null) {
                listener.onBloodPressureMeasurementReported(deviceAddress, bloodPressureMeasurement);
            }

        }
        if (characteristicUUID.equals(INTERMEDIATE_CUFF_PRESSURE_CHARACTERISTIC_UUID)) {
            HBLogger.v(TAG, "INTERMEDIATE_CUFF_PRESSURE_CHARACTERISTIC_UUID");

            byte[] data = characteristic.getValue();
            HBIntermediateCuffPressure intermediateCuffPressure = new HBIntermediateCuffPressure(data);

            HBBloodPressureListenerHandler listener = getServiceListener(deviceAddress);
            if (listener != null) {
                listener.onIntermediateCuffPressureReported(deviceAddress, intermediateCuffPressure);
            }
        }
        if (characteristicUUID.equals(BLOOD_PRESSURE_FEATURE_CHARACTERISTIC_UUID)) {
            HBLogger.v(TAG, "BLOOD_PRESSURE_FEATURE_CHARACTERISTIC_UUID");

            bloodPressureFeature = new HBBloodPressureFeature(characteristic.getIntValue(FORMAT_SINT16, 0));

            HBBloodPressureListenerHandler listener = getServiceListener(deviceAddress);
            if (listener != null) {
                listener.onBloodPressureFeatureReported(deviceAddress, bloodPressureFeature);
            }
        }
        if (characteristicUUID.equals(RECORD_ACCESS_CONTROL_POINT_CHARACTERISTIC_UUID)) {
            HBLogger.v(TAG, "RECORD_ACCESS_CONTROL_POINT_CHARACTERISTIC_UUID");
            getRecordAccessControlPointValue(deviceAddress, characteristic);
        }
        if (characteristicUUID.equals(BLOOD_PRESSURE_RECORD_CHARACTERISTIC_UUID)) {
            HBLogger.v(TAG, "BLOOD_PRESSURE_RECORD_CHARACTERISTIC_UUID");
            getBloodPressureRecordValue(deviceAddress, characteristic);
        }
        if (characteristicUUID.equals(ENHANCED_BLOOD_PRESSURE_MEASUREMENT_CHARACTERISTIC_UUID)) {
            HBLogger.v(TAG, "ENHANCED_BLOOD_PRESSURE_MEASUREMENT_CHARACTERISTIC_UUID");

            byte[] data = characteristic.getValue();
            HBEnhancedBloodPressureMeasurement enhancedBloodPressureMeasurement = new HBEnhancedBloodPressureMeasurement(data);

            HBBloodPressureListenerHandler listener = getServiceListener(deviceAddress);
            if (listener != null) {
                listener.onEnhancedBloodPressureMeasurementReported(deviceAddress, enhancedBloodPressureMeasurement);
            }

        }
        if (characteristicUUID.equals(ENHANCED_INTERMEDIATE_CUFF_PRESSURE_CHARACTERISTIC_UUID)) {
            HBLogger.v(TAG, "ENHANCED_INTERMEDIATE_CUFF_PRESSURE_CHARACTERISTIC_UUID");

            byte[] data = characteristic.getValue();
            HBEnhancedIntermediateCuffPressure enhancedIntermediateCuffPressure = new HBEnhancedIntermediateCuffPressure(data);

            HBBloodPressureListenerHandler listener = getServiceListener(deviceAddress);
            if (listener != null) {
                listener.onEnhancedIntermediateCuffPressureReported(deviceAddress, enhancedIntermediateCuffPressure);
            }
        }
    }

    @Override
    public void characteristicErrorResponse(String deviceAddress, BluetoothGattCharacteristic characteristic, int status) {

    }

    /**
     * Get the data of of the Blood Pressure Feature type from the device
     *
     * @param device Device to execute characteristic read on
     */
    public void getBloodPressureFeature(HBDevice device) {
        HBLogger.v(TAG, String.format("getBloodPressureFeature device: %s", device.getAddress()));
        device.readCharacteristic(new HBReadCommand(SERVICE_UUID, BLOOD_PRESSURE_FEATURE_CHARACTERISTIC_UUID));
    }

    public void writeToBlsRACP(HBDevice device, HBRecordProcedure filterProcedure) {
        HBLogger.v(TAG, String.format("writeToBlsRACP operation: %d, operator: %d ", filterProcedure.getOperation().getValue(), filterProcedure.getOperator().getValue()));

        byte data[];

        byte opCodeData = (byte) filterProcedure.getOperation().getValue();
        byte operator = (byte) filterProcedure.getOperator().getValue();
        byte type;
        byte[] param1 = {0, 0 };
        byte[] param2= {0, 0 };

        data = new byte[2];
        data[0] = opCodeData;
        data[1] = operator;

        if (filterProcedure.getOperator().usesType()) {
            type = (byte) filterProcedure.getType().getValue();
            param1[0] = (byte) (filterProcedure.getParameter1() & 0xff);
            param1[1] = (byte) ((filterProcedure.getParameter1() & 0xff00) >> 8);

            data = new byte[5];
            data[0] = opCodeData;
            data[1] = operator;
            data[2] = type;
            data[3] = param1[0];
            data[4] = param1[1];

            if (filterProcedure.getOperator().numberOfParameters() == 2) {
                param1[0] = (byte) (filterProcedure.getParameter1() & 0xff);
                param1[1] = (byte) ((filterProcedure.getParameter1() & 0xff00) >> 8);
                param2[0] = (byte) (filterProcedure.getParameter2() & 0xff);
                param2[1] = (byte) ((filterProcedure.getParameter2() & 0xff00) >> 8);

                data = new byte[7];
                data[0] = opCodeData;
                data[1] = operator;
                data[2] = type;
                data[3] = param1[0];
                data[4] = param1[1];
                data[5] = param2[0];
                data[6] = param2[1];
            }
        }

        device.writeCharacteristic(new HBWriteCommand(SERVICE_UUID,
                RECORD_ACCESS_CONTROL_POINT_CHARACTERISTIC_UUID, data));
    }

    private void getRecordAccessControlPointValue(String deviceAddress,
                                                  BluetoothGattCharacteristic characteristic) {
        HBLogger.v(TAG, String.format("getRecordAccessControlPointValue,  device: %s, data: %s ", deviceAddress, Arrays.toString(characteristic.getValue())));
        HBBloodPressureListenerHandler listener = getServiceListener(deviceAddress);

        if (characteristic.getIntValue(FORMAT_UINT8, 0) == 6) {
            if (listener != null) {
                listener.onRecordsReported(deviceAddress, new ArrayList<>(bloodPressureRecordMap.values()));
            }
            bloodPressureRecordMap.clear();

        } else if (characteristic.getIntValue(FORMAT_UINT8, 0) == 5) {
            listener.onRecordAccessControlPointReported(deviceAddress, characteristic.getValue());
        }
    }


    private void getBloodPressureRecordValue(String deviceAddress,
                                             BluetoothGattCharacteristic characteristic) {
        HBLogger.v(TAG, String.format("getBloodPressureRecordValue,  device: %s, data: %s ", deviceAddress, Arrays.toString(characteristic.getValue())));
        int offset = 0;
        int sequenceNumber;
        int uuid = 0;
        boolean dataValid = true;

        byte[] data = characteristic.getValue();
        byte[] field;

        // Check the CRC if it is enabled according to the bp feature
        //if (bloodPressureFeature.getE2eCrcFlag()) {
        ///    dataValid = checkCRCValue(data);
        //}

        //if(dataValid) {
            int nrOfMessages = data[offset] & 0x0F;
            int currentMessage = data[offset] >> 4;
            HBLogger.v(TAG, String.format("Check message numbers, message %d of %s ", currentMessage, nrOfMessages ));
            offset++;

            if (currentMessage == 1) {
                ByteBuffer buffer = ByteBuffer.wrap(data, offset, 2);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                sequenceNumber = buffer.getShort();
                offset += 2;
                sequenceNumberInProgress = sequenceNumber;


                buffer = ByteBuffer.wrap(data, offset, 2);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                uuid = buffer.getShort();
                offset += 2;
            }

            // Set the sequence number to the sequenceNumberInProgress for following records
            sequenceNumber = sequenceNumberInProgress;

            // Fill field with the remainder of the data
            field = Arrays.copyOfRange(data, offset, data.length);

            if (bloodPressureRecordMap.containsKey(sequenceNumber)) {
                bloodPressureRecordMap.get(sequenceNumber).addFieldPart(currentMessage, field);
            } else {
                HBBloodPressureRecord bloodPressureRecord = new HBBloodPressureRecord(sequenceNumber, uuid, nrOfMessages);
                bloodPressureRecord.addFieldPart(currentMessage, field);
                bloodPressureRecordMap.put(sequenceNumber, bloodPressureRecord);
            }

        // If last message and crc is enabled, check the crc field from the data
        if (currentMessage == nrOfMessages) {
            if (bloodPressureFeature.getE2eCrcFlag()) {
                dataValid = checkCRCValue(bloodPressureRecordMap.get(sequenceNumber).getField());
                if (!dataValid) {
                    bloodPressureRecordMap.remove(sequenceNumber);
                }
                //field = Arrays.copyOfRange(data, offset, data.length - 2);
            }
        }

        //}
    }

    private boolean checkCRCValue(byte[] crcData) {
        int crc = (short) 0xFFFF;

        int i;
        //StringBuilder sb = new StringBuilder();

        for (i=0;i<crcData.length;i++)
        {
            crc = ((crc>>8)&0xFF) ^ CRC16_Lookup[(crc ^ crcData[i]) & 0xFF];
            //sb.append(String.format("%02X ", crcData[i]));
        }
        //HBLogger.v(TAG, "size: " + Integer.toString(crcData.length) + " crcData: " + sb.toString());
        HBLogger.v(TAG, "Result of the CRC calculation: "+ crc);
        crc &= 0xffff;

        return crc == 0;
    }
}
