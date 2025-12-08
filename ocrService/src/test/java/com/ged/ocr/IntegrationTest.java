package com.ged.ocr;

import com.ged.ocr.config.AsyncSyncConfiguration;
import com.ged.ocr.config.EmbeddedElasticsearch;
import com.ged.ocr.config.EmbeddedKafka;
import com.ged.ocr.config.EmbeddedRedis;
import com.ged.ocr.config.EmbeddedSQL;
import com.ged.ocr.config.JacksonConfiguration;
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
public @interface IntegrationTest {
}
