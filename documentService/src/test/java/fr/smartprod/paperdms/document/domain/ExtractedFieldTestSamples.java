package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ExtractedFieldTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ExtractedField getExtractedFieldSample1() {
        return new ExtractedField().id(1L).documentId(1L).fieldKey("fieldKey1");
    }

    public static ExtractedField getExtractedFieldSample2() {
        return new ExtractedField().id(2L).documentId(2L).fieldKey("fieldKey2");
    }

    public static ExtractedField getExtractedFieldRandomSampleGenerator() {
        return new ExtractedField()
            .id(longCount.incrementAndGet())
            .documentId(longCount.incrementAndGet())
            .fieldKey(UUID.randomUUID().toString());
    }
}
