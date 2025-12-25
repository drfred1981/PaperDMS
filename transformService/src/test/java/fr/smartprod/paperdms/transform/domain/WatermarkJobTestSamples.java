package fr.smartprod.paperdms.transform.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class WatermarkJobTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static WatermarkJob getWatermarkJobSample1() {
        return new WatermarkJob()
            .id(1L)
            .documentId(1L)
            .watermarkText("watermarkText1")
            .watermarkImageS3Key("watermarkImageS3Key1")
            .opacity(1)
            .fontSize(1)
            .color("color1")
            .rotation(1)
            .outputS3Key("outputS3Key1")
            .outputDocumentId(1L)
            .createdBy("createdBy1");
    }

    public static WatermarkJob getWatermarkJobSample2() {
        return new WatermarkJob()
            .id(2L)
            .documentId(2L)
            .watermarkText("watermarkText2")
            .watermarkImageS3Key("watermarkImageS3Key2")
            .opacity(2)
            .fontSize(2)
            .color("color2")
            .rotation(2)
            .outputS3Key("outputS3Key2")
            .outputDocumentId(2L)
            .createdBy("createdBy2");
    }

    public static WatermarkJob getWatermarkJobRandomSampleGenerator() {
        return new WatermarkJob()
            .id(longCount.incrementAndGet())
            .documentId(longCount.incrementAndGet())
            .watermarkText(UUID.randomUUID().toString())
            .watermarkImageS3Key(UUID.randomUUID().toString())
            .opacity(intCount.incrementAndGet())
            .fontSize(intCount.incrementAndGet())
            .color(UUID.randomUUID().toString())
            .rotation(intCount.incrementAndGet())
            .outputS3Key(UUID.randomUUID().toString())
            .outputDocumentId(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString());
    }
}
