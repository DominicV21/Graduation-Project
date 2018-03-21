/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib;

import android.os.Handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.UUID;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class HBConnectionListenerHandlerTest {
    private HBConnectionListenerHandler hbConnectionListenerHandler;

    @Mock
    private HBConnectionListener listener;

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

        hbConnectionListenerHandler = new HBConnectionListenerHandler(handler, listener);
    }

    @Test
    public void connectedTest() {
        UUID serviceUUID = UUID.randomUUID();
        hbConnectionListenerHandler.connected("0.0.0.0", serviceUUID);

        verify(handler).post(any(Runnable.class));
        verify(listener).connected("0.0.0.0", serviceUUID);
    }

    @Test
    public void connectFailedTest() {
        UUID serviceUUID = UUID.randomUUID();
        hbConnectionListenerHandler.connectFailed("0.0.0.0", serviceUUID);

        verify(handler).post(any(Runnable.class));
        verify(listener).connectFailed("0.0.0.0", serviceUUID);
    }
}
