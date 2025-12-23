package fr.smartprod.paperdms.export.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ExportJobCriteriaTest {

    @Test
    void newExportJobCriteriaHasAllFiltersNullTest() {
        var exportJobCriteria = new ExportJobCriteria();
        assertThat(exportJobCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void exportJobCriteriaFluentMethodsCreatesFiltersTest() {
        var exportJobCriteria = new ExportJobCriteria();

        setAllFilters(exportJobCriteria);

        assertThat(exportJobCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void exportJobCriteriaCopyCreatesNullFilterTest() {
        var exportJobCriteria = new ExportJobCriteria();
        var copy = exportJobCriteria.copy();

        assertThat(exportJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(exportJobCriteria)
        );
    }

    @Test
    void exportJobCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var exportJobCriteria = new ExportJobCriteria();
        setAllFilters(exportJobCriteria);

        var copy = exportJobCriteria.copy();

        assertThat(exportJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(exportJobCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var exportJobCriteria = new ExportJobCriteria();

        assertThat(exportJobCriteria).hasToString("ExportJobCriteria{}");
    }

    private static void setAllFilters(ExportJobCriteria exportJobCriteria) {
        exportJobCriteria.id();
        exportJobCriteria.name();
        exportJobCriteria.exportPatternId();
        exportJobCriteria.exportFormat();
        exportJobCriteria.includeMetadata();
        exportJobCriteria.includeVersions();
        exportJobCriteria.includeComments();
        exportJobCriteria.includeAuditTrail();
        exportJobCriteria.s3ExportKey();
        exportJobCriteria.exportSize();
        exportJobCriteria.documentCount();
        exportJobCriteria.filesGenerated();
        exportJobCriteria.status();
        exportJobCriteria.startDate();
        exportJobCriteria.endDate();
        exportJobCriteria.createdBy();
        exportJobCriteria.createdDate();
        exportJobCriteria.exportPatternId();
        exportJobCriteria.distinct();
    }

    private static Condition<ExportJobCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getExportPatternId()) &&
                condition.apply(criteria.getExportFormat()) &&
                condition.apply(criteria.getIncludeMetadata()) &&
                condition.apply(criteria.getIncludeVersions()) &&
                condition.apply(criteria.getIncludeComments()) &&
                condition.apply(criteria.getIncludeAuditTrail()) &&
                condition.apply(criteria.gets3ExportKey()) &&
                condition.apply(criteria.getExportSize()) &&
                condition.apply(criteria.getDocumentCount()) &&
                condition.apply(criteria.getFilesGenerated()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getExportPatternId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ExportJobCriteria> copyFiltersAre(ExportJobCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getExportPatternId(), copy.getExportPatternId()) &&
                condition.apply(criteria.getExportFormat(), copy.getExportFormat()) &&
                condition.apply(criteria.getIncludeMetadata(), copy.getIncludeMetadata()) &&
                condition.apply(criteria.getIncludeVersions(), copy.getIncludeVersions()) &&
                condition.apply(criteria.getIncludeComments(), copy.getIncludeComments()) &&
                condition.apply(criteria.getIncludeAuditTrail(), copy.getIncludeAuditTrail()) &&
                condition.apply(criteria.gets3ExportKey(), copy.gets3ExportKey()) &&
                condition.apply(criteria.getExportSize(), copy.getExportSize()) &&
                condition.apply(criteria.getDocumentCount(), copy.getDocumentCount()) &&
                condition.apply(criteria.getFilesGenerated(), copy.getFilesGenerated()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getExportPatternId(), copy.getExportPatternId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
