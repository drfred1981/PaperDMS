package fr.smartprod.paperdms.reporting.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SystemMetricTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SystemMetric getSystemMetricSample1() {
        return new SystemMetric().id(1L).metricName("metricName1").networkIn(1L).networkOut(1L).activeConnections(1);
    }

    public static SystemMetric getSystemMetricSample2() {
        return new SystemMetric().id(2L).metricName("metricName2").networkIn(2L).networkOut(2L).activeConnections(2);
    }

    public static SystemMetric getSystemMetricRandomSampleGenerator() {
        return new SystemMetric()
            .id(longCount.incrementAndGet())
            .metricName(UUID.randomUUID().toString())
            .networkIn(longCount.incrementAndGet())
            .networkOut(longCount.incrementAndGet())
            .activeConnections(intCount.incrementAndGet());
    }
}
