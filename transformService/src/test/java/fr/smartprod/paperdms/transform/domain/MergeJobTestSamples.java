package fr.smartprod.paperdms.transform.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MergeJobTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static MergeJob getMergeJobSample1() {
        return new MergeJob().id(1L).name("name1").outputS3Key("outputS3Key1").outputDocumentId(1L).createdBy("createdBy1");
    }

    public static MergeJob getMergeJobSample2() {
        return new MergeJob().id(2L).name("name2").outputS3Key("outputS3Key2").outputDocumentId(2L).createdBy("createdBy2");
    }

    public static MergeJob getMergeJobRandomSampleGenerator() {
        return new MergeJob()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .outputS3Key(UUID.randomUUID().toString())
            .outputDocumentId(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString());
    }
}
