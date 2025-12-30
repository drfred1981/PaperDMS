package fr.smartprod.paperdms.emailimportdocument.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EmailImportImportRuleCriteriaTest {

    @Test
    void newEmailImportImportRuleCriteriaHasAllFiltersNullTest() {
        var emailImportImportRuleCriteria = new EmailImportImportRuleCriteria();
        assertThat(emailImportImportRuleCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void emailImportImportRuleCriteriaFluentMethodsCreatesFiltersTest() {
        var emailImportImportRuleCriteria = new EmailImportImportRuleCriteria();

        setAllFilters(emailImportImportRuleCriteria);

        assertThat(emailImportImportRuleCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void emailImportImportRuleCriteriaCopyCreatesNullFilterTest() {
        var emailImportImportRuleCriteria = new EmailImportImportRuleCriteria();
        var copy = emailImportImportRuleCriteria.copy();

        assertThat(emailImportImportRuleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(emailImportImportRuleCriteria)
        );
    }

    @Test
    void emailImportImportRuleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var emailImportImportRuleCriteria = new EmailImportImportRuleCriteria();
        setAllFilters(emailImportImportRuleCriteria);

        var copy = emailImportImportRuleCriteria.copy();

        assertThat(emailImportImportRuleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(emailImportImportRuleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var emailImportImportRuleCriteria = new EmailImportImportRuleCriteria();

        assertThat(emailImportImportRuleCriteria).hasToString("EmailImportImportRuleCriteria{}");
    }

    private static void setAllFilters(EmailImportImportRuleCriteria emailImportImportRuleCriteria) {
        emailImportImportRuleCriteria.id();
        emailImportImportRuleCriteria.name();
        emailImportImportRuleCriteria.priority();
        emailImportImportRuleCriteria.isActive();
        emailImportImportRuleCriteria.matchCount();
        emailImportImportRuleCriteria.lastMatchDate();
        emailImportImportRuleCriteria.createdBy();
        emailImportImportRuleCriteria.createdDate();
        emailImportImportRuleCriteria.lastModifiedDate();
        emailImportImportRuleCriteria.emailImportImportMappingsId();
        emailImportImportRuleCriteria.emailImportDocumentsId();
        emailImportImportRuleCriteria.distinct();
    }

    private static Condition<EmailImportImportRuleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getPriority()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getMatchCount()) &&
                condition.apply(criteria.getLastMatchDate()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getEmailImportImportMappingsId()) &&
                condition.apply(criteria.getEmailImportDocumentsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EmailImportImportRuleCriteria> copyFiltersAre(
        EmailImportImportRuleCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getPriority(), copy.getPriority()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getMatchCount(), copy.getMatchCount()) &&
                condition.apply(criteria.getLastMatchDate(), copy.getLastMatchDate()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getEmailImportImportMappingsId(), copy.getEmailImportImportMappingsId()) &&
                condition.apply(criteria.getEmailImportDocumentsId(), copy.getEmailImportDocumentsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
