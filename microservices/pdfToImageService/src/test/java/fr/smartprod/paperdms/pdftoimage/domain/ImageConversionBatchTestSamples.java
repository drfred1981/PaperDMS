package fr.smartprod.paperdms.pdftoimage.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ImageConversionBatchTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ImageConversionBatch getImageConversionBatchSample1() {
        return new ImageConversionBatch()
            .id(1L)
            .batchName("batchName1")
            .description("description1")
            .totalConversions(1)
            .completedConversions(1)
            .failedConversions(1)
            .totalProcessingDuration(1L)
            .createdByUserId(1L);
    }

    public static ImageConversionBatch getImageConversionBatchSample2() {
        return new ImageConversionBatch()
            .id(2L)
            .batchName("batchName2")
            .description("description2")
            .totalConversions(2)
            .completedConversions(2)
            .failedConversions(2)
            .totalProcessingDuration(2L)
            .createdByUserId(2L);
    }

    public static ImageConversionBatch getImageConversionBatchRandomSampleGenerator() {
        return new ImageConversionBatch()
            .id(longCount.incrementAndGet())
            .batchName(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .totalConversions(intCount.incrementAndGet())
            .completedConversions(intCount.incrementAndGet())
            .failedConversions(intCount.incrementAndGet())
            .totalProcessingDuration(longCount.incrementAndGet())
            .createdByUserId(longCount.incrementAndGet());
    }
}
