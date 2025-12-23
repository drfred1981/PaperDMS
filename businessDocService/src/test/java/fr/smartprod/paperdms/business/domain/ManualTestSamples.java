package fr.smartprod.paperdms.business.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ManualTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Manual getManualSample1() {
        return new Manual().id(1L).documentId(1L).title("title1").version("version1").language("language1").pageCount(1);
    }

    public static Manual getManualSample2() {
        return new Manual().id(2L).documentId(2L).title("title2").version("version2").language("language2").pageCount(2);
    }

    public static Manual getManualRandomSampleGenerator() {
        return new Manual()
            .id(longCount.incrementAndGet())
            .documentId(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .version(UUID.randomUUID().toString())
            .language(UUID.randomUUID().toString())
            .pageCount(intCount.incrementAndGet());
    }
}
