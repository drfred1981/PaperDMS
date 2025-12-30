package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentExtractedFieldTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DocumentExtractedField getDocumentExtractedFieldSample1() {
        return new DocumentExtractedField().id(1L).fieldKey("fieldKey1");
    }

    public static DocumentExtractedField getDocumentExtractedFieldSample2() {
        return new DocumentExtractedField().id(2L).fieldKey("fieldKey2");
    }

    public static DocumentExtractedField getDocumentExtractedFieldRandomSampleGenerator() {
        return new DocumentExtractedField().id(longCount.incrementAndGet()).fieldKey(UUID.randomUUID().toString());
    }
}
