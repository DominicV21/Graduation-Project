/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import chdsw.philips.com.haraldlib.services.HBServiceHandler;
import chdsw.philips.com.haraldlib.services.healththermometer.HBHealthThermometerServiceHandler;

import static android.bluetooth.BluetoothDevice.BOND_BONDED;
import static android.bluetooth.BluetoothGatt.GATT_FAILURE;
import static android.bluetooth.BluetoothGatt.GATT_SUCCESS;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_INDICATE;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_NOTIFY;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_READ;
import static android.os.Build.VERSION_CODES.M;
import static junit.framework.Assert.assertFalse;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = { M })
public class HBDeviceTest {
    private HBDevice hBDevice;
    private Map<UUID, HBServiceHandler> supportedServices;

    @Mock
    private Context context;

    @Mock
    private BluetoothDevice device;

    @Mock
    private HBDeviceListener listener;

    @Mock
    private BluetoothGatt gatt;

    @Mock
    private HBHealthThermometerServiceHandler serviceHandler;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        when(device.connectGatt(any(Context.class), anyBoolean(), any(BluetoothGattCallback.class))).thenReturn(gatt);

        supportedServices = new HashMap();
        supportedServices.put(HBHealthThermometerServiceHandler.SERVICE_UUID, serviceHandler);

