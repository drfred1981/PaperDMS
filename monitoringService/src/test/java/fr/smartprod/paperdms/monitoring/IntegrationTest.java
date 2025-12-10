package fr.smartprod.paperdms.monitoring;

import fr.smartprod.paperdms.monitoring.config.AsyncSyncConfiguration;
import fr.smartprod.paperdms.monitoring.config.EmbeddedKafka;
import fr.smartprod.paperdms.monitoring.config.EmbeddedRedis;
import fr.smartprod.paperdms.monitoring.config.EmbeddedSQL;
import fr.smartprod.paperdms.monitoring.config.JacksonConfiguration;
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
@SpringBootTest(classes = { MonitoringServiceApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {}
