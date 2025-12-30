package fr.smartprod.paperdms.transform.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TransformRedactionJobTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TransformRedactionJob getTransformRedactionJobSample1() {
        return new TransformRedactionJob()
            .id(1L)
            .documentSha256("documentSha2561")
            .redactionColor("redactionColor1")
            .replaceWith("replaceWith1")
            .outputS3Key("outputS3Key1")
            .outputDocumentSha256("outputDocumentSha2561")
            .createdBy("createdBy1");
    }

    public static TransformRedactionJob getTransformRedactionJobSample2() {
        return new TransformRedactionJob()
            .id(2L)
            .documentSha256("documentSha2562")
            .redactionColor("redactionColor2")
            .replaceWith("replaceWith2")
            .outputS3Key("outputS3Key2")
            .outputDocumentSha256("outputDocumentSha2562")
            .createdBy("createdBy2");
    }

    public static TransformRedactionJob getTransformRedactionJobRandomSampleGenerator() {
        return new TransformRedactionJob()
            .id(longCount.incrementAndGet())
            .documentSha256(UUID.randomUUID().toString())
            .redactionColor(UUID.randomUUID().toString())
            .replaceWith(UUID.randomUUID().toString())
            .outputS3Key(UUID.randomUUID().toString())
            .outputDocumentSha256(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString());
    }
}
