package fr.smartprod.paperdms.business.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.business.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContractClauseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContractClauseDTO.class);
        ContractClauseDTO contractClauseDTO1 = new ContractClauseDTO();
        contractClauseDTO1.setId(1L);
        ContractClauseDTO contractClauseDTO2 = new ContractClauseDTO();
        assertThat(contractClauseDTO1).isNotEqualTo(contractClauseDTO2);
        contractClauseDTO2.setId(contractClauseDTO1.getId());
        assertThat(contractClauseDTO1).isEqualTo(contractClauseDTO2);
        contractClauseDTO2.setId(2L);
        assertThat(contractClauseDTO1).isNotEqualTo(contractClauseDTO2);
        contractClauseDTO1.setId(null);
        assertThat(contractClauseDTO1).isNotEqualTo(contractClauseDTO2);
    }
}
