package fr.smartprod.paperdms.document.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DocumentServiceStatusCriteriaTest {

    @Test
    void newDocumentServiceStatusCriteriaHasAllFiltersNullTest() {
        var documentServiceStatusCriteria = new DocumentServiceStatusCriteria();
        assertThat(documentServiceStatusCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void documentServiceStatusCriteriaFluentMethodsCreatesFiltersTest() {
        var documentServiceStatusCriteria = new DocumentServiceStatusCriteria();

        setAllFilters(documentServiceStatusCriteria);

        assertThat(documentServiceStatusCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void documentServiceStatusCriteriaCopyCreatesNullFilterTest() {
        var documentServiceStatusCriteria = new DocumentServiceStatusCriteria();
        var copy = documentServiceStatusCriteria.copy();

        assertThat(documentServiceStatusCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(documentServiceStatusCriteria)
        );
    }

    @Test
    void documentServiceStatusCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var documentServiceStatusCriteria = new DocumentServiceStatusCriteria();
        setAllFilters(documentServiceStatusCriteria);

        var copy = documentServiceStatusCriteria.copy();

        assertThat(documentServiceStatusCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(documentServiceStatusCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var documentServiceStatusCriteria = new DocumentServiceStatusCriteria();

        assertThat(documentServiceStatusCriteria).hasToString("DocumentServiceStatusCriteria{}");
    }

    private static void setAllFilters(DocumentServiceStatusCriteria documentServiceStatusCriteria) {
        documentServiceStatusCriteria.id();
        documentServiceStatusCriteria.documentId();
        documentServiceStatusCriteria.serviceType();
        documentServiceStatusCriteria.status();
        documentServiceStatusCriteria.retryCount();
        documentServiceStatusCriteria.lastProcessedDate();
        documentServiceStatusCriteria.processingStartDate();
        documentServiceStatusCriteria.processingEndDate();
        documentServiceStatusCriteria.processingDuration();
        documentServiceStatusCriteria.jobId();
        documentServiceStatusCriteria.priority();
        documentServiceStatusCriteria.updatedBy();
        documentServiceStatusCriteria.updatedDate();
        documentServiceStatusCriteria.documentId();
        documentServiceStatusCriteria.distinct();
    }

    private static Condition<DocumentServiceStatusCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentId()) &&
                condition.apply(criteria.getServiceType()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getRetryCount()) &&
                condition.apply(criteria.getLastProcessedDate()) &&
                condition.apply(criteria.getProcessingStartDate()) &&
                condition.apply(criteria.getProcessingEndDate()) &&
                condition.apply(criteria.getProcessingDuration()) &&
                condition.apply(criteria.getJobId()) &&
                condition.apply(criteria.getPriority()) &&
                condition.apply(criteria.getUpdatedBy()) &&
                condition.apply(criteria.getUpdatedDate()) &&
                condition.apply(criteria.getDocumentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DocumentServiceStatusCriteria> copyFiltersAre(
        DocumentServiceStatusCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentId(), copy.getDocumentId()) &&
                condition.apply(criteria.getServiceType(), copy.getServiceType()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getRetryCount(), copy.getRetryCount()) &&
                condition.apply(criteria.getLastProcessedDate(), copy.getLastProcessedDate()) &&
                condition.apply(criteria.getProcessingStartDate(), copy.getProcessingStartDate()) &&
                condition.apply(criteria.getProcessingEndDate(), copy.getProcessingEndDate()) &&
                condition.apply(criteria.getProcessingDuration(), copy.getProcessingDuration()) &&
                condition.apply(criteria.getJobId(), copy.getJobId()) &&
                condition.apply(criteria.getPriority(), copy.getPriority()) &&
                condition.apply(criteria.getUpdatedBy(), copy.getUpdatedBy()) &&
                condition.apply(criteria.getUpdatedDate(), copy.getUpdatedDate()) &&
                condition.apply(criteria.getDocumentId(), copy.getDocumentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
