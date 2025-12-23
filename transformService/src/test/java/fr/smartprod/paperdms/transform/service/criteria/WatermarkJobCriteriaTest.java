package fr.smartprod.paperdms.transform.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class WatermarkJobCriteriaTest {

    @Test
    void newWatermarkJobCriteriaHasAllFiltersNullTest() {
        var watermarkJobCriteria = new WatermarkJobCriteria();
        assertThat(watermarkJobCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void watermarkJobCriteriaFluentMethodsCreatesFiltersTest() {
        var watermarkJobCriteria = new WatermarkJobCriteria();

        setAllFilters(watermarkJobCriteria);

        assertThat(watermarkJobCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void watermarkJobCriteriaCopyCreatesNullFilterTest() {
        var watermarkJobCriteria = new WatermarkJobCriteria();
        var copy = watermarkJobCriteria.copy();

        assertThat(watermarkJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(watermarkJobCriteria)
        );
    }

    @Test
    void watermarkJobCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var watermarkJobCriteria = new WatermarkJobCriteria();
        setAllFilters(watermarkJobCriteria);

        var copy = watermarkJobCriteria.copy();

        assertThat(watermarkJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(watermarkJobCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var watermarkJobCriteria = new WatermarkJobCriteria();

        assertThat(watermarkJobCriteria).hasToString("WatermarkJobCriteria{}");
    }

    private static void setAllFilters(WatermarkJobCriteria watermarkJobCriteria) {
        watermarkJobCriteria.id();
        watermarkJobCriteria.documentId();
        watermarkJobCriteria.watermarkType();
        watermarkJobCriteria.watermarkText();
        watermarkJobCriteria.watermarkImageS3Key();
        watermarkJobCriteria.position();
        watermarkJobCriteria.opacity();
        watermarkJobCriteria.fontSize();
        watermarkJobCriteria.color();
        watermarkJobCriteria.rotation();
        watermarkJobCriteria.tiled();
        watermarkJobCriteria.outputS3Key();
        watermarkJobCriteria.outputDocumentId();
        watermarkJobCriteria.status();
        watermarkJobCriteria.startDate();
        watermarkJobCriteria.endDate();
        watermarkJobCriteria.createdBy();
        watermarkJobCriteria.createdDate();
        watermarkJobCriteria.distinct();
    }

    private static Condition<WatermarkJobCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentId()) &&
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
                condition.apply(criteria.getOutputDocumentId()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<WatermarkJobCriteria> copyFiltersAre(
        WatermarkJobCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentId(), copy.getDocumentId()) &&
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
                condition.apply(criteria.getOutputDocumentId(), copy.getOutputDocumentId()) &&
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
