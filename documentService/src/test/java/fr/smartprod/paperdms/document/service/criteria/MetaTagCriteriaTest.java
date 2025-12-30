package fr.smartprod.paperdms.document.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MetaTagCriteriaTest {

    @Test
    void newMetaTagCriteriaHasAllFiltersNullTest() {
        var metaTagCriteria = new MetaTagCriteria();
        assertThat(metaTagCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void metaTagCriteriaFluentMethodsCreatesFiltersTest() {
        var metaTagCriteria = new MetaTagCriteria();

        setAllFilters(metaTagCriteria);

        assertThat(metaTagCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void metaTagCriteriaCopyCreatesNullFilterTest() {
        var metaTagCriteria = new MetaTagCriteria();
        var copy = metaTagCriteria.copy();

        assertThat(metaTagCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(metaTagCriteria)
        );
    }

    @Test
    void metaTagCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var metaTagCriteria = new MetaTagCriteria();
        setAllFilters(metaTagCriteria);

        var copy = metaTagCriteria.copy();

        assertThat(metaTagCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(metaTagCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var metaTagCriteria = new MetaTagCriteria();

        assertThat(metaTagCriteria).hasToString("MetaTagCriteria{}");
    }

    private static void setAllFilters(MetaTagCriteria metaTagCriteria) {
        metaTagCriteria.id();
        metaTagCriteria.name();
        metaTagCriteria.color();
        metaTagCriteria.description();
        metaTagCriteria.usageCount();
        metaTagCriteria.isSystem();
        metaTagCriteria.createdDate();
        metaTagCriteria.createdBy();
        metaTagCriteria.documentTagsId();
        metaTagCriteria.metaMetaTagCategoryId();
        metaTagCriteria.distinct();
    }

    private static Condition<MetaTagCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getColor()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getUsageCount()) &&
                condition.apply(criteria.getIsSystem()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getDocumentTagsId()) &&
                condition.apply(criteria.getMetaMetaTagCategoryId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MetaTagCriteria> copyFiltersAre(MetaTagCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getColor(), copy.getColor()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getUsageCount(), copy.getUsageCount()) &&
                condition.apply(criteria.getIsSystem(), copy.getIsSystem()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getDocumentTagsId(), copy.getDocumentTagsId()) &&
                condition.apply(criteria.getMetaMetaTagCategoryId(), copy.getMetaMetaTagCategoryId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
