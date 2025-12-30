package fr.smartprod.paperdms.emailimportdocument;

import fr.smartprod.paperdms.emailimportdocument.config.AsyncSyncConfiguration;
import fr.smartprod.paperdms.emailimportdocument.config.EmbeddedKafka;
import fr.smartprod.paperdms.emailimportdocument.config.EmbeddedRedis;
import fr.smartprod.paperdms.emailimportdocument.config.EmbeddedSQL;
import fr.smartprod.paperdms.emailimportdocument.config.JacksonConfiguration;
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
@SpringBootTest(classes = { EmailImportDocumentServiceApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
}
