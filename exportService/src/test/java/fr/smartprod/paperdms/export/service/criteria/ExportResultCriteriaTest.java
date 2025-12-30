package fr.smartprod.paperdms.export.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ExportResultCriteriaTest {

    @Test
    void newExportResultCriteriaHasAllFiltersNullTest() {
        var exportResultCriteria = new ExportResultCriteria();
        assertThat(exportResultCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void exportResultCriteriaFluentMethodsCreatesFiltersTest() {
        var exportResultCriteria = new ExportResultCriteria();

        setAllFilters(exportResultCriteria);

        assertThat(exportResultCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void exportResultCriteriaCopyCreatesNullFilterTest() {
        var exportResultCriteria = new ExportResultCriteria();
        var copy = exportResultCriteria.copy();

        assertThat(exportResultCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(exportResultCriteria)
        );
    }

    @Test
    void exportResultCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var exportResultCriteria = new ExportResultCriteria();
        setAllFilters(exportResultCriteria);

        var copy = exportResultCriteria.copy();

        assertThat(exportResultCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(exportResultCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var exportResultCriteria = new ExportResultCriteria();

        assertThat(exportResultCriteria).hasToString("ExportResultCriteria{}");
    }

    private static void setAllFilters(ExportResultCriteria exportResultCriteria) {
        exportResultCriteria.id();
        exportResultCriteria.documentSha256();
        exportResultCriteria.originalFileName();
        exportResultCriteria.exportedPath();
        exportResultCriteria.exportedFileName();
        exportResultCriteria.s3ExportKey();
        exportResultCriteria.fileSize();
        exportResultCriteria.status();
        exportResultCriteria.exportedDate();
        exportResultCriteria.exportJobId();
        exportResultCriteria.distinct();
    }

    private static Condition<ExportResultCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentSha256()) &&
                condition.apply(criteria.getOriginalFileName()) &&
                condition.apply(criteria.getExportedPath()) &&
                condition.apply(criteria.getExportedFileName()) &&
                condition.apply(criteria.gets3ExportKey()) &&
                condition.apply(criteria.getFileSize()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getExportedDate()) &&
                condition.apply(criteria.getExportJobId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ExportResultCriteria> copyFiltersAre(
        ExportResultCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentSha256(), copy.getDocumentSha256()) &&
                condition.apply(criteria.getOriginalFileName(), copy.getOriginalFileName()) &&
                condition.apply(criteria.getExportedPath(), copy.getExportedPath()) &&
                condition.apply(criteria.getExportedFileName(), copy.getExportedFileName()) &&
                condition.apply(criteria.gets3ExportKey(), copy.gets3ExportKey()) &&
                condition.apply(criteria.getFileSize(), copy.getFileSize()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getExportedDate(), copy.getExportedDate()) &&
                condition.apply(criteria.getExportJobId(), copy.getExportJobId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
