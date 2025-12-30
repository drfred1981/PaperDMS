package fr.smartprod.paperdms.export.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ExportJobTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ExportJob getExportJobSample1() {
        return new ExportJob()
            .id(1L)
            .name("name1")
            .s3ExportKey("s3ExportKey1")
            .exportSize(1L)
            .documentCount(1)
            .filesGenerated(1)
            .createdBy("createdBy1");
    }

    public static ExportJob getExportJobSample2() {
        return new ExportJob()
            .id(2L)
            .name("name2")
            .s3ExportKey("s3ExportKey2")
            .exportSize(2L)
            .documentCount(2)
            .filesGenerated(2)
            .createdBy("createdBy2");
    }

    public static ExportJob getExportJobRandomSampleGenerator() {
        return new ExportJob()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .s3ExportKey(UUID.randomUUID().toString())
            .exportSize(longCount.incrementAndGet())
            .documentCount(intCount.incrementAndGet())
            .filesGenerated(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString());
    }
}
