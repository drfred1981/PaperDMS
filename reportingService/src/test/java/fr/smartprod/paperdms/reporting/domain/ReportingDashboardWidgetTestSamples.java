package fr.smartprod.paperdms.reporting.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ReportingDashboardWidgetTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ReportingDashboardWidget getReportingDashboardWidgetSample1() {
        return new ReportingDashboardWidget().id(1L).title("title1").dataSource("dataSource1").position(1).sizeX(1).sizeY(1);
    }

    public static ReportingDashboardWidget getReportingDashboardWidgetSample2() {
        return new ReportingDashboardWidget().id(2L).title("title2").dataSource("dataSource2").position(2).sizeX(2).sizeY(2);
    }

    public static ReportingDashboardWidget getReportingDashboardWidgetRandomSampleGenerator() {
        return new ReportingDashboardWidget()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .dataSource(UUID.randomUUID().toString())
            .position(intCount.incrementAndGet())
            .sizeX(intCount.incrementAndGet())
            .sizeY(intCount.incrementAndGet());
    }
}
