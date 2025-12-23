package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SavedSearchTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static SavedSearch getSavedSearchSample1() {
        return new SavedSearch().id(1L).name("name1").userId("userId1");
    }

    public static SavedSearch getSavedSearchSample2() {
        return new SavedSearch().id(2L).name("name2").userId("userId2");
    }

    public static SavedSearch getSavedSearchRandomSampleGenerator() {
        return new SavedSearch().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).userId(UUID.randomUUID().toString());
    }
}
