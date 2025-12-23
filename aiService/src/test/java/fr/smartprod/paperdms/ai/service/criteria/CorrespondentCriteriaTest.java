package fr.smartprod.paperdms.ai.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CorrespondentCriteriaTest {

    @Test
    void newCorrespondentCriteriaHasAllFiltersNullTest() {
        var correspondentCriteria = new CorrespondentCriteria();
        assertThat(correspondentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void correspondentCriteriaFluentMethodsCreatesFiltersTest() {
        var correspondentCriteria = new CorrespondentCriteria();

        setAllFilters(correspondentCriteria);

        assertThat(correspondentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void correspondentCriteriaCopyCreatesNullFilterTest() {
        var correspondentCriteria = new CorrespondentCriteria();
        var copy = correspondentCriteria.copy();

        assertThat(correspondentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(correspondentCriteria)
        );
    }

    @Test
    void correspondentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var correspondentCriteria = new CorrespondentCriteria();
        setAllFilters(correspondentCriteria);

        var copy = correspondentCriteria.copy();

        assertThat(correspondentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(correspondentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var correspondentCriteria = new CorrespondentCriteria();

        assertThat(correspondentCriteria).hasToString("CorrespondentCriteria{}");
    }

    private static void setAllFilters(CorrespondentCriteria correspondentCriteria) {
        correspondentCriteria.id();
        correspondentCriteria.name();
        correspondentCriteria.email();
        correspondentCriteria.phone();
        correspondentCriteria.company();
        correspondentCriteria.type();
        correspondentCriteria.role();
        correspondentCriteria.confidence();
        correspondentCriteria.isVerified();
        correspondentCriteria.verifiedBy();
        correspondentCriteria.verifiedDate();
        correspondentCriteria.extractedDate();
        correspondentCriteria.extractionId();
        correspondentCriteria.distinct();
    }

    private static Condition<CorrespondentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getPhone()) &&
                condition.apply(criteria.getCompany()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getRole()) &&
                condition.apply(criteria.getConfidence()) &&
                condition.apply(criteria.getIsVerified()) &&
                condition.apply(criteria.getVerifiedBy()) &&
                condition.apply(criteria.getVerifiedDate()) &&
                condition.apply(criteria.getExtractedDate()) &&
                condition.apply(criteria.getExtractionId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CorrespondentCriteria> copyFiltersAre(
        CorrespondentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getPhone(), copy.getPhone()) &&
                condition.apply(criteria.getCompany(), copy.getCompany()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getRole(), copy.getRole()) &&
                condition.apply(criteria.getConfidence(), copy.getConfidence()) &&
                condition.apply(criteria.getIsVerified(), copy.getIsVerified()) &&
                condition.apply(criteria.getVerifiedBy(), copy.getVerifiedBy()) &&
                condition.apply(criteria.getVerifiedDate(), copy.getVerifiedDate()) &&
                condition.apply(criteria.getExtractedDate(), copy.getExtractedDate()) &&
                condition.apply(criteria.getExtractionId(), copy.getExtractionId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
