package fr.smartprod.paperdms.document.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DocumentCriteriaTest {

    @Test
    void newDocumentCriteriaHasAllFiltersNullTest() {
        var documentCriteria = new DocumentCriteria();
        assertThat(documentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void documentCriteriaFluentMethodsCreatesFiltersTest() {
        var documentCriteria = new DocumentCriteria();

        setAllFilters(documentCriteria);

        assertThat(documentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void documentCriteriaCopyCreatesNullFilterTest() {
        var documentCriteria = new DocumentCriteria();
        var copy = documentCriteria.copy();

        assertThat(documentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(documentCriteria)
        );
    }

    @Test
    void documentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var documentCriteria = new DocumentCriteria();
        setAllFilters(documentCriteria);

        var copy = documentCriteria.copy();

        assertThat(documentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(documentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var documentCriteria = new DocumentCriteria();

        assertThat(documentCriteria).hasToString("DocumentCriteria{}");
    }

    private static void setAllFilters(DocumentCriteria documentCriteria) {
        documentCriteria.id();
        documentCriteria.title();
        documentCriteria.fileName();
        documentCriteria.fileSize();
        documentCriteria.mimeType();
        documentCriteria.sha256();
        documentCriteria.s3Key();
        documentCriteria.s3Bucket();
        documentCriteria.s3Region();
        documentCriteria.s3Etag();
        documentCriteria.thumbnailS3Key();
        documentCriteria.thumbnailSha256();
        documentCriteria.webpPreviewS3Key();
        documentCriteria.webpPreviewSha256();
        documentCriteria.uploadDate();
        documentCriteria.isPublic();
        documentCriteria.downloadCount();
        documentCriteria.viewCount();
        documentCriteria.detectedLanguage();
        documentCriteria.manualLanguage();
        documentCriteria.languageConfidence();
        documentCriteria.pageCount();
        documentCriteria.createdDate();
        documentCriteria.createdBy();
        documentCriteria.folderId();
        documentCriteria.documentTypeId();
        documentCriteria.distinct();
    }

    private static Condition<DocumentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getFileName()) &&
                condition.apply(criteria.getFileSize()) &&
                condition.apply(criteria.getMimeType()) &&
                condition.apply(criteria.getSha256()) &&
                condition.apply(criteria.gets3Key()) &&
                condition.apply(criteria.gets3Bucket()) &&
                condition.apply(criteria.gets3Region()) &&
                condition.apply(criteria.gets3Etag()) &&
                condition.apply(criteria.getThumbnailS3Key()) &&
                condition.apply(criteria.getThumbnailSha256()) &&
                condition.apply(criteria.getWebpPreviewS3Key()) &&
                condition.apply(criteria.getWebpPreviewSha256()) &&
                condition.apply(criteria.getUploadDate()) &&
                condition.apply(criteria.getIsPublic()) &&
                condition.apply(criteria.getDownloadCount()) &&
                condition.apply(criteria.getViewCount()) &&
                condition.apply(criteria.getDetectedLanguage()) &&
                condition.apply(criteria.getManualLanguage()) &&
                condition.apply(criteria.getLanguageConfidence()) &&
                condition.apply(criteria.getPageCount()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getFolderId()) &&
                condition.apply(criteria.getDocumentTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DocumentCriteria> copyFiltersAre(DocumentCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getFileName(), copy.getFileName()) &&
                condition.apply(criteria.getFileSize(), copy.getFileSize()) &&
                condition.apply(criteria.getMimeType(), copy.getMimeType()) &&
                condition.apply(criteria.getSha256(), copy.getSha256()) &&
                condition.apply(criteria.gets3Key(), copy.gets3Key()) &&
                condition.apply(criteria.gets3Bucket(), copy.gets3Bucket()) &&
                condition.apply(criteria.gets3Region(), copy.gets3Region()) &&
                condition.apply(criteria.gets3Etag(), copy.gets3Etag()) &&
                condition.apply(criteria.getThumbnailS3Key(), copy.getThumbnailS3Key()) &&
                condition.apply(criteria.getThumbnailSha256(), copy.getThumbnailSha256()) &&
                condition.apply(criteria.getWebpPreviewS3Key(), copy.getWebpPreviewS3Key()) &&
                condition.apply(criteria.getWebpPreviewSha256(), copy.getWebpPreviewSha256()) &&
                condition.apply(criteria.getUploadDate(), copy.getUploadDate()) &&
                condition.apply(criteria.getIsPublic(), copy.getIsPublic()) &&
                condition.apply(criteria.getDownloadCount(), copy.getDownloadCount()) &&
                condition.apply(criteria.getViewCount(), copy.getViewCount()) &&
                condition.apply(criteria.getDetectedLanguage(), copy.getDetectedLanguage()) &&
                condition.apply(criteria.getManualLanguage(), copy.getManualLanguage()) &&
                condition.apply(criteria.getLanguageConfidence(), copy.getLanguageConfidence()) &&
                condition.apply(criteria.getPageCount(), copy.getPageCount()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getFolderId(), copy.getFolderId()) &&
                condition.apply(criteria.getDocumentTypeId(), copy.getDocumentTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
