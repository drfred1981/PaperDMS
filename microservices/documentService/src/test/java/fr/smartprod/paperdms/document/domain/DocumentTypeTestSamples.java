package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DocumentType getDocumentTypeSample1() {
        return new DocumentType().id(1L).name("name1").code("code1").icon("icon1").color("color1").createdBy("createdBy1");
    }

    public static DocumentType getDocumentTypeSample2() {
        return new DocumentType().id(2L).name("name2").code("code2").icon("icon2").color("color2").createdBy("createdBy2");
    }

    public static DocumentType getDocumentTypeRandomSampleGenerator() {
        return new DocumentType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .icon(UUID.randomUUID().toString())
            .color(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString());
    }
}
