package fr.smartprod.paperdms.transform.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TransformConversionJobTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TransformConversionJob getTransformConversionJobSample1() {
        return new TransformConversionJob()
            .id(1L)
            .documentSha256("documentSha2561")
            .sourceFormat("sourceFormat1")
            .targetFormat("targetFormat1")
            .conversionEngine("conversionEngine1")
            .outputS3Key("outputS3Key1")
            .outputDocumentSha256("outputDocumentSha2561")
            .createdBy("createdBy1");
    }

    public static TransformConversionJob getTransformConversionJobSample2() {
        return new TransformConversionJob()
            .id(2L)
            .documentSha256("documentSha2562")
            .sourceFormat("sourceFormat2")
            .targetFormat("targetFormat2")
            .conversionEngine("conversionEngine2")
            .outputS3Key("outputS3Key2")
            .outputDocumentSha256("outputDocumentSha2562")
            .createdBy("createdBy2");
    }

    public static TransformConversionJob getTransformConversionJobRandomSampleGenerator() {
        return new TransformConversionJob()
            .id(longCount.incrementAndGet())
            .documentSha256(UUID.randomUUID().toString())
            .sourceFormat(UUID.randomUUID().toString())
            .targetFormat(UUID.randomUUID().toString())
            .conversionEngine(UUID.randomUUID().toString())
            .outputS3Key(UUID.randomUUID().toString())
            .outputDocumentSha256(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString());
    }
}
