package fr.smartprod.paperdms.similarity;

import fr.smartprod.paperdms.similarity.config.AsyncSyncConfiguration;
import fr.smartprod.paperdms.similarity.config.EmbeddedElasticsearch;
import fr.smartprod.paperdms.similarity.config.EmbeddedKafka;
import fr.smartprod.paperdms.similarity.config.EmbeddedRedis;
import fr.smartprod.paperdms.similarity.config.EmbeddedSQL;
import fr.smartprod.paperdms.similarity.config.JacksonConfiguration;
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
@SpringBootTest(classes = { SimilarityServiceApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedElasticsearch
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {}
