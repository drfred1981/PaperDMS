package fr.smartprod.paperdms.ai.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LanguageDetectionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static LanguageDetection getLanguageDetectionSample1() {
        return new LanguageDetection()
            .id(1L)
            .documentId(1L)
            .documentSha256("documentSha2561")
            .detectedLanguage("detectedLanguage1")
            .resultCacheKey("resultCacheKey1")
            .modelVersion("modelVersion1");
    }

    public static LanguageDetection getLanguageDetectionSample2() {
        return new LanguageDetection()
            .id(2L)
            .documentId(2L)
            .documentSha256("documentSha2562")
            .detectedLanguage("detectedLanguage2")
            .resultCacheKey("resultCacheKey2")
            .modelVersion("modelVersion2");
    }

    public static LanguageDetection getLanguageDetectionRandomSampleGenerator() {
        return new LanguageDetection()
            .id(longCount.incrementAndGet())
            .documentId(longCount.incrementAndGet())
            .documentSha256(UUID.randomUUID().toString())
            .detectedLanguage(UUID.randomUUID().toString())
            .resultCacheKey(UUID.randomUUID().toString())
            .modelVersion(UUID.randomUUID().toString());
    }
}
