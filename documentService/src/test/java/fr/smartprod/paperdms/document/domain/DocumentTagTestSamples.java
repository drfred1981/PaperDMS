package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentTagTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DocumentTag getDocumentTagSample1() {
        return new DocumentTag().id(1L).assignedBy("assignedBy1");
    }

    public static DocumentTag getDocumentTagSample2() {
        return new DocumentTag().id(2L).assignedBy("assignedBy2");
    }

    public static DocumentTag getDocumentTagRandomSampleGenerator() {
        return new DocumentTag().id(longCount.incrementAndGet()).assignedBy(UUID.randomUUID().toString());
    }
}
