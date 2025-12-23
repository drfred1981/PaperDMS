package fr.smartprod.paperdms.notification.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NotificationPreferenceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static NotificationPreference getNotificationPreferenceSample1() {
        return new NotificationPreference().id(1L).userId("userId1").quietHoursStart("quietHoursStart1").quietHoursEnd("quietHoursEnd1");
    }

    public static NotificationPreference getNotificationPreferenceSample2() {
        return new NotificationPreference().id(2L).userId("userId2").quietHoursStart("quietHoursStart2").quietHoursEnd("quietHoursEnd2");
    }

    public static NotificationPreference getNotificationPreferenceRandomSampleGenerator() {
        return new NotificationPreference()
            .id(longCount.incrementAndGet())
            .userId(UUID.randomUUID().toString())
            .quietHoursStart(UUID.randomUUID().toString())
            .quietHoursEnd(UUID.randomUUID().toString());
    }
}
