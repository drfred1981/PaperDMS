package fr.smartprod.paperdms.monitoring.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MonitoringMaintenanceTaskTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MonitoringMaintenanceTask getMonitoringMaintenanceTaskSample1() {
        return new MonitoringMaintenanceTask()
            .id(1L)
            .name("name1")
            .schedule("schedule1")
            .duration(1L)
            .recordsProcessed(1)
            .createdBy("createdBy1");
    }

    public static MonitoringMaintenanceTask getMonitoringMaintenanceTaskSample2() {
        return new MonitoringMaintenanceTask()
            .id(2L)
            .name("name2")
            .schedule("schedule2")
            .duration(2L)
            .recordsProcessed(2)
            .createdBy("createdBy2");
    }

    public static MonitoringMaintenanceTask getMonitoringMaintenanceTaskRandomSampleGenerator() {
        return new MonitoringMaintenanceTask()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .schedule(UUID.randomUUID().toString())
            .duration(longCount.incrementAndGet())
            .recordsProcessed(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString());
    }
}
