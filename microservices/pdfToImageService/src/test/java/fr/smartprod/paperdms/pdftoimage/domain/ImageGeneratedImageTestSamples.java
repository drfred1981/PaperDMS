package fr.smartprod.paperdms.pdftoimage.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ImageGeneratedImageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ImageGeneratedImage getImageGeneratedImageSample1() {
        return new ImageGeneratedImage()
            .id(1L)
            .pageNumber(1)
            .fileName("fileName1")
            .s3Key("s3Key1")
            .preSignedUrl("preSignedUrl1")
            .width(1)
            .height(1)
            .fileSize(1L)
            .dpi(1)
            .sha256Hash("sha256Hash1");
    }

    public static ImageGeneratedImage getImageGeneratedImageSample2() {
        return new ImageGeneratedImage()
            .id(2L)
            .pageNumber(2)
            .fileName("fileName2")
            .s3Key("s3Key2")
            .preSignedUrl("preSignedUrl2")
            .width(2)
            .height(2)
            .fileSize(2L)
            .dpi(2)
            .sha256Hash("sha256Hash2");
    }

    public static ImageGeneratedImage getImageGeneratedImageRandomSampleGenerator() {
        return new ImageGeneratedImage()
            .id(longCount.incrementAndGet())
            .pageNumber(intCount.incrementAndGet())
            .fileName(UUID.randomUUID().toString())
            .s3Key(UUID.randomUUID().toString())
            .preSignedUrl(UUID.randomUUID().toString())
            .width(intCount.incrementAndGet())
            .height(intCount.incrementAndGet())
            .fileSize(longCount.incrementAndGet())
            .dpi(intCount.incrementAndGet())
            .sha256Hash(UUID.randomUUID().toString());
    }
}
