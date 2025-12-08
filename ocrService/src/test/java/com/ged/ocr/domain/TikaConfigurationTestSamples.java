package com.ged.ocr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TikaConfigurationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TikaConfiguration getTikaConfigurationSample1() {
        return new TikaConfiguration()
            .id(1L)
            .name("name1")
            .endpoint("endpoint1")
            .apiKey("apiKey1")
            .timeout(1)
            .maxFileSize(1L)
            .supportedLanguages("supportedLanguages1")
            .ocrEngine("ocrEngine1");
    }

    public static TikaConfiguration getTikaConfigurationSample2() {
        return new TikaConfiguration()
            .id(2L)
            .name("name2")
            .endpoint("endpoint2")
            .apiKey("apiKey2")
            .timeout(2)
            .maxFileSize(2L)
            .supportedLanguages("supportedLanguages2")
            .ocrEngine("ocrEngine2");
    }

    public static TikaConfiguration getTikaConfigurationRandomSampleGenerator() {
        return new TikaConfiguration()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .endpoint(UUID.randomUUID().toString())
            .apiKey(UUID.randomUUID().toString())
            .timeout(intCount.incrementAndGet())
            .maxFileSize(longCount.incrementAndGet())
            .supportedLanguages(UUID.randomUUID().toString())
            .ocrEngine(UUID.randomUUID().toString());
    }
}
