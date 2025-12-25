package fr.smartprod.paperdms.reporting.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ScheduledReportTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ScheduledReport getScheduledReportSample1() {
        return new ScheduledReport().id(1L).name("name1").schedule("schedule1").createdBy("createdBy1");
    }

    public static ScheduledReport getScheduledReportSample2() {
        return new ScheduledReport().id(2L).name("name2").schedule("schedule2").createdBy("createdBy2");
    }

    public static ScheduledReport getScheduledReportRandomSampleGenerator() {
        return new ScheduledReport()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .schedule(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString());
    }
}
