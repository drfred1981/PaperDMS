package fr.smartprod.paperdms.ai.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AITagPredictionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AITagPrediction getAITagPredictionSample1() {
        return new AITagPrediction()
            .id(1L)
            .tagName("tagName1")
            .reason("reason1")
            .modelVersion("modelVersion1")
            .predictionS3Key("predictionS3Key1")
            .acceptedBy("acceptedBy1");
    }

    public static AITagPrediction getAITagPredictionSample2() {
        return new AITagPrediction()
            .id(2L)
            .tagName("tagName2")
            .reason("reason2")
            .modelVersion("modelVersion2")
            .predictionS3Key("predictionS3Key2")
            .acceptedBy("acceptedBy2");
    }

    public static AITagPrediction getAITagPredictionRandomSampleGenerator() {
        return new AITagPrediction()
            .id(longCount.incrementAndGet())
            .tagName(UUID.randomUUID().toString())
            .reason(UUID.randomUUID().toString())
            .modelVersion(UUID.randomUUID().toString())
            .predictionS3Key(UUID.randomUUID().toString())
            .acceptedBy(UUID.randomUUID().toString());
    }
}
