/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.currenttime;

import android.os.Handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import chdsw.philips.com.haraldlib.services.currenttime.object.HBCurrentTime;
import chdsw.philips.com.haraldlib.services.currenttime.object.HBLocalTimeInformation;
import chdsw.philips.com.haraldlib.services.currenttime.object.HBReferenceTimeInformation;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class HBCurrentTimeListenerHandlerTest {
    private HBCurrentTimeListenerHandler hBCentralListenerHandler;

    @Mock
    private HBCurrentTimeListener listener;

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

        hBCentralListenerHandler = new HBCurrentTimeListenerHandler(handler, listener);
    }

    @Test
    public void onCurrentTimeTest() {
        HBCurrentTime currentTime = new HBCurrentTime();
        hBCentralListenerHandler.onCurrentTime("0.0.0.0", currentTime);

        verify(handler).post(any(Runnable.class));
        verify(listener).onCurrentTime("0.0.0.0", currentTime);
    }

    @Test
    public void onLocalTimeInformationTest() {
        HBLocalTimeInformation localTimeInformation = new HBLocalTimeInformation();
        hBCentralListenerHandler.onLocalTimeInformation("0.0.0.0", localTimeInformation);

        verify(handler).post(any(Runnable.class));
        verify(listener).onLocalTimeInformation("0.0.0.0", localTimeInformation);
    }

    @Test
    public void onReferenceTimeInformationTest() {
        HBReferenceTimeInformation referenceTimeInformation = new HBReferenceTimeInformation();
        hBCentralListenerHandler.onReferenceTimeInformation("0.0.0.0", referenceTimeInformation);

        verify(handler).post(any(Runnable.class));
        verify(listener).onReferenceTimeInformation("0.0.0.0", referenceTimeInformation);
    }
}
