package fr.smartprod.paperdms.pdftoimage.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ImageConversionStatisticsCriteriaTest {

    @Test
    void newImageConversionStatisticsCriteriaHasAllFiltersNullTest() {
        var imageConversionStatisticsCriteria = new ImageConversionStatisticsCriteria();
        assertThat(imageConversionStatisticsCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void imageConversionStatisticsCriteriaFluentMethodsCreatesFiltersTest() {
        var imageConversionStatisticsCriteria = new ImageConversionStatisticsCriteria();

        setAllFilters(imageConversionStatisticsCriteria);

        assertThat(imageConversionStatisticsCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void imageConversionStatisticsCriteriaCopyCreatesNullFilterTest() {
        var imageConversionStatisticsCriteria = new ImageConversionStatisticsCriteria();
        var copy = imageConversionStatisticsCriteria.copy();

        assertThat(imageConversionStatisticsCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(imageConversionStatisticsCriteria)
        );
    }

    @Test
    void imageConversionStatisticsCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var imageConversionStatisticsCriteria = new ImageConversionStatisticsCriteria();
        setAllFilters(imageConversionStatisticsCriteria);

        var copy = imageConversionStatisticsCriteria.copy();

        assertThat(imageConversionStatisticsCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(imageConversionStatisticsCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var imageConversionStatisticsCriteria = new ImageConversionStatisticsCriteria();

        assertThat(imageConversionStatisticsCriteria).hasToString("ImageConversionStatisticsCriteria{}");
    }

    private static void setAllFilters(ImageConversionStatisticsCriteria imageConversionStatisticsCriteria) {
        imageConversionStatisticsCriteria.id();
        imageConversionStatisticsCriteria.statisticsDate();
        imageConversionStatisticsCriteria.totalConversions();
        imageConversionStatisticsCriteria.successfulConversions();
        imageConversionStatisticsCriteria.failedConversions();
        imageConversionStatisticsCriteria.totalPagesConverted();
        imageConversionStatisticsCriteria.totalImagesGenerated();
        imageConversionStatisticsCriteria.totalImagesSize();
        imageConversionStatisticsCriteria.averageProcessingDuration();
        imageConversionStatisticsCriteria.maxProcessingDuration();
        imageConversionStatisticsCriteria.minProcessingDuration();
        imageConversionStatisticsCriteria.calculatedAt();
        imageConversionStatisticsCriteria.distinct();
    }

    private static Condition<ImageConversionStatisticsCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStatisticsDate()) &&
                condition.apply(criteria.getTotalConversions()) &&
                condition.apply(criteria.getSuccessfulConversions()) &&
                condition.apply(criteria.getFailedConversions()) &&
                condition.apply(criteria.getTotalPagesConverted()) &&
                condition.apply(criteria.getTotalImagesGenerated()) &&
                condition.apply(criteria.getTotalImagesSize()) &&
                condition.apply(criteria.getAverageProcessingDuration()) &&
                condition.apply(criteria.getMaxProcessingDuration()) &&
                condition.apply(criteria.getMinProcessingDuration()) &&
                condition.apply(criteria.getCalculatedAt()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ImageConversionStatisticsCriteria> copyFiltersAre(
        ImageConversionStatisticsCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStatisticsDate(), copy.getStatisticsDate()) &&
                condition.apply(criteria.getTotalConversions(), copy.getTotalConversions()) &&
                condition.apply(criteria.getSuccessfulConversions(), copy.getSuccessfulConversions()) &&
                condition.apply(criteria.getFailedConversions(), copy.getFailedConversions()) &&
                condition.apply(criteria.getTotalPagesConverted(), copy.getTotalPagesConverted()) &&
                condition.apply(criteria.getTotalImagesGenerated(), copy.getTotalImagesGenerated()) &&
                condition.apply(criteria.getTotalImagesSize(), copy.getTotalImagesSize()) &&
                condition.apply(criteria.getAverageProcessingDuration(), copy.getAverageProcessingDuration()) &&
                condition.apply(criteria.getMaxProcessingDuration(), copy.getMaxProcessingDuration()) &&
                condition.apply(criteria.getMinProcessingDuration(), copy.getMinProcessingDuration()) &&
                condition.apply(criteria.getCalculatedAt(), copy.getCalculatedAt()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
