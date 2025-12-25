package fr.smartprod.paperdms.archive.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ArchiveDocumentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ArchiveDocument getArchiveDocumentSample1() {
        return new ArchiveDocument()
            .id(1L)
            .archiveJobId(1L)
            .documentId(1L)
            .documentSha256("documentSha2561")
            .originalPath("originalPath1")
            .archivePath("archivePath1")
            .fileSize(1L);
    }

    public static ArchiveDocument getArchiveDocumentSample2() {
        return new ArchiveDocument()
            .id(2L)
            .archiveJobId(2L)
            .documentId(2L)
            .documentSha256("documentSha2562")
            .originalPath("originalPath2")
            .archivePath("archivePath2")
            .fileSize(2L);
    }

    public static ArchiveDocument getArchiveDocumentRandomSampleGenerator() {
        return new ArchiveDocument()
            .id(longCount.incrementAndGet())
            .archiveJobId(longCount.incrementAndGet())
            .documentId(longCount.incrementAndGet())
            .documentSha256(UUID.randomUUID().toString())
            .originalPath(UUID.randomUUID().toString())
            .archivePath(UUID.randomUUID().toString())
            .fileSize(longCount.incrementAndGet());
    }
}
