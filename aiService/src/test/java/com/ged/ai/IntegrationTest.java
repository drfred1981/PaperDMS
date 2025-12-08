package com.ged.ai;

import com.ged.ai.config.AsyncSyncConfiguration;
import com.ged.ai.config.EmbeddedElasticsearch;
import com.ged.ai.config.EmbeddedKafka;
import com.ged.ai.config.EmbeddedRedis;
import com.ged.ai.config.EmbeddedSQL;
import com.ged.ai.config.JacksonConfiguration;
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
@SpringBootTest(classes = { AiServiceApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedElasticsearch
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
}
