package fr.smartprod.paperdms.scan.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ScanBatchCriteriaTest {

    @Test
    void newScanBatchCriteriaHasAllFiltersNullTest() {
        var scanBatchCriteria = new ScanBatchCriteria();
        assertThat(scanBatchCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void scanBatchCriteriaFluentMethodsCreatesFiltersTest() {
        var scanBatchCriteria = new ScanBatchCriteria();

        setAllFilters(scanBatchCriteria);

        assertThat(scanBatchCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void scanBatchCriteriaCopyCreatesNullFilterTest() {
        var scanBatchCriteria = new ScanBatchCriteria();
        var copy = scanBatchCriteria.copy();

        assertThat(scanBatchCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(scanBatchCriteria)
        );
    }

    @Test
    void scanBatchCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var scanBatchCriteria = new ScanBatchCriteria();
        setAllFilters(scanBatchCriteria);

        var copy = scanBatchCriteria.copy();

        assertThat(scanBatchCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(scanBatchCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var scanBatchCriteria = new ScanBatchCriteria();

        assertThat(scanBatchCriteria).hasToString("ScanBatchCriteria{}");
    }

    private static void setAllFilters(ScanBatchCriteria scanBatchCriteria) {
        scanBatchCriteria.id();
        scanBatchCriteria.name();
        scanBatchCriteria.totalJobs();
        scanBatchCriteria.completedJobs();
        scanBatchCriteria.totalPages();
        scanBatchCriteria.status();
        scanBatchCriteria.createdBy();
        scanBatchCriteria.createdDate();
        scanBatchCriteria.distinct();
    }

    private static Condition<ScanBatchCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getTotalJobs()) &&
                condition.apply(criteria.getCompletedJobs()) &&
                condition.apply(criteria.getTotalPages()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ScanBatchCriteria> copyFiltersAre(ScanBatchCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getTotalJobs(), copy.getTotalJobs()) &&
                condition.apply(criteria.getCompletedJobs(), copy.getCompletedJobs()) &&
                condition.apply(criteria.getTotalPages(), copy.getTotalPages()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
