package fr.smartprod.paperdms.transform.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RedactionJobTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static RedactionJob getRedactionJobSample1() {
        return new RedactionJob()
            .id(1L)
            .documentId(1L)
            .redactionColor("redactionColor1")
            .replaceWith("replaceWith1")
            .outputS3Key("outputS3Key1")
            .outputDocumentId(1L)
            .createdBy("createdBy1");
    }

    public static RedactionJob getRedactionJobSample2() {
        return new RedactionJob()
            .id(2L)
            .documentId(2L)
            .redactionColor("redactionColor2")
            .replaceWith("replaceWith2")
            .outputS3Key("outputS3Key2")
            .outputDocumentId(2L)
            .createdBy("createdBy2");
    }

    public static RedactionJob getRedactionJobRandomSampleGenerator() {
        return new RedactionJob()
            .id(longCount.incrementAndGet())
            .documentId(longCount.incrementAndGet())
            .redactionColor(UUID.randomUUID().toString())
            .replaceWith(UUID.randomUUID().toString())
            .outputS3Key(UUID.randomUUID().toString())
            .outputDocumentId(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString());
    }
}
