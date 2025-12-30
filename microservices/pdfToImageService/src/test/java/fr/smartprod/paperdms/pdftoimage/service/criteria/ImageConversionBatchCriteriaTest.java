package fr.smartprod.paperdms.pdftoimage.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ImageConversionBatchCriteriaTest {

    @Test
    void newImageConversionBatchCriteriaHasAllFiltersNullTest() {
        var imageConversionBatchCriteria = new ImageConversionBatchCriteria();
        assertThat(imageConversionBatchCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void imageConversionBatchCriteriaFluentMethodsCreatesFiltersTest() {
        var imageConversionBatchCriteria = new ImageConversionBatchCriteria();

        setAllFilters(imageConversionBatchCriteria);

        assertThat(imageConversionBatchCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void imageConversionBatchCriteriaCopyCreatesNullFilterTest() {
        var imageConversionBatchCriteria = new ImageConversionBatchCriteria();
        var copy = imageConversionBatchCriteria.copy();

        assertThat(imageConversionBatchCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(imageConversionBatchCriteria)
        );
    }

    @Test
    void imageConversionBatchCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var imageConversionBatchCriteria = new ImageConversionBatchCriteria();
        setAllFilters(imageConversionBatchCriteria);

        var copy = imageConversionBatchCriteria.copy();

        assertThat(imageConversionBatchCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(imageConversionBatchCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var imageConversionBatchCriteria = new ImageConversionBatchCriteria();

        assertThat(imageConversionBatchCriteria).hasToString("ImageConversionBatchCriteria{}");
    }

    private static void setAllFilters(ImageConversionBatchCriteria imageConversionBatchCriteria) {
        imageConversionBatchCriteria.id();
        imageConversionBatchCriteria.batchName();
        imageConversionBatchCriteria.description();
        imageConversionBatchCriteria.createdAt();
        imageConversionBatchCriteria.status();
        imageConversionBatchCriteria.totalConversions();
        imageConversionBatchCriteria.completedConversions();
        imageConversionBatchCriteria.failedConversions();
        imageConversionBatchCriteria.startedAt();
        imageConversionBatchCriteria.completedAt();
        imageConversionBatchCriteria.totalProcessingDuration();
        imageConversionBatchCriteria.createdByUserId();
        imageConversionBatchCriteria.conversionsId();
        imageConversionBatchCriteria.distinct();
    }

    private static Condition<ImageConversionBatchCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getBatchName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getTotalConversions()) &&
                condition.apply(criteria.getCompletedConversions()) &&
                condition.apply(criteria.getFailedConversions()) &&
                condition.apply(criteria.getStartedAt()) &&
                condition.apply(criteria.getCompletedAt()) &&
                condition.apply(criteria.getTotalProcessingDuration()) &&
                condition.apply(criteria.getCreatedByUserId()) &&
                condition.apply(criteria.getConversionsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ImageConversionBatchCriteria> copyFiltersAre(
        ImageConversionBatchCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getBatchName(), copy.getBatchName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getTotalConversions(), copy.getTotalConversions()) &&
                condition.apply(criteria.getCompletedConversions(), copy.getCompletedConversions()) &&
                condition.apply(criteria.getFailedConversions(), copy.getFailedConversions()) &&
                condition.apply(criteria.getStartedAt(), copy.getStartedAt()) &&
                condition.apply(criteria.getCompletedAt(), copy.getCompletedAt()) &&
                condition.apply(criteria.getTotalProcessingDuration(), copy.getTotalProcessingDuration()) &&
                condition.apply(criteria.getCreatedByUserId(), copy.getCreatedByUserId()) &&
                condition.apply(criteria.getConversionsId(), copy.getConversionsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
