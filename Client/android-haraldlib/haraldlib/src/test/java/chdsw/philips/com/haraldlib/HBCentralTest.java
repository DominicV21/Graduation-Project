/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Build;
import android.os.Handler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLooper;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import chdsw.philips.com.haraldlib.services.HBServiceHandler;
import chdsw.philips.com.haraldlib.services.batterylevel.HBBatteryListener;
import chdsw.philips.com.haraldlib.services.batterylevel.HBBatteryServiceHandler;
import chdsw.philips.com.haraldlib.services.currenttime.object.HBCurrentTime;
import chdsw.philips.com.haraldlib.services.currenttime.HBCurrentTimeListener;
import chdsw.philips.com.haraldlib.services.currenttime.HBCurrentTimeServiceHandler;
import chdsw.philips.com.haraldlib.services.currenttime.object.HBLocalTimeInformation;
import chdsw.philips.com.haraldlib.services.currenttime.object.HBSTDOffset;
import chdsw.philips.com.haraldlib.services.deviceinformation.HBDeviceInformationListener;
import chdsw.philips.com.haraldlib.services.deviceinformation.HBDeviceInformationServiceHandler;
import chdsw.philips.com.haraldlib.services.healththermometer.HBHealthThermometerListener;
import chdsw.philips.com.haraldlib.services.healththermometer.HBHealthThermometerServiceHandler;
import chdsw.philips.com.haraldlib.services.nordicdfu.HBNordicDFUListener;
import chdsw.philips.com.haraldlib.services.nordicdfu.HBNordicDFUServiceHandler;
import chdsw.philips.com.haraldlib.services.philipsextension.HBExtendedUserDataType;
import chdsw.philips.com.haraldlib.services.philipsextension.HBPhilipsExtensionListener;
import chdsw.philips.com.haraldlib.services.philipsextension.HBPhilipsExtensionServiceHandler;
import chdsw.philips.com.haraldlib.services.philipsextension.data.HBExtendedUserData;
import chdsw.philips.com.haraldlib.services.philipsextension.data.HBExtendedUserDataNumber;
import chdsw.philips.com.haraldlib.services.userdata.HBUserDataListener;
import chdsw.philips.com.haraldlib.services.userdata.HBUserDataServiceHandler;
import chdsw.philips.com.haraldlib.services.userdata.HBUserDataType;
import chdsw.philips.com.haraldlib.services.userdata.data.HBUserData;
import chdsw.philips.com.haraldlib.services.userdata.data.HBUserDataGender;
import chdsw.philips.com.haraldlib.services.userdata.data.HBUserDataNumber;
import chdsw.philips.com.haraldlib.services.userdata.object.HBGender;
import chdsw.philips.com.haraldlib.services.weightscale.HBWeightScaleListener;
import chdsw.philips.com.haraldlib.services.weightscale.HBWeightScaleServiceHandler;

import static android.os.Build.VERSION_CODES.KITKAT;
import static android.os.Build.VERSION_CODES.M;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = { M }, shadows={ShadowBluetoothLEAdapter.class} )
public class HBCentralTest {
    private HBCentral hbCentral;
    private ShadowBluetoothLEAdapter bluetoothAdapter;
    private ShadowApplication application;

    @Mock
    private BluetoothLeScanner scanner;

    @Mock
    private HBDeviceConnectionListener deviceConnectionListener;

    @Mock
    private Handler handler;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        application = ShadowApplication.getInstance();
        application.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
        application.grantPermissions(Manifest.permission.READ_EXTERNAL_STORAGE);

        bluetoothAdapter = Shadow.extract(ShadowBluetoothLEAdapter.getDefaultAdapter());
        bluetoothAdapter.setEnabled(true);
        bluetoothAdapter.setBluetoothLeScanner(scanner);

