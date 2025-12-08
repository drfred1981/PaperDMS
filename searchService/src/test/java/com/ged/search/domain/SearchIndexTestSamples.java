package com.ged.search.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SearchIndexTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SearchIndex getSearchIndexSample1() {
        return new SearchIndex().id(1L).documentId(1L).tags("tags1").correspondents("correspondents1");
    }

    public static SearchIndex getSearchIndexSample2() {
        return new SearchIndex().id(2L).documentId(2L).tags("tags2").correspondents("correspondents2");
    }

    public static SearchIndex getSearchIndexRandomSampleGenerator() {
        return new SearchIndex()
            .id(longCount.incrementAndGet())
            .documentId(longCount.incrementAndGet())
            .tags(UUID.randomUUID().toString())
            .correspondents(UUID.randomUUID().toString());
    }
}
