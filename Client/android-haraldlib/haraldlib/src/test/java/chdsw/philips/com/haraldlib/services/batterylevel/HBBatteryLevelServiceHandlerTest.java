/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib.services.batterylevel;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;
import java.util.UUID;

import chdsw.philips.com.haraldlib.BuildConfig;
import chdsw.philips.com.haraldlib.HBConnectionListener;
import chdsw.philips.com.haraldlib.HBDevice;
import chdsw.philips.com.haraldlib.HBReadCommand;

import static android.bluetooth.BluetoothGattCharacteristic.FORMAT_UINT8;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class HBBatteryLevelServiceHandlerTest {
    private HBBatteryServiceHandler handler;

    @Mock
    private Context context;

    @Mock
    private HBBatteryListenerHandler listener;

    @Before
    public void setUp() {
        initMocks(this);

        handler = new HBBatteryServiceHandler(context);
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
    public void onCurrentTimeValueUpdatedTest() {
        BluetoothGattCharacteristic characteristic = mock(BluetoothGattCharacteristic.class);
        when(characteristic.getUuid()).thenReturn(UUID.fromString("00002A19-0000-1000-8000-00805f9b34fb"));
        when(characteristic.getIntValue(FORMAT_UINT8, 0)).thenReturn(60);

        handler.addServiceListener("0.0.0.0", listener);
        handler.characteristicValueUpdated("0.0.0.0", characteristic);

        verify(listener).onBatteryLevel("0.0.0.0", 60);
    }

    @Test
    public void getCurrentTimeTest() {
        HBDevice device = mock(HBDevice.class);

        handler.getBatteryLevel(device);

        ArgumentCaptor<HBReadCommand> commandCaptor = ArgumentCaptor.forClass(HBReadCommand.class);
        verify(device).readCharacteristic(commandCaptor.capture());

        List<HBReadCommand> capturedCommands = commandCaptor.getAllValues();
        assertEquals(UUID.fromString("0000180F-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getServiceUUID());
        assertEquals(UUID.fromString("00002A19-0000-1000-8000-00805f9b34fb"), capturedCommands.get(0).getCharacteristicUUID());
    }
}