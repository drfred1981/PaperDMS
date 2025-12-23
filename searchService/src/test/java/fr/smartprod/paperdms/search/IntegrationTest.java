package fr.smartprod.paperdms.search;

import fr.smartprod.paperdms.search.config.AsyncSyncConfiguration;
import fr.smartprod.paperdms.search.config.EmbeddedElasticsearch;
import fr.smartprod.paperdms.search.config.EmbeddedKafka;
import fr.smartprod.paperdms.search.config.EmbeddedRedis;
import fr.smartprod.paperdms.search.config.EmbeddedSQL;
import fr.smartprod.paperdms.search.config.JacksonConfiguration;
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
@SpringBootTest(classes = { SearchServiceApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedElasticsearch
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {}
