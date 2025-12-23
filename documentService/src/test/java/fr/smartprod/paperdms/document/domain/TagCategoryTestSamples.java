package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TagCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TagCategory getTagCategorySample1() {
        return new TagCategory().id(1L).name("name1").color("color1").displayOrder(1).createdBy("createdBy1");
    }

    public static TagCategory getTagCategorySample2() {
        return new TagCategory().id(2L).name("name2").color("color2").displayOrder(2).createdBy("createdBy2");
    }

    public static TagCategory getTagCategoryRandomSampleGenerator() {
        return new TagCategory()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .color(UUID.randomUUID().toString())
            .displayOrder(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString());
    }
}
