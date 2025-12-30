package fr.smartprod.paperdms.document.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MetaBookmarkCriteriaTest {

    @Test
    void newMetaBookmarkCriteriaHasAllFiltersNullTest() {
        var metaBookmarkCriteria = new MetaBookmarkCriteria();
        assertThat(metaBookmarkCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void metaBookmarkCriteriaFluentMethodsCreatesFiltersTest() {
        var metaBookmarkCriteria = new MetaBookmarkCriteria();

        setAllFilters(metaBookmarkCriteria);

        assertThat(metaBookmarkCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void metaBookmarkCriteriaCopyCreatesNullFilterTest() {
        var metaBookmarkCriteria = new MetaBookmarkCriteria();
        var copy = metaBookmarkCriteria.copy();

        assertThat(metaBookmarkCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(metaBookmarkCriteria)
        );
    }

    @Test
    void metaBookmarkCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var metaBookmarkCriteria = new MetaBookmarkCriteria();
        setAllFilters(metaBookmarkCriteria);

        var copy = metaBookmarkCriteria.copy();

        assertThat(metaBookmarkCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(metaBookmarkCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var metaBookmarkCriteria = new MetaBookmarkCriteria();

        assertThat(metaBookmarkCriteria).hasToString("MetaBookmarkCriteria{}");
    }

    private static void setAllFilters(MetaBookmarkCriteria metaBookmarkCriteria) {
        metaBookmarkCriteria.id();
        metaBookmarkCriteria.userId();
        metaBookmarkCriteria.entityType();
        metaBookmarkCriteria.entityName();
        metaBookmarkCriteria.createdDate();
        metaBookmarkCriteria.distinct();
    }

    private static Condition<MetaBookmarkCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getEntityType()) &&
                condition.apply(criteria.getEntityName()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MetaBookmarkCriteria> copyFiltersAre(
        MetaBookmarkCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getEntityType(), copy.getEntityType()) &&
                condition.apply(criteria.getEntityName(), copy.getEntityName()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
