package fr.smartprod.paperdms.similarity.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SimilarityJobTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SimilarityJob getSimilarityJobSample1() {
        return new SimilarityJob().id(1L).documentSha256("documentSha2561").matchesFound(1).createdBy("createdBy1");
    }

    public static SimilarityJob getSimilarityJobSample2() {
        return new SimilarityJob().id(2L).documentSha256("documentSha2562").matchesFound(2).createdBy("createdBy2");
    }

    public static SimilarityJob getSimilarityJobRandomSampleGenerator() {
        return new SimilarityJob()
            .id(longCount.incrementAndGet())
            .documentSha256(UUID.randomUUID().toString())
            .matchesFound(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString());
    }
}
