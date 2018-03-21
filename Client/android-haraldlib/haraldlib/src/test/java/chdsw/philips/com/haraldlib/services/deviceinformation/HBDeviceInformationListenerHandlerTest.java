/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.deviceinformation;

import android.os.Handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import chdsw.philips.com.haraldlib.services.deviceinformation.object.HBPnPID;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class HBDeviceInformationListenerHandlerTest {
    private HBDeviceInformationListenerHandler hBCentralListenerHandler;

    @Mock
    private HBDeviceInformationListener listener;

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

        hBCentralListenerHandler = new HBDeviceInformationListenerHandler(handler, listener);
    }

    @Test
    public void onManufacturerName() {
        hBCentralListenerHandler.onManufacturerName("0.0.0.0", "value");

        verify(handler).post(any(Runnable.class));
        verify(listener).onManufacturerName("0.0.0.0", "value");
    }

    @Test
    public void onModelNumberTest() {
        hBCentralListenerHandler.onModelNumber("0.0.0.0", "value");

        verify(handler).post(any(Runnable.class));
        verify(listener).onModelNumber("0.0.0.0", "value");
    }

    @Test
    public void onSerialNumberTest() {
        hBCentralListenerHandler.onSerialNumber("0.0.0.0", "value");

        verify(handler).post(any(Runnable.class));
        verify(listener).onSerialNumber("0.0.0.0", "value");
    }

    @Test
    public void onHardwareRevisionTest() {
        hBCentralListenerHandler.onHardwareRevision("0.0.0.0", "value");

        verify(handler).post(any(Runnable.class));
        verify(listener).onHardwareRevision("0.0.0.0", "value");
    }

    @Test
    public void onFirmwareRevisionTest() {
        hBCentralListenerHandler.onFirmwareRevision("0.0.0.0", "value");

        verify(handler).post(any(Runnable.class));
        verify(listener).onFirmwareRevision("0.0.0.0", "value");
    }

    @Test
    public void onSoftwareRevisionTest() {
        hBCentralListenerHandler.onSoftwareRevision("0.0.0.0", "value");

        verify(handler).post(any(Runnable.class));
        verify(listener).onSoftwareRevision("0.0.0.0", "value");
    }

    @Test
    public void onSystemIDTest() {
        byte[] systemID = new byte[] { 1, 2, 3};
        hBCentralListenerHandler.onSystemID("0.0.0.0", systemID);

        verify(handler).post(any(Runnable.class));
        verify(listener).onSystemID("0.0.0.0", systemID);
    }

    @Test
    public void onRegulatoryCertificationDataListTest() {
        byte[] values = new byte[] { 1, 2, 3};
        hBCentralListenerHandler.onRegulatoryCertificationDataList("0.0.0.0", values);

        verify(handler).post(any(Runnable.class));
        verify(listener).onRegulatoryCertificationDataList("0.0.0.0", values);
    }

    @Test
    public void onPnPIDTest() {
        HBPnPID pnPID = new HBPnPID();
        hBCentralListenerHandler.onPnPID("0.0.0.0", pnPID);

        verify(handler).post(any(Runnable.class));
        verify(listener).onPnPID("0.0.0.0", pnPID);
    }
}
