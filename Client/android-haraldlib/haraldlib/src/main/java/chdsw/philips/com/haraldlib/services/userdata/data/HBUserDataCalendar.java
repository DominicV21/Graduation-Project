/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.userdata.data;

import java.util.Calendar;

import chdsw.philips.com.haraldlib.services.userdata.HBUserDataType;
import chdsw.philips.com.haraldlib.services.userdata.data.HBUserData;

import static chdsw.philips.com.haraldlib.services.HBDataParser.createDateValue;

public class HBUserDataCalendar implements HBUserData<Calendar> {
    private HBUserDataType type;
    private Calendar value;

    /**
     * Create a new user data object for a calendar value
     *
     * @param type Type of user data
     * @param value Calendar value
     */
    public HBUserDataCalendar(HBUserDataType type, Calendar value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Get the user data type
     *
     * @return User data type
     */
    @Override
    public HBUserDataType getUserDataType() {
        return this.type;
    }

    /**
     * Get the byte[] value of the calendar value
     *
     * @return Byte[] of the calendar value
     */
    @Override
    public byte[] getData() {
        return createDateValue(value);
    }

    /**
     * Get the calendar value
     *
     * @return Calendar value
     */
    @Override
    public Calendar getValue() {
        return this.value;
    }
}
