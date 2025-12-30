package fr.smartprod.paperdms.pdftoimage.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ImageConversionConfigTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ImageConversionConfig getImageConversionConfigSample1() {
        return new ImageConversionConfig().id(1L).configName("configName1").description("description1").defaultDpi(1).defaultPriority(1);
    }

    public static ImageConversionConfig getImageConversionConfigSample2() {
        return new ImageConversionConfig().id(2L).configName("configName2").description("description2").defaultDpi(2).defaultPriority(2);
    }

    public static ImageConversionConfig getImageConversionConfigRandomSampleGenerator() {
        return new ImageConversionConfig()
            .id(longCount.incrementAndGet())
            .configName(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .defaultDpi(intCount.incrementAndGet())
            .defaultPriority(intCount.incrementAndGet());
    }
}
