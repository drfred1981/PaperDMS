package fr.smartprod.paperdms.document.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FolderCriteriaTest {

    @Test
    void newFolderCriteriaHasAllFiltersNullTest() {
        var folderCriteria = new FolderCriteria();
        assertThat(folderCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void folderCriteriaFluentMethodsCreatesFiltersTest() {
        var folderCriteria = new FolderCriteria();

        setAllFilters(folderCriteria);

        assertThat(folderCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void folderCriteriaCopyCreatesNullFilterTest() {
        var folderCriteria = new FolderCriteria();
        var copy = folderCriteria.copy();

        assertThat(folderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(folderCriteria)
        );
    }

    @Test
    void folderCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var folderCriteria = new FolderCriteria();
        setAllFilters(folderCriteria);

        var copy = folderCriteria.copy();

        assertThat(folderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(folderCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var folderCriteria = new FolderCriteria();

        assertThat(folderCriteria).hasToString("FolderCriteria{}");
    }

    private static void setAllFilters(FolderCriteria folderCriteria) {
        folderCriteria.id();
        folderCriteria.name();
        folderCriteria.path();
        folderCriteria.isShared();
        folderCriteria.createdDate();
        folderCriteria.createdBy();
        folderCriteria.childrenId();
        folderCriteria.parentId();
        folderCriteria.distinct();
    }

    private static Condition<FolderCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getPath()) &&
                condition.apply(criteria.getIsShared()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getChildrenId()) &&
                condition.apply(criteria.getParentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FolderCriteria> copyFiltersAre(FolderCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getPath(), copy.getPath()) &&
                condition.apply(criteria.getIsShared(), copy.getIsShared()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getChildrenId(), copy.getChildrenId()) &&
                condition.apply(criteria.getParentId(), copy.getParentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
