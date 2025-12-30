package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MetaFolderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MetaFolder getMetaFolderSample1() {
        return new MetaFolder().id(1L).name("name1").path("path1").createdBy("createdBy1");
    }

    public static MetaFolder getMetaFolderSample2() {
        return new MetaFolder().id(2L).name("name2").path("path2").createdBy("createdBy2");
    }

    public static MetaFolder getMetaFolderRandomSampleGenerator() {
        return new MetaFolder()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .path(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString());
    }
}
