package fr.smartprod.paperdms.document.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DocumentMetadataCriteriaTest {

    @Test
    void newDocumentMetadataCriteriaHasAllFiltersNullTest() {
        var documentMetadataCriteria = new DocumentMetadataCriteria();
        assertThat(documentMetadataCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void documentMetadataCriteriaFluentMethodsCreatesFiltersTest() {
        var documentMetadataCriteria = new DocumentMetadataCriteria();

        setAllFilters(documentMetadataCriteria);

        assertThat(documentMetadataCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void documentMetadataCriteriaCopyCreatesNullFilterTest() {
        var documentMetadataCriteria = new DocumentMetadataCriteria();
        var copy = documentMetadataCriteria.copy();

        assertThat(documentMetadataCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(documentMetadataCriteria)
        );
    }

    @Test
    void documentMetadataCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var documentMetadataCriteria = new DocumentMetadataCriteria();
        setAllFilters(documentMetadataCriteria);

        var copy = documentMetadataCriteria.copy();

        assertThat(documentMetadataCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(documentMetadataCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var documentMetadataCriteria = new DocumentMetadataCriteria();

        assertThat(documentMetadataCriteria).hasToString("DocumentMetadataCriteria{}");
    }

    private static void setAllFilters(DocumentMetadataCriteria documentMetadataCriteria) {
        documentMetadataCriteria.id();
        documentMetadataCriteria.key();
        documentMetadataCriteria.dataType();
        documentMetadataCriteria.isSearchable();
        documentMetadataCriteria.createdDate();
        documentMetadataCriteria.documentId();
        documentMetadataCriteria.distinct();
    }

    private static Condition<DocumentMetadataCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getKey()) &&
                condition.apply(criteria.getDataType()) &&
                condition.apply(criteria.getIsSearchable()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDocumentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DocumentMetadataCriteria> copyFiltersAre(
        DocumentMetadataCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getKey(), copy.getKey()) &&
                condition.apply(criteria.getDataType(), copy.getDataType()) &&
                condition.apply(criteria.getIsSearchable(), copy.getIsSearchable()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getDocumentId(), copy.getDocumentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
