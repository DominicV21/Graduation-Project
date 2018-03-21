/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.philipsextension.data;

import chdsw.philips.com.haraldlib.services.philipsextension.HBExtendedUserDataType;
import chdsw.philips.com.haraldlib.services.userdata.HBUserDataType;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT16;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT32;
import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;
import static chdsw.philips.com.haraldlib.services.HBDataParser.createValue;

public class HBExtendedUserDataNumber implements HBExtendedUserData<Number> {
    private HBExtendedUserDataType type;
    private Number value;

    /**
     * Create a new extended user data object for a number value
     *
     * @param type Type of extended user data
     * @param value Number value
     */
    public HBExtendedUserDataNumber(HBExtendedUserDataType type, Number value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Get the extended user data type
     *
     * @return Extended user data type
     */
    @Override
    public HBExtendedUserDataType getExtendedUserDataType() {
        return this.type;
    }

    /**
     * Get the byte[] value of the number value
     *
     * @return Byte[] of the number value
     */
    @Override
    public byte[] getData() {
        switch (type) {
            case WEIGHT_GOAL:
                return createValue(value.intValue(), FORMAT_UINT16);
        }

        return new byte[0];
    }

    /**
     * Get the number value
     *
     * @return Number value
     */
    @Override
    public Number getValue() {
        return this.value;
    }
}
