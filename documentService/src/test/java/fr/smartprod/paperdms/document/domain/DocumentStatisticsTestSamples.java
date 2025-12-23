package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentStatisticsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static DocumentStatistics getDocumentStatisticsSample1() {
        return new DocumentStatistics().id(1L).documentId(1L).viewsTotal(1).downloadsTotal(1).uniqueViewers(1);
    }

    public static DocumentStatistics getDocumentStatisticsSample2() {
        return new DocumentStatistics().id(2L).documentId(2L).viewsTotal(2).downloadsTotal(2).uniqueViewers(2);
    }

    public static DocumentStatistics getDocumentStatisticsRandomSampleGenerator() {
        return new DocumentStatistics()
            .id(longCount.incrementAndGet())
            .documentId(longCount.incrementAndGet())
            .viewsTotal(intCount.incrementAndGet())
            .downloadsTotal(intCount.incrementAndGet())
            .uniqueViewers(intCount.incrementAndGet());
    }
}
