package com.ged.ai.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AutoTagJobCriteriaTest {

    @Test
    void newAutoTagJobCriteriaHasAllFiltersNullTest() {
        var autoTagJobCriteria = new AutoTagJobCriteria();
        assertThat(autoTagJobCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void autoTagJobCriteriaFluentMethodsCreatesFiltersTest() {
        var autoTagJobCriteria = new AutoTagJobCriteria();

        setAllFilters(autoTagJobCriteria);

        assertThat(autoTagJobCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void autoTagJobCriteriaCopyCreatesNullFilterTest() {
        var autoTagJobCriteria = new AutoTagJobCriteria();
        var copy = autoTagJobCriteria.copy();

        assertThat(autoTagJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(autoTagJobCriteria)
        );
    }

    @Test
    void autoTagJobCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var autoTagJobCriteria = new AutoTagJobCriteria();
        setAllFilters(autoTagJobCriteria);

        var copy = autoTagJobCriteria.copy();

        assertThat(autoTagJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(autoTagJobCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var autoTagJobCriteria = new AutoTagJobCriteria();

        assertThat(autoTagJobCriteria).hasToString("AutoTagJobCriteria{}");
    }

    private static void setAllFilters(AutoTagJobCriteria autoTagJobCriteria) {
        autoTagJobCriteria.id();
        autoTagJobCriteria.documentId();
        autoTagJobCriteria.s3Key();
        autoTagJobCriteria.status();
        autoTagJobCriteria.modelVersion();
        autoTagJobCriteria.startDate();
        autoTagJobCriteria.endDate();
        autoTagJobCriteria.confidence();
        autoTagJobCriteria.createdDate();
        autoTagJobCriteria.distinct();
    }

    private static Condition<AutoTagJobCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentId()) &&
                condition.apply(criteria.gets3Key()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getModelVersion()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getConfidence()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AutoTagJobCriteria> copyFiltersAre(AutoTagJobCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentId(), copy.getDocumentId()) &&
                condition.apply(criteria.gets3Key(), copy.gets3Key()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getModelVersion(), copy.getModelVersion()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getConfidence(), copy.getConfidence()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
