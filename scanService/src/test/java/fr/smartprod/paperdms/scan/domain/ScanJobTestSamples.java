package fr.smartprod.paperdms.scan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ScanJobTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ScanJob getScanJobSample1() {
        return new ScanJob()
            .id(1L)
            .name("name1")
            .scannerConfigId(1L)
            .batchId(1L)
            .documentTypeId(1L)
            .folderId(1L)
            .pageCount(1)
            .resolution(1)
            .createdBy("createdBy1");
    }

    public static ScanJob getScanJobSample2() {
        return new ScanJob()
            .id(2L)
            .name("name2")
            .scannerConfigId(2L)
            .batchId(2L)
            .documentTypeId(2L)
            .folderId(2L)
            .pageCount(2)
            .resolution(2)
            .createdBy("createdBy2");
    }

    public static ScanJob getScanJobRandomSampleGenerator() {
        return new ScanJob()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .scannerConfigId(longCount.incrementAndGet())
            .batchId(longCount.incrementAndGet())
            .documentTypeId(longCount.incrementAndGet())
            .folderId(longCount.incrementAndGet())
            .pageCount(intCount.incrementAndGet())
            .resolution(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString());
    }
}
