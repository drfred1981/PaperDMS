package fr.smartprod.paperdms.document.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MetaPermissionGroupCriteriaTest {

    @Test
    void newMetaPermissionGroupCriteriaHasAllFiltersNullTest() {
        var metaPermissionGroupCriteria = new MetaPermissionGroupCriteria();
        assertThat(metaPermissionGroupCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void metaPermissionGroupCriteriaFluentMethodsCreatesFiltersTest() {
        var metaPermissionGroupCriteria = new MetaPermissionGroupCriteria();

        setAllFilters(metaPermissionGroupCriteria);

        assertThat(metaPermissionGroupCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void metaPermissionGroupCriteriaCopyCreatesNullFilterTest() {
        var metaPermissionGroupCriteria = new MetaPermissionGroupCriteria();
        var copy = metaPermissionGroupCriteria.copy();

        assertThat(metaPermissionGroupCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(metaPermissionGroupCriteria)
        );
    }

    @Test
    void metaPermissionGroupCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var metaPermissionGroupCriteria = new MetaPermissionGroupCriteria();
        setAllFilters(metaPermissionGroupCriteria);

        var copy = metaPermissionGroupCriteria.copy();

        assertThat(metaPermissionGroupCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(metaPermissionGroupCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var metaPermissionGroupCriteria = new MetaPermissionGroupCriteria();

        assertThat(metaPermissionGroupCriteria).hasToString("MetaPermissionGroupCriteria{}");
    }

    private static void setAllFilters(MetaPermissionGroupCriteria metaPermissionGroupCriteria) {
        metaPermissionGroupCriteria.id();
        metaPermissionGroupCriteria.name();
        metaPermissionGroupCriteria.isSystem();
        metaPermissionGroupCriteria.createdDate();
        metaPermissionGroupCriteria.createdBy();
        metaPermissionGroupCriteria.documentPermissionsId();
        metaPermissionGroupCriteria.distinct();
    }

    private static Condition<MetaPermissionGroupCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getIsSystem()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getDocumentPermissionsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MetaPermissionGroupCriteria> copyFiltersAre(
        MetaPermissionGroupCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getIsSystem(), copy.getIsSystem()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getDocumentPermissionsId(), copy.getDocumentPermissionsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
