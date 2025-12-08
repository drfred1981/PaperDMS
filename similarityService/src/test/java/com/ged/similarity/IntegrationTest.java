package com.ged.similarity;

import com.ged.similarity.config.AsyncSyncConfiguration;
import com.ged.similarity.config.EmbeddedElasticsearch;
import com.ged.similarity.config.EmbeddedKafka;
import com.ged.similarity.config.EmbeddedRedis;
import com.ged.similarity.config.EmbeddedSQL;
import com.ged.similarity.config.JacksonConfiguration;
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
public @interface IntegrationTest {
}
