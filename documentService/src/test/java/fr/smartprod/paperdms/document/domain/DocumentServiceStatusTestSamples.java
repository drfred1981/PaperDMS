package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentServiceStatusTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static DocumentServiceStatus getDocumentServiceStatusSample1() {
        return new DocumentServiceStatus().id(1L).retryCount(1).processingDuration(1L).jobId("jobId1").priority(1).updatedBy("updatedBy1");
    }

    public static DocumentServiceStatus getDocumentServiceStatusSample2() {
        return new DocumentServiceStatus().id(2L).retryCount(2).processingDuration(2L).jobId("jobId2").priority(2).updatedBy("updatedBy2");
    }

    public static DocumentServiceStatus getDocumentServiceStatusRandomSampleGenerator() {
        return new DocumentServiceStatus()
            .id(longCount.incrementAndGet())
            .retryCount(intCount.incrementAndGet())
            .processingDuration(longCount.incrementAndGet())
            .jobId(UUID.randomUUID().toString())
            .priority(intCount.incrementAndGet())
            .updatedBy(UUID.randomUUID().toString());
    }
}
