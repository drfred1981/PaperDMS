package fr.smartprod.paperdms.archive.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ArchiveJobTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ArchiveJob getArchiveJobSample1() {
        return new ArchiveJob()
            .id(1L)
            .name("name1")
            .compressionLevel(1)
            .encryptionAlgorithm("encryptionAlgorithm1")
            .password("password1")
            .s3ArchiveKey("s3ArchiveKey1")
            .archiveSha256("archiveSha2561")
            .archiveSize(1L)
            .documentCount(1)
            .createdBy("createdBy1");
    }

    public static ArchiveJob getArchiveJobSample2() {
        return new ArchiveJob()
            .id(2L)
            .name("name2")
            .compressionLevel(2)
            .encryptionAlgorithm("encryptionAlgorithm2")
            .password("password2")
            .s3ArchiveKey("s3ArchiveKey2")
            .archiveSha256("archiveSha2562")
            .archiveSize(2L)
            .documentCount(2)
            .createdBy("createdBy2");
    }

    public static ArchiveJob getArchiveJobRandomSampleGenerator() {
        return new ArchiveJob()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .compressionLevel(intCount.incrementAndGet())
            .encryptionAlgorithm(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString())
            .s3ArchiveKey(UUID.randomUUID().toString())
            .archiveSha256(UUID.randomUUID().toString())
            .archiveSize(longCount.incrementAndGet())
            .documentCount(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString());
    }
}
