package fr.smartprod.paperdms.notification.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NotificationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Notification getNotificationSample1() {
        return new Notification()
            .id(1L)
            .title("title1")
            .recipientId("recipientId1")
            .relatedEntityType("relatedEntityType1")
            .relatedEntityId(1L)
            .actionUrl("actionUrl1");
    }

    public static Notification getNotificationSample2() {
        return new Notification()
            .id(2L)
            .title("title2")
            .recipientId("recipientId2")
            .relatedEntityType("relatedEntityType2")
            .relatedEntityId(2L)
            .actionUrl("actionUrl2");
    }

    public static Notification getNotificationRandomSampleGenerator() {
        return new Notification()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .recipientId(UUID.randomUUID().toString())
            .relatedEntityType(UUID.randomUUID().toString())
            .relatedEntityId(longCount.incrementAndGet())
            .actionUrl(UUID.randomUUID().toString());
    }
}
