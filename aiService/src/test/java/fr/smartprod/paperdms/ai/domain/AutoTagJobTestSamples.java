package fr.smartprod.paperdms.ai.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AutoTagJobTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AutoTagJob getAutoTagJobSample1() {
        return new AutoTagJob()
            .id(1L)
            .documentId(1L)
            .documentSha256("documentSha2561")
            .s3Key("s3Key1")
            .extractedTextSha256("extractedTextSha2561")
            .detectedLanguage("detectedLanguage1")
            .modelVersion("modelVersion1")
            .resultCacheKey("resultCacheKey1");
    }

    public static AutoTagJob getAutoTagJobSample2() {
        return new AutoTagJob()
            .id(2L)
            .documentId(2L)
            .documentSha256("documentSha2562")
            .s3Key("s3Key2")
            .extractedTextSha256("extractedTextSha2562")
            .detectedLanguage("detectedLanguage2")
            .modelVersion("modelVersion2")
            .resultCacheKey("resultCacheKey2");
    }

    public static AutoTagJob getAutoTagJobRandomSampleGenerator() {
        return new AutoTagJob()
            .id(longCount.incrementAndGet())
            .documentId(longCount.incrementAndGet())
            .documentSha256(UUID.randomUUID().toString())
            .s3Key(UUID.randomUUID().toString())
            .extractedTextSha256(UUID.randomUUID().toString())
            .detectedLanguage(UUID.randomUUID().toString())
            .modelVersion(UUID.randomUUID().toString())
            .resultCacheKey(UUID.randomUUID().toString());
    }
}
