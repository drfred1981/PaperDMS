package fr.smartprod.paperdms.archive;

import fr.smartprod.paperdms.archive.config.AsyncSyncConfiguration;
import fr.smartprod.paperdms.archive.config.EmbeddedKafka;
import fr.smartprod.paperdms.archive.config.EmbeddedRedis;
import fr.smartprod.paperdms.archive.config.EmbeddedSQL;
import fr.smartprod.paperdms.archive.config.JacksonConfiguration;
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
@SpringBootTest(classes = { ArchiveServiceApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
}
