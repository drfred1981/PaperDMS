package fr.smartprod.paperdms.ai.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CorrespondentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Correspondent getCorrespondentSample1() {
        return new Correspondent().id(1L).name("name1").email("email1").phone("phone1").company("company1").verifiedBy("verifiedBy1");
    }

    public static Correspondent getCorrespondentSample2() {
        return new Correspondent().id(2L).name("name2").email("email2").phone("phone2").company("company2").verifiedBy("verifiedBy2");
    }

    public static Correspondent getCorrespondentRandomSampleGenerator() {
        return new Correspondent()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .company(UUID.randomUUID().toString())
            .verifiedBy(UUID.randomUUID().toString());
    }
}
