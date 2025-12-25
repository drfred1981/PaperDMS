package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentPermissionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DocumentPermission getDocumentPermissionSample1() {
        return new DocumentPermission().id(1L).documentId(1L).principalId("principalId1").grantedBy("grantedBy1");
    }

    public static DocumentPermission getDocumentPermissionSample2() {
        return new DocumentPermission().id(2L).documentId(2L).principalId("principalId2").grantedBy("grantedBy2");
    }

    public static DocumentPermission getDocumentPermissionRandomSampleGenerator() {
        return new DocumentPermission()
            .id(longCount.incrementAndGet())
            .documentId(longCount.incrementAndGet())
            .principalId(UUID.randomUUID().toString())
            .grantedBy(UUID.randomUUID().toString());
    }
}
