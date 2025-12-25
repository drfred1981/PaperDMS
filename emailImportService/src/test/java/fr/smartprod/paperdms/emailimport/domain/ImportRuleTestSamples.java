package fr.smartprod.paperdms.emailimport.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ImportRuleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ImportRule getImportRuleSample1() {
        return new ImportRule().id(1L).name("name1").priority(1).folderId(1L).documentTypeId(1L).matchCount(1).createdBy("createdBy1");
    }

    public static ImportRule getImportRuleSample2() {
        return new ImportRule().id(2L).name("name2").priority(2).folderId(2L).documentTypeId(2L).matchCount(2).createdBy("createdBy2");
    }

    public static ImportRule getImportRuleRandomSampleGenerator() {
        return new ImportRule()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .priority(intCount.incrementAndGet())
            .folderId(longCount.incrementAndGet())
            .documentTypeId(longCount.incrementAndGet())
            .matchCount(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString());
    }
}
