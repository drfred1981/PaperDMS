package fr.smartprod.paperdms.monitoring.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MonitoringAlertRuleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MonitoringAlertRule getMonitoringAlertRuleSample1() {
        return new MonitoringAlertRule().id(1L).name("name1").triggerCount(1).createdBy("createdBy1");
    }

    public static MonitoringAlertRule getMonitoringAlertRuleSample2() {
        return new MonitoringAlertRule().id(2L).name("name2").triggerCount(2).createdBy("createdBy2");
    }

    public static MonitoringAlertRule getMonitoringAlertRuleRandomSampleGenerator() {
        return new MonitoringAlertRule()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .triggerCount(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString());
    }
}
