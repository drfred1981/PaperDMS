package fr.smartprod.paperdms.pdftoimage.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ImagePdfConversionRequestCriteriaTest {

    @Test
    void newImagePdfConversionRequestCriteriaHasAllFiltersNullTest() {
        var imagePdfConversionRequestCriteria = new ImagePdfConversionRequestCriteria();
        assertThat(imagePdfConversionRequestCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void imagePdfConversionRequestCriteriaFluentMethodsCreatesFiltersTest() {
        var imagePdfConversionRequestCriteria = new ImagePdfConversionRequestCriteria();

        setAllFilters(imagePdfConversionRequestCriteria);

        assertThat(imagePdfConversionRequestCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void imagePdfConversionRequestCriteriaCopyCreatesNullFilterTest() {
        var imagePdfConversionRequestCriteria = new ImagePdfConversionRequestCriteria();
        var copy = imagePdfConversionRequestCriteria.copy();

        assertThat(imagePdfConversionRequestCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(imagePdfConversionRequestCriteria)
        );
    }

    @Test
    void imagePdfConversionRequestCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var imagePdfConversionRequestCriteria = new ImagePdfConversionRequestCriteria();
        setAllFilters(imagePdfConversionRequestCriteria);

        var copy = imagePdfConversionRequestCriteria.copy();

        assertThat(imagePdfConversionRequestCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(imagePdfConversionRequestCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var imagePdfConversionRequestCriteria = new ImagePdfConversionRequestCriteria();

        assertThat(imagePdfConversionRequestCriteria).hasToString("ImagePdfConversionRequestCriteria{}");
    }

    private static void setAllFilters(ImagePdfConversionRequestCriteria imagePdfConversionRequestCriteria) {
        imagePdfConversionRequestCriteria.id();
        imagePdfConversionRequestCriteria.sourceDocumentId();
        imagePdfConversionRequestCriteria.sourceFileName();
        imagePdfConversionRequestCriteria.sourcePdfS3Key();
        imagePdfConversionRequestCriteria.imageQuality();
        imagePdfConversionRequestCriteria.imageFormat();
        imagePdfConversionRequestCriteria.conversionType();
        imagePdfConversionRequestCriteria.startPage();
        imagePdfConversionRequestCriteria.endPage();
        imagePdfConversionRequestCriteria.totalPages();
        imagePdfConversionRequestCriteria.status();
        imagePdfConversionRequestCriteria.errorMessage();
        imagePdfConversionRequestCriteria.requestedAt();
        imagePdfConversionRequestCriteria.startedAt();
        imagePdfConversionRequestCriteria.completedAt();
        imagePdfConversionRequestCriteria.processingDuration();
        imagePdfConversionRequestCriteria.totalImagesSize();
        imagePdfConversionRequestCriteria.imagesGenerated();
        imagePdfConversionRequestCriteria.dpi();
        imagePdfConversionRequestCriteria.requestedByUserId();
        imagePdfConversionRequestCriteria.priority();
        imagePdfConversionRequestCriteria.generatedImagesId();
        imagePdfConversionRequestCriteria.batchId();
        imagePdfConversionRequestCriteria.distinct();
    }

    private static Condition<ImagePdfConversionRequestCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSourceDocumentId()) &&
                condition.apply(criteria.getSourceFileName()) &&
                condition.apply(criteria.getSourcePdfS3Key()) &&
                condition.apply(criteria.getImageQuality()) &&
                condition.apply(criteria.getImageFormat()) &&
                condition.apply(criteria.getConversionType()) &&
                condition.apply(criteria.getStartPage()) &&
                condition.apply(criteria.getEndPage()) &&
                condition.apply(criteria.getTotalPages()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getErrorMessage()) &&
                condition.apply(criteria.getRequestedAt()) &&
                condition.apply(criteria.getStartedAt()) &&
                condition.apply(criteria.getCompletedAt()) &&
                condition.apply(criteria.getProcessingDuration()) &&
                condition.apply(criteria.getTotalImagesSize()) &&
                condition.apply(criteria.getImagesGenerated()) &&
                condition.apply(criteria.getDpi()) &&
                condition.apply(criteria.getRequestedByUserId()) &&
                condition.apply(criteria.getPriority()) &&
                condition.apply(criteria.getGeneratedImagesId()) &&
                condition.apply(criteria.getBatchId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ImagePdfConversionRequestCriteria> copyFiltersAre(
        ImagePdfConversionRequestCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSourceDocumentId(), copy.getSourceDocumentId()) &&
                condition.apply(criteria.getSourceFileName(), copy.getSourceFileName()) &&
                condition.apply(criteria.getSourcePdfS3Key(), copy.getSourcePdfS3Key()) &&
                condition.apply(criteria.getImageQuality(), copy.getImageQuality()) &&
                condition.apply(criteria.getImageFormat(), copy.getImageFormat()) &&
                condition.apply(criteria.getConversionType(), copy.getConversionType()) &&
                condition.apply(criteria.getStartPage(), copy.getStartPage()) &&
                condition.apply(criteria.getEndPage(), copy.getEndPage()) &&
                condition.apply(criteria.getTotalPages(), copy.getTotalPages()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getErrorMessage(), copy.getErrorMessage()) &&
                condition.apply(criteria.getRequestedAt(), copy.getRequestedAt()) &&
                condition.apply(criteria.getStartedAt(), copy.getStartedAt()) &&
                condition.apply(criteria.getCompletedAt(), copy.getCompletedAt()) &&
                condition.apply(criteria.getProcessingDuration(), copy.getProcessingDuration()) &&
                condition.apply(criteria.getTotalImagesSize(), copy.getTotalImagesSize()) &&
                condition.apply(criteria.getImagesGenerated(), copy.getImagesGenerated()) &&
                condition.apply(criteria.getDpi(), copy.getDpi()) &&
                condition.apply(criteria.getRequestedByUserId(), copy.getRequestedByUserId()) &&
                condition.apply(criteria.getPriority(), copy.getPriority()) &&
                condition.apply(criteria.getGeneratedImagesId(), copy.getGeneratedImagesId()) &&
                condition.apply(criteria.getBatchId(), copy.getBatchId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
