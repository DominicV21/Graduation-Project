/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import java.util.Map;
import java.util.WeakHashMap;

public abstract class HBServiceHandler<T extends HBServiceListenerHandler> {
    private Map<String, T> listeners;
    protected Context context;

    /**
     * Construct new service handler with callback listener.
     *
     * @param context Application context
     */
    protected HBServiceHandler(Context context) {
        this.listeners = new WeakHashMap<>();
        this.context = context;
    }

    /**
     * Process characteristic value update in service handler.
     *
     * @param deviceAddress  Device address which received characteristic value update.
     * @param characteristic Characteristic that received value update.
     */
    public abstract void characteristicValueUpdated(String deviceAddress,
                                                    BluetoothGattCharacteristic characteristic);

    /**
     * Process characteristic error response in service handler.
     *
     * @param deviceAddress  Device address which received characteristic value update.
     * @param characteristic Characteristic that received value update.
     * @param status         Value of the error response
     */
    public abstract void characteristicErrorResponse(String deviceAddress,
                                                    BluetoothGattCharacteristic characteristic, int status);

    /**
     * Add a listener tot the service handler for a specific device
     *
     * @param address Address of the device
     * @param listener Listener to add
     */
    public void addServiceListener(String address, T listener) {
        this.listeners.put(address, listener);
    }

    /**
     * Remove the listener for a specific device
     *
     * @param address Address of the device
     */
    public void removeServiceListener(String address) {
        this.listeners.remove(address);
    }

    /**
     * Get the listener for a specific device
     *
     * @param address Address of the device
     * @return Listener, null is not found
     */
    public T getServiceListener(String address) {
        return this.listeners.get(address);
    }
}
