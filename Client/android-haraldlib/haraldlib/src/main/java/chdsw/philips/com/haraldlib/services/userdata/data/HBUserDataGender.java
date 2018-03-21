/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.userdata.data;

import chdsw.philips.com.haraldlib.services.userdata.object.HBGender;
import chdsw.philips.com.haraldlib.services.userdata.HBUserDataType;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;
import static chdsw.philips.com.haraldlib.services.HBDataParser.createValue;

public class HBUserDataGender implements HBUserData<HBGender> {
    private HBGender value;

    /**
     * Create a new user data object for a gender value
     *
     * @param value Gender value
     */
    public HBUserDataGender(HBGender value) {
        this.value = value;
    }

    /**
     * Get the user data type
     *
     * @return User data type
     */
    @Override
    public HBUserDataType getUserDataType() {
        return HBUserDataType.GENDER;
    }

    /**
     * Get the byte[] value of the gender value
     *
     * @return Byte[] of the gender value
     */
    @Override
    public byte[] getData() {
        return createValue(value.getValue(), FORMAT_UINT8);
    }

    /**
     * Get the gender value
     *
     * @return Gender value
     */
    @Override
    public HBGender getValue() {
        return this.value;
    }
}
