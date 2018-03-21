/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.bloodpressure.object;

import java.nio.ByteBuffer;

import chdsw.philips.com.haraldlib.HBLogger;

public class Sfloat {
    private int exponent;
    private int sign;
    private int mantissa;
    private double value;

    private static final int exponentPos = 0xF000;
    private static final int signPos = 0x0800;
    private static final int mantissaPos = 0x07FF;

    public Sfloat(byte[] data) {
        ByteBuffer bytes = ByteBuffer.wrap(data);

        String s1 = String.format("%8s", Integer.toBinaryString(bytes.get(0) & 0xFF)).replace(' ', '0');
        String s2 = String.format("%8s", Integer.toBinaryString(bytes.get(1) & 0xFF)).replace(' ', '0');

        HBLogger.v("SFloat", "Raw data= " + s2 + s1);


        exponent = (bytes.get(1) >> 4) & 0x0F;
        HBLogger.v("SFloat", "Exponent = " + exponent);
        sign = (bytes.get(1)) & 0x08;
        mantissa = bytes.get(0) & 0xFF;
        mantissa |= (bytes.get(1) & 0x07) << 8;

        if(exponent > 7)
        {
            exponent -= 16;
        }

        if(sign > 1) {
            mantissa *= -1;
        }

        value = (mantissa * Math.pow(10, exponent));
        HBLogger.v("SFloat", "exponent: " + exponent + ", sign: " + sign + ", mantissa: " + mantissa + ", value: " + value);
    }

    public double getValue() {
        // remove the floating point error and round the the correct amount of digits
        double returnValue = Math.round(value * Math.pow(10, Math.abs(exponent)));
        return returnValue / Math.pow(10, Math.abs(exponent));
    }

}
