package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MetaMetaTagCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MetaMetaTagCategory getMetaMetaTagCategorySample1() {
        return new MetaMetaTagCategory().id(1L).name("name1").color("color1").displayOrder(1).createdBy("createdBy1");
    }

    public static MetaMetaTagCategory getMetaMetaTagCategorySample2() {
        return new MetaMetaTagCategory().id(2L).name("name2").color("color2").displayOrder(2).createdBy("createdBy2");
    }

    public static MetaMetaTagCategory getMetaMetaTagCategoryRandomSampleGenerator() {
        return new MetaMetaTagCategory()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .color(UUID.randomUUID().toString())
            .displayOrder(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString());
    }
}
