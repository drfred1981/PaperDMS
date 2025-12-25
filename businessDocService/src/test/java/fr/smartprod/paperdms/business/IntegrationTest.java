package fr.smartprod.paperdms.business;

import fr.smartprod.paperdms.business.config.AsyncSyncConfiguration;
import fr.smartprod.paperdms.business.config.EmbeddedElasticsearch;
import fr.smartprod.paperdms.business.config.EmbeddedKafka;
import fr.smartprod.paperdms.business.config.EmbeddedRedis;
import fr.smartprod.paperdms.business.config.EmbeddedSQL;
import fr.smartprod.paperdms.business.config.JacksonConfiguration;
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
@SpringBootTest(classes = { BusinessDocServiceApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedElasticsearch
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
}
