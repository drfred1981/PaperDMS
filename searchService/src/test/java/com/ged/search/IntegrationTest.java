package com.ged.search;

import com.ged.search.config.AsyncSyncConfiguration;
import com.ged.search.config.EmbeddedElasticsearch;
import com.ged.search.config.EmbeddedKafka;
import com.ged.search.config.EmbeddedRedis;
import com.ged.search.config.EmbeddedSQL;
import com.ged.search.config.JacksonConfiguration;
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
public @interface IntegrationTest {
}
