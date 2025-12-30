package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MetaBookmarkTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MetaBookmark getMetaBookmarkSample1() {
        return new MetaBookmark().id(1L).userId("userId1").entityName("entityName1");
    }

    public static MetaBookmark getMetaBookmarkSample2() {
        return new MetaBookmark().id(2L).userId("userId2").entityName("entityName2");
    }

    public static MetaBookmark getMetaBookmarkRandomSampleGenerator() {
        return new MetaBookmark()
            .id(longCount.incrementAndGet())
            .userId(UUID.randomUUID().toString())
            .entityName(UUID.randomUUID().toString());
    }
}
