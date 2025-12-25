package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TagTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Tag getTagSample1() {
        return new Tag().id(1L).name("name1").color("color1").description("description1").usageCount(1).createdBy("createdBy1");
    }

    public static Tag getTagSample2() {
        return new Tag().id(2L).name("name2").color("color2").description("description2").usageCount(2).createdBy("createdBy2");
    }

    public static Tag getTagRandomSampleGenerator() {
        return new Tag()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .color(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .usageCount(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString());
    }
}
