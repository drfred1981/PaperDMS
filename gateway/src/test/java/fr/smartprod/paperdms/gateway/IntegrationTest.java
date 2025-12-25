package fr.smartprod.paperdms.gateway;

import fr.smartprod.paperdms.gateway.config.AsyncSyncConfiguration;
import fr.smartprod.paperdms.gateway.config.EmbeddedElasticsearch;
import fr.smartprod.paperdms.gateway.config.EmbeddedKafka;
import fr.smartprod.paperdms.gateway.config.EmbeddedSQL;
import fr.smartprod.paperdms.gateway.config.JacksonConfiguration;
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
@SpringBootTest(classes = { GatewayApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedElasticsearch
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
}
