package fr.smartprod.paperdms.ai.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AICacheCriteriaTest {

    @Test
    void newAICacheCriteriaHasAllFiltersNullTest() {
        var aICacheCriteria = new AICacheCriteria();
        assertThat(aICacheCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void aICacheCriteriaFluentMethodsCreatesFiltersTest() {
        var aICacheCriteria = new AICacheCriteria();

        setAllFilters(aICacheCriteria);

        assertThat(aICacheCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void aICacheCriteriaCopyCreatesNullFilterTest() {
        var aICacheCriteria = new AICacheCriteria();
        var copy = aICacheCriteria.copy();

        assertThat(aICacheCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(aICacheCriteria)
        );
    }

    @Test
    void aICacheCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var aICacheCriteria = new AICacheCriteria();
        setAllFilters(aICacheCriteria);

        var copy = aICacheCriteria.copy();

        assertThat(aICacheCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(aICacheCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var aICacheCriteria = new AICacheCriteria();

        assertThat(aICacheCriteria).hasToString("AICacheCriteria{}");
    }

    private static void setAllFilters(AICacheCriteria aICacheCriteria) {
        aICacheCriteria.id();
        aICacheCriteria.cacheKey();
        aICacheCriteria.inputSha256();
        aICacheCriteria.aiProvider();
        aICacheCriteria.aiModel();
        aICacheCriteria.operation();
        aICacheCriteria.s3ResultKey();
        aICacheCriteria.confidence();
        aICacheCriteria.hits();
        aICacheCriteria.cost();
        aICacheCriteria.lastAccessDate();
        aICacheCriteria.createdDate();
        aICacheCriteria.expirationDate();
        aICacheCriteria.distinct();
    }

    private static Condition<AICacheCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCacheKey()) &&
                condition.apply(criteria.getInputSha256()) &&
                condition.apply(criteria.getAiProvider()) &&
                condition.apply(criteria.getAiModel()) &&
                condition.apply(criteria.getOperation()) &&
                condition.apply(criteria.gets3ResultKey()) &&
                condition.apply(criteria.getConfidence()) &&
                condition.apply(criteria.getHits()) &&
                condition.apply(criteria.getCost()) &&
                condition.apply(criteria.getLastAccessDate()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getExpirationDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AICacheCriteria> copyFiltersAre(AICacheCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCacheKey(), copy.getCacheKey()) &&
                condition.apply(criteria.getInputSha256(), copy.getInputSha256()) &&
                condition.apply(criteria.getAiProvider(), copy.getAiProvider()) &&
                condition.apply(criteria.getAiModel(), copy.getAiModel()) &&
                condition.apply(criteria.getOperation(), copy.getOperation()) &&
                condition.apply(criteria.gets3ResultKey(), copy.gets3ResultKey()) &&
                condition.apply(criteria.getConfidence(), copy.getConfidence()) &&
                condition.apply(criteria.getHits(), copy.getHits()) &&
                condition.apply(criteria.getCost(), copy.getCost()) &&
                condition.apply(criteria.getLastAccessDate(), copy.getLastAccessDate()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getExpirationDate(), copy.getExpirationDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
