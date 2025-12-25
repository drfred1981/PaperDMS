package fr.smartprod.paperdms.emailimport.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EmailAttachmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EmailAttachment getEmailAttachmentSample1() {
        return new EmailAttachment()
            .id(1L)
            .emailImportId(1L)
            .fileName("fileName1")
            .fileSize(1L)
            .mimeType("mimeType1")
            .sha256("sha2561")
            .s3Key("s3Key1")
            .documentId(1L);
    }

    public static EmailAttachment getEmailAttachmentSample2() {
        return new EmailAttachment()
            .id(2L)
            .emailImportId(2L)
            .fileName("fileName2")
            .fileSize(2L)
            .mimeType("mimeType2")
            .sha256("sha2562")
            .s3Key("s3Key2")
            .documentId(2L);
    }

    public static EmailAttachment getEmailAttachmentRandomSampleGenerator() {
        return new EmailAttachment()
            .id(longCount.incrementAndGet())
            .emailImportId(longCount.incrementAndGet())
            .fileName(UUID.randomUUID().toString())
            .fileSize(longCount.incrementAndGet())
            .mimeType(UUID.randomUUID().toString())
            .sha256(UUID.randomUUID().toString())
            .s3Key(UUID.randomUUID().toString())
            .documentId(longCount.incrementAndGet());
    }
}
