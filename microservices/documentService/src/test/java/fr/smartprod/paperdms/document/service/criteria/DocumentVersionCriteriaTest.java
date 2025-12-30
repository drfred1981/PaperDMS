package fr.smartprod.paperdms.document.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DocumentVersionCriteriaTest {

    @Test
    void newDocumentVersionCriteriaHasAllFiltersNullTest() {
        var documentVersionCriteria = new DocumentVersionCriteria();
        assertThat(documentVersionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void documentVersionCriteriaFluentMethodsCreatesFiltersTest() {
        var documentVersionCriteria = new DocumentVersionCriteria();

        setAllFilters(documentVersionCriteria);

        assertThat(documentVersionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void documentVersionCriteriaCopyCreatesNullFilterTest() {
        var documentVersionCriteria = new DocumentVersionCriteria();
        var copy = documentVersionCriteria.copy();

        assertThat(documentVersionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(documentVersionCriteria)
        );
    }

    @Test
    void documentVersionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var documentVersionCriteria = new DocumentVersionCriteria();
        setAllFilters(documentVersionCriteria);

        var copy = documentVersionCriteria.copy();

        assertThat(documentVersionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(documentVersionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var documentVersionCriteria = new DocumentVersionCriteria();

        assertThat(documentVersionCriteria).hasToString("DocumentVersionCriteria{}");
    }

    private static void setAllFilters(DocumentVersionCriteria documentVersionCriteria) {
        documentVersionCriteria.id();
        documentVersionCriteria.versionNumber();
        documentVersionCriteria.sha256();
        documentVersionCriteria.s3Key();
        documentVersionCriteria.fileSize();
        documentVersionCriteria.uploadDate();
        documentVersionCriteria.isActive();
        documentVersionCriteria.createdBy();
        documentVersionCriteria.documentId();
        documentVersionCriteria.distinct();
    }

    private static Condition<DocumentVersionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getVersionNumber()) &&
                condition.apply(criteria.getSha256()) &&
                condition.apply(criteria.gets3Key()) &&
                condition.apply(criteria.getFileSize()) &&
                condition.apply(criteria.getUploadDate()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getDocumentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DocumentVersionCriteria> copyFiltersAre(
        DocumentVersionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getVersionNumber(), copy.getVersionNumber()) &&
                condition.apply(criteria.getSha256(), copy.getSha256()) &&
                condition.apply(criteria.gets3Key(), copy.gets3Key()) &&
                condition.apply(criteria.getFileSize(), copy.getFileSize()) &&
                condition.apply(criteria.getUploadDate(), copy.getUploadDate()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getDocumentId(), copy.getDocumentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
