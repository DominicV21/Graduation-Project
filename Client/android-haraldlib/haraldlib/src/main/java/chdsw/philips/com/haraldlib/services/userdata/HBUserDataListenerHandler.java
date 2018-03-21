/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.userdata;

import android.os.Handler;

import java.util.List;

import chdsw.philips.com.haraldlib.services.HBServiceListener;
import chdsw.philips.com.haraldlib.services.HBServiceListenerHandler;
import chdsw.philips.com.haraldlib.services.userdata.data.HBUserData;
import chdsw.philips.com.haraldlib.services.userdata.object.HBRegisteredUser;

/**
 * Callback handler that passed each callback call onto the right handler.
 */
public class HBUserDataListenerHandler implements HBUserDataListener, HBServiceListenerHandler {
    private Handler handler;
    private HBUserDataListener listener;

    /**
     * Constructs the callback handler with a handler and the callback interface
     *
     * @param handler Handler to use on the callbacks
     */
    public HBUserDataListenerHandler(Handler handler, HBUserDataListener listener) {
        this.handler = handler;
        this.listener = listener;
    }

    /**
     * Pass the onUserData call onto the handler
     *
     * @param deviceAddress Address of the device.
     * @param data User data object contain the value of a user data type
     */
    @Override
    public void onUserData(final String deviceAddress, final HBUserData data) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onUserData(deviceAddress, data);
            }
        });
    }

    /**
     * Pass the onUserRegistered call onto the handler
     *
     * @param deviceAddress Address of the device.
     * @param userIndex User index of the registered user
     */
    @Override
    public void onUserRegistered(final String deviceAddress, final int userIndex) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onUserRegistered(deviceAddress, userIndex);
            }
        });
    }

    /**
     * Pass the onUserSet call onto the handler
     *
     * @param deviceAddress Address of the device.
     */
    @Override
    public void onUserSet(final String deviceAddress) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onUserSet(deviceAddress);
            }
        });
    }

    /**
     * Pass the onUserDeleted call onto the handler
     *
     * @param deviceAddress Address of the device.
     */
    @Override
    public void onUserDeleted(final String deviceAddress, final int index) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onUserDeleted(deviceAddress, index);
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