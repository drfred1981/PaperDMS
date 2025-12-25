package fr.smartprod.paperdms.scan.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ScannedPageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ScannedPage getScannedPageSample1() {
        return new ScannedPage()
            .id(1L)
            .scanJobId(1L)
            .pageNumber(1)
            .sha256("sha2561")
            .s3Key("s3Key1")
            .s3PreviewKey("s3PreviewKey1")
            .fileSize(1L)
            .width(1)
            .height(1)
            .dpi(1)
            .documentId(1L);
    }

    public static ScannedPage getScannedPageSample2() {
        return new ScannedPage()
            .id(2L)
            .scanJobId(2L)
            .pageNumber(2)
            .sha256("sha2562")
            .s3Key("s3Key2")
            .s3PreviewKey("s3PreviewKey2")
            .fileSize(2L)
            .width(2)
            .height(2)
            .dpi(2)
            .documentId(2L);
    }

    public static ScannedPage getScannedPageRandomSampleGenerator() {
        return new ScannedPage()
            .id(longCount.incrementAndGet())
            .scanJobId(longCount.incrementAndGet())
            .pageNumber(intCount.incrementAndGet())
            .sha256(UUID.randomUUID().toString())
            .s3Key(UUID.randomUUID().toString())
            .s3PreviewKey(UUID.randomUUID().toString())
            .fileSize(longCount.incrementAndGet())
            .width(intCount.incrementAndGet())
            .height(intCount.incrementAndGet())
            .dpi(intCount.incrementAndGet())
            .documentId(longCount.incrementAndGet());
    }
}
