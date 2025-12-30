package fr.smartprod.paperdms.ai.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AICorrespondentPredictionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AICorrespondentPrediction getAICorrespondentPredictionSample1() {
        return new AICorrespondentPrediction()
            .id(1L)
            .correspondentName("correspondentName1")
            .name("name1")
            .email("email1")
            .phone("phone1")
            .company("company1")
            .reason("reason1")
            .modelVersion("modelVersion1")
            .predictionS3Key("predictionS3Key1")
            .acceptedBy("acceptedBy1");
    }

    public static AICorrespondentPrediction getAICorrespondentPredictionSample2() {
        return new AICorrespondentPrediction()
            .id(2L)
            .correspondentName("correspondentName2")
            .name("name2")
            .email("email2")
            .phone("phone2")
            .company("company2")
            .reason("reason2")
            .modelVersion("modelVersion2")
            .predictionS3Key("predictionS3Key2")
            .acceptedBy("acceptedBy2");
    }

    public static AICorrespondentPrediction getAICorrespondentPredictionRandomSampleGenerator() {
        return new AICorrespondentPrediction()
            .id(longCount.incrementAndGet())
            .correspondentName(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .company(UUID.randomUUID().toString())
            .reason(UUID.randomUUID().toString())
            .modelVersion(UUID.randomUUID().toString())
            .predictionS3Key(UUID.randomUUID().toString())
            .acceptedBy(UUID.randomUUID().toString());
    }
}
