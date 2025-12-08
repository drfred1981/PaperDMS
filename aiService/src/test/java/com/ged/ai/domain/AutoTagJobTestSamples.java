package com.ged.ai.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AutoTagJobTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AutoTagJob getAutoTagJobSample1() {
        return new AutoTagJob().id(1L).documentId(1L).s3Key("s3Key1").modelVersion("modelVersion1");
    }

    public static AutoTagJob getAutoTagJobSample2() {
        return new AutoTagJob().id(2L).documentId(2L).s3Key("s3Key2").modelVersion("modelVersion2");
    }

    public static AutoTagJob getAutoTagJobRandomSampleGenerator() {
        return new AutoTagJob()
            .id(longCount.incrementAndGet())
            .documentId(longCount.incrementAndGet())
            .s3Key(UUID.randomUUID().toString())
            .modelVersion(UUID.randomUUID().toString());
    }
}
