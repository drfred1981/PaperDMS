package fr.smartprod.paperdms.emailimportdocument.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EmailImportDocumentCriteriaTest {

    @Test
    void newEmailImportDocumentCriteriaHasAllFiltersNullTest() {
        var emailImportDocumentCriteria = new EmailImportDocumentCriteria();
        assertThat(emailImportDocumentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void emailImportDocumentCriteriaFluentMethodsCreatesFiltersTest() {
        var emailImportDocumentCriteria = new EmailImportDocumentCriteria();

        setAllFilters(emailImportDocumentCriteria);

        assertThat(emailImportDocumentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void emailImportDocumentCriteriaCopyCreatesNullFilterTest() {
        var emailImportDocumentCriteria = new EmailImportDocumentCriteria();
        var copy = emailImportDocumentCriteria.copy();

        assertThat(emailImportDocumentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(emailImportDocumentCriteria)
        );
    }

    @Test
    void emailImportDocumentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var emailImportDocumentCriteria = new EmailImportDocumentCriteria();
        setAllFilters(emailImportDocumentCriteria);

        var copy = emailImportDocumentCriteria.copy();

        assertThat(emailImportDocumentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(emailImportDocumentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var emailImportDocumentCriteria = new EmailImportDocumentCriteria();

        assertThat(emailImportDocumentCriteria).hasToString("EmailImportDocumentCriteria{}");
    }

    private static void setAllFilters(EmailImportDocumentCriteria emailImportDocumentCriteria) {
        emailImportDocumentCriteria.id();
        emailImportDocumentCriteria.sha256();
        emailImportDocumentCriteria.fromEmail();
        emailImportDocumentCriteria.toEmail();
        emailImportDocumentCriteria.subject();
        emailImportDocumentCriteria.receivedDate();
        emailImportDocumentCriteria.processedDate();
        emailImportDocumentCriteria.status();
        emailImportDocumentCriteria.attachmentCount();
        emailImportDocumentCriteria.documentsCreated();
        emailImportDocumentCriteria.documentSha256();
        emailImportDocumentCriteria.emailImportEmailAttachmentsId();
        emailImportDocumentCriteria.appliedRuleId();
        emailImportDocumentCriteria.distinct();
    }

    private static Condition<EmailImportDocumentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSha256()) &&
                condition.apply(criteria.getFromEmail()) &&
                condition.apply(criteria.getToEmail()) &&
                condition.apply(criteria.getSubject()) &&
                condition.apply(criteria.getReceivedDate()) &&
                condition.apply(criteria.getProcessedDate()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getAttachmentCount()) &&
                condition.apply(criteria.getDocumentsCreated()) &&
                condition.apply(criteria.getDocumentSha256()) &&
                condition.apply(criteria.getEmailImportEmailAttachmentsId()) &&
                condition.apply(criteria.getAppliedRuleId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EmailImportDocumentCriteria> copyFiltersAre(
        EmailImportDocumentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSha256(), copy.getSha256()) &&
                condition.apply(criteria.getFromEmail(), copy.getFromEmail()) &&
                condition.apply(criteria.getToEmail(), copy.getToEmail()) &&
                condition.apply(criteria.getSubject(), copy.getSubject()) &&
                condition.apply(criteria.getReceivedDate(), copy.getReceivedDate()) &&
                condition.apply(criteria.getProcessedDate(), copy.getProcessedDate()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getAttachmentCount(), copy.getAttachmentCount()) &&
                condition.apply(criteria.getDocumentsCreated(), copy.getDocumentsCreated()) &&
                condition.apply(criteria.getDocumentSha256(), copy.getDocumentSha256()) &&
                condition.apply(criteria.getEmailImportEmailAttachmentsId(), copy.getEmailImportEmailAttachmentsId()) &&
                condition.apply(criteria.getAppliedRuleId(), copy.getAppliedRuleId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
