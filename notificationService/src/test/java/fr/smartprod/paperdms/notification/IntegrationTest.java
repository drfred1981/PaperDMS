package fr.smartprod.paperdms.notification;

import fr.smartprod.paperdms.notification.config.AsyncSyncConfiguration;
import fr.smartprod.paperdms.notification.config.EmbeddedKafka;
import fr.smartprod.paperdms.notification.config.EmbeddedRedis;
import fr.smartprod.paperdms.notification.config.EmbeddedSQL;
import fr.smartprod.paperdms.notification.config.JacksonConfiguration;
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
public @interface IntegrationTest {}
