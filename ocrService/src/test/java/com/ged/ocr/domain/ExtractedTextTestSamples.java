package com.ged.ocr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ExtractedTextTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ExtractedText getExtractedTextSample1() {
        return new ExtractedText().id(1L).pageNumber(1).language("language1").wordCount(1);
    }

    public static ExtractedText getExtractedTextSample2() {
        return new ExtractedText().id(2L).pageNumber(2).language("language2").wordCount(2);
    }

    public static ExtractedText getExtractedTextRandomSampleGenerator() {
        return new ExtractedText()
            .id(longCount.incrementAndGet())
            .pageNumber(intCount.incrementAndGet())
            .language(UUID.randomUUID().toString())
            .wordCount(intCount.incrementAndGet());
    }
}
