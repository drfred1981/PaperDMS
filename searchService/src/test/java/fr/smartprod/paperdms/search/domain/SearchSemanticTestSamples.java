package fr.smartprod.paperdms.search.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SearchSemanticTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SearchSemantic getSearchSemanticSample1() {
        return new SearchSemantic().id(1L).query("query1").modelUsed("modelUsed1").executionTime(1L).userId("userId1");
    }

    public static SearchSemantic getSearchSemanticSample2() {
        return new SearchSemantic().id(2L).query("query2").modelUsed("modelUsed2").executionTime(2L).userId("userId2");
    }

    public static SearchSemantic getSearchSemanticRandomSampleGenerator() {
        return new SearchSemantic()
            .id(longCount.incrementAndGet())
            .query(UUID.randomUUID().toString())
            .modelUsed(UUID.randomUUID().toString())
            .executionTime(longCount.incrementAndGet())
            .userId(UUID.randomUUID().toString());
    }
}
