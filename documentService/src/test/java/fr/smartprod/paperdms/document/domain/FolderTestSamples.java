package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FolderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Folder getFolderSample1() {
        return new Folder().id(1L).name("name1").path("path1").createdBy("createdBy1");
    }

    public static Folder getFolderSample2() {
        return new Folder().id(2L).name("name2").path("path2").createdBy("createdBy2");
    }

    public static Folder getFolderRandomSampleGenerator() {
        return new Folder()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .path(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString());
    }
}
