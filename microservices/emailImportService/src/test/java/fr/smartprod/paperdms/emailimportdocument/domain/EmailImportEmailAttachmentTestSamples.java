package fr.smartprod.paperdms.emailimportdocument.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EmailImportEmailAttachmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EmailImportEmailAttachment getEmailImportEmailAttachmentSample1() {
        return new EmailImportEmailAttachment()
            .id(1L)
            .fileName("fileName1")
            .fileSize(1L)
            .mimeType("mimeType1")
            .sha256("sha2561")
            .s3Key("s3Key1")
            .documentSha256("documentSha2561");
    }

    public static EmailImportEmailAttachment getEmailImportEmailAttachmentSample2() {
        return new EmailImportEmailAttachment()
            .id(2L)
            .fileName("fileName2")
            .fileSize(2L)
            .mimeType("mimeType2")
            .sha256("sha2562")
            .s3Key("s3Key2")
            .documentSha256("documentSha2562");
    }

    public static EmailImportEmailAttachment getEmailImportEmailAttachmentRandomSampleGenerator() {
        return new EmailImportEmailAttachment()
            .id(longCount.incrementAndGet())
            .fileName(UUID.randomUUID().toString())
            .fileSize(longCount.incrementAndGet())
            .mimeType(UUID.randomUUID().toString())
            .sha256(UUID.randomUUID().toString())
            .s3Key(UUID.randomUUID().toString())
            .documentSha256(UUID.randomUUID().toString());
    }
}
