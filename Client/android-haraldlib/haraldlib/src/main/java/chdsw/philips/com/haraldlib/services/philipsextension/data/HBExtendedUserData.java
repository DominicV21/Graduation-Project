/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.philipsextension.data;

import chdsw.philips.com.haraldlib.services.philipsextension.HBExtendedUserDataType;

 /**
 * Extended user data that is used to read and write extended user data service
 */
public interface HBExtendedUserData<T> {

    /**
     * Get the extended user data type
     *
     * @return Extended user data type
     */
    HBExtendedUserDataType getExtendedUserDataType();

    /**
     * Get the byte[] value of the extended user data
     *
     * @return Byte[] of the data
     */
    byte[] getData();

     /**
      * Get the value of the extended user data
      *
      * @return Value of the data
      */
     T getValue();
}
