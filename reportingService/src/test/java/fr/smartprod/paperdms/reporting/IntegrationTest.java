package fr.smartprod.paperdms.reporting;

import fr.smartprod.paperdms.reporting.config.AsyncSyncConfiguration;
import fr.smartprod.paperdms.reporting.config.EmbeddedKafka;
import fr.smartprod.paperdms.reporting.config.EmbeddedRedis;
import fr.smartprod.paperdms.reporting.config.EmbeddedSQL;
import fr.smartprod.paperdms.reporting.config.JacksonConfiguration;
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
@SpringBootTest(classes = { ReportingServiceApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {}
