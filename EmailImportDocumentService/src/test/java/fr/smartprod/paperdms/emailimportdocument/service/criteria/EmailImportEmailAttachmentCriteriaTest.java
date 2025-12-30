package fr.smartprod.paperdms.emailimportdocument.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EmailImportEmailAttachmentCriteriaTest {

    @Test
    void newEmailImportEmailAttachmentCriteriaHasAllFiltersNullTest() {
        var emailImportEmailAttachmentCriteria = new EmailImportEmailAttachmentCriteria();
        assertThat(emailImportEmailAttachmentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void emailImportEmailAttachmentCriteriaFluentMethodsCreatesFiltersTest() {
        var emailImportEmailAttachmentCriteria = new EmailImportEmailAttachmentCriteria();

        setAllFilters(emailImportEmailAttachmentCriteria);

        assertThat(emailImportEmailAttachmentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void emailImportEmailAttachmentCriteriaCopyCreatesNullFilterTest() {
        var emailImportEmailAttachmentCriteria = new EmailImportEmailAttachmentCriteria();
        var copy = emailImportEmailAttachmentCriteria.copy();

        assertThat(emailImportEmailAttachmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(emailImportEmailAttachmentCriteria)
        );
    }

    @Test
    void emailImportEmailAttachmentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var emailImportEmailAttachmentCriteria = new EmailImportEmailAttachmentCriteria();
        setAllFilters(emailImportEmailAttachmentCriteria);

        var copy = emailImportEmailAttachmentCriteria.copy();

        assertThat(emailImportEmailAttachmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(emailImportEmailAttachmentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var emailImportEmailAttachmentCriteria = new EmailImportEmailAttachmentCriteria();

        assertThat(emailImportEmailAttachmentCriteria).hasToString("EmailImportEmailAttachmentCriteria{}");
    }

    private static void setAllFilters(EmailImportEmailAttachmentCriteria emailImportEmailAttachmentCriteria) {
        emailImportEmailAttachmentCriteria.id();
        emailImportEmailAttachmentCriteria.fileName();
        emailImportEmailAttachmentCriteria.fileSize();
        emailImportEmailAttachmentCriteria.mimeType();
        emailImportEmailAttachmentCriteria.sha256();
        emailImportEmailAttachmentCriteria.s3Key();
        emailImportEmailAttachmentCriteria.status();
        emailImportEmailAttachmentCriteria.documentSha256();
        emailImportEmailAttachmentCriteria.emailImportDocumentId();
        emailImportEmailAttachmentCriteria.distinct();
    }

    private static Condition<EmailImportEmailAttachmentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFileName()) &&
                condition.apply(criteria.getFileSize()) &&
                condition.apply(criteria.getMimeType()) &&
                condition.apply(criteria.getSha256()) &&
                condition.apply(criteria.gets3Key()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getDocumentSha256()) &&
                condition.apply(criteria.getEmailImportDocumentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EmailImportEmailAttachmentCriteria> copyFiltersAre(
        EmailImportEmailAttachmentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFileName(), copy.getFileName()) &&
                condition.apply(criteria.getFileSize(), copy.getFileSize()) &&
                condition.apply(criteria.getMimeType(), copy.getMimeType()) &&
                condition.apply(criteria.getSha256(), copy.getSha256()) &&
                condition.apply(criteria.gets3Key(), copy.gets3Key()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getDocumentSha256(), copy.getDocumentSha256()) &&
                condition.apply(criteria.getEmailImportDocumentId(), copy.getEmailImportDocumentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
