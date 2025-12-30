package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentTypeFieldTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DocumentTypeField getDocumentTypeFieldSample1() {
        return new DocumentTypeField().id(1L).fieldKey("fieldKey1").fieldLabel("fieldLabel1");
    }

    public static DocumentTypeField getDocumentTypeFieldSample2() {
        return new DocumentTypeField().id(2L).fieldKey("fieldKey2").fieldLabel("fieldLabel2");
    }

    public static DocumentTypeField getDocumentTypeFieldRandomSampleGenerator() {
        return new DocumentTypeField()
            .id(longCount.incrementAndGet())
            .fieldKey(UUID.randomUUID().toString())
            .fieldLabel(UUID.randomUUID().toString());
    }
}
