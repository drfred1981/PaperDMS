package fr.smartprod.paperdms.transform.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TransformConversionJobCriteriaTest {

    @Test
    void newTransformConversionJobCriteriaHasAllFiltersNullTest() {
        var transformConversionJobCriteria = new TransformConversionJobCriteria();
        assertThat(transformConversionJobCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void transformConversionJobCriteriaFluentMethodsCreatesFiltersTest() {
        var transformConversionJobCriteria = new TransformConversionJobCriteria();

        setAllFilters(transformConversionJobCriteria);

        assertThat(transformConversionJobCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void transformConversionJobCriteriaCopyCreatesNullFilterTest() {
        var transformConversionJobCriteria = new TransformConversionJobCriteria();
        var copy = transformConversionJobCriteria.copy();

        assertThat(transformConversionJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(transformConversionJobCriteria)
        );
    }

    @Test
    void transformConversionJobCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var transformConversionJobCriteria = new TransformConversionJobCriteria();
        setAllFilters(transformConversionJobCriteria);

        var copy = transformConversionJobCriteria.copy();

        assertThat(transformConversionJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(transformConversionJobCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var transformConversionJobCriteria = new TransformConversionJobCriteria();

        assertThat(transformConversionJobCriteria).hasToString("TransformConversionJobCriteria{}");
    }

    private static void setAllFilters(TransformConversionJobCriteria transformConversionJobCriteria) {
        transformConversionJobCriteria.id();
        transformConversionJobCriteria.documentSha256();
        transformConversionJobCriteria.sourceFormat();
        transformConversionJobCriteria.targetFormat();
        transformConversionJobCriteria.conversionEngine();
        transformConversionJobCriteria.outputS3Key();
        transformConversionJobCriteria.outputDocumentSha256();
        transformConversionJobCriteria.status();
        transformConversionJobCriteria.startDate();
        transformConversionJobCriteria.endDate();
        transformConversionJobCriteria.createdBy();
        transformConversionJobCriteria.createdDate();
        transformConversionJobCriteria.distinct();
    }

    private static Condition<TransformConversionJobCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentSha256()) &&
                condition.apply(criteria.getSourceFormat()) &&
                condition.apply(criteria.getTargetFormat()) &&
                condition.apply(criteria.getConversionEngine()) &&
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

    private static Condition<TransformConversionJobCriteria> copyFiltersAre(
        TransformConversionJobCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentSha256(), copy.getDocumentSha256()) &&
                condition.apply(criteria.getSourceFormat(), copy.getSourceFormat()) &&
                condition.apply(criteria.getTargetFormat(), copy.getTargetFormat()) &&
                condition.apply(criteria.getConversionEngine(), copy.getConversionEngine()) &&
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
