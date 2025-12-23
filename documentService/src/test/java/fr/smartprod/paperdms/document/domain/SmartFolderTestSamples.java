package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SmartFolderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static SmartFolder getSmartFolderSample1() {
        return new SmartFolder().id(1L).name("name1").createdBy("createdBy1");
    }

    public static SmartFolder getSmartFolderSample2() {
        return new SmartFolder().id(2L).name("name2").createdBy("createdBy2");
    }

    public static SmartFolder getSmartFolderRandomSampleGenerator() {
        return new SmartFolder().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).createdBy(UUID.randomUUID().toString());
    }
}
