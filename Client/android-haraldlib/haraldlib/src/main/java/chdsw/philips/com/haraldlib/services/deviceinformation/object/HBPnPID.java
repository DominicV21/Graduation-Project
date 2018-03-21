/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.deviceinformation.object;

/**
 * PnP ID object that contains PnP ID information as described in
 * https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.pnp_id.xml
 */
public class HBPnPID {
    private int vendorIDSource;
    private int vendorID;
    private int productID;
    private int productVersion;

    /**
     * Get the vendor ID source of the measurement
     *
     * @return The vendor ID source
     */
    public int getVendorIDSource() {
        return this.vendorIDSource;
    }

    /**
     * Set the vendor ID source of the measurement
     *
     * @param vendorIDSource Value of the vendor ID source
     */
    public void setVendorIDSource(int vendorIDSource) {
        this.vendorIDSource = vendorIDSource;
    }

    /**
     * Get the vendor ID of the measurement
     *
     * @return The vendor ID
     */
    public int getVendorID() {
        return this.vendorID;
    }

    /**
     * Set the vendor ID of the measurement
     *
     * @param vendorID Value of the vendor ID
     */
    public void setVendorID(int vendorID) {
        this.vendorID = vendorID;
    }

    /**
     * Get the product ID of the measurement
     *
     * @return The product ID
     */
    public int getProductID() {
        return this.productID;
    }

    /**
     * Set the product ID of the measurement
     *
     * @param productID Value of the product ID
     */
    public void setProductID(int productID) {
        this.productID = productID;
    }

    /**
     * Get the product version of the measurement
     *
     * @return The product version
     */
    public int getProductVersion() {
        return this.productVersion;
    }

    /**
     * Set the product version of the measurement
     *
     * @param productVersion Value of the product version
     */
    public void setProductVersion(int productVersion) {
        this.productVersion = productVersion;
    }
}
