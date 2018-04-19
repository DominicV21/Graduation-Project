package chdsw.philips.com.haraldlib.services.physicalactivitymonitor.object;

import android.support.annotation.NonNull;

import chdsw.philips.com.haraldlib.HBDevice;
import chdsw.philips.com.haraldlib.HBLogger;
import chdsw.philips.com.haraldlib.services.HBServiceHandler;

/**
 * Created by 310309623 on 9-3-2018.
 */
public class HBPhysicalActivityMonitorControlPoint {
    private HBControlPointOperation operation;
    private int parameter1 = 0;
    private int parameter2 = 0;
    private int parameter3 = 0;

    private byte response = 0;
    private int value;

    public HBPhysicalActivityMonitorControlPoint(byte[] data) {
        int maxOffset = data.length;
        int offset = 0;

        response = data[offset++];
        for(; offset < maxOffset; offset++)
        {
            value |= data[offset]  << ((offset-1) * 8);
        }
    }

    public HBPhysicalActivityMonitorControlPoint(HBControlPointOperation operation) {
        this.operation = operation;
    }

    public HBPhysicalActivityMonitorControlPoint(HBControlPointOperation operation, int parameter1) {
        this.operation = operation;
        this.parameter1 = parameter1;
    }

    public HBPhysicalActivityMonitorControlPoint(HBControlPointOperation operation, int parameter1, int parameter2) {
        this.operation = operation;
        this.parameter1 = parameter1;
        this.parameter2 = parameter2;
    }

    public HBPhysicalActivityMonitorControlPoint(HBControlPointOperation operation, int parameter1, int parameter2, int parameter3) {
        this.operation = operation;
        this.parameter1 = parameter1;
        this.parameter2 = parameter2;
        this.parameter3 = parameter3;
    }

    public HBControlPointOperation getOperation() {
        return operation;
    }

    public int getParameter1() {
        return parameter1;
    }

    public int getParameter2() {
        return parameter2;
    }

    public int getParameter3() {
        return parameter3;
    }

    public byte getResponse() { return response; }

    public int getValue() { return value; }
}
