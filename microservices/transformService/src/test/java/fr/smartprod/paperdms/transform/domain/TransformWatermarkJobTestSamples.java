package fr.smartprod.paperdms.transform.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TransformWatermarkJobTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TransformWatermarkJob getTransformWatermarkJobSample1() {
        return new TransformWatermarkJob()
            .id(1L)
            .documentSha256("documentSha2561")
            .watermarkText("watermarkText1")
            .watermarkImageS3Key("watermarkImageS3Key1")
            .opacity(1)
            .fontSize(1)
            .color("color1")
            .rotation(1)
            .outputS3Key("outputS3Key1")
            .outputDocumentSha256("outputDocumentSha2561")
            .createdBy("createdBy1");
    }

    public static TransformWatermarkJob getTransformWatermarkJobSample2() {
        return new TransformWatermarkJob()
            .id(2L)
            .documentSha256("documentSha2562")
            .watermarkText("watermarkText2")
            .watermarkImageS3Key("watermarkImageS3Key2")
            .opacity(2)
            .fontSize(2)
            .color("color2")
            .rotation(2)
            .outputS3Key("outputS3Key2")
            .outputDocumentSha256("outputDocumentSha2562")
            .createdBy("createdBy2");
    }

    public static TransformWatermarkJob getTransformWatermarkJobRandomSampleGenerator() {
        return new TransformWatermarkJob()
            .id(longCount.incrementAndGet())
            .documentSha256(UUID.randomUUID().toString())
            .watermarkText(UUID.randomUUID().toString())
            .watermarkImageS3Key(UUID.randomUUID().toString())
            .opacity(intCount.incrementAndGet())
            .fontSize(intCount.incrementAndGet())
            .color(UUID.randomUUID().toString())
            .rotation(intCount.incrementAndGet())
            .outputS3Key(UUID.randomUUID().toString())
            .outputDocumentSha256(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString());
    }
}
