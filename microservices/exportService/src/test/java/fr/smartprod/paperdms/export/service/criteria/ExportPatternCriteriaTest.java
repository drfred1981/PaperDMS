package fr.smartprod.paperdms.export.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ExportPatternCriteriaTest {

    @Test
    void newExportPatternCriteriaHasAllFiltersNullTest() {
        var exportPatternCriteria = new ExportPatternCriteria();
        assertThat(exportPatternCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void exportPatternCriteriaFluentMethodsCreatesFiltersTest() {
        var exportPatternCriteria = new ExportPatternCriteria();

        setAllFilters(exportPatternCriteria);

        assertThat(exportPatternCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void exportPatternCriteriaCopyCreatesNullFilterTest() {
        var exportPatternCriteria = new ExportPatternCriteria();
        var copy = exportPatternCriteria.copy();

        assertThat(exportPatternCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(exportPatternCriteria)
        );
    }

    @Test
    void exportPatternCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var exportPatternCriteria = new ExportPatternCriteria();
        setAllFilters(exportPatternCriteria);

        var copy = exportPatternCriteria.copy();

        assertThat(exportPatternCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(exportPatternCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var exportPatternCriteria = new ExportPatternCriteria();

        assertThat(exportPatternCriteria).hasToString("ExportPatternCriteria{}");
    }

    private static void setAllFilters(ExportPatternCriteria exportPatternCriteria) {
        exportPatternCriteria.id();
        exportPatternCriteria.name();
        exportPatternCriteria.pathTemplate();
        exportPatternCriteria.fileNameTemplate();
        exportPatternCriteria.isSystem();
        exportPatternCriteria.isActive();
        exportPatternCriteria.usageCount();
        exportPatternCriteria.createdBy();
        exportPatternCriteria.createdDate();
        exportPatternCriteria.lastModifiedDate();
        exportPatternCriteria.exportJobsId();
        exportPatternCriteria.distinct();
    }

    private static Condition<ExportPatternCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getPathTemplate()) &&
                condition.apply(criteria.getFileNameTemplate()) &&
                condition.apply(criteria.getIsSystem()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getUsageCount()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getExportJobsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ExportPatternCriteria> copyFiltersAre(
        ExportPatternCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getPathTemplate(), copy.getPathTemplate()) &&
                condition.apply(criteria.getFileNameTemplate(), copy.getFileNameTemplate()) &&
                condition.apply(criteria.getIsSystem(), copy.getIsSystem()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getUsageCount(), copy.getUsageCount()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getExportJobsId(), copy.getExportJobsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
