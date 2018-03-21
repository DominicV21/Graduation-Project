/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.bloodpressure.object;

import chdsw.philips.com.haraldlib.HBLogger;
import chdsw.philips.com.haraldlib.services.devicetime.object.HBTimeChangeLogData;

public class HBBloodPressureRecord {
    public enum recordType {
        BloodPressureMeasurement(32514),
        IntermediateCuffPressure(32515),
        TimeChangeLogData(32564);

        recordType(int value) {
            this.value = value;
        }

        private int value;

        public int getValue() {
            return value;
        }

        public static recordType fromValue(int value) {
            for (recordType type : values()) {
                if (type.getValue() == value)
                    return type;
            }
            return null;
        }
    }

    private int sequenceNumber;
    private int uuid;
    private recordType type;
    private int messageParts;

    private HBEnhancedBloodPressureMeasurement bpm;
    private HBEnhancedIntermediateCuffPressure icp;
    private HBTimeChangeLogData tcld;

    private byte[] field;

    String TAG = "HBBloodPressureRecord";


    public HBBloodPressureRecord(int sequenceNumber, int uuid, int messageParts) {
        this.sequenceNumber = sequenceNumber;
        this.uuid = uuid;
        type = recordType.fromValue(uuid);
        this.messageParts = messageParts;
        this.field = new byte[0];
        HBLogger.v(TAG, "Created new record with sequenceNumber "+ Integer.toString(this.sequenceNumber)
                + "\nUUID: " + Integer.toString(this.uuid)
                + "\nRecord type: " + type.name());
    }

    private void parseField() {
        switch (type) {
            case BloodPressureMeasurement:
                HBLogger.v(TAG, "BloodPressureMeasurement field");
                bpm = new HBEnhancedBloodPressureMeasurement(field);
                break;

            case IntermediateCuffPressure:
                HBLogger.v(TAG, "IntermediateCuffPressure field");
                icp = new HBEnhancedIntermediateCuffPressure(field);
                break;

            case TimeChangeLogData:
                HBLogger.v(TAG, "TimeChangeLogData field");
                tcld = new HBTimeChangeLogData(sequenceNumber);
                tcld.addFieldPart(true, field);
                break;

            default:
                HBLogger.v(TAG, "Error in the switch in the constructor with type: " + Integer.toString(type.getValue())
                        + "\nUUID: " + Integer.toString(this.uuid)
                        + "\nSequenceNumber: " + Integer.toString(sequenceNumber));
                break;
        }
    }

    public void addFieldPart(int fieldPart, byte[] firstNamePart) {
        if (fieldPart <= messageParts) {
            byte[] newField = new byte[this.field.length + firstNamePart.length];
            System.arraycopy(this.field, 0, newField, 0, this.field.length);
            System.arraycopy(firstNamePart, 0, newField, this.field.length, firstNamePart.length);
            this.field = newField;
        }

        if (fieldPart == this.messageParts) {
            parseField();
        }
    }

    public byte[] getField() {

        byte dataArray[] = new byte[field.length + 4];
        dataArray[0] = (byte) sequenceNumber;
        dataArray[1] = (byte) (sequenceNumber >> 8);
        dataArray[2] = (byte) uuid;
        dataArray[3] = (byte) (uuid >> 8);
        System.arraycopy(field, 0, dataArray, 4, field.length);
        return dataArray;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public int getUuid() {
        return uuid;
    }

    public recordType getType() {
        return type;
    }

    public HBEnhancedBloodPressureMeasurement getBpm() {
        return bpm;
    }

    public HBEnhancedIntermediateCuffPressure getIcp() {
        return icp;
    }

    public HBTimeChangeLogData getTcld() {
        return tcld;
    }
}