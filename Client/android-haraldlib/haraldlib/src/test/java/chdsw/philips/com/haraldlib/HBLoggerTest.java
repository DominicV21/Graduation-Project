/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package chdsw.philips.com.haraldlib;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class HBLoggerTest {

    @Mock
    HBLogger.LoggerImplementation mockedImplementation1;

    @Mock
    HBLogger.LoggerImplementation mockedImplementation2;

    @Mock
    Throwable mockedThrowable;

    public static final String TEST_TAG = "This Is a Tag";
    public static final String TEST_MSG = "This is a message\n over two lines";
    public static final String LOGGER_HANDLER_PREFIX = "[TID: 0] ";
    public static final String TEST_MSG_ON_LOGGER_HANDLER = LOGGER_HANDLER_PREFIX + TEST_MSG;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        HBLogger.registerLogger(mockedImplementation1);
        HBLogger.registerLogger(mockedImplementation2);
    }

    private void verifyForwarded(int priority, String tag, String msg, Throwable tr) {
        verify(mockedImplementation1).logLine(priority, tag, msg, tr);
        verify(mockedImplementation2).logLine(priority, tag, msg, tr);
    }

    @Test
    public void ShouldCallAllRegisteredLoggers_When_V_FunctionIsCalled() throws Exception {
        HBLogger.v(TEST_TAG, TEST_MSG);
        verifyForwarded(Log.VERBOSE, TEST_TAG, TEST_MSG, null);

        HBLogger.v(TEST_TAG, TEST_MSG, mockedThrowable);
        verifyForwarded(Log.VERBOSE, TEST_TAG, TEST_MSG, mockedThrowable);
    }

    @Test
    public void ShouldCallAllRegisteredLoggers_When_D_LoggingFunctionIsCalled() throws Exception {
        HBLogger.d(TEST_TAG, TEST_MSG);
        verifyForwarded(Log.DEBUG, TEST_TAG, TEST_MSG, null);

        HBLogger.d(TEST_TAG, TEST_MSG, mockedThrowable);
        verifyForwarded(Log.DEBUG, TEST_TAG, TEST_MSG, mockedThrowable);
    }

    @Test
    public void ShouldCallAllRegisteredLoggers_When_I_LoggingFunctionIsCalled() throws Exception {
        HBLogger.i(TEST_TAG, TEST_MSG);
        verifyForwarded(Log.INFO, TEST_TAG, TEST_MSG, null);

        HBLogger.i(TEST_TAG, TEST_MSG, mockedThrowable);
        verifyForwarded(Log.INFO, TEST_TAG, TEST_MSG, mockedThrowable);
    }

    @Test
    public void ShouldCallAllRegisteredLoggers_When_W_LoggingFunctionIsCalled() throws Exception {
        HBLogger.w(TEST_TAG, TEST_MSG);
        verifyForwarded(Log.WARN, TEST_TAG, TEST_MSG, null);

        HBLogger.w(TEST_TAG, TEST_MSG, mockedThrowable);
        verifyForwarded(Log.WARN, TEST_TAG, TEST_MSG, mockedThrowable);

        HBLogger.w(TEST_TAG, mockedThrowable);
        verifyForwarded(Log.WARN, TEST_TAG, "", mockedThrowable);
    }

    @Test
    public void ShouldCallAllRegisteredLoggers_When_E_LoggingFunctionIsCalled() throws Exception {
        HBLogger.e(TEST_TAG, TEST_MSG);
        verifyForwarded(Log.ERROR, TEST_TAG, TEST_MSG, null);

        HBLogger.e(TEST_TAG, TEST_MSG, mockedThrowable);
        verifyForwarded(Log.ERROR, TEST_TAG, TEST_MSG, mockedThrowable);
    }

    @Test
    public void ShouldCallAllRegisteredLoggers_When_WTF_LoggingFunctionIsCalled() throws Exception {
        HBLogger.wtf(TEST_TAG, TEST_MSG);
        verifyForwarded(Log.ASSERT, TEST_TAG, TEST_MSG, null);

        HBLogger.wtf(TEST_TAG, TEST_MSG, mockedThrowable);
        verifyForwarded(Log.ASSERT, TEST_TAG, TEST_MSG, mockedThrowable);

        HBLogger.wtf(TEST_TAG, mockedThrowable);
        verifyForwarded(Log.ASSERT, TEST_TAG, "", mockedThrowable);
    }

    @Test
    public void ShouldNotCallALogger_WhenItWasAlreadyRemoved_When() throws Exception {
        HBLogger.unregisterLogger(mockedImplementation2);

        HBLogger.e(TEST_TAG, TEST_MSG);

        verify(mockedImplementation1).logLine(Log.ERROR, TEST_TAG, TEST_MSG, null);
        verify(mockedImplementation2, never()).logLine(anyInt(), anyString(), anyString(), any(Throwable.class));
    }

    @Test
    public void ShouldBePossibleToCreateALogCatLogger() throws Exception {
        assertNotNull(new HBLogger.LogCatLogger());
    }
}