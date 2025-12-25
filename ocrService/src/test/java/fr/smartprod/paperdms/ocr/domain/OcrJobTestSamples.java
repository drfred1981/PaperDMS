package fr.smartprod.paperdms.ocr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OcrJobTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static OcrJob getOcrJobSample1() {
        return new OcrJob()
            .id(1L)
            .documentId(1L)
            .documentSha256("documentSha2561")
            .s3Key("s3Key1")
            .s3Bucket("s3Bucket1")
            .requestedLanguage("requestedLanguage1")
            .detectedLanguage("detectedLanguage1")
            .tikaEndpoint("tikaEndpoint1")
            .aiProvider("aiProvider1")
            .aiModel("aiModel1")
            .resultCacheKey("resultCacheKey1")
            .pageCount(1)
            .progress(1)
            .retryCount(1)
            .priority(1)
            .processingTime(1L)
            .createdBy("createdBy1");
    }

    public static OcrJob getOcrJobSample2() {
        return new OcrJob()
            .id(2L)
            .documentId(2L)
            .documentSha256("documentSha2562")
            .s3Key("s3Key2")
            .s3Bucket("s3Bucket2")
            .requestedLanguage("requestedLanguage2")
            .detectedLanguage("detectedLanguage2")
            .tikaEndpoint("tikaEndpoint2")
            .aiProvider("aiProvider2")
            .aiModel("aiModel2")
            .resultCacheKey("resultCacheKey2")
            .pageCount(2)
            .progress(2)
            .retryCount(2)
            .priority(2)
            .processingTime(2L)
            .createdBy("createdBy2");
    }

    public static OcrJob getOcrJobRandomSampleGenerator() {
        return new OcrJob()
            .id(longCount.incrementAndGet())
            .documentId(longCount.incrementAndGet())
            .documentSha256(UUID.randomUUID().toString())
            .s3Key(UUID.randomUUID().toString())
            .s3Bucket(UUID.randomUUID().toString())
            .requestedLanguage(UUID.randomUUID().toString())
            .detectedLanguage(UUID.randomUUID().toString())
            .tikaEndpoint(UUID.randomUUID().toString())
            .aiProvider(UUID.randomUUID().toString())
            .aiModel(UUID.randomUUID().toString())
            .resultCacheKey(UUID.randomUUID().toString())
            .pageCount(intCount.incrementAndGet())
            .progress(intCount.incrementAndGet())
            .retryCount(intCount.incrementAndGet())
            .priority(intCount.incrementAndGet())
            .processingTime(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString());
    }
}
