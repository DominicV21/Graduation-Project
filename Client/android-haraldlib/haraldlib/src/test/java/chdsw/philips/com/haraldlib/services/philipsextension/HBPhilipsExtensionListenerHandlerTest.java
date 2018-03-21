/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.philipsextension;

import android.os.Handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import chdsw.philips.com.haraldlib.services.philipsextension.data.HBExtendedUserData;
import chdsw.philips.com.haraldlib.services.philipsextension.object.HBRegisteredUser;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class HBPhilipsExtensionListenerHandlerTest {
    private HBPhilipsExtensionListenerHandler hbPhilipsExtensionListenerHandler;

    @Mock
    private HBPhilipsExtensionListener listener;

    @Mock
    private Handler handler;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        Answer<Boolean> handlerPostAnswer = new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                Runnable runnable = invocation.getArgumentAt(0, Runnable.class);
                runnable.run();
                return true;
            }
        };
        doAnswer(handlerPostAnswer).when(handler).post(any(Runnable.class));

        hbPhilipsExtensionListenerHandler = new HBPhilipsExtensionListenerHandler(handler, listener);
    }

    @Test
    public void onExtendedUserDataTest() {
        HBExtendedUserData userData = mock(HBExtendedUserData.class);
        hbPhilipsExtensionListenerHandler.onExtendedUserData("0.0.0.0", userData);

        verify(handler).post(any(Runnable.class));
        verify(listener).onExtendedUserData("0.0.0.0", userData);
    }

    @Test
    public void onUserDeletedTest() {
        hbPhilipsExtensionListenerHandler.onUserDeleted("0.0.0.0");

        verify(handler).post(any(Runnable.class));
        verify(listener).onUserDeleted("0.0.0.0");
    }

    @Test
    public void onRegisteredUsersTest() {
        List<HBRegisteredUser> users = new ArrayList<>();
        hbPhilipsExtensionListenerHandler.onRegisteredUsers("0.0.0.0", users);

        verify(handler).post(any(Runnable.class));
        verify(listener).onRegisteredUsers("0.0.0.0", users);
    }

    @Test
    public void onInvalidRequestTest() {
        hbPhilipsExtensionListenerHandler.onInvalidRequest("0.0.0.0", 2);

        verify(handler).post(any(Runnable.class));
        verify(listener).onInvalidRequest("0.0.0.0", 2);
    }
}
