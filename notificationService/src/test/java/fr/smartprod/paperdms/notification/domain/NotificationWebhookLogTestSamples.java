package fr.smartprod.paperdms.notification.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NotificationWebhookLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static NotificationWebhookLog getNotificationWebhookLogSample1() {
        return new NotificationWebhookLog().id(1L).eventType("eventType1").responseStatus(1).responseTime(1L).attemptNumber(1);
    }

    public static NotificationWebhookLog getNotificationWebhookLogSample2() {
        return new NotificationWebhookLog().id(2L).eventType("eventType2").responseStatus(2).responseTime(2L).attemptNumber(2);
    }

    public static NotificationWebhookLog getNotificationWebhookLogRandomSampleGenerator() {
        return new NotificationWebhookLog()
            .id(longCount.incrementAndGet())
            .eventType(UUID.randomUUID().toString())
            .responseStatus(intCount.incrementAndGet())
            .responseTime(longCount.incrementAndGet())
            .attemptNumber(intCount.incrementAndGet());
    }
}
