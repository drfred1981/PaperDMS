package fr.smartprod.paperdms.document.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DocumentTagCriteriaTest {

    @Test
    void newDocumentTagCriteriaHasAllFiltersNullTest() {
        var documentTagCriteria = new DocumentTagCriteria();
        assertThat(documentTagCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void documentTagCriteriaFluentMethodsCreatesFiltersTest() {
        var documentTagCriteria = new DocumentTagCriteria();

        setAllFilters(documentTagCriteria);

        assertThat(documentTagCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void documentTagCriteriaCopyCreatesNullFilterTest() {
        var documentTagCriteria = new DocumentTagCriteria();
        var copy = documentTagCriteria.copy();

        assertThat(documentTagCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(documentTagCriteria)
        );
    }

    @Test
    void documentTagCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var documentTagCriteria = new DocumentTagCriteria();
        setAllFilters(documentTagCriteria);

        var copy = documentTagCriteria.copy();

        assertThat(documentTagCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(documentTagCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var documentTagCriteria = new DocumentTagCriteria();

        assertThat(documentTagCriteria).hasToString("DocumentTagCriteria{}");
    }

    private static void setAllFilters(DocumentTagCriteria documentTagCriteria) {
        documentTagCriteria.id();
        documentTagCriteria.assignedDate();
        documentTagCriteria.assignedBy();
        documentTagCriteria.confidence();
        documentTagCriteria.isAutoMetaTagged();
        documentTagCriteria.source();
        documentTagCriteria.documentId();
        documentTagCriteria.metaTagId();
        documentTagCriteria.distinct();
    }

    private static Condition<DocumentTagCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getAssignedDate()) &&
                condition.apply(criteria.getAssignedBy()) &&
                condition.apply(criteria.getConfidence()) &&
                condition.apply(criteria.getIsAutoMetaTagged()) &&
                condition.apply(criteria.getSource()) &&
                condition.apply(criteria.getDocumentId()) &&
                condition.apply(criteria.getMetaTagId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DocumentTagCriteria> copyFiltersAre(DocumentTagCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getAssignedDate(), copy.getAssignedDate()) &&
                condition.apply(criteria.getAssignedBy(), copy.getAssignedBy()) &&
                condition.apply(criteria.getConfidence(), copy.getConfidence()) &&
                condition.apply(criteria.getIsAutoMetaTagged(), copy.getIsAutoMetaTagged()) &&
                condition.apply(criteria.getSource(), copy.getSource()) &&
                condition.apply(criteria.getDocumentId(), copy.getDocumentId()) &&
                condition.apply(criteria.getMetaTagId(), copy.getMetaTagId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
