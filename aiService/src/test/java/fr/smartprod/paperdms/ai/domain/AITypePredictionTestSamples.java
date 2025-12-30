package fr.smartprod.paperdms.ai.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AITypePredictionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AITypePrediction getAITypePredictionSample1() {
        return new AITypePrediction()
            .id(1L)
            .documentTypeName("documentTypeName1")
            .reason("reason1")
            .modelVersion("modelVersion1")
            .predictionS3Key("predictionS3Key1")
            .acceptedBy("acceptedBy1");
    }

    public static AITypePrediction getAITypePredictionSample2() {
        return new AITypePrediction()
            .id(2L)
            .documentTypeName("documentTypeName2")
            .reason("reason2")
            .modelVersion("modelVersion2")
            .predictionS3Key("predictionS3Key2")
            .acceptedBy("acceptedBy2");
    }

    public static AITypePrediction getAITypePredictionRandomSampleGenerator() {
        return new AITypePrediction()
            .id(longCount.incrementAndGet())
            .documentTypeName(UUID.randomUUID().toString())
            .reason(UUID.randomUUID().toString())
            .modelVersion(UUID.randomUUID().toString())
            .predictionS3Key(UUID.randomUUID().toString())
            .acceptedBy(UUID.randomUUID().toString());
    }
}
