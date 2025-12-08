package com.ged.ai.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TagPredictionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TagPrediction getTagPredictionSample1() {
        return new TagPrediction().id(1L).tagName("tagName1").reason("reason1").modelVersion("modelVersion1").acceptedBy("acceptedBy1");
    }

    public static TagPrediction getTagPredictionSample2() {
        return new TagPrediction().id(2L).tagName("tagName2").reason("reason2").modelVersion("modelVersion2").acceptedBy("acceptedBy2");
    }

    public static TagPrediction getTagPredictionRandomSampleGenerator() {
        return new TagPrediction()
            .id(longCount.incrementAndGet())
            .tagName(UUID.randomUUID().toString())
            .reason(UUID.randomUUID().toString())
            .modelVersion(UUID.randomUUID().toString())
            .acceptedBy(UUID.randomUUID().toString());
    }
}
