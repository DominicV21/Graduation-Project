/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.philipsextension;

import android.os.Handler;

import java.util.List;

import chdsw.philips.com.haraldlib.services.HBServiceListener;
import chdsw.philips.com.haraldlib.services.HBServiceListenerHandler;
import chdsw.philips.com.haraldlib.services.philipsextension.data.HBExtendedUserData;
import chdsw.philips.com.haraldlib.services.philipsextension.object.HBRegisteredUser;

/**
 * Callback handler that passed each callback call onto the right handler.
 */
public class HBPhilipsExtensionListenerHandler implements HBPhilipsExtensionListener, HBServiceListenerHandler {
    private Handler handler;
    private HBPhilipsExtensionListener listener;

    /**
     * Constructs the callback handler with a handler and the callback interface
     *
     * @param handler Handler to use on the callbacks
     */
    public HBPhilipsExtensionListenerHandler(Handler handler, HBPhilipsExtensionListener listener) {
        this.handler = handler;
        this.listener = listener;
    }

    /**
     * Pass the onExtendedUserData call onto the handler
     *
     * @param deviceAddress Address of the device.
     * @param data Externded user data object contain the value of a user data type
     */
    @Override
    public void onExtendedUserData(final String deviceAddress, final HBExtendedUserData data) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onExtendedUserData(deviceAddress, data);
            }
        });
    }

    /**
     * Pass the onUserDeleted call onto the handler
     *
     * @param deviceAddress Address of the device.
     */
    @Override
    public void onUserDeleted(final String deviceAddress) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onUserDeleted(deviceAddress);
            }
        });
    }

    /**
     * Pass the onRegisteredUsers call onto the handler
     *
     * @param deviceAddress Address of the device.
     * @param users List of registered users
     */
    @Override
    public void onRegisteredUsers(final String deviceAddress, final List<HBRegisteredUser> users) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onRegisteredUsers(deviceAddress, users);
            }
        });
    }

    /**
     * Pass the onInvalidRequest call onto the handler
     *
     * @param deviceAddress Address of the device.
     * @param responseValue Response of the request
     */
    @Override
    public void onInvalidRequest(final String deviceAddress, final int responseValue) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onInvalidRequest(deviceAddress, responseValue);
            }
        });

    }

    /**
     * Check if the listener is the same as the handlers listener
     *
     * @param listener Listener to compare
     * @return Lister equality
     */
    @Override
    public boolean equalsListener(HBServiceListener listener) {
        return this.listener.equals(listener);
    }
}