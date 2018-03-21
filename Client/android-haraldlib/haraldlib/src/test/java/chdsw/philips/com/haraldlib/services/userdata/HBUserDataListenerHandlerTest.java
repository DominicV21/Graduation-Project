/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.userdata;

import android.os.Handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import chdsw.philips.com.haraldlib.services.userdata.data.HBUserData;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class HBUserDataListenerHandlerTest {
    private HBUserDataListenerHandler hbUserDataListenerHandler;

    @Mock
    private HBUserDataListener listener;

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

        hbUserDataListenerHandler = new HBUserDataListenerHandler(handler, listener);
    }

    @Test
    public void onUserDataTest() {
        HBUserData userData = mock(HBUserData.class);
        hbUserDataListenerHandler.onUserData("0.0.0.0", userData);

        verify(handler).post(any(Runnable.class));
        verify(listener).onUserData("0.0.0.0", userData);
    }

    @Test
    public void onUserRegisteredTest() {
        hbUserDataListenerHandler.onUserRegistered("0.0.0.0", 1);

        verify(handler).post(any(Runnable.class));
        verify(listener).onUserRegistered("0.0.0.0", 1);
    }

    @Test
    public void onUserSetTest() {
        hbUserDataListenerHandler.onUserSet("0.0.0.0");

        verify(handler).post(any(Runnable.class));
        verify(listener).onUserSet("0.0.0.0");
    }

    @Test
    public void onUserDeletedTest() {
        hbUserDataListenerHandler.onUserDeleted("0.0.0.0", 1);

        verify(handler).post(any(Runnable.class));
        verify(listener).onUserDeleted("0.0.0.0", 1);
    }

    @Test
    public void onInvalidRequestTest() {
        hbUserDataListenerHandler.onInvalidRequest("0.0.0.0", 2);

        verify(handler).post(any(Runnable.class));
        verify(listener).onInvalidRequest("0.0.0.0", 2);
    }
}
