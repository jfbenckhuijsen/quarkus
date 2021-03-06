package io.quarkus.logging.sentry;

import static io.sentry.jvmti.ResetFrameCache.resetFrameCache;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jboss.logmanager.handlers.DelayedHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.runtime.logging.InitialConfigurator;
import io.quarkus.test.QuarkusUnitTest;
import io.sentry.jul.SentryHandler;
import io.sentry.jvmti.FrameCache;

public class SentryLoggerCustomTest {

    @RegisterExtension
    static final QuarkusUnitTest config = new QuarkusUnitTest()
            .withConfigurationResource("application-sentry-logger-custom.properties");

    @Test
    public void sentryLoggerCustomTest() {
        LogManager logManager = LogManager.getLogManager();
        assertThat(logManager).isInstanceOf(org.jboss.logmanager.LogManager.class);

        DelayedHandler delayedHandler = InitialConfigurator.DELAYED_HANDLER;
        assertThat(Logger.getLogger("").getHandlers()).contains(delayedHandler);
        assertThat(delayedHandler.getLevel()).isEqualTo(Level.ALL);

        Handler handler = Arrays.stream(delayedHandler.getHandlers())
                .filter(h -> (h instanceof SentryHandler))
                .findFirst().orElse(null);
        SentryHandler sentryHandler = (SentryHandler) handler;
        assertThat(sentryHandler).isNotNull();
        assertThat(sentryHandler.getLevel()).isEqualTo(org.jboss.logmanager.Level.TRACE);
        assertThat(FrameCache.shouldCacheThrowable(new IllegalStateException("Test frame"), 1)).isTrue();
    }

    @AfterAll
    static void reset() {
        resetFrameCache();
    }
}