        hBDevice = new HBDevice(context, device, HBHealthThermometerServiceHandler.SERVICE_UUID, supportedServices, listener);
    }

    @Test
    public void connectTest() throws Exception {
        hBDevice.connect(true);

        verify(device).connectGatt(any(Context.class), anyBoolean(), any(BluetoothGattCallback.class));
    }

    @Test
    public void disconnectTest() throws Exception {
        hBDevice.connect(true);

        hBDevice.disconnect();

        verify(gatt).disconnect();
    }

    @Test
    public void disconnectNullTest() throws Exception {
        hBDevice.disconnect();

        verify(gatt, never()).disconnect();
    }

    @Test
    public void getAddressTest() throws Exception {
        when(device.getAddress()).thenReturn("0.0.0.0");

        assertEquals(hBDevice.getAddress(), "0.0.0.0");
    }

    @Test
    public void getServicesTest() throws Exception {
        BluetoothGattCallback callback = connectAndGetCallback();

        BluetoothGattService service = mock(BluetoothGattService.class);
        when(service.getUuid()).thenReturn(HBHealthThermometerServiceHandler.SERVICE_UUID);
        when(gatt.getServices()).thenReturn(Arrays.asList(service));

        callback.onServicesDiscovered(gatt, 0);

        List<UUID> expected = Arrays.asList(HBHealthThermometerServiceHandler.SERVICE_UUID);

        assertThat(hBDevice.getServices(), is(expected));
    }

    @Test
    public void supportsServiceTest() throws Exception {
        BluetoothGattCallback callback = connectAndGetCallback();

        BluetoothGattService service = mock(BluetoothGattService.class);
        when(service.getUuid()).thenReturn(HBHealthThermometerServiceHandler.SERVICE_UUID);
        when(gatt.getServices()).thenReturn(Arrays.asList(service));

        callback.onServicesDiscovered(gatt, 0);

        assertTrue(hBDevice.supportsService(HBHealthThermometerServiceHandler.SERVICE_UUID));
    }

    @Test
    public void supportsServiceUnsupportedTest() throws Exception {
        BluetoothGattCallback callback = connectAndGetCallback();

        BluetoothGattService service = mock(BluetoothGattService.class);
        when(gatt.getServices()).thenReturn(Arrays.asList(service));

        callback.onServicesDiscovered(gatt, 0);

        assertFalse(hBDevice.supportsService(HBHealthThermometerServiceHandler.SERVICE_UUID));
    }

    @Test
    public void readCharacteristicTest() throws Exception {
        hBDevice.connect(false);

        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        BluetoothGattService service = mock(BluetoothGattService.class);

        when(gatt.getService(HBHealthThermometerServiceHandler.SERVICE_UUID)).thenReturn(service);
        when(service.getCharacteristic(UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"))).thenReturn(characteristic);
        when(gatt.readCharacteristic(characteristic)).thenReturn(true);
        when(characteristic.getProperties()).thenReturn(BluetoothGattCharacteristic.PROPERTY_READ);

        HBReadCommand command = new HBReadCommand(HBHealthThermometerServiceHandler.SERVICE_UUID,
                UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"));
        hBDevice.readCharacteristic(command);

        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(gatt).readCharacteristic(any(BluetoothGattCharacteristic.class));
        verify(listener, never()).onReadWriteError(any(HBDevice.class));
    }

    @Test
    public void readUnknownCharacteristicTest() throws Exception {
        hBDevice.connect(false);

        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        BluetoothGattService service = mock(BluetoothGattService.class);

        when(gatt.getService(HBHealthThermometerServiceHandler.SERVICE_UUID)).thenReturn(service);
        when(service.getCharacteristic(UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"))).thenReturn(characteristic);
        when(gatt.readCharacteristic(characteristic)).thenReturn(true);
        when(characteristic.getProperties()).thenReturn(BluetoothGattCharacteristic.PROPERTY_READ);

        HBReadCommand command = new HBReadCommand(HBHealthThermometerServiceHandler.SERVICE_UUID,
                UUID.fromString("00002A1C-0000-1000-8000-000123456789"));
        hBDevice.readCharacteristic(command);

        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(gatt, never()).readCharacteristic(any(BluetoothGattCharacteristic.class));
        verify(listener).onReadWriteError(any(HBDevice.class));
    }

    @Test
    public void readCharacteristicUnknownServiceTest() throws Exception {
        hBDevice.connect(false);

        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        BluetoothGattService service = mock(BluetoothGattService.class);

        when(gatt.getService(HBHealthThermometerServiceHandler.SERVICE_UUID)).thenReturn(service);
        when(service.getCharacteristic(UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"))).thenReturn(characteristic);
        when(gatt.readCharacteristic(characteristic)).thenReturn(true);

        HBReadCommand command = new HBReadCommand(UUID.fromString("00001809-0000-1000-8000-000123456789"),
                UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"));
        hBDevice.readCharacteristic(command);

        verify(gatt, never()).readCharacteristic(any(BluetoothGattCharacteristic.class));
        verify(listener).onReadWriteError(any(HBDevice.class));
    }

    @Test
    public void readCharacteristicNotConnectedTest() throws Exception {
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        BluetoothGattService service = mock(BluetoothGattService.class);

        when(gatt.getService(HBHealthThermometerServiceHandler.SERVICE_UUID)).thenReturn(service);
        when(service.getCharacteristic(UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"))).thenReturn(characteristic);
        when(gatt.readCharacteristic(characteristic)).thenReturn(true);

        HBReadCommand command = new HBReadCommand(HBHealthThermometerServiceHandler.SERVICE_UUID,
                UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"));
        hBDevice.readCharacteristic(command);

        verify(gatt, never()).readCharacteristic(any(BluetoothGattCharacteristic.class));
        verify(listener).onReadWriteError(any(HBDevice.class));
    }

    @Test
    public void readCharacteristicNoReadPropertyTest() throws Exception {
        hBDevice.connect(false);

        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        BluetoothGattService service = mock(BluetoothGattService.class);

        when(gatt.getService(HBHealthThermometerServiceHandler.SERVICE_UUID)).thenReturn(service);
        when(service.getCharacteristic(UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"))).thenReturn(characteristic);
        when(gatt.readCharacteristic(characteristic)).thenReturn(true);
        when(characteristic.getProperties()).thenReturn(0);

        HBReadCommand command = new HBReadCommand(HBHealthThermometerServiceHandler.SERVICE_UUID,
                UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"));
        hBDevice.readCharacteristic(command);

        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(gatt, never()).readCharacteristic(any(BluetoothGattCharacteristic.class));
        verify(listener).onReadWriteError(any(HBDevice.class));
    }

    @Test
    public void readCharacteristicFalseTest() throws Exception {
        hBDevice.connect(false);

        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        BluetoothGattService service = mock(BluetoothGattService.class);

        when(gatt.getService(HBHealthThermometerServiceHandler.SERVICE_UUID)).thenReturn(service);
        when(service.getCharacteristic(UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"))).thenReturn(characteristic);
        when(gatt.readCharacteristic(characteristic)).thenReturn(false);
        when(characteristic.getProperties()).thenReturn(BluetoothGattCharacteristic.PROPERTY_READ);

        HBReadCommand command = new HBReadCommand(HBHealthThermometerServiceHandler.SERVICE_UUID,
                UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"));
        hBDevice.readCharacteristic(command);

        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(gatt).readCharacteristic(any(BluetoothGattCharacteristic.class));
        verify(listener).onReadWriteError(any(HBDevice.class));
    }

    @Test
    public void writeCharacteristicTest() throws Exception {
        hBDevice.connect(false);

        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        BluetoothGattService service = mock(BluetoothGattService.class);

        when(gatt.getService(HBHealthThermometerServiceHandler.SERVICE_UUID)).thenReturn(service);
        when(service.getCharacteristic(UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"))).thenReturn(characteristic);
        when(gatt.writeCharacteristic(characteristic)).thenReturn(true);
        when(characteristic.getProperties()).thenReturn(BluetoothGattCharacteristic.PROPERTY_WRITE);

        HBWriteCommand command = new HBWriteCommand(HBHealthThermometerServiceHandler.SERVICE_UUID,
                UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"), new byte[] { 0, 0 });
        hBDevice.writeCharacteristic(command);

        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(gatt).writeCharacteristic(any(BluetoothGattCharacteristic.class));
        verify(listener, never()).onReadWriteError(any(HBDevice.class));
    }

    @Test
    public void writeUnknownCharacteristicTest() throws Exception {
        hBDevice.connect(false);

        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        BluetoothGattService service = mock(BluetoothGattService.class);

        when(gatt.getService(HBHealthThermometerServiceHandler.SERVICE_UUID)).thenReturn(service);
        when(service.getCharacteristic(UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"))).thenReturn(characteristic);
        when(gatt.writeCharacteristic(characteristic)).thenReturn(true);
        when(characteristic.getProperties()).thenReturn(BluetoothGattCharacteristic.PROPERTY_WRITE);

        HBWriteCommand command = new HBWriteCommand(HBHealthThermometerServiceHandler.SERVICE_UUID,
                UUID.fromString("00002A1C-0000-1000-8000-000123456789"), new byte[] { 0, 0 });
        hBDevice.writeCharacteristic(command);

        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(gatt, never()).writeCharacteristic(any(BluetoothGattCharacteristic.class));
        verify(listener).onReadWriteError(any(HBDevice.class));
    }

    @Test
    public void writeCharacteristicUnknownServiceTest() throws Exception {
        hBDevice.connect(false);

        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        BluetoothGattService service = mock(BluetoothGattService.class);

        when(gatt.getService(HBHealthThermometerServiceHandler.SERVICE_UUID)).thenReturn(service);
        when(service.getCharacteristic(UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"))).thenReturn(characteristic);
        when(gatt.writeCharacteristic(characteristic)).thenReturn(true);
        when(characteristic.getProperties()).thenReturn(BluetoothGattCharacteristic.PROPERTY_WRITE);

        HBWriteCommand command = new HBWriteCommand(UUID.fromString("00001809-0000-1000-8000-000123456789"),
                UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"), new byte[] { 0, 0 });
        hBDevice.writeCharacteristic(command);

        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(gatt, never()).writeCharacteristic(any(BluetoothGattCharacteristic.class));
        verify(listener).onReadWriteError(any(HBDevice.class));
    }

    @Test
    public void writeCharacteristicNotConnectedTest() throws Exception {
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        BluetoothGattService service = mock(BluetoothGattService.class);

        when(gatt.getService(HBHealthThermometerServiceHandler.SERVICE_UUID)).thenReturn(service);
        when(service.getCharacteristic(UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"))).thenReturn(characteristic);
        when(gatt.writeCharacteristic(characteristic)).thenReturn(true);
        when(characteristic.getProperties()).thenReturn(BluetoothGattCharacteristic.PROPERTY_WRITE);

        HBWriteCommand command = new HBWriteCommand(HBHealthThermometerServiceHandler.SERVICE_UUID,
                UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"), new byte[] { 0, 0 });
        hBDevice.writeCharacteristic(command);

        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(gatt, never()).writeCharacteristic(any(BluetoothGattCharacteristic.class));
        verify(listener).onReadWriteError(any(HBDevice.class));
    }

    @Test
    public void writeCharacteristicNotWritePropertyTest() throws Exception {
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        BluetoothGattService service = mock(BluetoothGattService.class);

        when(gatt.getService(HBHealthThermometerServiceHandler.SERVICE_UUID)).thenReturn(service);
        when(service.getCharacteristic(UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"))).thenReturn(characteristic);
        when(gatt.writeCharacteristic(characteristic)).thenReturn(true);
        when(characteristic.getProperties()).thenReturn(BluetoothGattCharacteristic.PROPERTY_WRITE);
        when(characteristic.getProperties()).thenReturn(0);

        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        HBWriteCommand command = new HBWriteCommand(HBHealthThermometerServiceHandler.SERVICE_UUID,
                UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"), new byte[] { 0, 0 });
        hBDevice.writeCharacteristic(command);

        verify(gatt, never()).writeCharacteristic(any(BluetoothGattCharacteristic.class));
        verify(listener).onReadWriteError(any(HBDevice.class));
    }

    @Test
    public void writeCharacteristicFalseTest() throws Exception {
        hBDevice.connect(false);

        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        BluetoothGattService service = mock(BluetoothGattService.class);

        when(gatt.getService(HBHealthThermometerServiceHandler.SERVICE_UUID)).thenReturn(service);
        when(service.getCharacteristic(UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"))).thenReturn(characteristic);
        when(gatt.writeCharacteristic(characteristic)).thenReturn(false);
        when(characteristic.getProperties()).thenReturn(BluetoothGattCharacteristic.PROPERTY_WRITE);

        HBWriteCommand command = new HBWriteCommand(HBHealthThermometerServiceHandler.SERVICE_UUID,
                UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"), new byte[] { 0, 0 });
        hBDevice.writeCharacteristic(command);

        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(gatt).writeCharacteristic(any(BluetoothGattCharacteristic.class));
        verify(listener).onReadWriteError(any(HBDevice.class));
    }

    @Test
    public void writeCharacteristicNoValueTest() throws Exception {
        hBDevice.connect(false);

        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        BluetoothGattService service = mock(BluetoothGattService.class);

        when(gatt.getService(HBHealthThermometerServiceHandler.SERVICE_UUID)).thenReturn(service);
        when(service.getCharacteristic(UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"))).thenReturn(characteristic);
        when(gatt.writeCharacteristic(characteristic)).thenReturn(true);
        when(characteristic.getProperties()).thenReturn(BluetoothGattCharacteristic.PROPERTY_WRITE);

        HBWriteCommand command = new HBWriteCommand(HBHealthThermometerServiceHandler.SERVICE_UUID,
                UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"), null);
        hBDevice.writeCharacteristic(command);

        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(gatt).writeCharacteristic(any(BluetoothGattCharacteristic.class));
        verify(listener, never()).onReadWriteError(any(HBDevice.class));
    }

    @Test
    public void onConnectionStateChangedConnectedTest() throws Exception {
        BluetoothGattCallback callback = connectAndGetCallback();

        callback.onConnectionStateChange(gatt, GATT_SUCCESS, BluetoothProfile.STATE_CONNECTED);

        verify(gatt).discoverServices();
    }

    @Test
    public void onConnectionStateChangedConnectedCreateBondTest() throws Exception {
        hBDevice.connect(true);
        BluetoothGattCallback callback = connectAndGetCallback();

        callback.onConnectionStateChange(gatt, GATT_SUCCESS, BluetoothProfile.STATE_CONNECTED);

        verify(device).createBond();
    }

    @Test
    public void onConnectionStateChangedConnectedGattFailedTest() throws Exception {
        BluetoothGattCallback callback = connectAndGetCallback();

        callback.onConnectionStateChange(gatt, GATT_FAILURE, BluetoothProfile.STATE_CONNECTED);

        verify(gatt).disconnect();
    }

    @Test
    public void onConnectionStateChangedConnectedAlreadyBondedTest() throws Exception {
        when(device.getBondState()).thenReturn(BOND_BONDED);

        hBDevice.connect(true);
        BluetoothGattCallback callback = connectAndGetCallback();

        callback.onConnectionStateChange(gatt, GATT_SUCCESS, BluetoothProfile.STATE_CONNECTED);

        verify(gatt).discoverServices();
    }

    @Test
    public void onConnectionStateChangedDisconnectedTest() throws Exception {
        BluetoothGattCallback callback = connectAndGetCallback();

        callback.onConnectionStateChange(gatt, GATT_SUCCESS, BluetoothProfile.STATE_DISCONNECTED);

        verify(gatt).close();
        verify(listener).disconnected(any(HBDevice.class));
    }

    @Test
    public void onServicesDiscoveredTest() throws Exception {
        BluetoothGattCallback callback = connectAndGetCallback();

        BluetoothGattService service = mock(BluetoothGattService.class);
        when(service.getUuid()).thenReturn(HBHealthThermometerServiceHandler.SERVICE_UUID);
        when(gatt.getServices()).thenReturn(Arrays.asList(service));

        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        when(characteristic.getProperties()).thenReturn(PROPERTY_READ);
        when(service.getCharacteristics()).thenReturn(Arrays.asList(characteristic));

        callback.onServicesDiscovered(gatt, 0);

        List<UUID> expected = Arrays.asList(HBHealthThermometerServiceHandler.SERVICE_UUID);

        assertThat(hBDevice.getServices(), is(expected));
    }

    @Test
    public void onServicesDiscoveredCharacteristicNotifyTest() throws Exception {
        BluetoothGattCallback callback = connectAndGetCallback();

        BluetoothGattService service = mock(BluetoothGattService.class);
        when(service.getUuid()).thenReturn(HBHealthThermometerServiceHandler.SERVICE_UUID);
        when(gatt.getServices()).thenReturn(Arrays.asList(service));

        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        when(characteristic.getProperties()).thenReturn(PROPERTY_NOTIFY);
        when(service.getCharacteristics()).thenReturn(Arrays.asList(characteristic));

        callback.onServicesDiscovered(gatt, 0);

        List<UUID> expected = Arrays.asList(HBHealthThermometerServiceHandler.SERVICE_UUID);

        assertThat(hBDevice.getServices(), is(expected));
    }

    @Test
    public void onServicesDiscoveredCharacteristicIndicateTest() throws Exception {
        BluetoothGattCallback callback = connectAndGetCallback();

        BluetoothGattService service = mock(BluetoothGattService.class);
        when(service.getUuid()).thenReturn(HBHealthThermometerServiceHandler.SERVICE_UUID);
        when(gatt.getServices()).thenReturn(Arrays.asList(service));

        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        when(characteristic.getProperties()).thenReturn(PROPERTY_INDICATE);
        when(service.getCharacteristics()).thenReturn(Arrays.asList(characteristic));

        callback.onServicesDiscovered(gatt, 0);

        List<UUID> expected = Arrays.asList(HBHealthThermometerServiceHandler.SERVICE_UUID);

        assertThat(hBDevice.getServices(), is(expected));
    }

    @Test
    public void onServicesDiscoveredServicesNotFoundTest() throws Exception {
        BluetoothGattCallback callback = connectAndGetCallback();

        callback.onServicesDiscovered(gatt, 129);

        verify(listener).connectFailed(any(HBDevice.class));
    }

    @Test
    public void onDescriptorWriteTest() throws Exception {
        BluetoothGattCallback callback = connectAndGetCallback();

        BluetoothGattService service = mock(BluetoothGattService.class);
        BluetoothGattDescriptor descriptor = mock(BluetoothGattDescriptor.class);
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);

        when(gatt.getServices()).thenReturn(Arrays.asList(service));
        when(gatt.getService(HBHealthThermometerServiceHandler.SERVICE_UUID)).thenReturn(service);
        when(service.getUuid()).thenReturn(HBHealthThermometerServiceHandler.SERVICE_UUID);
        when(service.getCharacteristics()).thenReturn(Arrays.asList(characteristic));
        when(service.getCharacteristic(UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"))).thenReturn(characteristic);
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A1C-0000-1000-8000-00805f9b34fb"));
        when(characteristic.getProperties()).thenReturn(PROPERTY_INDICATE);
        when(characteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"))).thenReturn(descriptor);

        callback.onServicesDiscovered(gatt, 0);

        callback.onDescriptorWrite(gatt, descriptor, 0);

        verify(gatt).setCharacteristicNotification(characteristic, true);
        verify(gatt).writeDescriptor(descriptor);
    }

    @Test
    public void onCharacteristicReadTest() throws Exception {
        BluetoothGattCallback callback = connectAndGetCallback();

        BluetoothGattService service = mock(BluetoothGattService.class);
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);

        when(characteristic.getService()).thenReturn(service);
        when(service.getUuid()).thenReturn(HBHealthThermometerServiceHandler.SERVICE_UUID);
        when(device.getAddress()).thenReturn("0.0.0.0");

        callback.onCharacteristicRead(gatt, characteristic, GATT_SUCCESS);

        verify(serviceHandler).characteristicValueUpdated("0.0.0.0", characteristic);
    }

    @Test
    public void onCharacteristicWriteTest() throws Exception {
        BluetoothGattCallback callback = connectAndGetCallback();

        BluetoothGattService service = mock(BluetoothGattService.class);
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);

        when(characteristic.getService()).thenReturn(service);
        when(service.getUuid()).thenReturn(HBHealthThermometerServiceHandler.SERVICE_UUID);
        when(device.getAddress()).thenReturn("0.0.0.0");

        callback.onCharacteristicWrite(gatt, characteristic, GATT_SUCCESS);

        verify(serviceHandler).characteristicValueUpdated("0.0.0.0", characteristic);
    }

    @Test
    public void onBondStateChangedBonded() throws Exception {
        connectAndGetCallback();

        Field field = HBDevice.class.getDeclaredField("bondStateReceiver");
        field.setAccessible(true);
        BroadcastReceiver broadcastReceiver = (BroadcastReceiver) field.get(hBDevice);

        Intent intent = mock(Intent.class);
        when(intent.getAction()).thenReturn(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        when(intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR)).thenReturn(BOND_BONDED);

        broadcastReceiver.onReceive(context, intent);

        verify(gatt).discoverServices();
    }

    @Test
    public void onBondStateChangedNone() throws Exception {
        Field field = HBDevice.class.getDeclaredField("bondStateReceiver");
        field.setAccessible(true);
        BroadcastReceiver broadcastReceiver = (BroadcastReceiver) field.get(hBDevice);

        Intent intent = mock(Intent.class);
        when(intent.getAction()).thenReturn(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        when(intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR)).thenReturn(BluetoothDevice.BOND_NONE);

        broadcastReceiver.onReceive(context, intent);

        verify(listener).connectFailed(any(HBDevice.class));
    }

    @Test
    public void onCharacteristicChangedTest() throws Exception {
        BluetoothGattCallback callback = connectAndGetCallback();

        BluetoothGattService service = mock(BluetoothGattService.class);
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);

        when(characteristic.getService()).thenReturn(service);
        when(service.getUuid()).thenReturn(HBHealthThermometerServiceHandler.SERVICE_UUID);
        when(device.getAddress()).thenReturn("0.0.0.0");

        callback.onCharacteristicChanged(gatt, characteristic);

        verify(serviceHandler).characteristicValueUpdated("0.0.0.0", characteristic);
    }

    private BluetoothGattCallback connectAndGetCallback() {
        hBDevice.connect(false);

        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        ArgumentCaptor<BluetoothGattCallback> bluetoothGattCallbackCaptor = ArgumentCaptor.forClass(BluetoothGattCallback.class);
        verify(device).connectGatt(any(Context.class), anyBoolean(), bluetoothGattCallbackCaptor.capture());

        List<BluetoothGattCallback> capturedGatts = bluetoothGattCallbackCaptor.getAllValues();
        return capturedGatts.get(0);
    }
}