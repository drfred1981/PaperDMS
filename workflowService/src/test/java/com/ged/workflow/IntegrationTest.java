package com.ged.workflow;

import com.ged.workflow.config.AsyncSyncConfiguration;
import com.ged.workflow.config.EmbeddedElasticsearch;
import com.ged.workflow.config.EmbeddedKafka;
import com.ged.workflow.config.EmbeddedRedis;
import com.ged.workflow.config.EmbeddedSQL;
import com.ged.workflow.config.JacksonConfiguration;
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
@SpringBootTest(classes = { WorkflowServiceApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedElasticsearch
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
}
