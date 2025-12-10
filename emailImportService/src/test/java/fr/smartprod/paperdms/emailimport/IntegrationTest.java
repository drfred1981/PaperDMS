package fr.smartprod.paperdms.emailimport;

import fr.smartprod.paperdms.emailimport.config.AsyncSyncConfiguration;
import fr.smartprod.paperdms.emailimport.config.EmbeddedKafka;
import fr.smartprod.paperdms.emailimport.config.EmbeddedRedis;
import fr.smartprod.paperdms.emailimport.config.EmbeddedSQL;
import fr.smartprod.paperdms.emailimport.config.JacksonConfiguration;
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
@SpringBootTest(classes = { EmailImportServiceApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {}
