package fr.smartprod.paperdms.document.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MetaMetaTagCategoryCriteriaTest {

    @Test
    void newMetaMetaTagCategoryCriteriaHasAllFiltersNullTest() {
        var metaMetaTagCategoryCriteria = new MetaMetaTagCategoryCriteria();
        assertThat(metaMetaTagCategoryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void metaMetaTagCategoryCriteriaFluentMethodsCreatesFiltersTest() {
        var metaMetaTagCategoryCriteria = new MetaMetaTagCategoryCriteria();

        setAllFilters(metaMetaTagCategoryCriteria);

        assertThat(metaMetaTagCategoryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void metaMetaTagCategoryCriteriaCopyCreatesNullFilterTest() {
        var metaMetaTagCategoryCriteria = new MetaMetaTagCategoryCriteria();
        var copy = metaMetaTagCategoryCriteria.copy();

        assertThat(metaMetaTagCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(metaMetaTagCategoryCriteria)
        );
    }

    @Test
    void metaMetaTagCategoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var metaMetaTagCategoryCriteria = new MetaMetaTagCategoryCriteria();
        setAllFilters(metaMetaTagCategoryCriteria);

        var copy = metaMetaTagCategoryCriteria.copy();

        assertThat(metaMetaTagCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(metaMetaTagCategoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var metaMetaTagCategoryCriteria = new MetaMetaTagCategoryCriteria();

        assertThat(metaMetaTagCategoryCriteria).hasToString("MetaMetaTagCategoryCriteria{}");
    }

    private static void setAllFilters(MetaMetaTagCategoryCriteria metaMetaTagCategoryCriteria) {
        metaMetaTagCategoryCriteria.id();
        metaMetaTagCategoryCriteria.name();
        metaMetaTagCategoryCriteria.color();
        metaMetaTagCategoryCriteria.displayOrder();
        metaMetaTagCategoryCriteria.isSystem();
        metaMetaTagCategoryCriteria.createdDate();
        metaMetaTagCategoryCriteria.createdBy();
        metaMetaTagCategoryCriteria.childrenId();
        metaMetaTagCategoryCriteria.metaTagsId();
        metaMetaTagCategoryCriteria.parentId();
        metaMetaTagCategoryCriteria.distinct();
    }

    private static Condition<MetaMetaTagCategoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getColor()) &&
                condition.apply(criteria.getDisplayOrder()) &&
                condition.apply(criteria.getIsSystem()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getChildrenId()) &&
                condition.apply(criteria.getMetaTagsId()) &&
                condition.apply(criteria.getParentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MetaMetaTagCategoryCriteria> copyFiltersAre(
        MetaMetaTagCategoryCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getColor(), copy.getColor()) &&
                condition.apply(criteria.getDisplayOrder(), copy.getDisplayOrder()) &&
                condition.apply(criteria.getIsSystem(), copy.getIsSystem()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getChildrenId(), copy.getChildrenId()) &&
                condition.apply(criteria.getMetaTagsId(), copy.getMetaTagsId()) &&
                condition.apply(criteria.getParentId(), copy.getParentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
