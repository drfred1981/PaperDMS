package fr.smartprod.paperdms.document.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DocumentStatisticsCriteriaTest {

    @Test
    void newDocumentStatisticsCriteriaHasAllFiltersNullTest() {
        var documentStatisticsCriteria = new DocumentStatisticsCriteria();
        assertThat(documentStatisticsCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void documentStatisticsCriteriaFluentMethodsCreatesFiltersTest() {
        var documentStatisticsCriteria = new DocumentStatisticsCriteria();

        setAllFilters(documentStatisticsCriteria);

        assertThat(documentStatisticsCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void documentStatisticsCriteriaCopyCreatesNullFilterTest() {
        var documentStatisticsCriteria = new DocumentStatisticsCriteria();
        var copy = documentStatisticsCriteria.copy();

        assertThat(documentStatisticsCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(documentStatisticsCriteria)
        );
    }

    @Test
    void documentStatisticsCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var documentStatisticsCriteria = new DocumentStatisticsCriteria();
        setAllFilters(documentStatisticsCriteria);

        var copy = documentStatisticsCriteria.copy();

        assertThat(documentStatisticsCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(documentStatisticsCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var documentStatisticsCriteria = new DocumentStatisticsCriteria();

        assertThat(documentStatisticsCriteria).hasToString("DocumentStatisticsCriteria{}");
    }

    private static void setAllFilters(DocumentStatisticsCriteria documentStatisticsCriteria) {
        documentStatisticsCriteria.id();
        documentStatisticsCriteria.viewsTotal();
        documentStatisticsCriteria.downloadsTotal();
        documentStatisticsCriteria.uniqueViewers();
        documentStatisticsCriteria.lastUpdated();
        documentStatisticsCriteria.documentId();
        documentStatisticsCriteria.distinct();
    }

    private static Condition<DocumentStatisticsCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getViewsTotal()) &&
                condition.apply(criteria.getDownloadsTotal()) &&
                condition.apply(criteria.getUniqueViewers()) &&
                condition.apply(criteria.getLastUpdated()) &&
                condition.apply(criteria.getDocumentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DocumentStatisticsCriteria> copyFiltersAre(
        DocumentStatisticsCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getViewsTotal(), copy.getViewsTotal()) &&
                condition.apply(criteria.getDownloadsTotal(), copy.getDownloadsTotal()) &&
                condition.apply(criteria.getUniqueViewers(), copy.getUniqueViewers()) &&
                condition.apply(criteria.getLastUpdated(), copy.getLastUpdated()) &&
                condition.apply(criteria.getDocumentId(), copy.getDocumentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
