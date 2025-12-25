package fr.smartprod.paperdms.business.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ManualChapterTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ManualChapter getManualChapterSample1() {
        return new ManualChapter()
            .id(1L)
            .manualId(1L)
            .chapterNumber("chapterNumber1")
            .title("title1")
            .pageStart(1)
            .pageEnd(1)
            .level(1)
            .displayOrder(1);
    }

    public static ManualChapter getManualChapterSample2() {
        return new ManualChapter()
            .id(2L)
            .manualId(2L)
            .chapterNumber("chapterNumber2")
            .title("title2")
            .pageStart(2)
            .pageEnd(2)
            .level(2)
            .displayOrder(2);
    }

    public static ManualChapter getManualChapterRandomSampleGenerator() {
        return new ManualChapter()
            .id(longCount.incrementAndGet())
            .manualId(longCount.incrementAndGet())
            .chapterNumber(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .pageStart(intCount.incrementAndGet())
            .pageEnd(intCount.incrementAndGet())
            .level(intCount.incrementAndGet())
            .displayOrder(intCount.incrementAndGet());
    }
}
