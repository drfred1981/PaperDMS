package fr.smartprod.paperdms.ai.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CorrespondentExtractionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CorrespondentExtraction getCorrespondentExtractionSample1() {
        return new CorrespondentExtraction()
            .id(1L)
            .documentId(1L)
            .documentSha256("documentSha2561")
            .extractedTextSha256("extractedTextSha2561")
            .detectedLanguage("detectedLanguage1")
            .resultCacheKey("resultCacheKey1")
            .resultS3Key("resultS3Key1")
            .sendersCount(1)
            .recipientsCount(1);
    }

    public static CorrespondentExtraction getCorrespondentExtractionSample2() {
        return new CorrespondentExtraction()
            .id(2L)
            .documentId(2L)
            .documentSha256("documentSha2562")
            .extractedTextSha256("extractedTextSha2562")
            .detectedLanguage("detectedLanguage2")
            .resultCacheKey("resultCacheKey2")
            .resultS3Key("resultS3Key2")
            .sendersCount(2)
            .recipientsCount(2);
    }

    public static CorrespondentExtraction getCorrespondentExtractionRandomSampleGenerator() {
        return new CorrespondentExtraction()
            .id(longCount.incrementAndGet())
            .documentId(longCount.incrementAndGet())
            .documentSha256(UUID.randomUUID().toString())
            .extractedTextSha256(UUID.randomUUID().toString())
            .detectedLanguage(UUID.randomUUID().toString())
            .resultCacheKey(UUID.randomUUID().toString())
            .resultS3Key(UUID.randomUUID().toString())
            .sendersCount(intCount.incrementAndGet())
            .recipientsCount(intCount.incrementAndGet());
    }
}
