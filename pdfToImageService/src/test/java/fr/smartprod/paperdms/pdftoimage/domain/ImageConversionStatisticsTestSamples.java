package fr.smartprod.paperdms.pdftoimage.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ImageConversionStatisticsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ImageConversionStatistics getImageConversionStatisticsSample1() {
        return new ImageConversionStatistics()
            .id(1L)
            .totalConversions(1)
            .successfulConversions(1)
            .failedConversions(1)
            .totalPagesConverted(1)
            .totalImagesGenerated(1)
            .totalImagesSize(1L)
            .averageProcessingDuration(1L)
            .maxProcessingDuration(1L)
            .minProcessingDuration(1L);
    }

    public static ImageConversionStatistics getImageConversionStatisticsSample2() {
        return new ImageConversionStatistics()
            .id(2L)
            .totalConversions(2)
            .successfulConversions(2)
            .failedConversions(2)
            .totalPagesConverted(2)
            .totalImagesGenerated(2)
            .totalImagesSize(2L)
            .averageProcessingDuration(2L)
            .maxProcessingDuration(2L)
            .minProcessingDuration(2L);
    }

    public static ImageConversionStatistics getImageConversionStatisticsRandomSampleGenerator() {
        return new ImageConversionStatistics()
            .id(longCount.incrementAndGet())
            .totalConversions(intCount.incrementAndGet())
            .successfulConversions(intCount.incrementAndGet())
            .failedConversions(intCount.incrementAndGet())
            .totalPagesConverted(intCount.incrementAndGet())
            .totalImagesGenerated(intCount.incrementAndGet())
            .totalImagesSize(longCount.incrementAndGet())
            .averageProcessingDuration(longCount.incrementAndGet())
            .maxProcessingDuration(longCount.incrementAndGet())
            .minProcessingDuration(longCount.incrementAndGet());
    }
}
