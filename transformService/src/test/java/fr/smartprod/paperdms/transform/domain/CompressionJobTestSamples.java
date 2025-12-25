package fr.smartprod.paperdms.transform.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CompressionJobTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CompressionJob getCompressionJobSample1() {
        return new CompressionJob()
            .id(1L)
            .documentId(1L)
            .quality(1)
            .targetSizeKb(1L)
            .originalSize(1L)
            .compressedSize(1L)
            .outputS3Key("outputS3Key1")
            .outputDocumentId(1L)
            .createdBy("createdBy1");
    }

    public static CompressionJob getCompressionJobSample2() {
        return new CompressionJob()
            .id(2L)
            .documentId(2L)
            .quality(2)
            .targetSizeKb(2L)
            .originalSize(2L)
            .compressedSize(2L)
            .outputS3Key("outputS3Key2")
            .outputDocumentId(2L)
            .createdBy("createdBy2");
    }

    public static CompressionJob getCompressionJobRandomSampleGenerator() {
        return new CompressionJob()
            .id(longCount.incrementAndGet())
            .documentId(longCount.incrementAndGet())
            .quality(intCount.incrementAndGet())
            .targetSizeKb(longCount.incrementAndGet())
            .originalSize(longCount.incrementAndGet())
            .compressedSize(longCount.incrementAndGet())
            .outputS3Key(UUID.randomUUID().toString())
            .outputDocumentId(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString());
    }
}
