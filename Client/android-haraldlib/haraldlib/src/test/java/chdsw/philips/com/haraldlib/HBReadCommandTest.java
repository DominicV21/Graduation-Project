/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import chdsw.philips.com.haraldlib.HBReadCommand;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

public class HBReadCommandTest {
    private HBReadCommand readCommand;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        readCommand = new HBReadCommand(UUID.fromString("00000000-0000-0000-8000-000000000001"),
                UUID.fromString("00000000-0000-0000-8000-000000000002"));
    }

    @Test
    public void getServiceUUIDTest() {
        assertEquals(readCommand.getServiceUUID(), UUID.fromString("00000000-0000-0000-8000-000000000001"));
    }

    @Test
    public void getCharacteristicUUIDTest() {
        assertEquals(readCommand.getCharacteristicUUID(), UUID.fromString("00000000-0000-0000-8000-000000000002"));
    }
}