package com.ged.ai.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CorrespondentExtractionCriteriaTest {

    @Test
    void newCorrespondentExtractionCriteriaHasAllFiltersNullTest() {
        var correspondentExtractionCriteria = new CorrespondentExtractionCriteria();
        assertThat(correspondentExtractionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void correspondentExtractionCriteriaFluentMethodsCreatesFiltersTest() {
        var correspondentExtractionCriteria = new CorrespondentExtractionCriteria();

        setAllFilters(correspondentExtractionCriteria);

        assertThat(correspondentExtractionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void correspondentExtractionCriteriaCopyCreatesNullFilterTest() {
        var correspondentExtractionCriteria = new CorrespondentExtractionCriteria();
        var copy = correspondentExtractionCriteria.copy();

        assertThat(correspondentExtractionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(correspondentExtractionCriteria)
        );
    }

    @Test
    void correspondentExtractionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var correspondentExtractionCriteria = new CorrespondentExtractionCriteria();
        setAllFilters(correspondentExtractionCriteria);

        var copy = correspondentExtractionCriteria.copy();

        assertThat(correspondentExtractionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(correspondentExtractionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var correspondentExtractionCriteria = new CorrespondentExtractionCriteria();

        assertThat(correspondentExtractionCriteria).hasToString("CorrespondentExtractionCriteria{}");
    }

    private static void setAllFilters(CorrespondentExtractionCriteria correspondentExtractionCriteria) {
        correspondentExtractionCriteria.id();
        correspondentExtractionCriteria.documentId();
        correspondentExtractionCriteria.status();
        correspondentExtractionCriteria.startDate();
        correspondentExtractionCriteria.endDate();
        correspondentExtractionCriteria.sendersCount();
        correspondentExtractionCriteria.recipientsCount();
        correspondentExtractionCriteria.createdDate();
        correspondentExtractionCriteria.distinct();
    }

    private static Condition<CorrespondentExtractionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentId()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getSendersCount()) &&
                condition.apply(criteria.getRecipientsCount()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CorrespondentExtractionCriteria> copyFiltersAre(
        CorrespondentExtractionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentId(), copy.getDocumentId()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getSendersCount(), copy.getSendersCount()) &&
                condition.apply(criteria.getRecipientsCount(), copy.getRecipientsCount()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
