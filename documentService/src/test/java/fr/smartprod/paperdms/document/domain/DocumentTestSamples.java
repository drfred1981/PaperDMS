package fr.smartprod.paperdms.document.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Document getDocumentSample1() {
        return new Document()
            .id(1L)
            .title("title1")
            .fileName("fileName1")
            .fileSize(1L)
            .mimeType("mimeType1")
            .sha256("sha2561")
            .s3Key("s3Key1")
            .s3Bucket("s3Bucket1")
            .s3Region("s3Region1")
            .s3Etag("s3Etag1")
            .thumbnailS3Key("thumbnailS3Key1")
            .thumbnailSha256("thumbnailSha2561")
            .webpPreviewS3Key("webpPreviewS3Key1")
            .webpPreviewSha256("webpPreviewSha2561")
            .downloadCount(1)
            .viewCount(1)
            .detectedLanguage("detectedLanguage1")
            .manualLanguage("manualLanguage1")
            .pageCount(1)
            .createdBy("createdBy1");
    }

    public static Document getDocumentSample2() {
        return new Document()
            .id(2L)
            .title("title2")
            .fileName("fileName2")
            .fileSize(2L)
            .mimeType("mimeType2")
            .sha256("sha2562")
            .s3Key("s3Key2")
            .s3Bucket("s3Bucket2")
            .s3Region("s3Region2")
            .s3Etag("s3Etag2")
            .thumbnailS3Key("thumbnailS3Key2")
            .thumbnailSha256("thumbnailSha2562")
            .webpPreviewS3Key("webpPreviewS3Key2")
            .webpPreviewSha256("webpPreviewSha2562")
            .downloadCount(2)
            .viewCount(2)
            .detectedLanguage("detectedLanguage2")
            .manualLanguage("manualLanguage2")
            .pageCount(2)
            .createdBy("createdBy2");
    }

    public static Document getDocumentRandomSampleGenerator() {
        return new Document()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .fileName(UUID.randomUUID().toString())
            .fileSize(longCount.incrementAndGet())
            .mimeType(UUID.randomUUID().toString())
            .sha256(UUID.randomUUID().toString())
            .s3Key(UUID.randomUUID().toString())
            .s3Bucket(UUID.randomUUID().toString())
            .s3Region(UUID.randomUUID().toString())
            .s3Etag(UUID.randomUUID().toString())
            .thumbnailS3Key(UUID.randomUUID().toString())
            .thumbnailSha256(UUID.randomUUID().toString())
            .webpPreviewS3Key(UUID.randomUUID().toString())
            .webpPreviewSha256(UUID.randomUUID().toString())
            .downloadCount(intCount.incrementAndGet())
            .viewCount(intCount.incrementAndGet())
            .detectedLanguage(UUID.randomUUID().toString())
            .manualLanguage(UUID.randomUUID().toString())
            .pageCount(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString());
    }
}