        Answer<Boolean> handlerPostAnswer = new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                Runnable runnable = invocation.getArgumentAt(0, Runnable.class);
                runnable.run();
                return true;
            }
        };
        doAnswer(handlerPostAnswer).when(handler).post(any(Runnable.class));

        hbCentral = new HBCentral(application.getApplicationContext(), handler);
        hbCentral.addDeviceConnectionListener("00:00:00:00", deviceConnectionListener);
    }

    //region Device Connection

    @Test
    @TargetApi(Build.VERSION_CODES.M)
    public void connectServiceTest() throws Exception {
        HBConnectionListener listener = mock(HBConnectionListener.class);
        hbCentral.connect(HBHealthThermometerServiceHandler.SERVICE_UUID, listener);
        verify(listener, never()).connectFailed(anyString(), any(UUID.class));
        verify(scanner).startScan(anyList(), any(ScanSettings.class), any(ScanCallback.class));
    }

    @Test
    @Config(sdk = {KITKAT})
    public void connectServiceOnOldAndroidTest() throws Exception {
        HBConnectionListener listener = mock(HBConnectionListener.class);
        hbCentral.connect(HBHealthThermometerServiceHandler.SERVICE_UUID, listener);
        verify(listener, never()).connectFailed(anyString(), any(UUID.class));
        assertEquals(bluetoothAdapter.getLeScanCallbacks().size(), 1);
    }

    @Test
    public void connectServiceWhenNoBLETest() throws Exception {
        HBConnectionListener listener = mock(HBConnectionListener.class);
        bluetoothAdapter.setEnabled(false);
        hbCentral.connect(HBHealthThermometerServiceHandler.SERVICE_UUID, listener);
        verify(listener).connectFailed(anyString(), any(UUID.class));
    }

    @Test
    public void connectServiceWhenNoPermissionTest() throws Exception {
        HBConnectionListener listener = mock(HBConnectionListener.class);
        application.denyPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
        hbCentral.connect(HBHealthThermometerServiceHandler.SERVICE_UUID, listener);
        verify(listener).connectFailed(anyString(), any(UUID.class));
    }

    @Test
    public void connectServiceWhenAlreadyScanningTest() throws Exception {
        HBConnectionListener listener = mock(HBConnectionListener.class);
        hbCentral.connect(HBHealthThermometerServiceHandler.SERVICE_UUID, listener);
        verify(listener, never()).connectFailed(anyString(), any(UUID.class));
        hbCentral.connect(HBHealthThermometerServiceHandler.SERVICE_UUID, listener);
        verify(listener).connectFailed(anyString(), any(UUID.class));
    }

    @Test
    public void connectServiceWhenScannerNullTest() throws Exception {
        HBConnectionListener listener = mock(HBConnectionListener.class);
        bluetoothAdapter.setBluetoothLeScanner(null);
        hbCentral.connect(HBHealthThermometerServiceHandler.SERVICE_UUID, listener);
        verify(listener).connectFailed(anyString(), any(UUID.class));
    }

    @Test
    public void connectServiceWithTimeoutTest() throws Exception {
        HBConnectionListener listener = mock(HBConnectionListener.class);

        hbCentral.connect(UUID.randomUUID(), listener);
        verify(listener, never()).connectFailed(anyString(), any(UUID.class));

        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(listener).connectFailed(anyString(), any(UUID.class));
    }

    @Test
    @TargetApi(Build.VERSION_CODES.M)
    public void connectDeviceTest() throws Exception {
        HBConnectionListener listener = mock(HBConnectionListener.class);
        hbCentral.connect("00:00:00:00", HBHealthThermometerServiceHandler.SERVICE_UUID, listener);
        verify(listener, never()).connectFailed(anyString(), any(UUID.class));
        verify(scanner).startScan(anyList(), any(ScanSettings.class), any(ScanCallback.class));
    }

    @Test
    @Config(sdk = {KITKAT})
    public void onLeScanFoundCorrectServiceTest() throws Exception {
        HBConnectionListener listener = mock(HBConnectionListener.class);
        when(listener.discoveredDeviceIsCorrect(anyString(), any(UUID.class), any(HBScanRecord.class))).thenReturn(true);

        hbCentral.connect(HBHealthThermometerServiceHandler.SERVICE_UUID, listener);
        verify(listener, never()).connectFailed(anyString(), any(UUID.class));

        BluetoothAdapter.LeScanCallback scanCallback = bluetoothAdapter.getLeScanCallbacks().iterator().next();

        BluetoothDevice device = mock(BluetoothDevice.class);
        scanCallback.onLeScan(device, 0, null);

        assertEquals(bluetoothAdapter.getLeScanCallbacks().size(), 0);
    }

    @Test
    @Config(sdk = {KITKAT})
    public void onLeScanFoundCorrectDeviceTest() throws Exception {
        HBConnectionListener listener = mock(HBConnectionListener.class);
        when(listener.discoveredDeviceIsCorrect(anyString(), any(UUID.class), any(HBScanRecord.class))).thenReturn(true);

        hbCentral.connect("00:00:00:00", HBHealthThermometerServiceHandler.SERVICE_UUID, listener);
        verify(listener, never()).connectFailed(anyString(), any(UUID.class));

        BluetoothAdapter.LeScanCallback scanCallback = bluetoothAdapter.getLeScanCallbacks().iterator().next();

        BluetoothDevice device = mock(BluetoothDevice.class);
        when(device.getAddress()).thenReturn("00:00:00:00");
        scanCallback.onLeScan(device, 0, null);

        assertEquals(bluetoothAdapter.getLeScanCallbacks().size(), 0);
    }

    @Test
    @Config(sdk = {KITKAT})
    public void onLEScanFoundIncorrectDeviceTest() throws Exception {
        HBConnectionListener listener = mock(HBConnectionListener.class);
        when(listener.discoveredDeviceIsCorrect(anyString(), any(UUID.class), any(HBScanRecord.class))).thenReturn(false);

        hbCentral.connect("00:00:00:00", HBHealthThermometerServiceHandler.SERVICE_UUID, listener);
        verify(listener, never()).connectFailed(anyString(), any(UUID.class));

        BluetoothAdapter.LeScanCallback scanCallback = bluetoothAdapter.getLeScanCallbacks().iterator().next();

        BluetoothDevice device = mock(BluetoothDevice.class);
        when(device.getAddress()).thenReturn("00:00:00:00");
        scanCallback.onLeScan(device, 0, null);

        assertEquals(bluetoothAdapter.getLeScanCallbacks().size(), 1);
    }

    @Test
    @Config(sdk = {KITKAT})
    public void onLEScanFoundIncorrectDeviceAddressTest() throws Exception {
        HBConnectionListener listener = mock(HBConnectionListener.class);
        when(listener.discoveredDeviceIsCorrect(anyString(), any(UUID.class), any(HBScanRecord.class))).thenReturn(true);

        hbCentral.connect("00:00:00:00", HBHealthThermometerServiceHandler.SERVICE_UUID, listener);
        verify(listener, never()).connectFailed(anyString(), any(UUID.class));

        BluetoothAdapter.LeScanCallback scanCallback = bluetoothAdapter.getLeScanCallbacks().iterator().next();

        BluetoothDevice device = mock(BluetoothDevice.class);
        when(device.getAddress()).thenReturn("00:00:00:01");
        scanCallback.onLeScan(device, 0, null);

        assertEquals(bluetoothAdapter.getLeScanCallbacks().size(), 1);
    }

    @Test
    @Config(sdk = {KITKAT})
    public void onLEScanFoundCorrectServiceAlreadyConnectTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());

        HBConnectionListener listener = mock(HBConnectionListener.class);
        when(listener.discoveredDeviceIsCorrect(anyString(), any(UUID.class), any(HBScanRecord.class))).thenReturn(true);

        hbCentral.connect(HBHealthThermometerServiceHandler.SERVICE_UUID, listener);
        verify(listener, never()).connectFailed(anyString(), any(UUID.class));

        BluetoothAdapter.LeScanCallback scanCallback = bluetoothAdapter.getLeScanCallbacks().iterator().next();

        BluetoothDevice device = mock(BluetoothDevice.class);
        when(device.getAddress()).thenReturn("00:00:00:00");
        scanCallback.onLeScan(device, 0, null);

        verify(listener).connected(anyString(), any(UUID.class));
    }

    @Test
    @TargetApi(Build.VERSION_CODES.M)
    public void onScanFoundCorrectServiceTest() throws Exception {
        HBConnectionListener listener = mock(HBConnectionListener.class);
        when(listener.discoveredDeviceIsCorrect(anyString(), any(UUID.class), any(HBScanRecord.class))).thenReturn(true);

        hbCentral.connect(HBHealthThermometerServiceHandler.SERVICE_UUID, listener);
        verify(listener, never()).connectFailed(anyString(), any(UUID.class));

        ArgumentCaptor<ScanCallback> callbackCaptor = ArgumentCaptor.forClass(ScanCallback.class);
        verify(scanner).startScan(anyList(), any(ScanSettings.class), callbackCaptor.capture());

        ScanResult scanResult = mock(ScanResult.class);
        ScanRecord scanRecord = mock(ScanRecord.class);
        BluetoothDevice device = mock(BluetoothDevice.class);
        when(scanResult.getDevice()).thenReturn(device);
        when(scanResult.getScanRecord()).thenReturn(scanRecord);

        List<ScanCallback> capturedMeasurements = callbackCaptor.getAllValues();
        capturedMeasurements.get(0).onScanResult(0, scanResult);

        verify(scanner).stopScan(any(ScanCallback.class));
    }

    @Test
    @TargetApi(Build.VERSION_CODES.M)
    public void onScanFoundCorrectDeviceTest() throws Exception {
        HBConnectionListener listener = mock(HBConnectionListener.class);
        when(listener.discoveredDeviceIsCorrect(anyString(), any(UUID.class), any(HBScanRecord.class))).thenReturn(true);

        hbCentral.connect("00:00:00:00", HBHealthThermometerServiceHandler.SERVICE_UUID, listener);
        verify(listener, never()).connectFailed(anyString(), any(UUID.class));

        ArgumentCaptor<ScanCallback> callbackCaptor = ArgumentCaptor.forClass(ScanCallback.class);
        verify(scanner).startScan(anyList(), any(ScanSettings.class), callbackCaptor.capture());

        ScanResult scanResult = mock(ScanResult.class);
        ScanRecord scanRecord = mock(ScanRecord.class);
        BluetoothDevice device = mock(BluetoothDevice.class);
        when(scanResult.getDevice()).thenReturn(device);
        when(scanResult.getScanRecord()).thenReturn(scanRecord);
        when(device.getAddress()).thenReturn("00:00:00:00");

        List<ScanCallback> capturedMeasurements = callbackCaptor.getAllValues();
        capturedMeasurements.get(0).onScanResult(0, scanResult);

        verify(scanner).stopScan(any(ScanCallback.class));
    }

    @Test
    @TargetApi(Build.VERSION_CODES.M)
    public void onScanFoundIncorrectDeviceTest() throws Exception {
        HBConnectionListener listener = mock(HBConnectionListener.class);
        when(listener.discoveredDeviceIsCorrect(anyString(), any(UUID.class), any(HBScanRecord.class))).thenReturn(false);

        hbCentral.connect("00:00:00:00", HBHealthThermometerServiceHandler.SERVICE_UUID, listener);
        verify(listener, never()).connectFailed(anyString(), any(UUID.class));

        ArgumentCaptor<ScanCallback> callbackCaptor = ArgumentCaptor.forClass(ScanCallback.class);
        verify(scanner).startScan(anyList(), any(ScanSettings.class), callbackCaptor.capture());

        ScanResult scanResult = mock(ScanResult.class);
        ScanRecord scanRecord = mock(ScanRecord.class);
        BluetoothDevice device = mock(BluetoothDevice.class);
        when(scanResult.getDevice()).thenReturn(device);
        when(scanResult.getScanRecord()).thenReturn(scanRecord);
        when(device.getAddress()).thenReturn("00:00:00:00");

        List<ScanCallback> capturedMeasurements = callbackCaptor.getAllValues();
        capturedMeasurements.get(0).onScanResult(0, scanResult);

        verify(scanner, never()).stopScan(any(ScanCallback.class));
    }

    @Test
    @TargetApi(Build.VERSION_CODES.M)
    public void onScanFoundIncorrectDeviceAddressTest() throws Exception {
        HBConnectionListener listener = mock(HBConnectionListener.class);
        when(listener.discoveredDeviceIsCorrect(anyString(), any(UUID.class), any(HBScanRecord.class))).thenReturn(true);

        hbCentral.connect("00:00:00:00", HBHealthThermometerServiceHandler.SERVICE_UUID, listener);
        verify(listener, never()).connectFailed(anyString(), any(UUID.class));

        ArgumentCaptor<ScanCallback> callbackCaptor = ArgumentCaptor.forClass(ScanCallback.class);
        verify(scanner).startScan(anyList(), any(ScanSettings.class), callbackCaptor.capture());

        ScanResult scanResult = mock(ScanResult.class);
        ScanRecord scanRecord = mock(ScanRecord.class);
        BluetoothDevice device = mock(BluetoothDevice.class);
        when(scanResult.getDevice()).thenReturn(device);
        when(scanResult.getScanRecord()).thenReturn(scanRecord);
        when(device.getAddress()).thenReturn("00:00:00:01");

        List<ScanCallback> capturedMeasurements = callbackCaptor.getAllValues();
        capturedMeasurements.get(0).onScanResult(0, scanResult);

        verify(scanner, never()).stopScan(any(ScanCallback.class));
    }

    @Test
    @TargetApi(Build.VERSION_CODES.M)
    public void onScanFoundCorrectServiceAlreadyConnectTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());

        HBConnectionListener listener = mock(HBConnectionListener.class);
        when(listener.discoveredDeviceIsCorrect(anyString(), any(UUID.class), any(HBScanRecord.class))).thenReturn(true);

        hbCentral.connect(HBHealthThermometerServiceHandler.SERVICE_UUID, listener);
        verify(listener, never()).connectFailed(anyString(), any(UUID.class));

        ArgumentCaptor<ScanCallback> callbackCaptor = ArgumentCaptor.forClass(ScanCallback.class);
        verify(scanner).startScan(anyList(), any(ScanSettings.class), callbackCaptor.capture());

        ScanResult scanResult = mock(ScanResult.class);
        ScanRecord scanRecord = mock(ScanRecord.class);
        BluetoothDevice device = mock(BluetoothDevice.class);
        when(device.getAddress()).thenReturn("00:00:00:00");
        when(scanResult.getDevice()).thenReturn(device);
        when(scanResult.getScanRecord()).thenReturn(scanRecord);

        List<ScanCallback> capturedMeasurements = callbackCaptor.getAllValues();
        capturedMeasurements.get(0).onScanResult(0, scanResult);

        verify(scanner).stopScan(any(ScanCallback.class));
        verify(listener).connected(anyString(), any(UUID.class));
    }

    @Test
    public void onConnectedTest() throws Exception {
        Field field = HBCentral.class.getDeclaredField("deviceListener");
        field.setAccessible(true);
        HBDeviceListener hbDeviceListener = (HBDeviceListener) field.get(hbCentral);

        HBConnectionListenerHandler listener = mock(HBConnectionListenerHandler.class);
        Field listenerField = HBCentral.class.getDeclaredField("connectionListenerHandler");
        listenerField.setAccessible(true);
        listenerField.set(hbCentral, listener);

        HBDevice device = mock(HBDevice.class);
        when(device.getAddress()).thenReturn("00:00:00:00");
        when(device.getPrimaryService()).thenReturn(HBNordicDFUServiceHandler.SERVICE_UUID);
        hbDeviceListener.connected(device);

        verify(listener).connected(device.getAddress(), device.getPrimaryService());
    }

    @Test
    public void onConnectFailedTest() throws Exception {
        Field field = HBCentral.class.getDeclaredField("deviceListener");
        field.setAccessible(true);
        HBDeviceListener hbDeviceListener = (HBDeviceListener) field.get(hbCentral);

        HBConnectionListenerHandler listener = mock(HBConnectionListenerHandler.class);
        Field listenerField = HBCentral.class.getDeclaredField("connectionListenerHandler");
        listenerField.setAccessible(true);
        listenerField.set(hbCentral, listener);

        HBDevice device = mock(HBDevice.class);
        when(device.getAddress()).thenReturn("00:00:00:00");
        when(device.getPrimaryService()).thenReturn(HBNordicDFUServiceHandler.SERVICE_UUID);
        hbDeviceListener.connectFailed(device);

        verify(listener).connectFailed(device.getAddress(), device.getPrimaryService());
    }

    @Test
    public void onDisconnectedTest() throws Exception {
        Field field = HBCentral.class.getDeclaredField("deviceListener");
        field.setAccessible(true);
        HBDeviceListener hbDeviceListener = (HBDeviceListener) field.get(hbCentral);

        HBDeviceConnectionListener listener = mock(HBDeviceConnectionListener.class);
        hbCentral.addDeviceConnectionListener("0.0.0.0", listener);

        HBDevice device = mock(HBDevice.class);
        when(device.getAddress()).thenReturn("0.0.0.0");
        hbDeviceListener.disconnected(device);

        verify(listener).disconnected(device.getAddress());
    }

    @Test
    public void onReadWriteErrorTest() throws Exception {
        Field field = HBCentral.class.getDeclaredField("deviceListener");
        field.setAccessible(true);
        HBDeviceListener hbDeviceListener = (HBDeviceListener) field.get(hbCentral);

        HBDeviceConnectionListener listener = mock(HBDeviceConnectionListener.class);
        hbCentral.addDeviceConnectionListener("0.0.0.0", listener);

        HBDevice device = mock(HBDevice.class);
        when(device.getAddress()).thenReturn("0.0.0.0");
        hbDeviceListener.onReadWriteError(device);

        verify(listener).onReadWriteError(device.getAddress());
    }

    @Test
    public void disconnectedTest() throws Exception {
        HBDevice device = addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.disconnect("00:00:00:00");
        verify(device).disconnect();
    }

    @Test
    public void disconnectedInvalidAddressTest() throws Exception {
        HBDevice device = addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.disconnect("00:00:00:01");
        verify(device, never()).disconnect();
    }

    @Test
    public void getSupportedServicesTest() throws Exception {
        assertEquals(hbCentral.getSupportedServices().size(), 9);
    }

    @Test
    public void getDeviceSupportedServicesTest() throws Exception {
        List<UUID> services = new ArrayList<>();
        services.add(UUID.randomUUID());
        addConnectedDevice(true, services);
        assertEquals(hbCentral.getDeviceSupportedServices("00:00:00:00").size(), 1);
    }

    @Test
    public void supportedServicesInvalidAddressTest() throws Exception {
        Field field = HBCentral.class.getDeclaredField("deviceListener");
        field.setAccessible(true);
        HBDeviceListener hbDeviceListener = (HBDeviceListener) field.get(hbCentral);

        HBDevice device = mock(HBDevice.class);
        when(device.getAddress()).thenReturn("00:00:00:00");
        List<UUID> services = new ArrayList<>();
        services.add(UUID.randomUUID());
        when(device.getServices()).thenReturn(services);
        hbDeviceListener.connected(device);

        assertEquals(hbCentral.getDeviceSupportedServices("00:00:00:01").size(), 0);
    }

    @Test
    public void getStateConnectedTest() throws Exception {
        HBDevice device = addConnectedDevice(true, new ArrayList<UUID>());
        when(device.getState()).thenReturn(HBDevice.State.Connected);
        assertEquals(hbCentral.getState("00:00:00:00"), HBDevice.State.Connected);
    }

    @Test
    public void getStateDisconnectedTest() throws Exception {
        assertEquals(hbCentral.getState("00:00:00:00"), HBDevice.State.Disconnected);
    }

    //endregion

    //region Health Thermometer Service

    @Test
    public void addHealthThermometerServiceListenerTest() throws Exception {
        HBHealthThermometerListener listener = mock(HBHealthThermometerListener.class);
        hbCentral.addHealthThermometerServiceListener("00:00:00:00", listener);

        HBServiceHandler handler = getService(HBHealthThermometerServiceHandler.SERVICE_UUID);
        assertTrue(handler.getServiceListener("00:00:00:00").equalsListener(listener));
    }

    @Test
    public void removeHealthThermometerServiceListenerTest() throws Exception {
        addHealthThermometerServiceListenerTest();
        hbCentral.removeHealthThermometerServiceListener("00:00:00:00");

        HBServiceHandler handler = getService(HBHealthThermometerServiceHandler.SERVICE_UUID);
        assertTrue(handler.getServiceListener("00:00:00:00") == null);
    }

    @Test
    public void getTemperatureTypeTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.getTemperatureType("00:00:00:00");
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getTemperatureTypeUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        hbCentral.getTemperatureType("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getTemperatureTypeNoDeviceTest() throws Exception {
        hbCentral.getTemperatureType("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getMeasurementIntervalTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.getMeasurementInterval("00:00:00:00");
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getMeasurementIntervalUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        hbCentral.getMeasurementInterval("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getMeasurementIntervalNoDeviceTest() throws Exception {
        hbCentral.getMeasurementInterval("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void setMeasurementIntervalTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.setMeasurementInterval("00:00:00:00", 0);
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void setMeasurementIntervalUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        hbCentral.setMeasurementInterval("00:00:00:00", 0);
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void setMeasurementIntervalNoDeviceTest() throws Exception {
        hbCentral.setMeasurementInterval("00:00:00:00", 0);
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    //endregion

    //region Current Time Service

    @Test
    public void addCurrentTimeServiceListenerTest() throws Exception {
        HBCurrentTimeListener listener = mock(HBCurrentTimeListener.class);
        hbCentral.addCurrentTimeServiceListener("00:00:00:00", listener);

        HBServiceHandler handler = getService(HBCurrentTimeServiceHandler.SERVICE_UUID);
        assertTrue(handler.getServiceListener("00:00:00:00").equalsListener(listener));
    }

    @Test
    public void removeCurrentTimeServiceListenerTest() throws Exception {
        addCurrentTimeServiceListenerTest();
        hbCentral.removeCurrentTimeServiceListener("00:00:00:00");

        HBServiceHandler handler = getService(HBCurrentTimeServiceHandler.SERVICE_UUID);
        assertTrue(handler.getServiceListener("00:00:00:00") == null);
    }

    @Test
    public void getCurrentTimeTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.getCurrentTime("00:00:00:00");
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getCurrentTimeUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        hbCentral.getCurrentTime("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getCurrentTimeNoDeviceTest() throws Exception {
        hbCentral.getCurrentTime("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void setCurrentTimeTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        HBCurrentTime currentTime = new HBCurrentTime();
        currentTime.setDateTime(new Date());
        hbCentral.setCurrentTime("00:00:00:00", currentTime);
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void setCurrentTimeUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        HBCurrentTime currentTime = new HBCurrentTime();
        currentTime.setDateTime(new Date());
        hbCentral.setCurrentTime("00:00:00:00", currentTime);
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void setCurrentTimeNoDeviceTest() throws Exception {
        hbCentral.setCurrentTime("00:00:00:00", new HBCurrentTime());
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getLocalTimeInformationTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.getLocalTimeInformation("00:00:00:00");
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getLocalTimeInformationUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        hbCentral.getLocalTimeInformation("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getLocalTimeInformationNoDeviceTest() throws Exception {
        hbCentral.getLocalTimeInformation("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void setLocalTimeInformationTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        HBLocalTimeInformation localTimeInformation = new HBLocalTimeInformation();
        localTimeInformation.setSTDOffset(HBSTDOffset.DaylightTime);
        hbCentral.setLocalTimeInformation("00:00:00:00", localTimeInformation);
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void setLocalTimeInformationUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        HBLocalTimeInformation localTimeInformation = new HBLocalTimeInformation();
        localTimeInformation.setSTDOffset(HBSTDOffset.DaylightTime);
        hbCentral.setLocalTimeInformation("00:00:00:00", localTimeInformation);
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void setLocalTimeInformationNoDeviceTest() throws Exception {
        hbCentral.setLocalTimeInformation("00:00:00:00", new HBLocalTimeInformation());
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getReferenceTimeInformationTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.getReferenceTimeInformation("00:00:00:00");
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getReferenceTimeInformationUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        hbCentral.getReferenceTimeInformation("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getReferenceTimeInformationNoDeviceTest() throws Exception {
        hbCentral.getReferenceTimeInformation("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    //endregion

    //region Nordic DFU Service

    @Test
    public void addNordicDFUServiceListenerTest() throws Exception {
        HBNordicDFUListener listener = mock(HBNordicDFUListener.class);
        hbCentral.addNordicDFUServiceListener("00:00:00:00", listener);

        HBServiceHandler handler = getService(HBNordicDFUServiceHandler.SERVICE_UUID);
        assertTrue(handler.getServiceListener("00:00:00:00").equalsListener(listener));
    }

    @Test
    public void removeNordicDFUServiceListenerTest() throws Exception {
        addNordicDFUServiceListenerTest();
        hbCentral.removeNordicDFUServiceListener("00:00:00:00");

        HBServiceHandler handler = getService(HBNordicDFUServiceHandler.SERVICE_UUID);
        assertTrue(handler.getServiceListener("00:00:00:00") == null);
    }

    @Test
    public void startNordicDFUTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        hbCentral.startNordicDFU("00:00:00:00", file);
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void startNordicDFUUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        hbCentral.startNordicDFU("00:00:00:00", file);
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void startNordicDFUNoDeviceTest() throws Exception {
        hbCentral.startNordicDFU("00:00:00:00", new File(""));
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void startNordicDFUNoPermissionTest() throws Exception {
        Field field = HBCentral.class.getDeclaredField("deviceListener");
        field.setAccessible(true);
        HBDeviceListener hbDeviceListener = (HBDeviceListener) field.get(hbCentral);
        application.denyPermissions(Manifest.permission.READ_EXTERNAL_STORAGE);

        HBDevice device = mock(HBDevice.class);
        when(device.getAddress()).thenReturn("00:00:00:00");
        List<UUID> services = new ArrayList<>();
        services.add(HBNordicDFUServiceHandler.SERVICE_UUID);
        when(device.getServices()).thenReturn(services);
        hbDeviceListener.connected(device);

        hbCentral.startNordicDFU("00:00:00:00", new File(""));
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    //endregion

    //region Battery Level Service

    @Test
    public void addBatteryServiceListenerTest() throws Exception {
        HBBatteryListener listener = mock(HBBatteryListener.class);
        hbCentral.addBatteryServiceListener("00:00:00:00", listener);

        HBServiceHandler handler = getService(HBBatteryServiceHandler.SERVICE_UUID);
        assertTrue(handler.getServiceListener("00:00:00:00").equalsListener(listener));
    }

    @Test
    public void removeBatteryServiceListenerTest() throws Exception {
        addBatteryServiceListenerTest();
        hbCentral.removeBatteryServiceListener("00:00:00:00");

        HBServiceHandler handler = getService(HBBatteryServiceHandler.SERVICE_UUID);
        assertTrue(handler.getServiceListener("00:00:00:00") == null);
    }

    @Test
    public void getBatteryLevelTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.getBatteryLevel("00:00:00:00");
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getBatteryLevelUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        hbCentral.getBatteryLevel("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getBatteryLevelNoDeviceTest() throws Exception {
        hbCentral.getBatteryLevel("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    //endregion

    //region Device Information Service

    @Test
    public void addDeviceInformationServiceListenerTest() throws Exception {
        HBDeviceInformationListener listener = mock(HBDeviceInformationListener.class);
        hbCentral.addDeviceInformationServiceListener("00:00:00:00", listener);

        HBServiceHandler handler = getService(HBDeviceInformationServiceHandler.SERVICE_UUID);
        assertTrue(handler.getServiceListener("00:00:00:00").equalsListener(listener));
    }

    @Test
    public void removeDeviceInformationServiceListenerTest() throws Exception {
        addDeviceInformationServiceListenerTest();
        hbCentral.removeDeviceInformationServiceListener("00:00:00:00");

        HBServiceHandler handler = getService(HBDeviceInformationServiceHandler.SERVICE_UUID);
        assertTrue(handler.getServiceListener("00:00:00:00") == null);
    }

    @Test
    public void getManufacturerNameTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.getManufacturerName("00:00:00:00");
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getManufacturerNameUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        hbCentral.getManufacturerName("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getManufacturerNameNoDeviceTest() throws Exception {
        hbCentral.getManufacturerName("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getModelNumberTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.getModelNumber("00:00:00:00");
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getModelNumberUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        hbCentral.getModelNumber("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getModelNumberNoDeviceTest() throws Exception {
        hbCentral.getModelNumber("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getSerialNumberTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.getSerialNumber("00:00:00:00");
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getSerialNumberUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        hbCentral.getSerialNumber("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getSerialNumberNoDeviceTest() throws Exception {
        hbCentral.getSerialNumber("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getHardwareRevisionTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.getHardwareRevision("00:00:00:00");
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getHardwareRevisionUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        hbCentral.getHardwareRevision("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getHardwareRevisionNoDeviceTest() throws Exception {
        hbCentral.getHardwareRevision("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getFirmwareRevisionTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.getFirmwareRevision("00:00:00:00");
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getFirmwareRevisionUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        hbCentral.getFirmwareRevision("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getFirmwareRevisionNoDeviceTest() throws Exception {
        hbCentral.getFirmwareRevision("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getSoftwareRevisionTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.getSoftwareRevision("00:00:00:00");
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getSoftwareRevisionUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        hbCentral.getSoftwareRevision("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getSoftwareRevisionNoDeviceTest() throws Exception {
        hbCentral.getSoftwareRevision("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getSystemIDTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.getSystemID("00:00:00:00");
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getSystemIDUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        hbCentral.getSystemID("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getSystemIDNoDeviceTest() throws Exception {
        hbCentral.getSystemID("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getRegulatoryCertificationDataListTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.getRegulatoryCertificationDataList("00:00:00:00");
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getRegulatoryCertificationDataListUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        hbCentral.getRegulatoryCertificationDataList("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getRegulatoryCertificationDataListNoDeviceTest() throws Exception {
        hbCentral.getRegulatoryCertificationDataList("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getPnPIDTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.getPnPID("00:00:00:00");
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getPnPIDUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        hbCentral.getPnPID("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getPnPIDNoDeviceTest() throws Exception {
        hbCentral.getPnPID("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    //endregion

    //region Weight Scale Service

    @Test
    public void addWeightScaleServiceListenerTest() throws Exception {
        HBWeightScaleListener listener = mock(HBWeightScaleListener.class);
        hbCentral.addWeightScaleServiceListener("00:00:00:00", listener);

        HBServiceHandler handler = getService(HBWeightScaleServiceHandler.SERVICE_UUID);
        assertTrue(handler.getServiceListener("00:00:00:00").equalsListener(listener));
    }

    @Test
    public void removeWeightScaleServiceListenerTest() throws Exception {
        addWeightScaleServiceListenerTest();
        hbCentral.removeWeightScaleServiceListener("00:00:00:00");

        HBServiceHandler handler = getService(HBWeightScaleServiceHandler.SERVICE_UUID);
        assertTrue(handler.getServiceListener("00:00:00:00") == null);
    }

    @Test
    public void getWeightScaleFeatureTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.getWeightScaleFeature("00:00:00:00");
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getWeightScaleFeatureUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        hbCentral.getWeightScaleFeature("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getWeightScaleFeatureNoDeviceTest() throws Exception {
        hbCentral.getWeightScaleFeature("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    //endregion

    //region User Data Service

    @Test
    public void addUserDataServiceListenerTest() throws Exception {
        HBUserDataListener listener = mock(HBUserDataListener.class);
        hbCentral.addUserDataServiceListener("00:00:00:00", listener);

        HBServiceHandler handler = getService(HBUserDataServiceHandler.SERVICE_UUID);
        assertTrue(handler.getServiceListener("00:00:00:00").equalsListener(listener));
    }

    @Test
    public void removeUserDataServiceListenerTest() throws Exception {
        addUserDataServiceListenerTest();
        hbCentral.removeUserDataServiceListener("00:00:00:00");

        HBServiceHandler handler = getService(HBUserDataServiceHandler.SERVICE_UUID);
        assertTrue(handler.getServiceListener("00:00:00:00") == null);
    }

    @Test
    public void registerUserTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.registerUser("00:00:00:00", 0001);
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void registerUserUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        hbCentral.registerUser("00:00:00:00", 0001);
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void registerUserNoDeviceTest() throws Exception {
        hbCentral.registerUser("00:00:00:00", 0001);
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void setUserTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.setUser("00:00:00:00", 1, 0001);
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void setUserUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        hbCentral.setUser("00:00:00:00", 1, 0001);
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void setUserNoDeviceTest() throws Exception {
        hbCentral.setUser("00:00:00:00", 1, 0001);
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void deleteCurrentUserTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.deleteCurrentUser("00:00:00:00");
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void deleteCurrentUserUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        hbCentral.deleteCurrentUser("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void deleteCurrentUserNoDeviceTest() throws Exception {
        hbCentral.deleteCurrentUser("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getUserDataTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.getUserData("00:00:00:00", HBUserDataType.GENDER);
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getUserDataUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        hbCentral.getUserData("00:00:00:00", HBUserDataType.GENDER);
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getUserDataNoDeviceTest() throws Exception {
        hbCentral.getUserData("00:00:00:00", HBUserDataType.GENDER);
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void setUserDataTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.setUserData("00:00:00:00", new HBUserDataGender(HBGender.Male));
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void setUserDataUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        hbCentral.setUserData("00:00:00:00", new HBUserDataGender(HBGender.Male));
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void setUserDataNoDeviceTest() throws Exception {
        hbCentral.setUserData("00:00:00:00", new HBUserDataGender(HBGender.Male));
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    //endregion

    //region Philips Extension Service

    @Test
    public void addPhilipsExtensionListenerTest() throws Exception {
        HBPhilipsExtensionListener listener = mock(HBPhilipsExtensionListener.class);
        hbCentral.addPhilipsExtensionListener("00:00:00:00", listener);

        HBServiceHandler handler = getService(HBPhilipsExtensionServiceHandler.SERVICE_UUID);
        assertTrue(handler.getServiceListener("00:00:00:00").equalsListener(listener));
    }

    @Test
    public void removePhilipsExtensionListenerTest() throws Exception {
        addPhilipsExtensionListenerTest();
        hbCentral.removePhilipsExtensionListener("00:00:00:00");

        HBServiceHandler handler = getService(HBPhilipsExtensionServiceHandler.SERVICE_UUID);
        assertTrue(handler.getServiceListener("00:00:00:00") == null);
    }

    @Test
    public void getAllUsersTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.getAllUsers("00:00:00:00");
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getAllUsersUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        hbCentral.getAllUsers("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getAllUsersNoDeviceTest() throws Exception {
        hbCentral.getAllUsers("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void deleteUserTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.deleteUser("00:00:00:00", 1);
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void deleteUserUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        hbCentral.deleteUser("00:00:00:00", 1);
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void deleteUserNoDeviceTest() throws Exception {
        hbCentral.deleteUser("00:00:00:00", 1);
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void deleteAllUsersTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.deleteAllUsers("00:00:00:00");
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void deleteAllUsersUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        hbCentral.deleteAllUsers("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void deleteAllUsersNoDeviceTest() throws Exception {
        hbCentral.deleteAllUsers("00:00:00:00");
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getExtendedUserDataTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.getExtendedUserData("00:00:00:00", HBExtendedUserDataType.WEIGHT_GOAL);
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getExtendedUserDataUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        hbCentral.getExtendedUserData("00:00:00:00", HBExtendedUserDataType.WEIGHT_GOAL);
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void getExtendedUserDataNoDeviceTest() throws Exception {
        hbCentral.getExtendedUserData("00:00:00:00", HBExtendedUserDataType.WEIGHT_GOAL);
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void setExtendedUserDataTest() throws Exception {
        addConnectedDevice(true, new ArrayList<UUID>());
        hbCentral.setExtendedUserData("00:00:00:00", new HBExtendedUserDataNumber(HBExtendedUserDataType.WEIGHT_GOAL, 2));
        verify(deviceConnectionListener, never()).onReadWriteError("00:00:00:00");
    }

    @Test
    public void setExtendedUserDataUnsupportedServiceTest() throws Exception {
        addConnectedDevice(false, new ArrayList<UUID>());
        hbCentral.setExtendedUserData("00:00:00:00", new HBExtendedUserDataNumber(HBExtendedUserDataType.WEIGHT_GOAL, 2));
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    @Test
    public void setExtendedUserDataNoDeviceTest() throws Exception {
        hbCentral.setExtendedUserData("00:00:00:00", new HBExtendedUserDataNumber(HBExtendedUserDataType.WEIGHT_GOAL, 2));
        verify(deviceConnectionListener).onReadWriteError("00:00:00:00");
    }

    //endregion

    private HBDevice addConnectedDevice(boolean serviceSupported, List<UUID> services) throws NoSuchFieldException, IllegalAccessException {
        Field field = HBCentral.class.getDeclaredField("connectedHbDevices");
        field.setAccessible(true);
        Map<String, HBDevice> connectedHbDevices = (Map<String, HBDevice>) field.get(hbCentral);

        HBDevice device = mock(HBDevice.class);
        when(device.getAddress()).thenReturn("00:00:00:00");
        when(device.supportsService(any(UUID.class))).thenReturn(serviceSupported);
        when(device.getServices()).thenReturn(services);
        connectedHbDevices.put(device.getAddress(), device);

        return device;
    }

    private HBServiceHandler getService(UUID serviceUUID) throws NoSuchFieldException, IllegalAccessException {
        Field field = HBCentral.class.getDeclaredField("supportedServices");
        field.setAccessible(true);
        Map<UUID, HBServiceHandler> supportedServices = (Map<UUID, HBServiceHandler>) field.get(hbCentral);
        return supportedServices.get(serviceUUID);
    }

}