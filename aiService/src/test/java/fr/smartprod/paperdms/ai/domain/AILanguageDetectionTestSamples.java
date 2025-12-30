package fr.smartprod.paperdms.ai.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AILanguageDetectionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AILanguageDetection getAILanguageDetectionSample1() {
        return new AILanguageDetection()
            .id(1L)
            .documentSha256("documentSha2561")
            .detectedLanguage("detectedLanguage1")
            .resultCacheKey("resultCacheKey1")
            .modelVersion("modelVersion1");
    }

    public static AILanguageDetection getAILanguageDetectionSample2() {
        return new AILanguageDetection()
            .id(2L)
            .documentSha256("documentSha2562")
            .detectedLanguage("detectedLanguage2")
            .resultCacheKey("resultCacheKey2")
            .modelVersion("modelVersion2");
    }

    public static AILanguageDetection getAILanguageDetectionRandomSampleGenerator() {
        return new AILanguageDetection()
            .id(longCount.incrementAndGet())
            .documentSha256(UUID.randomUUID().toString())
            .detectedLanguage(UUID.randomUUID().toString())
            .resultCacheKey(UUID.randomUUID().toString())
            .modelVersion(UUID.randomUUID().toString());
    }
}
