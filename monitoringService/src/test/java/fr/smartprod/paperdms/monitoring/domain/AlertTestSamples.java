package fr.smartprod.paperdms.monitoring.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlertTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Alert getAlertSample1() {
        return new Alert()
            .id(1L)
            .alertRuleId(1L)
            .title("title1")
            .entityType("entityType1")
            .entityId(1L)
            .acknowledgedBy("acknowledgedBy1")
            .resolvedBy("resolvedBy1");
    }

    public static Alert getAlertSample2() {
        return new Alert()
            .id(2L)
            .alertRuleId(2L)
            .title("title2")
            .entityType("entityType2")
            .entityId(2L)
            .acknowledgedBy("acknowledgedBy2")
            .resolvedBy("resolvedBy2");
    }

    public static Alert getAlertRandomSampleGenerator() {
        return new Alert()
            .id(longCount.incrementAndGet())
            .alertRuleId(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .entityType(UUID.randomUUID().toString())
            .entityId(longCount.incrementAndGet())
            .acknowledgedBy(UUID.randomUUID().toString())
            .resolvedBy(UUID.randomUUID().toString());
    }
}
