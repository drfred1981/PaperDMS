package fr.smartprod.paperdms.monitoring.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MonitoringServiceStatusTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MonitoringServiceStatus getMonitoringServiceStatusSample1() {
        return new MonitoringServiceStatus()
            .id(1L)
            .serviceName("serviceName1")
            .serviceType("serviceType1")
            .endpoint("endpoint1")
            .port(1)
            .version("version1");
    }

    public static MonitoringServiceStatus getMonitoringServiceStatusSample2() {
        return new MonitoringServiceStatus()
            .id(2L)
            .serviceName("serviceName2")
            .serviceType("serviceType2")
            .endpoint("endpoint2")
            .port(2)
            .version("version2");
    }

    public static MonitoringServiceStatus getMonitoringServiceStatusRandomSampleGenerator() {
        return new MonitoringServiceStatus()
            .id(longCount.incrementAndGet())
            .serviceName(UUID.randomUUID().toString())
            .serviceType(UUID.randomUUID().toString())
            .endpoint(UUID.randomUUID().toString())
            .port(intCount.incrementAndGet())
            .version(UUID.randomUUID().toString());
    }
}
