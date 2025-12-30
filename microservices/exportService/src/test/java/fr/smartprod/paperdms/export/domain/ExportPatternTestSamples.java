package fr.smartprod.paperdms.export.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ExportPatternTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ExportPattern getExportPatternSample1() {
        return new ExportPattern()
            .id(1L)
            .name("name1")
            .pathTemplate("pathTemplate1")
            .fileNameTemplate("fileNameTemplate1")
            .usageCount(1)
            .createdBy("createdBy1");
    }

    public static ExportPattern getExportPatternSample2() {
        return new ExportPattern()
            .id(2L)
            .name("name2")
            .pathTemplate("pathTemplate2")
            .fileNameTemplate("fileNameTemplate2")
            .usageCount(2)
            .createdBy("createdBy2");
    }

    public static ExportPattern getExportPatternRandomSampleGenerator() {
        return new ExportPattern()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .pathTemplate(UUID.randomUUID().toString())
            .fileNameTemplate(UUID.randomUUID().toString())
            .usageCount(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString());
    }
}
