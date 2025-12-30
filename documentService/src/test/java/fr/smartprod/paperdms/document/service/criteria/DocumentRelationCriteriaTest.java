package fr.smartprod.paperdms.document.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DocumentRelationCriteriaTest {

    @Test
    void newDocumentRelationCriteriaHasAllFiltersNullTest() {
        var documentRelationCriteria = new DocumentRelationCriteria();
        assertThat(documentRelationCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void documentRelationCriteriaFluentMethodsCreatesFiltersTest() {
        var documentRelationCriteria = new DocumentRelationCriteria();

        setAllFilters(documentRelationCriteria);

        assertThat(documentRelationCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void documentRelationCriteriaCopyCreatesNullFilterTest() {
        var documentRelationCriteria = new DocumentRelationCriteria();
        var copy = documentRelationCriteria.copy();

        assertThat(documentRelationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(documentRelationCriteria)
        );
    }

    @Test
    void documentRelationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var documentRelationCriteria = new DocumentRelationCriteria();
        setAllFilters(documentRelationCriteria);

        var copy = documentRelationCriteria.copy();

        assertThat(documentRelationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(documentRelationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var documentRelationCriteria = new DocumentRelationCriteria();

        assertThat(documentRelationCriteria).hasToString("DocumentRelationCriteria{}");
    }

    private static void setAllFilters(DocumentRelationCriteria documentRelationCriteria) {
        documentRelationCriteria.id();
        documentRelationCriteria.sourceDocumentId();
        documentRelationCriteria.targetDocumentId();
        documentRelationCriteria.relationType();
        documentRelationCriteria.createdBy();
        documentRelationCriteria.createdDate();
        documentRelationCriteria.distinct();
    }

    private static Condition<DocumentRelationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSourceDocumentId()) &&
                condition.apply(criteria.getTargetDocumentId()) &&
                condition.apply(criteria.getRelationType()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DocumentRelationCriteria> copyFiltersAre(
        DocumentRelationCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSourceDocumentId(), copy.getSourceDocumentId()) &&
                condition.apply(criteria.getTargetDocumentId(), copy.getTargetDocumentId()) &&
                condition.apply(criteria.getRelationType(), copy.getRelationType()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
