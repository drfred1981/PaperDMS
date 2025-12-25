package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentAuditTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DocumentAudit getDocumentAuditSample1() {
        return new DocumentAudit().id(1L).documentId(1L).documentSha256("documentSha2561").userId("userId1").userIp("userIp1");
    }

    public static DocumentAudit getDocumentAuditSample2() {
        return new DocumentAudit().id(2L).documentId(2L).documentSha256("documentSha2562").userId("userId2").userIp("userIp2");
    }

    public static DocumentAudit getDocumentAuditRandomSampleGenerator() {
        return new DocumentAudit()
            .id(longCount.incrementAndGet())
            .documentId(longCount.incrementAndGet())
            .documentSha256(UUID.randomUUID().toString())
            .userId(UUID.randomUUID().toString())
            .userIp(UUID.randomUUID().toString());
    }
}
