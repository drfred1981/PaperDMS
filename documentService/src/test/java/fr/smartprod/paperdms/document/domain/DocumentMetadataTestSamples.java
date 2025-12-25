package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentMetadataTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DocumentMetadata getDocumentMetadataSample1() {
        return new DocumentMetadata().id(1L).key("key1");
    }

    public static DocumentMetadata getDocumentMetadataSample2() {
        return new DocumentMetadata().id(2L).key("key2");
    }

    public static DocumentMetadata getDocumentMetadataRandomSampleGenerator() {
        return new DocumentMetadata().id(longCount.incrementAndGet()).key(UUID.randomUUID().toString());
    }
}
