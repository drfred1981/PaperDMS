package com.ged.ocr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OcrResultTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static OcrResult getOcrResultSample1() {
        return new OcrResult().id(1L).pageNumber(1).s3ResultKey("s3ResultKey1").language("language1").wordCount(1);
    }

    public static OcrResult getOcrResultSample2() {
        return new OcrResult().id(2L).pageNumber(2).s3ResultKey("s3ResultKey2").language("language2").wordCount(2);
    }

    public static OcrResult getOcrResultRandomSampleGenerator() {
        return new OcrResult()
            .id(longCount.incrementAndGet())
            .pageNumber(intCount.incrementAndGet())
            .s3ResultKey(UUID.randomUUID().toString())
            .language(UUID.randomUUID().toString())
            .wordCount(intCount.incrementAndGet());
    }
}
