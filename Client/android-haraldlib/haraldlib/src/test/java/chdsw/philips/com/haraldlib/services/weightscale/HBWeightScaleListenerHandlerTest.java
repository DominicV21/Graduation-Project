/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.weightscale;

import android.os.Handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import chdsw.philips.com.haraldlib.services.weightscale.object.HBWeightMeasurement;
import chdsw.philips.com.haraldlib.services.weightscale.object.HBWeightScaleFeature;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class HBWeightScaleListenerHandlerTest {
    private HBWeightScaleListenerHandler hBCentralListenerHandler;

    @Mock
    private HBWeightScaleListener listener;

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

        hBCentralListenerHandler = new HBWeightScaleListenerHandler(handler, listener);
    }

    @Test
    public void onWeightScaleFeatureTest() {
        HBWeightScaleFeature weightScaleFeature = new HBWeightScaleFeature();
        hBCentralListenerHandler.onWeightScaleFeature("0.0.0.0", weightScaleFeature);

        verify(handler).post(any(Runnable.class));
        verify(listener).onWeightScaleFeature("0.0.0.0", weightScaleFeature);
    }

    @Test
    public void onWeightMeasurementTest() {
        HBWeightMeasurement weightMeasurement = new HBWeightMeasurement();
        hBCentralListenerHandler.onWeightMeasurement("0.0.0.0", weightMeasurement);

        verify(handler).post(any(Runnable.class));
        verify(listener).onWeightMeasurement("0.0.0.0", weightMeasurement);
    }
}
