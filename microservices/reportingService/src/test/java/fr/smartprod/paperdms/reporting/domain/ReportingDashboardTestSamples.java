package fr.smartprod.paperdms.reporting.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ReportingDashboardTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ReportingDashboard getReportingDashboardSample1() {
        return new ReportingDashboard().id(1L).name("name1").userId("userId1").refreshInterval(1);
    }

    public static ReportingDashboard getReportingDashboardSample2() {
        return new ReportingDashboard().id(2L).name("name2").userId("userId2").refreshInterval(2);
    }

    public static ReportingDashboard getReportingDashboardRandomSampleGenerator() {
        return new ReportingDashboard()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .userId(UUID.randomUUID().toString())
            .refreshInterval(intCount.incrementAndGet());
    }
}
