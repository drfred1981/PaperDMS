package fr.smartprod.paperdms.pdftoimage.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ImageConversionHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ImageConversionHistory getImageConversionHistorySample1() {
        return new ImageConversionHistory().id(1L).originalRequestId(1L).imagesCount(1).totalSize(1L).processingDuration(1L);
    }

    public static ImageConversionHistory getImageConversionHistorySample2() {
        return new ImageConversionHistory().id(2L).originalRequestId(2L).imagesCount(2).totalSize(2L).processingDuration(2L);
    }

    public static ImageConversionHistory getImageConversionHistoryRandomSampleGenerator() {
        return new ImageConversionHistory()
            .id(longCount.incrementAndGet())
            .originalRequestId(longCount.incrementAndGet())
            .imagesCount(intCount.incrementAndGet())
            .totalSize(longCount.incrementAndGet())
            .processingDuration(longCount.incrementAndGet());
    }
}
