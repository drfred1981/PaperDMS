package fr.smartprod.paperdms.notification.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NotificationEventTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NotificationEvent getNotificationEventSample1() {
        return new NotificationEvent().id(1L).eventType("eventType1").entityType("entityType1").entityName("entityName1").userId("userId1");
    }

    public static NotificationEvent getNotificationEventSample2() {
        return new NotificationEvent().id(2L).eventType("eventType2").entityType("entityType2").entityName("entityName2").userId("userId2");
    }

    public static NotificationEvent getNotificationEventRandomSampleGenerator() {
        return new NotificationEvent()
            .id(longCount.incrementAndGet())
            .eventType(UUID.randomUUID().toString())
            .entityType(UUID.randomUUID().toString())
            .entityName(UUID.randomUUID().toString())
            .userId(UUID.randomUUID().toString());
    }
}
