package fr.smartprod.paperdms.scan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ScanBatchTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ScanBatch getScanBatchSample1() {
        return new ScanBatch().id(1L).name("name1").totalJobs(1).completedJobs(1).totalPages(1).createdBy("createdBy1");
    }

    public static ScanBatch getScanBatchSample2() {
        return new ScanBatch().id(2L).name("name2").totalJobs(2).completedJobs(2).totalPages(2).createdBy("createdBy2");
    }

    public static ScanBatch getScanBatchRandomSampleGenerator() {
        return new ScanBatch()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .totalJobs(intCount.incrementAndGet())
            .completedJobs(intCount.incrementAndGet())
            .totalPages(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString());
    }
}
