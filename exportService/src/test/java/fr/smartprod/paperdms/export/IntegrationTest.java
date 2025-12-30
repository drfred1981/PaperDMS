package fr.smartprod.paperdms.export;

import fr.smartprod.paperdms.export.config.AsyncSyncConfiguration;
import fr.smartprod.paperdms.export.config.EmbeddedKafka;
import fr.smartprod.paperdms.export.config.EmbeddedRedis;
import fr.smartprod.paperdms.export.config.EmbeddedSQL;
import fr.smartprod.paperdms.export.config.JacksonConfiguration;
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
@SpringBootTest(classes = { ExportServiceApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
}
