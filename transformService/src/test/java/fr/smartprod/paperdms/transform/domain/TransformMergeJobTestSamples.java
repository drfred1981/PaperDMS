package fr.smartprod.paperdms.transform.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TransformMergeJobTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TransformMergeJob getTransformMergeJobSample1() {
        return new TransformMergeJob()
            .id(1L)
            .name("name1")
            .outputS3Key("outputS3Key1")
            .outputDocumentSha256("outputDocumentSha2561")
            .createdBy("createdBy1");
    }

    public static TransformMergeJob getTransformMergeJobSample2() {
        return new TransformMergeJob()
            .id(2L)
            .name("name2")
            .outputS3Key("outputS3Key2")
            .outputDocumentSha256("outputDocumentSha2562")
            .createdBy("createdBy2");
    }

    public static TransformMergeJob getTransformMergeJobRandomSampleGenerator() {
        return new TransformMergeJob()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .outputS3Key(UUID.randomUUID().toString())
            .outputDocumentSha256(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString());
    }
}
