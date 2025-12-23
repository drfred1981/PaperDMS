package fr.smartprod.paperdms.document.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DocumentAuditCriteriaTest {

    @Test
    void newDocumentAuditCriteriaHasAllFiltersNullTest() {
        var documentAuditCriteria = new DocumentAuditCriteria();
        assertThat(documentAuditCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void documentAuditCriteriaFluentMethodsCreatesFiltersTest() {
        var documentAuditCriteria = new DocumentAuditCriteria();

        setAllFilters(documentAuditCriteria);

        assertThat(documentAuditCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void documentAuditCriteriaCopyCreatesNullFilterTest() {
        var documentAuditCriteria = new DocumentAuditCriteria();
        var copy = documentAuditCriteria.copy();

        assertThat(documentAuditCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(documentAuditCriteria)
        );
    }

    @Test
    void documentAuditCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var documentAuditCriteria = new DocumentAuditCriteria();
        setAllFilters(documentAuditCriteria);

        var copy = documentAuditCriteria.copy();

        assertThat(documentAuditCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(documentAuditCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var documentAuditCriteria = new DocumentAuditCriteria();

        assertThat(documentAuditCriteria).hasToString("DocumentAuditCriteria{}");
    }

    private static void setAllFilters(DocumentAuditCriteria documentAuditCriteria) {
        documentAuditCriteria.id();
        documentAuditCriteria.documentId();
        documentAuditCriteria.documentSha256();
        documentAuditCriteria.action();
        documentAuditCriteria.userId();
        documentAuditCriteria.userIp();
        documentAuditCriteria.actionDate();
        documentAuditCriteria.distinct();
    }

    private static Condition<DocumentAuditCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentId()) &&
                condition.apply(criteria.getDocumentSha256()) &&
                condition.apply(criteria.getAction()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getUserIp()) &&
                condition.apply(criteria.getActionDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DocumentAuditCriteria> copyFiltersAre(
        DocumentAuditCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentId(), copy.getDocumentId()) &&
                condition.apply(criteria.getDocumentSha256(), copy.getDocumentSha256()) &&
                condition.apply(criteria.getAction(), copy.getAction()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getUserIp(), copy.getUserIp()) &&
                condition.apply(criteria.getActionDate(), copy.getActionDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
