package fr.smartprod.paperdms.pdftoimage;

import fr.smartprod.paperdms.pdftoimage.config.AsyncSyncConfiguration;
import fr.smartprod.paperdms.pdftoimage.config.EmbeddedElasticsearch;
import fr.smartprod.paperdms.pdftoimage.config.EmbeddedKafka;
import fr.smartprod.paperdms.pdftoimage.config.EmbeddedRedis;
import fr.smartprod.paperdms.pdftoimage.config.EmbeddedSQL;
import fr.smartprod.paperdms.pdftoimage.config.JacksonConfiguration;
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
@SpringBootTest(classes = { PdfToImageServiceApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedElasticsearch
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
}
