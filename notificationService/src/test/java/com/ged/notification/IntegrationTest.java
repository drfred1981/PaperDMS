package com.ged.notification;

import com.ged.notification.config.AsyncSyncConfiguration;
import com.ged.notification.config.EmbeddedKafka;
import com.ged.notification.config.EmbeddedRedis;
import com.ged.notification.config.EmbeddedSQL;
import com.ged.notification.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { NotificationServiceApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
}
