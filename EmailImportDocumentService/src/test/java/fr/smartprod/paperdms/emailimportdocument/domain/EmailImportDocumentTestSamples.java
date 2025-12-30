package fr.smartprod.paperdms.emailimportdocument.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EmailImportDocumentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static EmailImportDocument getEmailImportDocumentSample1() {
        return new EmailImportDocument()
            .id(1L)
            .sha256("sha2561")
            .fromEmail("fromEmail1")
            .toEmail("toEmail1")
            .subject("subject1")
            .attachmentCount(1)
            .documentsCreated(1)
            .documentSha256("documentSha2561");
    }

    public static EmailImportDocument getEmailImportDocumentSample2() {
        return new EmailImportDocument()
            .id(2L)
            .sha256("sha2562")
            .fromEmail("fromEmail2")
            .toEmail("toEmail2")
            .subject("subject2")
            .attachmentCount(2)
            .documentsCreated(2)
            .documentSha256("documentSha2562");
    }

    public static EmailImportDocument getEmailImportDocumentRandomSampleGenerator() {
        return new EmailImportDocument()
            .id(longCount.incrementAndGet())
            .sha256(UUID.randomUUID().toString())
            .fromEmail(UUID.randomUUID().toString())
            .toEmail(UUID.randomUUID().toString())
            .subject(UUID.randomUUID().toString())
            .attachmentCount(intCount.incrementAndGet())
            .documentsCreated(intCount.incrementAndGet())
            .documentSha256(UUID.randomUUID().toString());
    }
}
