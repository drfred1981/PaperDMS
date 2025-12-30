package fr.smartprod.paperdms.transform.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TransformCompressionJobTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TransformCompressionJob getTransformCompressionJobSample1() {
        return new TransformCompressionJob()
            .id(1L)
            .documentSha256("documentSha2561")
            .quality(1)
            .targetSizeKb(1L)
            .originalSize(1L)
            .compressedSize(1L)
            .outputS3Key("outputS3Key1")
            .outputDocumentSha256("outputDocumentSha2561")
            .createdBy("createdBy1");
    }

    public static TransformCompressionJob getTransformCompressionJobSample2() {
        return new TransformCompressionJob()
            .id(2L)
            .documentSha256("documentSha2562")
            .quality(2)
            .targetSizeKb(2L)
            .originalSize(2L)
            .compressedSize(2L)
            .outputS3Key("outputS3Key2")
            .outputDocumentSha256("outputDocumentSha2562")
            .createdBy("createdBy2");
    }

    public static TransformCompressionJob getTransformCompressionJobRandomSampleGenerator() {
        return new TransformCompressionJob()
            .id(longCount.incrementAndGet())
            .documentSha256(UUID.randomUUID().toString())
            .quality(intCount.incrementAndGet())
            .targetSizeKb(longCount.incrementAndGet())
            .originalSize(longCount.incrementAndGet())
            .compressedSize(longCount.incrementAndGet())
            .outputS3Key(UUID.randomUUID().toString())
            .outputDocumentSha256(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString());
    }
}
