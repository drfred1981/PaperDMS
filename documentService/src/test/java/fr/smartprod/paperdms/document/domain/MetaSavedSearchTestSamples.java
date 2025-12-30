package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MetaSavedSearchTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MetaSavedSearch getMetaSavedSearchSample1() {
        return new MetaSavedSearch().id(1L).name("name1").userId("userId1");
    }

    public static MetaSavedSearch getMetaSavedSearchSample2() {
        return new MetaSavedSearch().id(2L).name("name2").userId("userId2");
    }

    public static MetaSavedSearch getMetaSavedSearchRandomSampleGenerator() {
        return new MetaSavedSearch()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .userId(UUID.randomUUID().toString());
    }
}
