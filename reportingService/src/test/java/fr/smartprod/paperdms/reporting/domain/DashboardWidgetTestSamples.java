package fr.smartprod.paperdms.reporting.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DashboardWidgetTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static DashboardWidget getDashboardWidgetSample1() {
        return new DashboardWidget().id(1L).dashboardId(1L).title("title1").dataSource("dataSource1").position(1).sizeX(1).sizeY(1);
    }

    public static DashboardWidget getDashboardWidgetSample2() {
        return new DashboardWidget().id(2L).dashboardId(2L).title("title2").dataSource("dataSource2").position(2).sizeX(2).sizeY(2);
    }

    public static DashboardWidget getDashboardWidgetRandomSampleGenerator() {
        return new DashboardWidget()
            .id(longCount.incrementAndGet())
            .dashboardId(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .dataSource(UUID.randomUUID().toString())
            .position(intCount.incrementAndGet())
            .sizeX(intCount.incrementAndGet())
            .sizeY(intCount.incrementAndGet());
    }
}
