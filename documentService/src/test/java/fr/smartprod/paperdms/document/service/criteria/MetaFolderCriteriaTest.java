package fr.smartprod.paperdms.document.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MetaFolderCriteriaTest {

    @Test
    void newMetaFolderCriteriaHasAllFiltersNullTest() {
        var metaFolderCriteria = new MetaFolderCriteria();
        assertThat(metaFolderCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void metaFolderCriteriaFluentMethodsCreatesFiltersTest() {
        var metaFolderCriteria = new MetaFolderCriteria();

        setAllFilters(metaFolderCriteria);

        assertThat(metaFolderCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void metaFolderCriteriaCopyCreatesNullFilterTest() {
        var metaFolderCriteria = new MetaFolderCriteria();
        var copy = metaFolderCriteria.copy();

        assertThat(metaFolderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(metaFolderCriteria)
        );
    }

    @Test
    void metaFolderCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var metaFolderCriteria = new MetaFolderCriteria();
        setAllFilters(metaFolderCriteria);

        var copy = metaFolderCriteria.copy();

        assertThat(metaFolderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(metaFolderCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var metaFolderCriteria = new MetaFolderCriteria();

        assertThat(metaFolderCriteria).hasToString("MetaFolderCriteria{}");
    }

    private static void setAllFilters(MetaFolderCriteria metaFolderCriteria) {
        metaFolderCriteria.id();
        metaFolderCriteria.name();
        metaFolderCriteria.path();
        metaFolderCriteria.isShared();
        metaFolderCriteria.createdDate();
        metaFolderCriteria.createdBy();
        metaFolderCriteria.childrenId();
        metaFolderCriteria.documentsId();
        metaFolderCriteria.parentId();
        metaFolderCriteria.distinct();
    }

    private static Condition<MetaFolderCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getPath()) &&
                condition.apply(criteria.getIsShared()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getChildrenId()) &&
                condition.apply(criteria.getDocumentsId()) &&
                condition.apply(criteria.getParentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MetaFolderCriteria> copyFiltersAre(MetaFolderCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getPath(), copy.getPath()) &&
                condition.apply(criteria.getIsShared(), copy.getIsShared()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getChildrenId(), copy.getChildrenId()) &&
                condition.apply(criteria.getDocumentsId(), copy.getDocumentsId()) &&
                condition.apply(criteria.getParentId(), copy.getParentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
