package fr.smartprod.paperdms.pdftoimage.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ImageGeneratedImageCriteriaTest {

    @Test
    void newImageGeneratedImageCriteriaHasAllFiltersNullTest() {
        var imageGeneratedImageCriteria = new ImageGeneratedImageCriteria();
        assertThat(imageGeneratedImageCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void imageGeneratedImageCriteriaFluentMethodsCreatesFiltersTest() {
        var imageGeneratedImageCriteria = new ImageGeneratedImageCriteria();

        setAllFilters(imageGeneratedImageCriteria);

        assertThat(imageGeneratedImageCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void imageGeneratedImageCriteriaCopyCreatesNullFilterTest() {
        var imageGeneratedImageCriteria = new ImageGeneratedImageCriteria();
        var copy = imageGeneratedImageCriteria.copy();

        assertThat(imageGeneratedImageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(imageGeneratedImageCriteria)
        );
    }

    @Test
    void imageGeneratedImageCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var imageGeneratedImageCriteria = new ImageGeneratedImageCriteria();
        setAllFilters(imageGeneratedImageCriteria);

        var copy = imageGeneratedImageCriteria.copy();

        assertThat(imageGeneratedImageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(imageGeneratedImageCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var imageGeneratedImageCriteria = new ImageGeneratedImageCriteria();

        assertThat(imageGeneratedImageCriteria).hasToString("ImageGeneratedImageCriteria{}");
    }

    private static void setAllFilters(ImageGeneratedImageCriteria imageGeneratedImageCriteria) {
        imageGeneratedImageCriteria.id();
        imageGeneratedImageCriteria.pageNumber();
        imageGeneratedImageCriteria.fileName();
        imageGeneratedImageCriteria.s3Key();
        imageGeneratedImageCriteria.preSignedUrl();
        imageGeneratedImageCriteria.urlExpiresAt();
        imageGeneratedImageCriteria.format();
        imageGeneratedImageCriteria.quality();
        imageGeneratedImageCriteria.width();
        imageGeneratedImageCriteria.height();
        imageGeneratedImageCriteria.fileSize();
        imageGeneratedImageCriteria.dpi();
        imageGeneratedImageCriteria.sha256Hash();
        imageGeneratedImageCriteria.generatedAt();
        imageGeneratedImageCriteria.conversionRequestId();
        imageGeneratedImageCriteria.distinct();
    }

    private static Condition<ImageGeneratedImageCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPageNumber()) &&
                condition.apply(criteria.getFileName()) &&
                condition.apply(criteria.gets3Key()) &&
                condition.apply(criteria.getPreSignedUrl()) &&
                condition.apply(criteria.getUrlExpiresAt()) &&
                condition.apply(criteria.getFormat()) &&
                condition.apply(criteria.getQuality()) &&
                condition.apply(criteria.getWidth()) &&
                condition.apply(criteria.getHeight()) &&
                condition.apply(criteria.getFileSize()) &&
                condition.apply(criteria.getDpi()) &&
                condition.apply(criteria.getSha256Hash()) &&
                condition.apply(criteria.getGeneratedAt()) &&
                condition.apply(criteria.getConversionRequestId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ImageGeneratedImageCriteria> copyFiltersAre(
        ImageGeneratedImageCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPageNumber(), copy.getPageNumber()) &&
                condition.apply(criteria.getFileName(), copy.getFileName()) &&
                condition.apply(criteria.gets3Key(), copy.gets3Key()) &&
                condition.apply(criteria.getPreSignedUrl(), copy.getPreSignedUrl()) &&
                condition.apply(criteria.getUrlExpiresAt(), copy.getUrlExpiresAt()) &&
                condition.apply(criteria.getFormat(), copy.getFormat()) &&
                condition.apply(criteria.getQuality(), copy.getQuality()) &&
                condition.apply(criteria.getWidth(), copy.getWidth()) &&
                condition.apply(criteria.getHeight(), copy.getHeight()) &&
                condition.apply(criteria.getFileSize(), copy.getFileSize()) &&
                condition.apply(criteria.getDpi(), copy.getDpi()) &&
                condition.apply(criteria.getSha256Hash(), copy.getSha256Hash()) &&
                condition.apply(criteria.getGeneratedAt(), copy.getGeneratedAt()) &&
                condition.apply(criteria.getConversionRequestId(), copy.getConversionRequestId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
