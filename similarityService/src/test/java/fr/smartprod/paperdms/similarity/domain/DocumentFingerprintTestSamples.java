package fr.smartprod.paperdms.similarity.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentFingerprintTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static DocumentFingerprint getDocumentFingerprintSample1() {
        return new DocumentFingerprint().id(1L).documentId(1L);
    }

    public static DocumentFingerprint getDocumentFingerprintSample2() {
        return new DocumentFingerprint().id(2L).documentId(2L);
    }

    public static DocumentFingerprint getDocumentFingerprintRandomSampleGenerator() {
        return new DocumentFingerprint().id(longCount.incrementAndGet()).documentId(longCount.incrementAndGet());
    }
}
