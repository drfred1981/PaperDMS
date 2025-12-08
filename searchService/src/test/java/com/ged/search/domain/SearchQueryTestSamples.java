package com.ged.search.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SearchQueryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SearchQuery getSearchQuerySample1() {
        return new SearchQuery().id(1L).query("query1").resultCount(1).executionTime(1L).userId("userId1");
    }

    public static SearchQuery getSearchQuerySample2() {
        return new SearchQuery().id(2L).query("query2").resultCount(2).executionTime(2L).userId("userId2");
    }

    public static SearchQuery getSearchQueryRandomSampleGenerator() {
        return new SearchQuery()
            .id(longCount.incrementAndGet())
            .query(UUID.randomUUID().toString())
            .resultCount(intCount.incrementAndGet())
            .executionTime(longCount.incrementAndGet())
            .userId(UUID.randomUUID().toString());
    }
}
