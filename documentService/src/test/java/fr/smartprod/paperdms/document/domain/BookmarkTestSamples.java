package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BookmarkTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Bookmark getBookmarkSample1() {
        return new Bookmark().id(1L).userId("userId1").entityId(1L);
    }

    public static Bookmark getBookmarkSample2() {
        return new Bookmark().id(2L).userId("userId2").entityId(2L);
    }

    public static Bookmark getBookmarkRandomSampleGenerator() {
        return new Bookmark().id(longCount.incrementAndGet()).userId(UUID.randomUUID().toString()).entityId(longCount.incrementAndGet());
    }
}
