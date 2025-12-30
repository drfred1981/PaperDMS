package fr.smartprod.paperdms.reporting.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReportingScheduledReportTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ReportingScheduledReport getReportingScheduledReportSample1() {
        return new ReportingScheduledReport().id(1L).name("name1").schedule("schedule1").createdBy("createdBy1");
    }

    public static ReportingScheduledReport getReportingScheduledReportSample2() {
        return new ReportingScheduledReport().id(2L).name("name2").schedule("schedule2").createdBy("createdBy2");
    }

    public static ReportingScheduledReport getReportingScheduledReportRandomSampleGenerator() {
        return new ReportingScheduledReport()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .schedule(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString());
    }
}
