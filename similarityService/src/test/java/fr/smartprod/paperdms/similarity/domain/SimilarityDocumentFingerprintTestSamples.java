package fr.smartprod.paperdms.similarity.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class SimilarityDocumentFingerprintTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SimilarityDocumentFingerprint getSimilarityDocumentFingerprintSample1() {
        return new SimilarityDocumentFingerprint().id(1L);
    }

    public static SimilarityDocumentFingerprint getSimilarityDocumentFingerprintSample2() {
        return new SimilarityDocumentFingerprint().id(2L);
    }

    public static SimilarityDocumentFingerprint getSimilarityDocumentFingerprintRandomSampleGenerator() {
        return new SimilarityDocumentFingerprint().id(longCount.incrementAndGet());
    }
}
