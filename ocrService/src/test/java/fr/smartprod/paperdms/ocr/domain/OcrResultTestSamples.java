package fr.smartprod.paperdms.ocr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OcrResultTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static OcrResult getOcrResultSample1() {
        return new OcrResult()
            .id(1L)
            .pageNumber(1)
            .pageSha256("pageSha2561")
            .s3ResultKey("s3ResultKey1")
            .s3Bucket("s3Bucket1")
            .s3BoundingBoxKey("s3BoundingBoxKey1")
            .language("language1")
            .wordCount(1)
            .processingTime(1L)
            .rawResponseS3Key("rawResponseS3Key1");
    }

    public static OcrResult getOcrResultSample2() {
        return new OcrResult()
            .id(2L)
            .pageNumber(2)
            .pageSha256("pageSha2562")
            .s3ResultKey("s3ResultKey2")
            .s3Bucket("s3Bucket2")
            .s3BoundingBoxKey("s3BoundingBoxKey2")
            .language("language2")
            .wordCount(2)
            .processingTime(2L)
            .rawResponseS3Key("rawResponseS3Key2");
    }

    public static OcrResult getOcrResultRandomSampleGenerator() {
        return new OcrResult()
            .id(longCount.incrementAndGet())
            .pageNumber(intCount.incrementAndGet())
            .pageSha256(UUID.randomUUID().toString())
            .s3ResultKey(UUID.randomUUID().toString())
            .s3Bucket(UUID.randomUUID().toString())
            .s3BoundingBoxKey(UUID.randomUUID().toString())
            .language(UUID.randomUUID().toString())
            .wordCount(intCount.incrementAndGet())
            .processingTime(longCount.incrementAndGet())
            .rawResponseS3Key(UUID.randomUUID().toString());
    }
}
