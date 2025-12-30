package fr.smartprod.paperdms.ai.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AICacheTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AICache getAICacheSample1() {
        return new AICache()
            .id(1L)
            .cacheKey("cacheKey1")
            .inputSha256("inputSha2561")
            .aiProvider("aiProvider1")
            .aiModel("aiModel1")
            .operation("operation1")
            .s3ResultKey("s3ResultKey1")
            .hits(1);
    }

    public static AICache getAICacheSample2() {
        return new AICache()
            .id(2L)
            .cacheKey("cacheKey2")
            .inputSha256("inputSha2562")
            .aiProvider("aiProvider2")
            .aiModel("aiModel2")
            .operation("operation2")
            .s3ResultKey("s3ResultKey2")
            .hits(2);
    }

    public static AICache getAICacheRandomSampleGenerator() {
        return new AICache()
            .id(longCount.incrementAndGet())
            .cacheKey(UUID.randomUUID().toString())
            .inputSha256(UUID.randomUUID().toString())
            .aiProvider(UUID.randomUUID().toString())
            .aiModel(UUID.randomUUID().toString())
            .operation(UUID.randomUUID().toString())
            .s3ResultKey(UUID.randomUUID().toString())
            .hits(intCount.incrementAndGet());
    }
}
