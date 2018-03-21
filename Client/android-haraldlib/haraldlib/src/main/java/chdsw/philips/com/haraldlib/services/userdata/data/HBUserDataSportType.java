/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.userdata.data;

import chdsw.philips.com.haraldlib.services.userdata.object.HBSportType;
import chdsw.philips.com.haraldlib.services.userdata.HBUserDataType;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;
import static chdsw.philips.com.haraldlib.services.HBDataParser.createValue;

public class HBUserDataSportType implements HBUserData<HBSportType> {
    private HBSportType value;

    /**
     * Create a new user data object for a sport type value
     *
     * @param value Sport type value
     */
    public HBUserDataSportType(HBSportType value) {
        this.value = value;
    }

    /**
     * Get the user data type
     *
     * @return User data type
     */
    @Override
    public HBUserDataType getUserDataType() {
        return HBUserDataType.SPORT_TYPE;
    }

    /**
     * Get the byte[] value of the sport type value
     *
     * @return Byte[] of the sport type value
     */
    @Override
    public byte[] getData() {
        return createValue(value.getValue(), FORMAT_UINT8);
    }

    /**
     * Get the sport type value
     *
     * @return Sport type value
     */
    @Override
    public HBSportType getValue() {
        return this.value;
    }
}
