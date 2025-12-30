package fr.smartprod.paperdms.pdftoimage.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ImageConversionHistoryCriteriaTest {

    @Test
    void newImageConversionHistoryCriteriaHasAllFiltersNullTest() {
        var imageConversionHistoryCriteria = new ImageConversionHistoryCriteria();
        assertThat(imageConversionHistoryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void imageConversionHistoryCriteriaFluentMethodsCreatesFiltersTest() {
        var imageConversionHistoryCriteria = new ImageConversionHistoryCriteria();

        setAllFilters(imageConversionHistoryCriteria);

        assertThat(imageConversionHistoryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void imageConversionHistoryCriteriaCopyCreatesNullFilterTest() {
        var imageConversionHistoryCriteria = new ImageConversionHistoryCriteria();
        var copy = imageConversionHistoryCriteria.copy();

        assertThat(imageConversionHistoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(imageConversionHistoryCriteria)
        );
    }

    @Test
    void imageConversionHistoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var imageConversionHistoryCriteria = new ImageConversionHistoryCriteria();
        setAllFilters(imageConversionHistoryCriteria);

        var copy = imageConversionHistoryCriteria.copy();

        assertThat(imageConversionHistoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(imageConversionHistoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var imageConversionHistoryCriteria = new ImageConversionHistoryCriteria();

        assertThat(imageConversionHistoryCriteria).hasToString("ImageConversionHistoryCriteria{}");
    }

    private static void setAllFilters(ImageConversionHistoryCriteria imageConversionHistoryCriteria) {
        imageConversionHistoryCriteria.id();
        imageConversionHistoryCriteria.originalRequestId();
        imageConversionHistoryCriteria.archivedAt();
        imageConversionHistoryCriteria.imagesCount();
        imageConversionHistoryCriteria.totalSize();
        imageConversionHistoryCriteria.finalStatus();
        imageConversionHistoryCriteria.processingDuration();
        imageConversionHistoryCriteria.distinct();
    }

    private static Condition<ImageConversionHistoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getOriginalRequestId()) &&
                condition.apply(criteria.getArchivedAt()) &&
                condition.apply(criteria.getImagesCount()) &&
                condition.apply(criteria.getTotalSize()) &&
                condition.apply(criteria.getFinalStatus()) &&
                condition.apply(criteria.getProcessingDuration()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ImageConversionHistoryCriteria> copyFiltersAre(
        ImageConversionHistoryCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getOriginalRequestId(), copy.getOriginalRequestId()) &&
                condition.apply(criteria.getArchivedAt(), copy.getArchivedAt()) &&
                condition.apply(criteria.getImagesCount(), copy.getImagesCount()) &&
                condition.apply(criteria.getTotalSize(), copy.getTotalSize()) &&
                condition.apply(criteria.getFinalStatus(), copy.getFinalStatus()) &&
                condition.apply(criteria.getProcessingDuration(), copy.getProcessingDuration()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
