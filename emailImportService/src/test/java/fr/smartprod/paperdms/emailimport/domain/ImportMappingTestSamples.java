package fr.smartprod.paperdms.emailimport.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ImportMappingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static ImportMapping getImportMappingSample1() {
        return new ImportMapping()
            .id(1L)
            .ruleId(1L)
            .documentField("documentField1")
            .defaultValue("defaultValue1")
            .validationRegex("validationRegex1");
    }

    public static ImportMapping getImportMappingSample2() {
        return new ImportMapping()
            .id(2L)
            .ruleId(2L)
            .documentField("documentField2")
            .defaultValue("defaultValue2")
            .validationRegex("validationRegex2");
    }

    public static ImportMapping getImportMappingRandomSampleGenerator() {
        return new ImportMapping()
            .id(longCount.incrementAndGet())
            .ruleId(longCount.incrementAndGet())
            .documentField(UUID.randomUUID().toString())
            .defaultValue(UUID.randomUUID().toString())
            .validationRegex(UUID.randomUUID().toString());
    }
}
