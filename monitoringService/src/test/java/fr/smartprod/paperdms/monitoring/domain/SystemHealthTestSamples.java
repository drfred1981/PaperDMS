package fr.smartprod.paperdms.monitoring.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SystemHealthTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SystemHealth getSystemHealthSample1() {
        return new SystemHealth().id(1L).serviceName("serviceName1").version("version1").uptime(1L);
    }

    public static SystemHealth getSystemHealthSample2() {
        return new SystemHealth().id(2L).serviceName("serviceName2").version("version2").uptime(2L);
    }

    public static SystemHealth getSystemHealthRandomSampleGenerator() {
        return new SystemHealth()
            .id(longCount.incrementAndGet())
            .serviceName(UUID.randomUUID().toString())
            .version(UUID.randomUUID().toString())
            .uptime(longCount.incrementAndGet());
    }
}
