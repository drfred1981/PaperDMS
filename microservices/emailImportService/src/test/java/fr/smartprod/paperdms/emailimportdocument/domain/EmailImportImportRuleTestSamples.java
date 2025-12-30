package fr.smartprod.paperdms.emailimportdocument.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EmailImportImportRuleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static EmailImportImportRule getEmailImportImportRuleSample1() {
        return new EmailImportImportRule().id(1L).name("name1").priority(1).matchCount(1).createdBy("createdBy1");
    }

    public static EmailImportImportRule getEmailImportImportRuleSample2() {
        return new EmailImportImportRule().id(2L).name("name2").priority(2).matchCount(2).createdBy("createdBy2");
    }

    public static EmailImportImportRule getEmailImportImportRuleRandomSampleGenerator() {
        return new EmailImportImportRule()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .priority(intCount.incrementAndGet())
            .matchCount(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString());
    }
}
