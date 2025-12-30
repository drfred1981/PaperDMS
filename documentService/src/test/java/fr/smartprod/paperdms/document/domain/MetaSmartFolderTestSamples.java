package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MetaSmartFolderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MetaSmartFolder getMetaSmartFolderSample1() {
        return new MetaSmartFolder().id(1L).name("name1").createdBy("createdBy1");
    }

    public static MetaSmartFolder getMetaSmartFolderSample2() {
        return new MetaSmartFolder().id(2L).name("name2").createdBy("createdBy2");
    }

    public static MetaSmartFolder getMetaSmartFolderRandomSampleGenerator() {
        return new MetaSmartFolder()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString());
    }
}
