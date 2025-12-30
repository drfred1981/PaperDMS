package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MetaPermissionGroupTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MetaPermissionGroup getMetaPermissionGroupSample1() {
        return new MetaPermissionGroup().id(1L).name("name1").createdBy("createdBy1");
    }

    public static MetaPermissionGroup getMetaPermissionGroupSample2() {
        return new MetaPermissionGroup().id(2L).name("name2").createdBy("createdBy2");
    }

    public static MetaPermissionGroup getMetaPermissionGroupRandomSampleGenerator() {
        return new MetaPermissionGroup()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString());
    }
}
