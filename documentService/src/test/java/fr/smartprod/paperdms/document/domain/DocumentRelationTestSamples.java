package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentRelationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DocumentRelation getDocumentRelationSample1() {
        return new DocumentRelation().id(1L).sourceDocumentId(1L).targetDocumentId(1L).createdBy("createdBy1");
    }

    public static DocumentRelation getDocumentRelationSample2() {
        return new DocumentRelation().id(2L).sourceDocumentId(2L).targetDocumentId(2L).createdBy("createdBy2");
    }

    public static DocumentRelation getDocumentRelationRandomSampleGenerator() {
        return new DocumentRelation()
            .id(longCount.incrementAndGet())
            .sourceDocumentId(longCount.incrementAndGet())
            .targetDocumentId(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString());
    }
}
