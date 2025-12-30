package fr.smartprod.paperdms.emailimportdocument.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EmailImportImportMappingCriteriaTest {

    @Test
    void newEmailImportImportMappingCriteriaHasAllFiltersNullTest() {
        var emailImportImportMappingCriteria = new EmailImportImportMappingCriteria();
        assertThat(emailImportImportMappingCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void emailImportImportMappingCriteriaFluentMethodsCreatesFiltersTest() {
        var emailImportImportMappingCriteria = new EmailImportImportMappingCriteria();

        setAllFilters(emailImportImportMappingCriteria);

        assertThat(emailImportImportMappingCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void emailImportImportMappingCriteriaCopyCreatesNullFilterTest() {
        var emailImportImportMappingCriteria = new EmailImportImportMappingCriteria();
        var copy = emailImportImportMappingCriteria.copy();

        assertThat(emailImportImportMappingCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(emailImportImportMappingCriteria)
        );
    }

    @Test
    void emailImportImportMappingCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var emailImportImportMappingCriteria = new EmailImportImportMappingCriteria();
        setAllFilters(emailImportImportMappingCriteria);

        var copy = emailImportImportMappingCriteria.copy();

        assertThat(emailImportImportMappingCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(emailImportImportMappingCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var emailImportImportMappingCriteria = new EmailImportImportMappingCriteria();

        assertThat(emailImportImportMappingCriteria).hasToString("EmailImportImportMappingCriteria{}");
    }

    private static void setAllFilters(EmailImportImportMappingCriteria emailImportImportMappingCriteria) {
        emailImportImportMappingCriteria.id();
        emailImportImportMappingCriteria.emailField();
        emailImportImportMappingCriteria.documentField();
        emailImportImportMappingCriteria.transformation();
        emailImportImportMappingCriteria.isRequired();
        emailImportImportMappingCriteria.defaultValue();
        emailImportImportMappingCriteria.validationRegex();
        emailImportImportMappingCriteria.ruleId();
        emailImportImportMappingCriteria.distinct();
    }

    private static Condition<EmailImportImportMappingCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getEmailField()) &&
                condition.apply(criteria.getDocumentField()) &&
                condition.apply(criteria.getTransformation()) &&
                condition.apply(criteria.getIsRequired()) &&
                condition.apply(criteria.getDefaultValue()) &&
                condition.apply(criteria.getValidationRegex()) &&
                condition.apply(criteria.getRuleId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EmailImportImportMappingCriteria> copyFiltersAre(
        EmailImportImportMappingCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getEmailField(), copy.getEmailField()) &&
                condition.apply(criteria.getDocumentField(), copy.getDocumentField()) &&
                condition.apply(criteria.getTransformation(), copy.getTransformation()) &&
                condition.apply(criteria.getIsRequired(), copy.getIsRequired()) &&
                condition.apply(criteria.getDefaultValue(), copy.getDefaultValue()) &&
                condition.apply(criteria.getValidationRegex(), copy.getValidationRegex()) &&
                condition.apply(criteria.getRuleId(), copy.getRuleId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
