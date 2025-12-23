package fr.smartprod.paperdms.emailimport.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EmailImportCriteriaTest {

    @Test
    void newEmailImportCriteriaHasAllFiltersNullTest() {
        var emailImportCriteria = new EmailImportCriteria();
        assertThat(emailImportCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void emailImportCriteriaFluentMethodsCreatesFiltersTest() {
        var emailImportCriteria = new EmailImportCriteria();

        setAllFilters(emailImportCriteria);

        assertThat(emailImportCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void emailImportCriteriaCopyCreatesNullFilterTest() {
        var emailImportCriteria = new EmailImportCriteria();
        var copy = emailImportCriteria.copy();

        assertThat(emailImportCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(emailImportCriteria)
        );
    }

    @Test
    void emailImportCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var emailImportCriteria = new EmailImportCriteria();
        setAllFilters(emailImportCriteria);

        var copy = emailImportCriteria.copy();

        assertThat(emailImportCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(emailImportCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var emailImportCriteria = new EmailImportCriteria();

        assertThat(emailImportCriteria).hasToString("EmailImportCriteria{}");
    }

    private static void setAllFilters(EmailImportCriteria emailImportCriteria) {
        emailImportCriteria.id();
        emailImportCriteria.fromEmail();
        emailImportCriteria.toEmail();
        emailImportCriteria.subject();
        emailImportCriteria.receivedDate();
        emailImportCriteria.processedDate();
        emailImportCriteria.status();
        emailImportCriteria.folderId();
        emailImportCriteria.documentTypeId();
        emailImportCriteria.attachmentCount();
        emailImportCriteria.documentsCreated();
        emailImportCriteria.appliedRuleId();
        emailImportCriteria.appliedRuleId();
        emailImportCriteria.distinct();
    }

    private static Condition<EmailImportCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFromEmail()) &&
                condition.apply(criteria.getToEmail()) &&
                condition.apply(criteria.getSubject()) &&
                condition.apply(criteria.getReceivedDate()) &&
                condition.apply(criteria.getProcessedDate()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getFolderId()) &&
                condition.apply(criteria.getDocumentTypeId()) &&
                condition.apply(criteria.getAttachmentCount()) &&
                condition.apply(criteria.getDocumentsCreated()) &&
                condition.apply(criteria.getAppliedRuleId()) &&
                condition.apply(criteria.getAppliedRuleId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EmailImportCriteria> copyFiltersAre(EmailImportCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFromEmail(), copy.getFromEmail()) &&
                condition.apply(criteria.getToEmail(), copy.getToEmail()) &&
                condition.apply(criteria.getSubject(), copy.getSubject()) &&
                condition.apply(criteria.getReceivedDate(), copy.getReceivedDate()) &&
                condition.apply(criteria.getProcessedDate(), copy.getProcessedDate()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getFolderId(), copy.getFolderId()) &&
                condition.apply(criteria.getDocumentTypeId(), copy.getDocumentTypeId()) &&
                condition.apply(criteria.getAttachmentCount(), copy.getAttachmentCount()) &&
                condition.apply(criteria.getDocumentsCreated(), copy.getDocumentsCreated()) &&
                condition.apply(criteria.getAppliedRuleId(), copy.getAppliedRuleId()) &&
                condition.apply(criteria.getAppliedRuleId(), copy.getAppliedRuleId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
