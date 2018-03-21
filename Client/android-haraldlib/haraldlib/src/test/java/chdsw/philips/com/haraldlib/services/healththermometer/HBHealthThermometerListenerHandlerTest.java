/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.healththermometer;

import android.os.Handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import chdsw.philips.com.haraldlib.services.healththermometer.object.HBTemperatureMeasurement;

import static chdsw.philips.com.haraldlib.services.healththermometer.object.HBTemperatureType.GastroIntestinalTract;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class HBHealthThermometerListenerHandlerTest {
    private HBHealthThermometerListenerHandler hBCentralListenerHandler;

    @Mock
    private HBHealthThermometerListener listener;

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

        hBCentralListenerHandler = new HBHealthThermometerListenerHandler(handler, listener);
    }

    @Test
    public void onTemperatureMeasurementTest() {
        HBTemperatureMeasurement measurement = new HBTemperatureMeasurement();
        hBCentralListenerHandler.onTemperatureMeasurement("0.0.0.0", measurement);

        verify(handler).post(any(Runnable.class));
        verify(listener).onTemperatureMeasurement("0.0.0.0", measurement);
    }

    @Test
    public void onMeasurementIntervalTest() {
        hBCentralListenerHandler.onMeasurementInterval("0.0.0.0", 1);

        verify(handler).post(any(Runnable.class));
        verify(listener).onMeasurementInterval("0.0.0.0", 1);
    }
    @Test
    public void onIntermediateTemperatureTest() {
        HBTemperatureMeasurement measurement = new HBTemperatureMeasurement();
        hBCentralListenerHandler.onIntermediateTemperature("0.0.0.0", measurement);

        verify(handler).post(any(Runnable.class));
        verify(listener).onIntermediateTemperature("0.0.0.0", measurement);
    }

    @Test
    public void onTemperatureTypeTest() {
        hBCentralListenerHandler.onTemperatureType("0.0.0.0", GastroIntestinalTract);

        verify(handler).post(any(Runnable.class));
        verify(listener).onTemperatureType("0.0.0.0", GastroIntestinalTract);
    }
}
