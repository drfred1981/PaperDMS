package fr.smartprod.paperdms.notification.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class WebhookSubscriptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static WebhookSubscription getWebhookSubscriptionSample1() {
        return new WebhookSubscription()
            .id(1L)
            .name("name1")
            .url("url1")
            .secret("secret1")
            .retryCount(1)
            .maxRetries(1)
            .retryDelay(1)
            .failureCount(1)
            .createdBy("createdBy1");
    }

    public static WebhookSubscription getWebhookSubscriptionSample2() {
        return new WebhookSubscription()
            .id(2L)
            .name("name2")
            .url("url2")
            .secret("secret2")
            .retryCount(2)
            .maxRetries(2)
            .retryDelay(2)
            .failureCount(2)
            .createdBy("createdBy2");
    }

    public static WebhookSubscription getWebhookSubscriptionRandomSampleGenerator() {
        return new WebhookSubscription()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .url(UUID.randomUUID().toString())
            .secret(UUID.randomUUID().toString())
            .retryCount(intCount.incrementAndGet())
            .maxRetries(intCount.incrementAndGet())
            .retryDelay(intCount.incrementAndGet())
            .failureCount(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString());
    }
}
