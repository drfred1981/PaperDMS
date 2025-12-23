package fr.smartprod.paperdms.notification.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class WebhookLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static WebhookLog getWebhookLogSample1() {
        return new WebhookLog().id(1L).subscriptionId(1L).eventType("eventType1").responseStatus(1).responseTime(1L).attemptNumber(1);
    }

    public static WebhookLog getWebhookLogSample2() {
        return new WebhookLog().id(2L).subscriptionId(2L).eventType("eventType2").responseStatus(2).responseTime(2L).attemptNumber(2);
    }

    public static WebhookLog getWebhookLogRandomSampleGenerator() {
        return new WebhookLog()
            .id(longCount.incrementAndGet())
            .subscriptionId(longCount.incrementAndGet())
            .eventType(UUID.randomUUID().toString())
            .responseStatus(intCount.incrementAndGet())
            .responseTime(longCount.incrementAndGet())
            .attemptNumber(intCount.incrementAndGet());
    }
}
