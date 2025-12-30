package fr.smartprod.paperdms.ocr.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OcrComparisonTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static OcrComparison getOcrComparisonSample1() {
        return new OcrComparison()
            .id(1L)
            .documentSha256("documentSha2561")
            .pageNumber(1)
            .differencesS3Key("differencesS3Key1")
            .selectedBy("selectedBy1");
    }

    public static OcrComparison getOcrComparisonSample2() {
        return new OcrComparison()
            .id(2L)
            .documentSha256("documentSha2562")
            .pageNumber(2)
            .differencesS3Key("differencesS3Key2")
            .selectedBy("selectedBy2");
    }

    public static OcrComparison getOcrComparisonRandomSampleGenerator() {
        return new OcrComparison()
            .id(longCount.incrementAndGet())
            .documentSha256(UUID.randomUUID().toString())
            .pageNumber(intCount.incrementAndGet())
            .differencesS3Key(UUID.randomUUID().toString())
            .selectedBy(UUID.randomUUID().toString());
    }
}
