package fr.smartprod.paperdms.transform.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TransformWatermarkJobCriteriaTest {

    @Test
    void newTransformWatermarkJobCriteriaHasAllFiltersNullTest() {
        var transformWatermarkJobCriteria = new TransformWatermarkJobCriteria();
        assertThat(transformWatermarkJobCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void transformWatermarkJobCriteriaFluentMethodsCreatesFiltersTest() {
        var transformWatermarkJobCriteria = new TransformWatermarkJobCriteria();

        setAllFilters(transformWatermarkJobCriteria);

        assertThat(transformWatermarkJobCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void transformWatermarkJobCriteriaCopyCreatesNullFilterTest() {
        var transformWatermarkJobCriteria = new TransformWatermarkJobCriteria();
        var copy = transformWatermarkJobCriteria.copy();

        assertThat(transformWatermarkJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(transformWatermarkJobCriteria)
        );
    }

    @Test
    void transformWatermarkJobCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var transformWatermarkJobCriteria = new TransformWatermarkJobCriteria();
        setAllFilters(transformWatermarkJobCriteria);

        var copy = transformWatermarkJobCriteria.copy();

        assertThat(transformWatermarkJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(transformWatermarkJobCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var transformWatermarkJobCriteria = new TransformWatermarkJobCriteria();

        assertThat(transformWatermarkJobCriteria).hasToString("TransformWatermarkJobCriteria{}");
    }

    private static void setAllFilters(TransformWatermarkJobCriteria transformWatermarkJobCriteria) {
        transformWatermarkJobCriteria.id();
        transformWatermarkJobCriteria.documentSha256();
        transformWatermarkJobCriteria.watermarkType();
        transformWatermarkJobCriteria.watermarkText();
        transformWatermarkJobCriteria.watermarkImageS3Key();
        transformWatermarkJobCriteria.position();
        transformWatermarkJobCriteria.opacity();
        transformWatermarkJobCriteria.fontSize();
        transformWatermarkJobCriteria.color();
        transformWatermarkJobCriteria.rotation();
        transformWatermarkJobCriteria.tiled();
        transformWatermarkJobCriteria.outputS3Key();
        transformWatermarkJobCriteria.outputDocumentSha256();
        transformWatermarkJobCriteria.status();
        transformWatermarkJobCriteria.startDate();
        transformWatermarkJobCriteria.endDate();
        transformWatermarkJobCriteria.createdBy();
        transformWatermarkJobCriteria.createdDate();
        transformWatermarkJobCriteria.distinct();
    }

    private static Condition<TransformWatermarkJobCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentSha256()) &&
                condition.apply(criteria.getWatermarkType()) &&
                condition.apply(criteria.getWatermarkText()) &&
                condition.apply(criteria.getWatermarkImageS3Key()) &&
                condition.apply(criteria.getPosition()) &&
                condition.apply(criteria.getOpacity()) &&
                condition.apply(criteria.getFontSize()) &&
                condition.apply(criteria.getColor()) &&
                condition.apply(criteria.getRotation()) &&
                condition.apply(criteria.getTiled()) &&
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

    private static Condition<TransformWatermarkJobCriteria> copyFiltersAre(
        TransformWatermarkJobCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentSha256(), copy.getDocumentSha256()) &&
                condition.apply(criteria.getWatermarkType(), copy.getWatermarkType()) &&
                condition.apply(criteria.getWatermarkText(), copy.getWatermarkText()) &&
                condition.apply(criteria.getWatermarkImageS3Key(), copy.getWatermarkImageS3Key()) &&
                condition.apply(criteria.getPosition(), copy.getPosition()) &&
                condition.apply(criteria.getOpacity(), copy.getOpacity()) &&
                condition.apply(criteria.getFontSize(), copy.getFontSize()) &&
                condition.apply(criteria.getColor(), copy.getColor()) &&
                condition.apply(criteria.getRotation(), copy.getRotation()) &&
                condition.apply(criteria.getTiled(), copy.getTiled()) &&
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
