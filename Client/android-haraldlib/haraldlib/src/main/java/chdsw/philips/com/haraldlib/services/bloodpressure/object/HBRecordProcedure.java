/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.bloodpressure.object;

public class HBRecordProcedure {
    private HBRecordOperation operation;
    private HBRecordOperator operator;
    private HBFilterType type;
    private int parameter1 = 0;
    private int parameter2 = 0;

    public HBRecordProcedure(HBRecordOperation operation, HBRecordOperator operator) {
        this.operation = operation;
        this.operator = operator;
    }

    public HBRecordProcedure(HBRecordOperation operation, HBRecordOperator operator, HBFilterType type, int parameter1) {
        this.operation = operation;
        this.operator = operator;
        this.type = type;
        this.parameter1 = parameter1;
    }

    public HBRecordProcedure(HBRecordOperation operation, HBRecordOperator operator, HBFilterType type, int parameter1, int parameter2) {
        this.operation = operation;
        this.operator = operator;
        this.type = type;
        this.parameter1 = parameter1;
        this.parameter2 = parameter2;
    }

    public HBRecordOperation getOperation(){
        return operation;
    }

    public HBRecordOperator getOperator() {
        return operator;
    }

    public HBFilterType getType() {
        return type;
    }

    public int getParameter1() {
        return parameter1;
    }

    public int getParameter2() {
        return parameter2;
    }
}
