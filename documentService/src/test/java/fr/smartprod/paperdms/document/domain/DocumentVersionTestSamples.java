package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentVersionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static DocumentVersion getDocumentVersionSample1() {
        return new DocumentVersion().id(1L).versionNumber(1).sha256("sha2561").s3Key("s3Key1").fileSize(1L).createdBy("createdBy1");
    }

    public static DocumentVersion getDocumentVersionSample2() {
        return new DocumentVersion().id(2L).versionNumber(2).sha256("sha2562").s3Key("s3Key2").fileSize(2L).createdBy("createdBy2");
    }

    public static DocumentVersion getDocumentVersionRandomSampleGenerator() {
        return new DocumentVersion()
            .id(longCount.incrementAndGet())
            .versionNumber(intCount.incrementAndGet())
            .sha256(UUID.randomUUID().toString())
            .s3Key(UUID.randomUUID().toString())
            .fileSize(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString());
    }
}
