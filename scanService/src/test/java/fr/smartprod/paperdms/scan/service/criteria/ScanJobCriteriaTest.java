package fr.smartprod.paperdms.scan.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ScanJobCriteriaTest {

    @Test
    void newScanJobCriteriaHasAllFiltersNullTest() {
        var scanJobCriteria = new ScanJobCriteria();
        assertThat(scanJobCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void scanJobCriteriaFluentMethodsCreatesFiltersTest() {
        var scanJobCriteria = new ScanJobCriteria();

        setAllFilters(scanJobCriteria);

        assertThat(scanJobCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void scanJobCriteriaCopyCreatesNullFilterTest() {
        var scanJobCriteria = new ScanJobCriteria();
        var copy = scanJobCriteria.copy();

        assertThat(scanJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(scanJobCriteria)
        );
    }

    @Test
    void scanJobCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var scanJobCriteria = new ScanJobCriteria();
        setAllFilters(scanJobCriteria);

        var copy = scanJobCriteria.copy();

        assertThat(scanJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(scanJobCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var scanJobCriteria = new ScanJobCriteria();

        assertThat(scanJobCriteria).hasToString("ScanJobCriteria{}");
    }

    private static void setAllFilters(ScanJobCriteria scanJobCriteria) {
        scanJobCriteria.id();
        scanJobCriteria.name();
        scanJobCriteria.scannerConfigId();
        scanJobCriteria.batchId();
        scanJobCriteria.documentTypeId();
        scanJobCriteria.folderId();
        scanJobCriteria.pageCount();
        scanJobCriteria.status();
        scanJobCriteria.colorMode();
        scanJobCriteria.resolution();
        scanJobCriteria.fileFormat();
        scanJobCriteria.startDate();
        scanJobCriteria.endDate();
        scanJobCriteria.createdBy();
        scanJobCriteria.createdDate();
        scanJobCriteria.scannerConfigId();
        scanJobCriteria.batchId();
        scanJobCriteria.distinct();
    }

    private static Condition<ScanJobCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getScannerConfigId()) &&
                condition.apply(criteria.getBatchId()) &&
                condition.apply(criteria.getDocumentTypeId()) &&
                condition.apply(criteria.getFolderId()) &&
                condition.apply(criteria.getPageCount()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getColorMode()) &&
                condition.apply(criteria.getResolution()) &&
                condition.apply(criteria.getFileFormat()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getScannerConfigId()) &&
                condition.apply(criteria.getBatchId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ScanJobCriteria> copyFiltersAre(ScanJobCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getScannerConfigId(), copy.getScannerConfigId()) &&
                condition.apply(criteria.getBatchId(), copy.getBatchId()) &&
                condition.apply(criteria.getDocumentTypeId(), copy.getDocumentTypeId()) &&
                condition.apply(criteria.getFolderId(), copy.getFolderId()) &&
                condition.apply(criteria.getPageCount(), copy.getPageCount()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getColorMode(), copy.getColorMode()) &&
                condition.apply(criteria.getResolution(), copy.getResolution()) &&
                condition.apply(criteria.getFileFormat(), copy.getFileFormat()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getScannerConfigId(), copy.getScannerConfigId()) &&
                condition.apply(criteria.getBatchId(), copy.getBatchId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
