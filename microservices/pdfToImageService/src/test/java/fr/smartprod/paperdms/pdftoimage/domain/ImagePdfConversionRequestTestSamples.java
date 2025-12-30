package fr.smartprod.paperdms.pdftoimage.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ImagePdfConversionRequestTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ImagePdfConversionRequest getImagePdfConversionRequestSample1() {
        return new ImagePdfConversionRequest()
            .id(1L)
            .sourceDocumentId(1L)
            .sourceFileName("sourceFileName1")
            .sourcePdfS3Key("sourcePdfS3Key1")
            .startPage(1)
            .endPage(1)
            .totalPages(1)
            .errorMessage("errorMessage1")
            .processingDuration(1L)
            .totalImagesSize(1L)
            .imagesGenerated(1)
            .dpi(1)
            .requestedByUserId(1L)
            .priority(1);
    }

    public static ImagePdfConversionRequest getImagePdfConversionRequestSample2() {
        return new ImagePdfConversionRequest()
            .id(2L)
            .sourceDocumentId(2L)
            .sourceFileName("sourceFileName2")
            .sourcePdfS3Key("sourcePdfS3Key2")
            .startPage(2)
            .endPage(2)
            .totalPages(2)
            .errorMessage("errorMessage2")
            .processingDuration(2L)
            .totalImagesSize(2L)
            .imagesGenerated(2)
            .dpi(2)
            .requestedByUserId(2L)
            .priority(2);
    }

    public static ImagePdfConversionRequest getImagePdfConversionRequestRandomSampleGenerator() {
        return new ImagePdfConversionRequest()
            .id(longCount.incrementAndGet())
            .sourceDocumentId(longCount.incrementAndGet())
            .sourceFileName(UUID.randomUUID().toString())
            .sourcePdfS3Key(UUID.randomUUID().toString())
            .startPage(intCount.incrementAndGet())
            .endPage(intCount.incrementAndGet())
            .totalPages(intCount.incrementAndGet())
            .errorMessage(UUID.randomUUID().toString())
            .processingDuration(longCount.incrementAndGet())
            .totalImagesSize(longCount.incrementAndGet())
            .imagesGenerated(intCount.incrementAndGet())
            .dpi(intCount.incrementAndGet())
            .requestedByUserId(longCount.incrementAndGet())
            .priority(intCount.incrementAndGet());
    }
}
