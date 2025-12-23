package fr.smartprod.paperdms.ai;

import fr.smartprod.paperdms.ai.config.AsyncSyncConfiguration;
import fr.smartprod.paperdms.ai.config.EmbeddedElasticsearch;
import fr.smartprod.paperdms.ai.config.EmbeddedKafka;
import fr.smartprod.paperdms.ai.config.EmbeddedRedis;
import fr.smartprod.paperdms.ai.config.EmbeddedSQL;
import fr.smartprod.paperdms.ai.config.JacksonConfiguration;
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
public @interface IntegrationTest {}
