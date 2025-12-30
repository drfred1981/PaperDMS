package fr.smartprod.paperdms.notification.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NotificationTemplateTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NotificationTemplate getNotificationTemplateSample1() {
        return new NotificationTemplate().id(1L).name("name1").subject("subject1");
    }

    public static NotificationTemplate getNotificationTemplateSample2() {
        return new NotificationTemplate().id(2L).name("name2").subject("subject2");
    }

    public static NotificationTemplate getNotificationTemplateRandomSampleGenerator() {
        return new NotificationTemplate()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .subject(UUID.randomUUID().toString());
    }
}
