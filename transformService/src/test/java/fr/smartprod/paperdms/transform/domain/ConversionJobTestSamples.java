package fr.smartprod.paperdms.transform.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ConversionJobTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ConversionJob getConversionJobSample1() {
        return new ConversionJob()
            .id(1L)
            .documentId(1L)
            .documentSha256("documentSha2561")
            .sourceFormat("sourceFormat1")
            .targetFormat("targetFormat1")
            .conversionEngine("conversionEngine1")
            .outputS3Key("outputS3Key1")
            .outputDocumentId(1L)
            .createdBy("createdBy1");
    }

    public static ConversionJob getConversionJobSample2() {
        return new ConversionJob()
            .id(2L)
            .documentId(2L)
            .documentSha256("documentSha2562")
            .sourceFormat("sourceFormat2")
            .targetFormat("targetFormat2")
            .conversionEngine("conversionEngine2")
            .outputS3Key("outputS3Key2")
            .outputDocumentId(2L)
            .createdBy("createdBy2");
    }

    public static ConversionJob getConversionJobRandomSampleGenerator() {
        return new ConversionJob()
            .id(longCount.incrementAndGet())
            .documentId(longCount.incrementAndGet())
            .documentSha256(UUID.randomUUID().toString())
            .sourceFormat(UUID.randomUUID().toString())
            .targetFormat(UUID.randomUUID().toString())
            .conversionEngine(UUID.randomUUID().toString())
            .outputS3Key(UUID.randomUUID().toString())
            .outputDocumentId(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString());
    }
}
