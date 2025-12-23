package fr.smartprod.paperdms.ocr;

import fr.smartprod.paperdms.ocr.config.AsyncSyncConfiguration;
import fr.smartprod.paperdms.ocr.config.EmbeddedElasticsearch;
import fr.smartprod.paperdms.ocr.config.EmbeddedKafka;
import fr.smartprod.paperdms.ocr.config.EmbeddedRedis;
import fr.smartprod.paperdms.ocr.config.EmbeddedSQL;
import fr.smartprod.paperdms.ocr.config.JacksonConfiguration;
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
@SpringBootTest(classes = { OcrServiceApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedElasticsearch
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {}
