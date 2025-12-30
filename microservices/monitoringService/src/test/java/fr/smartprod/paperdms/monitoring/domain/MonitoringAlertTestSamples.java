package fr.smartprod.paperdms.monitoring.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MonitoringAlertTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MonitoringAlert getMonitoringAlertSample1() {
        return new MonitoringAlert()
            .id(1L)
            .title("title1")
            .entityType("entityType1")
            .entityName("entityName1")
            .acknowledgedBy("acknowledgedBy1")
            .resolvedBy("resolvedBy1");
    }

    public static MonitoringAlert getMonitoringAlertSample2() {
        return new MonitoringAlert()
            .id(2L)
            .title("title2")
            .entityType("entityType2")
            .entityName("entityName2")
            .acknowledgedBy("acknowledgedBy2")
            .resolvedBy("resolvedBy2");
    }

    public static MonitoringAlert getMonitoringAlertRandomSampleGenerator() {
        return new MonitoringAlert()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .entityType(UUID.randomUUID().toString())
            .entityName(UUID.randomUUID().toString())
            .acknowledgedBy(UUID.randomUUID().toString())
            .resolvedBy(UUID.randomUUID().toString());
    }
}
