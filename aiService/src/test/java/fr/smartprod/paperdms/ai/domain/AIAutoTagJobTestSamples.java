package fr.smartprod.paperdms.ai.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AIAutoTagJobTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AIAutoTagJob getAIAutoTagJobSample1() {
        return new AIAutoTagJob()
            .id(1L)
            .documentSha256("documentSha2561")
            .s3Key("s3Key1")
            .extractedTextSha256("extractedTextSha2561")
            .modelVersion("modelVersion1")
            .resultCacheKey("resultCacheKey1");
    }

    public static AIAutoTagJob getAIAutoTagJobSample2() {
        return new AIAutoTagJob()
            .id(2L)
            .documentSha256("documentSha2562")
            .s3Key("s3Key2")
            .extractedTextSha256("extractedTextSha2562")
            .modelVersion("modelVersion2")
            .resultCacheKey("resultCacheKey2");
    }

    public static AIAutoTagJob getAIAutoTagJobRandomSampleGenerator() {
        return new AIAutoTagJob()
            .id(longCount.incrementAndGet())
            .documentSha256(UUID.randomUUID().toString())
            .s3Key(UUID.randomUUID().toString())
            .extractedTextSha256(UUID.randomUUID().toString())
            .modelVersion(UUID.randomUUID().toString())
            .resultCacheKey(UUID.randomUUID().toString());
    }
}
