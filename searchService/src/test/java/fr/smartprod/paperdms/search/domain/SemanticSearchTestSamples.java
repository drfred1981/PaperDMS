package fr.smartprod.paperdms.search.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SemanticSearchTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SemanticSearch getSemanticSearchSample1() {
        return new SemanticSearch().id(1L).query("query1").modelUsed("modelUsed1").executionTime(1L).userId("userId1");
    }

    public static SemanticSearch getSemanticSearchSample2() {
        return new SemanticSearch().id(2L).query("query2").modelUsed("modelUsed2").executionTime(2L).userId("userId2");
    }

    public static SemanticSearch getSemanticSearchRandomSampleGenerator() {
        return new SemanticSearch()
            .id(longCount.incrementAndGet())
            .query(UUID.randomUUID().toString())
            .modelUsed(UUID.randomUUID().toString())
            .executionTime(longCount.incrementAndGet())
            .userId(UUID.randomUUID().toString());
    }
}
