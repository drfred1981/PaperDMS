package fr.smartprod.paperdms.similarity.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentSimilarityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static DocumentSimilarity getDocumentSimilaritySample1() {
        return new DocumentSimilarity().id(1L).documentId1(1L).documentId2(1L).reviewedBy("reviewedBy1");
    }

    public static DocumentSimilarity getDocumentSimilaritySample2() {
        return new DocumentSimilarity().id(2L).documentId1(2L).documentId2(2L).reviewedBy("reviewedBy2");
    }

    public static DocumentSimilarity getDocumentSimilarityRandomSampleGenerator() {
        return new DocumentSimilarity()
            .id(longCount.incrementAndGet())
            .documentId1(longCount.incrementAndGet())
            .documentId2(longCount.incrementAndGet())
            .reviewedBy(UUID.randomUUID().toString());
    }
}
