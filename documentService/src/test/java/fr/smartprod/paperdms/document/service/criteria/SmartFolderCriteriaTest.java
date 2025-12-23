package fr.smartprod.paperdms.document.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SmartFolderCriteriaTest {

    @Test
    void newSmartFolderCriteriaHasAllFiltersNullTest() {
        var smartFolderCriteria = new SmartFolderCriteria();
        assertThat(smartFolderCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void smartFolderCriteriaFluentMethodsCreatesFiltersTest() {
        var smartFolderCriteria = new SmartFolderCriteria();

        setAllFilters(smartFolderCriteria);

        assertThat(smartFolderCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void smartFolderCriteriaCopyCreatesNullFilterTest() {
        var smartFolderCriteria = new SmartFolderCriteria();
        var copy = smartFolderCriteria.copy();

        assertThat(smartFolderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(smartFolderCriteria)
        );
    }

    @Test
    void smartFolderCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var smartFolderCriteria = new SmartFolderCriteria();
        setAllFilters(smartFolderCriteria);

        var copy = smartFolderCriteria.copy();

        assertThat(smartFolderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(smartFolderCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var smartFolderCriteria = new SmartFolderCriteria();

        assertThat(smartFolderCriteria).hasToString("SmartFolderCriteria{}");
    }

    private static void setAllFilters(SmartFolderCriteria smartFolderCriteria) {
        smartFolderCriteria.id();
        smartFolderCriteria.name();
        smartFolderCriteria.autoRefresh();
        smartFolderCriteria.isPublic();
        smartFolderCriteria.createdBy();
        smartFolderCriteria.createdDate();
        smartFolderCriteria.distinct();
    }

    private static Condition<SmartFolderCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getAutoRefresh()) &&
                condition.apply(criteria.getIsPublic()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SmartFolderCriteria> copyFiltersAre(SmartFolderCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getAutoRefresh(), copy.getAutoRefresh()) &&
                condition.apply(criteria.getIsPublic(), copy.getIsPublic()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
