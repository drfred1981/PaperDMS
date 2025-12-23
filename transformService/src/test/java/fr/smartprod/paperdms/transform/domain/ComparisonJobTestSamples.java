package fr.smartprod.paperdms.transform.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ComparisonJobTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ComparisonJob getComparisonJobSample1() {
        return new ComparisonJob()
            .id(1L)
            .documentId1(1L)
            .documentId2(1L)
            .differenceCount(1)
            .diffReportS3Key("diffReportS3Key1")
            .diffVisualS3Key("diffVisualS3Key1")
            .comparedBy("comparedBy1");
    }

    public static ComparisonJob getComparisonJobSample2() {
        return new ComparisonJob()
            .id(2L)
            .documentId1(2L)
            .documentId2(2L)
            .differenceCount(2)
            .diffReportS3Key("diffReportS3Key2")
            .diffVisualS3Key("diffVisualS3Key2")
            .comparedBy("comparedBy2");
    }

    public static ComparisonJob getComparisonJobRandomSampleGenerator() {
        return new ComparisonJob()
            .id(longCount.incrementAndGet())
            .documentId1(longCount.incrementAndGet())
            .documentId2(longCount.incrementAndGet())
            .differenceCount(intCount.incrementAndGet())
            .diffReportS3Key(UUID.randomUUID().toString())
            .diffVisualS3Key(UUID.randomUUID().toString())
            .comparedBy(UUID.randomUUID().toString());
    }
}
