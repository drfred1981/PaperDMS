package fr.smartprod.paperdms.monitoring.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DocumentWatchCriteriaTest {

    @Test
    void newDocumentWatchCriteriaHasAllFiltersNullTest() {
        var documentWatchCriteria = new DocumentWatchCriteria();
        assertThat(documentWatchCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void documentWatchCriteriaFluentMethodsCreatesFiltersTest() {
        var documentWatchCriteria = new DocumentWatchCriteria();

        setAllFilters(documentWatchCriteria);

        assertThat(documentWatchCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void documentWatchCriteriaCopyCreatesNullFilterTest() {
        var documentWatchCriteria = new DocumentWatchCriteria();
        var copy = documentWatchCriteria.copy();

        assertThat(documentWatchCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(documentWatchCriteria)
        );
    }

    @Test
    void documentWatchCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var documentWatchCriteria = new DocumentWatchCriteria();
        setAllFilters(documentWatchCriteria);

        var copy = documentWatchCriteria.copy();

        assertThat(documentWatchCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(documentWatchCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var documentWatchCriteria = new DocumentWatchCriteria();

        assertThat(documentWatchCriteria).hasToString("DocumentWatchCriteria{}");
    }

    private static void setAllFilters(DocumentWatchCriteria documentWatchCriteria) {
        documentWatchCriteria.id();
        documentWatchCriteria.documentId();
        documentWatchCriteria.userId();
        documentWatchCriteria.watchType();
        documentWatchCriteria.notifyOnView();
        documentWatchCriteria.notifyOnDownload();
        documentWatchCriteria.notifyOnModify();
        documentWatchCriteria.notifyOnShare();
        documentWatchCriteria.notifyOnDelete();
        documentWatchCriteria.createdDate();
        documentWatchCriteria.distinct();
    }

    private static Condition<DocumentWatchCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentId()) &&
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

    private static Condition<DocumentWatchCriteria> copyFiltersAre(
        DocumentWatchCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentId(), copy.getDocumentId()) &&
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
