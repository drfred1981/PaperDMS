package fr.smartprod.paperdms.emailimportdocument.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EmailImportImportMappingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EmailImportImportMapping getEmailImportImportMappingSample1() {
        return new EmailImportImportMapping()
            .id(1L)
            .documentField("documentField1")
            .defaultValue("defaultValue1")
            .validationRegex("validationRegex1");
    }

    public static EmailImportImportMapping getEmailImportImportMappingSample2() {
        return new EmailImportImportMapping()
            .id(2L)
            .documentField("documentField2")
            .defaultValue("defaultValue2")
            .validationRegex("validationRegex2");
    }

    public static EmailImportImportMapping getEmailImportImportMappingRandomSampleGenerator() {
        return new EmailImportImportMapping()
            .id(longCount.incrementAndGet())
            .documentField(UUID.randomUUID().toString())
            .defaultValue(UUID.randomUUID().toString())
            .validationRegex(UUID.randomUUID().toString());
    }
}
