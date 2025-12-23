package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PermissionGroupTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static PermissionGroup getPermissionGroupSample1() {
        return new PermissionGroup().id(1L).name("name1").createdBy("createdBy1");
    }

    public static PermissionGroup getPermissionGroupSample2() {
        return new PermissionGroup().id(2L).name("name2").createdBy("createdBy2");
    }

    public static PermissionGroup getPermissionGroupRandomSampleGenerator() {
        return new PermissionGroup()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString());
    }
}
