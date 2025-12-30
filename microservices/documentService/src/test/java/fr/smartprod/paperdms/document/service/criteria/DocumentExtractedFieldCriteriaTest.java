package fr.smartprod.paperdms.document.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DocumentExtractedFieldCriteriaTest {

    @Test
    void newDocumentExtractedFieldCriteriaHasAllFiltersNullTest() {
        var documentExtractedFieldCriteria = new DocumentExtractedFieldCriteria();
        assertThat(documentExtractedFieldCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void documentExtractedFieldCriteriaFluentMethodsCreatesFiltersTest() {
        var documentExtractedFieldCriteria = new DocumentExtractedFieldCriteria();

        setAllFilters(documentExtractedFieldCriteria);

        assertThat(documentExtractedFieldCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void documentExtractedFieldCriteriaCopyCreatesNullFilterTest() {
        var documentExtractedFieldCriteria = new DocumentExtractedFieldCriteria();
        var copy = documentExtractedFieldCriteria.copy();

        assertThat(documentExtractedFieldCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(documentExtractedFieldCriteria)
        );
    }

    @Test
    void documentExtractedFieldCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var documentExtractedFieldCriteria = new DocumentExtractedFieldCriteria();
        setAllFilters(documentExtractedFieldCriteria);

        var copy = documentExtractedFieldCriteria.copy();

        assertThat(documentExtractedFieldCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(documentExtractedFieldCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var documentExtractedFieldCriteria = new DocumentExtractedFieldCriteria();

        assertThat(documentExtractedFieldCriteria).hasToString("DocumentExtractedFieldCriteria{}");
    }

    private static void setAllFilters(DocumentExtractedFieldCriteria documentExtractedFieldCriteria) {
        documentExtractedFieldCriteria.id();
        documentExtractedFieldCriteria.fieldKey();
        documentExtractedFieldCriteria.confidence();
        documentExtractedFieldCriteria.extractionMethod();
        documentExtractedFieldCriteria.isVerified();
        documentExtractedFieldCriteria.extractedDate();
        documentExtractedFieldCriteria.documentId();
        documentExtractedFieldCriteria.distinct();
    }

    private static Condition<DocumentExtractedFieldCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFieldKey()) &&
                condition.apply(criteria.getConfidence()) &&
                condition.apply(criteria.getExtractionMethod()) &&
                condition.apply(criteria.getIsVerified()) &&
                condition.apply(criteria.getExtractedDate()) &&
                condition.apply(criteria.getDocumentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DocumentExtractedFieldCriteria> copyFiltersAre(
        DocumentExtractedFieldCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFieldKey(), copy.getFieldKey()) &&
                condition.apply(criteria.getConfidence(), copy.getConfidence()) &&
                condition.apply(criteria.getExtractionMethod(), copy.getExtractionMethod()) &&
                condition.apply(criteria.getIsVerified(), copy.getIsVerified()) &&
                condition.apply(criteria.getExtractedDate(), copy.getExtractedDate()) &&
                condition.apply(criteria.getDocumentId(), copy.getDocumentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
