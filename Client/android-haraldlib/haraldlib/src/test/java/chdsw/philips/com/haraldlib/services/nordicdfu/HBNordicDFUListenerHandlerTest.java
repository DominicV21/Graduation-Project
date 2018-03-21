/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.nordicdfu;

import android.os.Handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import chdsw.philips.com.haraldlib.services.batterylevel.HBBatteryListener;
import chdsw.philips.com.haraldlib.services.batterylevel.HBBatteryListenerHandler;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class HBNordicDFUListenerHandlerTest {
    private HBBatteryListenerHandler hBCentralListenerHandler;

    @Mock
    private HBBatteryListener listener;

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

        hBCentralListenerHandler = new HBBatteryListenerHandler(handler, listener);
    }

    @Test
    public void onBatteryLevelTest() {
        hBCentralListenerHandler.onBatteryLevel("0.0.0.0", 75);

        verify(handler).post(any(Runnable.class));
        verify(listener).onBatteryLevel("0.0.0.0", 75);
    }
}
