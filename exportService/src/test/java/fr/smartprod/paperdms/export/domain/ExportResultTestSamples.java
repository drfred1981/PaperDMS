package fr.smartprod.paperdms.export.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ExportResultTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ExportResult getExportResultSample1() {
        return new ExportResult()
            .id(1L)
            .exportJobId(1L)
            .documentId(1L)
            .documentSha256("documentSha2561")
            .originalFileName("originalFileName1")
            .exportedPath("exportedPath1")
            .exportedFileName("exportedFileName1")
            .s3ExportKey("s3ExportKey1")
            .fileSize(1L);
    }

    public static ExportResult getExportResultSample2() {
        return new ExportResult()
            .id(2L)
            .exportJobId(2L)
            .documentId(2L)
            .documentSha256("documentSha2562")
            .originalFileName("originalFileName2")
            .exportedPath("exportedPath2")
            .exportedFileName("exportedFileName2")
            .s3ExportKey("s3ExportKey2")
            .fileSize(2L);
    }

    public static ExportResult getExportResultRandomSampleGenerator() {
        return new ExportResult()
            .id(longCount.incrementAndGet())
            .exportJobId(longCount.incrementAndGet())
            .documentId(longCount.incrementAndGet())
            .documentSha256(UUID.randomUUID().toString())
            .originalFileName(UUID.randomUUID().toString())
            .exportedPath(UUID.randomUUID().toString())
            .exportedFileName(UUID.randomUUID().toString())
            .s3ExportKey(UUID.randomUUID().toString())
            .fileSize(longCount.incrementAndGet());
    }
}
