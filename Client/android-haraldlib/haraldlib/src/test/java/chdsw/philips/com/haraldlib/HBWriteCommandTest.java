/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import chdsw.philips.com.haraldlib.HBWriteCommand;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

public class HBWriteCommandTest {
    private HBWriteCommand writeCommand;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        writeCommand = new HBWriteCommand(UUID.fromString("00000000-0000-0000-8000-000000000001"),
                UUID.fromString("00000000-0000-0000-8000-000000000002"), new byte[] { 0, 0, 0, 3 });
    }

    @Test
    public void getServiceUUIDTest() {
        assertEquals(writeCommand.getServiceUUID(), UUID.fromString("00000000-0000-0000-8000-000000000001"));
    }

    @Test
    public void getCharacteristicUUIDTest() {
        assertEquals(writeCommand.getCharacteristicUUID(), UUID.fromString("00000000-0000-0000-8000-000000000002"));
    }

    @Test
    public void getValueTest() {
        assertArrayEquals(writeCommand.getValue(), new byte[] { 0, 0, 0, 3 });
    }
}