package fr.smartprod.paperdms.reporting.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ReportingExecutionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ReportingExecution getReportingExecutionSample1() {
        return new ReportingExecution().id(1L).recordsProcessed(1).outputS3Key("outputS3Key1").outputSize(1L);
    }

    public static ReportingExecution getReportingExecutionSample2() {
        return new ReportingExecution().id(2L).recordsProcessed(2).outputS3Key("outputS3Key2").outputSize(2L);
    }

    public static ReportingExecution getReportingExecutionRandomSampleGenerator() {
        return new ReportingExecution()
            .id(longCount.incrementAndGet())
            .recordsProcessed(intCount.incrementAndGet())
            .outputS3Key(UUID.randomUUID().toString())
            .outputSize(longCount.incrementAndGet());
    }
}
