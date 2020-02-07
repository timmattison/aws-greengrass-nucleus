package com.aws.iot.evergreen.ipc;

import com.aws.iot.evergreen.ipc.exceptions.IPCException;
import com.aws.iot.evergreen.util.Log;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class IPCRouterTest {
    @Mock
    Log log;

    @Test
    public void GIVEN_function_WHEN_register_callback_THEN_callback_can_be_called() throws Throwable {
        IPCRouter router = new IPCRouter(log);

        CountDownLatch cdl = new CountDownLatch(1);
        router.registerServiceCallback("dest", (a, b) -> {
            cdl.countDown();
            return null;
        });

        router.getCallbackForDestination("dest").onMessage(null, null);
        assertTrue(cdl.await(100, TimeUnit.MILLISECONDS));
    }

    @Test
    public void GIVEN_already_registered_function_WHEN_register_callback_THEN_exception_is_thrown() throws Throwable {
        IPCRouter router = new IPCRouter(log);

        router.registerServiceCallback("dest", (a, b) -> null);

        assertThrows(IPCException.class, () -> router.registerServiceCallback("dest", (a, b) -> null));
    }
}