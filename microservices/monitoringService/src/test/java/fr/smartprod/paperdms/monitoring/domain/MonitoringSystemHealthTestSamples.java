package fr.smartprod.paperdms.monitoring.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MonitoringSystemHealthTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MonitoringSystemHealth getMonitoringSystemHealthSample1() {
        return new MonitoringSystemHealth().id(1L).serviceName("serviceName1").version("version1").uptime(1L);
    }

    public static MonitoringSystemHealth getMonitoringSystemHealthSample2() {
        return new MonitoringSystemHealth().id(2L).serviceName("serviceName2").version("version2").uptime(2L);
    }

    public static MonitoringSystemHealth getMonitoringSystemHealthRandomSampleGenerator() {
        return new MonitoringSystemHealth()
            .id(longCount.incrementAndGet())
            .serviceName(UUID.randomUUID().toString())
            .version(UUID.randomUUID().toString())
            .uptime(longCount.incrementAndGet());
    }
}
