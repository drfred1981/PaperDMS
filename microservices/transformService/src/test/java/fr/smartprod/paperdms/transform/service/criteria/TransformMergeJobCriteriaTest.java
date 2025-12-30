package fr.smartprod.paperdms.transform.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TransformMergeJobCriteriaTest {

    @Test
    void newTransformMergeJobCriteriaHasAllFiltersNullTest() {
        var transformMergeJobCriteria = new TransformMergeJobCriteria();
        assertThat(transformMergeJobCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void transformMergeJobCriteriaFluentMethodsCreatesFiltersTest() {
        var transformMergeJobCriteria = new TransformMergeJobCriteria();

        setAllFilters(transformMergeJobCriteria);

        assertThat(transformMergeJobCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void transformMergeJobCriteriaCopyCreatesNullFilterTest() {
        var transformMergeJobCriteria = new TransformMergeJobCriteria();
        var copy = transformMergeJobCriteria.copy();

        assertThat(transformMergeJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(transformMergeJobCriteria)
        );
    }

    @Test
    void transformMergeJobCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var transformMergeJobCriteria = new TransformMergeJobCriteria();
        setAllFilters(transformMergeJobCriteria);

        var copy = transformMergeJobCriteria.copy();

        assertThat(transformMergeJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(transformMergeJobCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var transformMergeJobCriteria = new TransformMergeJobCriteria();

        assertThat(transformMergeJobCriteria).hasToString("TransformMergeJobCriteria{}");
    }

    private static void setAllFilters(TransformMergeJobCriteria transformMergeJobCriteria) {
        transformMergeJobCriteria.id();
        transformMergeJobCriteria.name();
        transformMergeJobCriteria.includeBookmarks();
        transformMergeJobCriteria.includeToc();
        transformMergeJobCriteria.addPageNumbers();
        transformMergeJobCriteria.outputS3Key();
        transformMergeJobCriteria.outputDocumentSha256();
        transformMergeJobCriteria.status();
        transformMergeJobCriteria.startDate();
        transformMergeJobCriteria.endDate();
        transformMergeJobCriteria.createdBy();
        transformMergeJobCriteria.createdDate();
        transformMergeJobCriteria.distinct();
    }

    private static Condition<TransformMergeJobCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getIncludeBookmarks()) &&
                condition.apply(criteria.getIncludeToc()) &&
                condition.apply(criteria.getAddPageNumbers()) &&
                condition.apply(criteria.getOutputS3Key()) &&
                condition.apply(criteria.getOutputDocumentSha256()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TransformMergeJobCriteria> copyFiltersAre(
        TransformMergeJobCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getIncludeBookmarks(), copy.getIncludeBookmarks()) &&
                condition.apply(criteria.getIncludeToc(), copy.getIncludeToc()) &&
                condition.apply(criteria.getAddPageNumbers(), copy.getAddPageNumbers()) &&
                condition.apply(criteria.getOutputS3Key(), copy.getOutputS3Key()) &&
                condition.apply(criteria.getOutputDocumentSha256(), copy.getOutputDocumentSha256()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
