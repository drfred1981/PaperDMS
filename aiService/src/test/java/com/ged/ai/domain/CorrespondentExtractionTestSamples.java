package com.ged.ai.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CorrespondentExtractionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CorrespondentExtraction getCorrespondentExtractionSample1() {
        return new CorrespondentExtraction().id(1L).documentId(1L).sendersCount(1).recipientsCount(1);
    }

    public static CorrespondentExtraction getCorrespondentExtractionSample2() {
        return new CorrespondentExtraction().id(2L).documentId(2L).sendersCount(2).recipientsCount(2);
    }

    public static CorrespondentExtraction getCorrespondentExtractionRandomSampleGenerator() {
        return new CorrespondentExtraction()
            .id(longCount.incrementAndGet())
            .documentId(longCount.incrementAndGet())
            .sendersCount(intCount.incrementAndGet())
            .recipientsCount(intCount.incrementAndGet());
    }
}
