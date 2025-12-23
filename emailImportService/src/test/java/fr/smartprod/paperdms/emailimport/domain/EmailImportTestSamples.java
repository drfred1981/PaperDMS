package fr.smartprod.paperdms.emailimport.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EmailImportTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static EmailImport getEmailImportSample1() {
        return new EmailImport()
            .id(1L)
            .fromEmail("fromEmail1")
            .toEmail("toEmail1")
            .subject("subject1")
            .folderId(1L)
            .documentTypeId(1L)
            .attachmentCount(1)
            .documentsCreated(1)
            .appliedRuleId(1L);
    }

    public static EmailImport getEmailImportSample2() {
        return new EmailImport()
            .id(2L)
            .fromEmail("fromEmail2")
            .toEmail("toEmail2")
            .subject("subject2")
            .folderId(2L)
            .documentTypeId(2L)
            .attachmentCount(2)
            .documentsCreated(2)
            .appliedRuleId(2L);
    }

    public static EmailImport getEmailImportRandomSampleGenerator() {
        return new EmailImport()
            .id(longCount.incrementAndGet())
            .fromEmail(UUID.randomUUID().toString())
            .toEmail(UUID.randomUUID().toString())
            .subject(UUID.randomUUID().toString())
            .folderId(longCount.incrementAndGet())
            .documentTypeId(longCount.incrementAndGet())
            .attachmentCount(intCount.incrementAndGet())
            .documentsCreated(intCount.incrementAndGet())
            .appliedRuleId(longCount.incrementAndGet());
    }
}
