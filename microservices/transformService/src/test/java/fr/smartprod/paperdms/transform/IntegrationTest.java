package fr.smartprod.paperdms.transform;

import fr.smartprod.paperdms.transform.config.AsyncSyncConfiguration;
import fr.smartprod.paperdms.transform.config.EmbeddedKafka;
import fr.smartprod.paperdms.transform.config.EmbeddedRedis;
import fr.smartprod.paperdms.transform.config.EmbeddedSQL;
import fr.smartprod.paperdms.transform.config.JacksonConfiguration;
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
@SpringBootTest(classes = { TransformServiceApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
}
