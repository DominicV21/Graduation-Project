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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class HBDeviceConnectionListenerHandlerTest {
    private HBDeviceConnectionListenerHandler hbDeviceConnectionListenerHandler;

    @Mock
    private HBDeviceConnectionListener listener;

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

        hbDeviceConnectionListenerHandler = new HBDeviceConnectionListenerHandler(handler, listener);
    }

    @Test
    public void disconnectedTest() {
        hbDeviceConnectionListenerHandler.disconnected("0.0.0.0");

        verify(handler).post(any(Runnable.class));
        verify(listener).disconnected("0.0.0.0");
    }

    @Test
    public void onReadWriteErrorTest() {
        hbDeviceConnectionListenerHandler.onReadWriteError("0.0.0.0");

        verify(handler).post(any(Runnable.class));
        verify(listener).onReadWriteError("0.0.0.0");
    }
}
