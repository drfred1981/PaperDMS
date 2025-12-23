package fr.smartprod.paperdms.monitoring.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AlertRuleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AlertRule getAlertRuleSample1() {
        return new AlertRule().id(1L).name("name1").triggerCount(1).createdBy("createdBy1");
    }

    public static AlertRule getAlertRuleSample2() {
        return new AlertRule().id(2L).name("name2").triggerCount(2).createdBy("createdBy2");
    }

    public static AlertRule getAlertRuleRandomSampleGenerator() {
        return new AlertRule()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .triggerCount(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString());
    }
}
