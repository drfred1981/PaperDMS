package fr.smartprod.paperdms.similarity.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SimilarityClusterTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SimilarityCluster getSimilarityClusterSample1() {
        return new SimilarityCluster().id(1L).name("name1").documentCount(1);
    }

    public static SimilarityCluster getSimilarityClusterSample2() {
        return new SimilarityCluster().id(2L).name("name2").documentCount(2);
    }

    public static SimilarityCluster getSimilarityClusterRandomSampleGenerator() {
        return new SimilarityCluster()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .documentCount(intCount.incrementAndGet());
    }
}
