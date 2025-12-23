package fr.smartprod.paperdms.monitoring.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentWatchTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static DocumentWatch getDocumentWatchSample1() {
        return new DocumentWatch().id(1L).documentId(1L).userId("userId1");
    }

    public static DocumentWatch getDocumentWatchSample2() {
        return new DocumentWatch().id(2L).documentId(2L).userId("userId2");
    }

    public static DocumentWatch getDocumentWatchRandomSampleGenerator() {
        return new DocumentWatch()
            .id(longCount.incrementAndGet())
            .documentId(longCount.incrementAndGet())
            .userId(UUID.randomUUID().toString());
    }
}
