package fr.smartprod.paperdms.business.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class BankStatementCriteriaTest {

    @Test
    void newBankStatementCriteriaHasAllFiltersNullTest() {
        var bankStatementCriteria = new BankStatementCriteria();
        assertThat(bankStatementCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void bankStatementCriteriaFluentMethodsCreatesFiltersTest() {
        var bankStatementCriteria = new BankStatementCriteria();

        setAllFilters(bankStatementCriteria);

        assertThat(bankStatementCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void bankStatementCriteriaCopyCreatesNullFilterTest() {
        var bankStatementCriteria = new BankStatementCriteria();
        var copy = bankStatementCriteria.copy();

        assertThat(bankStatementCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(bankStatementCriteria)
        );
    }

    @Test
    void bankStatementCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var bankStatementCriteria = new BankStatementCriteria();
        setAllFilters(bankStatementCriteria);

        var copy = bankStatementCriteria.copy();

        assertThat(bankStatementCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(bankStatementCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var bankStatementCriteria = new BankStatementCriteria();

        assertThat(bankStatementCriteria).hasToString("BankStatementCriteria{}");
    }

    private static void setAllFilters(BankStatementCriteria bankStatementCriteria) {
        bankStatementCriteria.id();
        bankStatementCriteria.documentId();
        bankStatementCriteria.accountNumber();
        bankStatementCriteria.bankName();
        bankStatementCriteria.statementDate();
        bankStatementCriteria.statementPeriodStart();
        bankStatementCriteria.statementPeriodEnd();
        bankStatementCriteria.openingBalance();
        bankStatementCriteria.closingBalance();
        bankStatementCriteria.currency();
        bankStatementCriteria.status();
        bankStatementCriteria.isReconciled();
        bankStatementCriteria.createdDate();
        bankStatementCriteria.distinct();
    }

    private static Condition<BankStatementCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentId()) &&
                condition.apply(criteria.getAccountNumber()) &&
                condition.apply(criteria.getBankName()) &&
                condition.apply(criteria.getStatementDate()) &&
                condition.apply(criteria.getStatementPeriodStart()) &&
                condition.apply(criteria.getStatementPeriodEnd()) &&
                condition.apply(criteria.getOpeningBalance()) &&
                condition.apply(criteria.getClosingBalance()) &&
                condition.apply(criteria.getCurrency()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getIsReconciled()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<BankStatementCriteria> copyFiltersAre(
        BankStatementCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentId(), copy.getDocumentId()) &&
                condition.apply(criteria.getAccountNumber(), copy.getAccountNumber()) &&
                condition.apply(criteria.getBankName(), copy.getBankName()) &&
                condition.apply(criteria.getStatementDate(), copy.getStatementDate()) &&
                condition.apply(criteria.getStatementPeriodStart(), copy.getStatementPeriodStart()) &&
                condition.apply(criteria.getStatementPeriodEnd(), copy.getStatementPeriodEnd()) &&
                condition.apply(criteria.getOpeningBalance(), copy.getOpeningBalance()) &&
                condition.apply(criteria.getClosingBalance(), copy.getClosingBalance()) &&
                condition.apply(criteria.getCurrency(), copy.getCurrency()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getIsReconciled(), copy.getIsReconciled()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
