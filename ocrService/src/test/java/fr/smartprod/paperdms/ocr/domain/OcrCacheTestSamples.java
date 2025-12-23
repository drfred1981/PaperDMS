package fr.smartprod.paperdms.ocr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OcrCacheTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static OcrCache getOcrCacheSample1() {
        return new OcrCache()
            .id(1L)
            .documentSha256("documentSha2561")
            .language("language1")
            .pageCount(1)
            .s3ResultKey("s3ResultKey1")
            .s3Bucket("s3Bucket1")
            .extractedTextS3Key("extractedTextS3Key1")
            .hits(1);
    }

    public static OcrCache getOcrCacheSample2() {
        return new OcrCache()
            .id(2L)
            .documentSha256("documentSha2562")
            .language("language2")
            .pageCount(2)
            .s3ResultKey("s3ResultKey2")
            .s3Bucket("s3Bucket2")
            .extractedTextS3Key("extractedTextS3Key2")
            .hits(2);
    }

    public static OcrCache getOcrCacheRandomSampleGenerator() {
        return new OcrCache()
            .id(longCount.incrementAndGet())
            .documentSha256(UUID.randomUUID().toString())
            .language(UUID.randomUUID().toString())
            .pageCount(intCount.incrementAndGet())
            .s3ResultKey(UUID.randomUUID().toString())
            .s3Bucket(UUID.randomUUID().toString())
            .extractedTextS3Key(UUID.randomUUID().toString())
            .hits(intCount.incrementAndGet());
    }
}
