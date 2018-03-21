/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.nordicdfu;

import android.Manifest;
import android.bluetooth.BluetoothGattCharacteristic;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.io.File;
import java.lang.reflect.Field;

import chdsw.philips.com.haraldlib.BuildConfig;
import chdsw.philips.com.haraldlib.HBDevice;
import no.nordicsemi.android.dfu.DfuProgressListener;

import static android.os.Build.VERSION_CODES.M;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = { M })
public class HBNordicDFUServiceHandlerTest {
    private ShadowApplication application;
    private HBNordicDFUServiceHandler handler;

    @Mock
    private HBNordicDFUListenerHandler listener;

    @Before
    public void setUp()  {
        initMocks(this);

        application = ShadowApplication.getInstance();
        application.grantPermissions(Manifest.permission.READ_EXTERNAL_STORAGE);

        handler = new HBNordicDFUServiceHandler(application.getApplicationContext());
    }

    @Test
    public void addServiceListenerTest() {
        handler.addServiceListener("0.0.0.0", listener);
        assertEquals(handler.getServiceListener("0.0.0.0"),listener);
    }

    @Test
    public void removeServiceListenerTest() {
        handler.addServiceListener("0.0.0.0", listener);
        assertEquals(listener, handler.getServiceListener("0.0.0.0"));
        handler.removeServiceListener("0.0.0.0");
        assertEquals(handler.getServiceListener("0.0.0.0"), null);
    }

    @Test
    public void characteristicValueUpdatedTest() {
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);
    }

    @Test
    public void starDFUTest() {
        File file = mock(File.class);
        when(file.exists()).thenReturn(true);

        HBDevice device = mock(HBDevice.class);
        when(device.getAddress()).thenReturn("0.0.0.0");

        handler.addServiceListener("0.0.0.0", listener);

        handler.startDFU(device, file);
        verify(listener, never()).onOTAError(anyString(), anyInt(), anyString());
    }

    @Test
    public void startDFUNoPermissionTest() {
        application.denyPermissions(Manifest.permission.READ_EXTERNAL_STORAGE);

        HBDevice device = mock(HBDevice.class);
        when(device.getAddress()).thenReturn("0.0.0.0");

        handler.addServiceListener("0.0.0.0", listener);

        handler.startDFU(device, new File(""));

        verify(listener).onOTAError(anyString(), anyInt(), anyString());
    }

    @Test
    public void startDFUFileNotExistsTest() {
        File file = mock(File.class);
        when(file.exists()).thenReturn(false);

        HBDevice device = mock(HBDevice.class);
        when(device.getAddress()).thenReturn("0.0.0.0");

        handler.addServiceListener("0.0.0.0", listener);

        handler.startDFU(device, file);

        verify(listener).onOTAError(anyString(), anyInt(), anyString());
    }

    @Test
    public void onDfuProcessStartedTest() throws Exception {
        Field field = HBNordicDFUServiceHandler.class.getDeclaredField("mDFUProgressListener");
        field.setAccessible(true);
        DfuProgressListener mDFUProgressListener = (DfuProgressListener) field.get(handler);

        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        when(file.length()).thenReturn(80L);

        HBDevice device = mock(HBDevice.class);
        when(device.getAddress()).thenReturn("0.0.0.0");

        handler.addServiceListener("0.0.0.0", listener);

        handler.startDFU(device, file);

        mDFUProgressListener.onDfuProcessStarted(device.getAddress());

        verify(listener).onOTAProgressChanged(device.getAddress(), 0.0f, 80.0f);
    }

    @Test
    public void onProgressChangedTest() throws Exception {
        Field field = HBNordicDFUServiceHandler.class.getDeclaredField("mDFUProgressListener");
        field.setAccessible(true);
        DfuProgressListener mDFUProgressListener = (DfuProgressListener) field.get(handler);

        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        when(file.length()).thenReturn(80L);

        HBDevice device = mock(HBDevice.class);
        when(device.getAddress()).thenReturn("0.0.0.0");

        handler.addServiceListener("0.0.0.0", listener);

        handler.startDFU(device, file);

        mDFUProgressListener.onProgressChanged(device.getAddress(), 50, 0, 0, 1, 2);

        verify(listener).onOTAProgressChanged(device.getAddress(), 20.0f, 80.0f);
    }

    @Test
    public void onDFUCompletedTest() throws Exception {
        Field field = HBNordicDFUServiceHandler.class.getDeclaredField("mDFUProgressListener");
        field.setAccessible(true);
        DfuProgressListener mDFUProgressListener = (DfuProgressListener) field.get(handler);

        handler.addServiceListener("0.0.0.0", listener);

        HBDevice device = mock(HBDevice.class);
        when(device.getAddress()).thenReturn("0.0.0.0");
        mDFUProgressListener.onDfuCompleted(device.getAddress());

        verify(listener).onOTACompleted(device.getAddress());
    }

    @Test
    public void onDFUAbortedTest() throws Exception {
        Field field = HBNordicDFUServiceHandler.class.getDeclaredField("mDFUProgressListener");
        field.setAccessible(true);
        DfuProgressListener mDFUProgressListener = (DfuProgressListener) field.get(handler);

        handler.addServiceListener("0.0.0.0", listener);

        HBDevice device = mock(HBDevice.class);
        when(device.getAddress()).thenReturn("0.0.0.0");
        mDFUProgressListener.onDfuAborted(device.getAddress());

        verify(listener).onOTAAborted(device.getAddress());
    }

    @Test
    public void onErrorTest() throws Exception {
        Field field = HBNordicDFUServiceHandler.class.getDeclaredField("mDFUProgressListener");
        field.setAccessible(true);
        DfuProgressListener mDFUProgressListener = (DfuProgressListener) field.get(handler);

        handler.addServiceListener("0.0.0.0", listener);

        HBDevice device = mock(HBDevice.class);
        when(device.getAddress()).thenReturn("0.0.0.0");
        mDFUProgressListener.onError(device.getAddress(), 3, 4, "5");

        verify(listener).onOTAError(device.getAddress(), 3, "5");
    }
}
