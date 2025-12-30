package fr.smartprod.paperdms.transform.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TransformCompressionJobCriteriaTest {

    @Test
    void newTransformCompressionJobCriteriaHasAllFiltersNullTest() {
        var transformCompressionJobCriteria = new TransformCompressionJobCriteria();
        assertThat(transformCompressionJobCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void transformCompressionJobCriteriaFluentMethodsCreatesFiltersTest() {
        var transformCompressionJobCriteria = new TransformCompressionJobCriteria();

        setAllFilters(transformCompressionJobCriteria);

        assertThat(transformCompressionJobCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void transformCompressionJobCriteriaCopyCreatesNullFilterTest() {
        var transformCompressionJobCriteria = new TransformCompressionJobCriteria();
        var copy = transformCompressionJobCriteria.copy();

        assertThat(transformCompressionJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(transformCompressionJobCriteria)
        );
    }

    @Test
    void transformCompressionJobCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var transformCompressionJobCriteria = new TransformCompressionJobCriteria();
        setAllFilters(transformCompressionJobCriteria);

        var copy = transformCompressionJobCriteria.copy();

        assertThat(transformCompressionJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(transformCompressionJobCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var transformCompressionJobCriteria = new TransformCompressionJobCriteria();

        assertThat(transformCompressionJobCriteria).hasToString("TransformCompressionJobCriteria{}");
    }

    private static void setAllFilters(TransformCompressionJobCriteria transformCompressionJobCriteria) {
        transformCompressionJobCriteria.id();
        transformCompressionJobCriteria.documentSha256();
        transformCompressionJobCriteria.compressionType();
        transformCompressionJobCriteria.quality();
        transformCompressionJobCriteria.targetSizeKb();
        transformCompressionJobCriteria.originalSize();
        transformCompressionJobCriteria.compressedSize();
        transformCompressionJobCriteria.compressionRatio();
        transformCompressionJobCriteria.outputS3Key();
        transformCompressionJobCriteria.outputDocumentSha256();
        transformCompressionJobCriteria.status();
        transformCompressionJobCriteria.startDate();
        transformCompressionJobCriteria.endDate();
        transformCompressionJobCriteria.createdBy();
        transformCompressionJobCriteria.createdDate();
        transformCompressionJobCriteria.distinct();
    }

    private static Condition<TransformCompressionJobCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentSha256()) &&
                condition.apply(criteria.getCompressionType()) &&
                condition.apply(criteria.getQuality()) &&
                condition.apply(criteria.getTargetSizeKb()) &&
                condition.apply(criteria.getOriginalSize()) &&
                condition.apply(criteria.getCompressedSize()) &&
                condition.apply(criteria.getCompressionRatio()) &&
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

    private static Condition<TransformCompressionJobCriteria> copyFiltersAre(
        TransformCompressionJobCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentSha256(), copy.getDocumentSha256()) &&
                condition.apply(criteria.getCompressionType(), copy.getCompressionType()) &&
                condition.apply(criteria.getQuality(), copy.getQuality()) &&
                condition.apply(criteria.getTargetSizeKb(), copy.getTargetSizeKb()) &&
                condition.apply(criteria.getOriginalSize(), copy.getOriginalSize()) &&
                condition.apply(criteria.getCompressedSize(), copy.getCompressedSize()) &&
                condition.apply(criteria.getCompressionRatio(), copy.getCompressionRatio()) &&
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
