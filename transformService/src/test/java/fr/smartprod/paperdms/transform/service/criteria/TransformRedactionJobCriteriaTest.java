package fr.smartprod.paperdms.transform.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TransformRedactionJobCriteriaTest {

    @Test
    void newTransformRedactionJobCriteriaHasAllFiltersNullTest() {
        var transformRedactionJobCriteria = new TransformRedactionJobCriteria();
        assertThat(transformRedactionJobCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void transformRedactionJobCriteriaFluentMethodsCreatesFiltersTest() {
        var transformRedactionJobCriteria = new TransformRedactionJobCriteria();

        setAllFilters(transformRedactionJobCriteria);

        assertThat(transformRedactionJobCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void transformRedactionJobCriteriaCopyCreatesNullFilterTest() {
        var transformRedactionJobCriteria = new TransformRedactionJobCriteria();
        var copy = transformRedactionJobCriteria.copy();

        assertThat(transformRedactionJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(transformRedactionJobCriteria)
        );
    }

    @Test
    void transformRedactionJobCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var transformRedactionJobCriteria = new TransformRedactionJobCriteria();
        setAllFilters(transformRedactionJobCriteria);

        var copy = transformRedactionJobCriteria.copy();

        assertThat(transformRedactionJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(transformRedactionJobCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var transformRedactionJobCriteria = new TransformRedactionJobCriteria();

        assertThat(transformRedactionJobCriteria).hasToString("TransformRedactionJobCriteria{}");
    }

    private static void setAllFilters(TransformRedactionJobCriteria transformRedactionJobCriteria) {
        transformRedactionJobCriteria.id();
        transformRedactionJobCriteria.documentSha256();
        transformRedactionJobCriteria.redactionType();
        transformRedactionJobCriteria.redactionColor();
        transformRedactionJobCriteria.replaceWith();
        transformRedactionJobCriteria.outputS3Key();
        transformRedactionJobCriteria.outputDocumentSha256();
        transformRedactionJobCriteria.status();
        transformRedactionJobCriteria.startDate();
        transformRedactionJobCriteria.endDate();
        transformRedactionJobCriteria.createdBy();
        transformRedactionJobCriteria.createdDate();
        transformRedactionJobCriteria.distinct();
    }

    private static Condition<TransformRedactionJobCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentSha256()) &&
                condition.apply(criteria.getRedactionType()) &&
                condition.apply(criteria.getRedactionColor()) &&
                condition.apply(criteria.getReplaceWith()) &&
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

    private static Condition<TransformRedactionJobCriteria> copyFiltersAre(
        TransformRedactionJobCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentSha256(), copy.getDocumentSha256()) &&
                condition.apply(criteria.getRedactionType(), copy.getRedactionType()) &&
                condition.apply(criteria.getRedactionColor(), copy.getRedactionColor()) &&
                condition.apply(criteria.getReplaceWith(), copy.getReplaceWith()) &&
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
