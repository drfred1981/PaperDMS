package fr.smartprod.paperdms.business.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ContractCriteriaTest {

    @Test
    void newContractCriteriaHasAllFiltersNullTest() {
        var contractCriteria = new ContractCriteria();
        assertThat(contractCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void contractCriteriaFluentMethodsCreatesFiltersTest() {
        var contractCriteria = new ContractCriteria();

        setAllFilters(contractCriteria);

        assertThat(contractCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void contractCriteriaCopyCreatesNullFilterTest() {
        var contractCriteria = new ContractCriteria();
        var copy = contractCriteria.copy();

        assertThat(contractCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(contractCriteria)
        );
    }

    @Test
    void contractCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var contractCriteria = new ContractCriteria();
        setAllFilters(contractCriteria);

        var copy = contractCriteria.copy();

        assertThat(contractCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(contractCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var contractCriteria = new ContractCriteria();

        assertThat(contractCriteria).hasToString("ContractCriteria{}");
    }

    private static void setAllFilters(ContractCriteria contractCriteria) {
        contractCriteria.id();
        contractCriteria.documentId();
        contractCriteria.contractNumber();
        contractCriteria.contractType();
        contractCriteria.title();
        contractCriteria.partyA();
        contractCriteria.partyB();
        contractCriteria.startDate();
        contractCriteria.endDate();
        contractCriteria.autoRenew();
        contractCriteria.contractValue();
        contractCriteria.currency();
        contractCriteria.status();
        contractCriteria.createdDate();
        contractCriteria.distinct();
    }

    private static Condition<ContractCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentId()) &&
                condition.apply(criteria.getContractNumber()) &&
                condition.apply(criteria.getContractType()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getPartyA()) &&
                condition.apply(criteria.getPartyB()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getAutoRenew()) &&
                condition.apply(criteria.getContractValue()) &&
                condition.apply(criteria.getCurrency()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ContractCriteria> copyFiltersAre(ContractCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentId(), copy.getDocumentId()) &&
                condition.apply(criteria.getContractNumber(), copy.getContractNumber()) &&
                condition.apply(criteria.getContractType(), copy.getContractType()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getPartyA(), copy.getPartyA()) &&
                condition.apply(criteria.getPartyB(), copy.getPartyB()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getAutoRenew(), copy.getAutoRenew()) &&
                condition.apply(criteria.getContractValue(), copy.getContractValue()) &&
                condition.apply(criteria.getCurrency(), copy.getCurrency()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
