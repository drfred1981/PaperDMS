package fr.smartprod.paperdms.monitoring.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MonitoringDocumentWatchCriteriaTest {

    @Test
    void newMonitoringDocumentWatchCriteriaHasAllFiltersNullTest() {
        var monitoringDocumentWatchCriteria = new MonitoringDocumentWatchCriteria();
        assertThat(monitoringDocumentWatchCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void monitoringDocumentWatchCriteriaFluentMethodsCreatesFiltersTest() {
        var monitoringDocumentWatchCriteria = new MonitoringDocumentWatchCriteria();

        setAllFilters(monitoringDocumentWatchCriteria);

        assertThat(monitoringDocumentWatchCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void monitoringDocumentWatchCriteriaCopyCreatesNullFilterTest() {
        var monitoringDocumentWatchCriteria = new MonitoringDocumentWatchCriteria();
        var copy = monitoringDocumentWatchCriteria.copy();

        assertThat(monitoringDocumentWatchCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(monitoringDocumentWatchCriteria)
        );
    }

    @Test
    void monitoringDocumentWatchCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var monitoringDocumentWatchCriteria = new MonitoringDocumentWatchCriteria();
        setAllFilters(monitoringDocumentWatchCriteria);

        var copy = monitoringDocumentWatchCriteria.copy();

        assertThat(monitoringDocumentWatchCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(monitoringDocumentWatchCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var monitoringDocumentWatchCriteria = new MonitoringDocumentWatchCriteria();

        assertThat(monitoringDocumentWatchCriteria).hasToString("MonitoringDocumentWatchCriteria{}");
    }

    private static void setAllFilters(MonitoringDocumentWatchCriteria monitoringDocumentWatchCriteria) {
        monitoringDocumentWatchCriteria.id();
        monitoringDocumentWatchCriteria.documentSha256();
        monitoringDocumentWatchCriteria.userId();
        monitoringDocumentWatchCriteria.watchType();
        monitoringDocumentWatchCriteria.notifyOnView();
        monitoringDocumentWatchCriteria.notifyOnDownload();
        monitoringDocumentWatchCriteria.notifyOnModify();
        monitoringDocumentWatchCriteria.notifyOnShare();
        monitoringDocumentWatchCriteria.notifyOnDelete();
        monitoringDocumentWatchCriteria.createdDate();
        monitoringDocumentWatchCriteria.distinct();
    }

    private static Condition<MonitoringDocumentWatchCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentSha256()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getWatchType()) &&
                condition.apply(criteria.getNotifyOnView()) &&
                condition.apply(criteria.getNotifyOnDownload()) &&
                condition.apply(criteria.getNotifyOnModify()) &&
                condition.apply(criteria.getNotifyOnShare()) &&
                condition.apply(criteria.getNotifyOnDelete()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MonitoringDocumentWatchCriteria> copyFiltersAre(
        MonitoringDocumentWatchCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentSha256(), copy.getDocumentSha256()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getWatchType(), copy.getWatchType()) &&
                condition.apply(criteria.getNotifyOnView(), copy.getNotifyOnView()) &&
                condition.apply(criteria.getNotifyOnDownload(), copy.getNotifyOnDownload()) &&
                condition.apply(criteria.getNotifyOnModify(), copy.getNotifyOnModify()) &&
                condition.apply(criteria.getNotifyOnShare(), copy.getNotifyOnShare()) &&
                condition.apply(criteria.getNotifyOnDelete(), copy.getNotifyOnDelete()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
