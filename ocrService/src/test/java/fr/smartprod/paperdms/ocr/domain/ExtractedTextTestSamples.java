package fr.smartprod.paperdms.ocr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ExtractedTextTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ExtractedText getExtractedTextSample1() {
        return new ExtractedText()
            .id(1L)
            .contentSha256("contentSha2561")
            .s3ContentKey("s3ContentKey1")
            .s3Bucket("s3Bucket1")
            .pageNumber(1)
            .language("language1")
            .wordCount(1)
            .structuredDataS3Key("structuredDataS3Key1");
    }

    public static ExtractedText getExtractedTextSample2() {
        return new ExtractedText()
            .id(2L)
            .contentSha256("contentSha2562")
            .s3ContentKey("s3ContentKey2")
            .s3Bucket("s3Bucket2")
            .pageNumber(2)
            .language("language2")
            .wordCount(2)
            .structuredDataS3Key("structuredDataS3Key2");
    }

    public static ExtractedText getExtractedTextRandomSampleGenerator() {
        return new ExtractedText()
            .id(longCount.incrementAndGet())
            .contentSha256(UUID.randomUUID().toString())
            .s3ContentKey(UUID.randomUUID().toString())
            .s3Bucket(UUID.randomUUID().toString())
            .pageNumber(intCount.incrementAndGet())
            .language(UUID.randomUUID().toString())
            .wordCount(intCount.incrementAndGet())
            .structuredDataS3Key(UUID.randomUUID().toString());
    }
}
