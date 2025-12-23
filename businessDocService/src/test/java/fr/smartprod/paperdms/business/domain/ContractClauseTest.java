package fr.smartprod.paperdms.business.domain;

import static fr.smartprod.paperdms.business.domain.ContractClauseTestSamples.*;
import static fr.smartprod.paperdms.business.domain.ContractTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.business.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContractClauseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContractClause.class);
        ContractClause contractClause1 = getContractClauseSample1();
        ContractClause contractClause2 = new ContractClause();
        assertThat(contractClause1).isNotEqualTo(contractClause2);

        contractClause2.setId(contractClause1.getId());
        assertThat(contractClause1).isEqualTo(contractClause2);

        contractClause2 = getContractClauseSample2();
        assertThat(contractClause1).isNotEqualTo(contractClause2);
    }

    @Test
    void contractTest() {
        ContractClause contractClause = getContractClauseRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        contractClause.setContract(contractBack);
        assertThat(contractClause.getContract()).isEqualTo(contractBack);

        contractClause.contract(null);
        assertThat(contractClause.getContract()).isNull();
    }
}
