package fr.smartprod.paperdms.document;

import fr.smartprod.paperdms.document.config.AsyncSyncConfiguration;
import fr.smartprod.paperdms.document.config.EmbeddedElasticsearch;
import fr.smartprod.paperdms.document.config.EmbeddedKafka;
import fr.smartprod.paperdms.document.config.EmbeddedRedis;
import fr.smartprod.paperdms.document.config.EmbeddedSQL;
import fr.smartprod.paperdms.document.config.JacksonConfiguration;
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
@SpringBootTest(classes = { DocumentServiceApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedElasticsearch
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {}
