package fr.smartprod.paperdms.similarity.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SimilarityDocumentComparisonTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SimilarityDocumentComparison getSimilarityDocumentComparisonSample1() {
        return new SimilarityDocumentComparison()
            .id(1L)
            .sourceDocumentSha256("sourceDocumentSha2561")
            .targetDocumentSha256("targetDocumentSha2561")
            .reviewedBy("reviewedBy1");
    }

    public static SimilarityDocumentComparison getSimilarityDocumentComparisonSample2() {
        return new SimilarityDocumentComparison()
            .id(2L)
            .sourceDocumentSha256("sourceDocumentSha2562")
            .targetDocumentSha256("targetDocumentSha2562")
            .reviewedBy("reviewedBy2");
    }

    public static SimilarityDocumentComparison getSimilarityDocumentComparisonRandomSampleGenerator() {
        return new SimilarityDocumentComparison()
            .id(longCount.incrementAndGet())
            .sourceDocumentSha256(UUID.randomUUID().toString())
            .targetDocumentSha256(UUID.randomUUID().toString())
            .reviewedBy(UUID.randomUUID().toString());
    }
}
