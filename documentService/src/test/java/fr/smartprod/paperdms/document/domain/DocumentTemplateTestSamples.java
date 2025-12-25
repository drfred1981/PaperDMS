package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentTemplateTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DocumentTemplate getDocumentTemplateSample1() {
        return new DocumentTemplate()
            .id(1L)
            .name("name1")
            .templateSha256("templateSha2561")
            .templateS3Key("templateS3Key1")
            .createdBy("createdBy1");
    }

    public static DocumentTemplate getDocumentTemplateSample2() {
        return new DocumentTemplate()
            .id(2L)
            .name("name2")
            .templateSha256("templateSha2562")
            .templateS3Key("templateS3Key2")
            .createdBy("createdBy2");
    }

    public static DocumentTemplate getDocumentTemplateRandomSampleGenerator() {
        return new DocumentTemplate()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .templateSha256(UUID.randomUUID().toString())
            .templateS3Key(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString());
    }
}
