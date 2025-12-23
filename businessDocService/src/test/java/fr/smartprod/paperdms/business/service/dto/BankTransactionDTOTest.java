package fr.smartprod.paperdms.business.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.business.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BankTransactionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BankTransactionDTO.class);
        BankTransactionDTO bankTransactionDTO1 = new BankTransactionDTO();
        bankTransactionDTO1.setId(1L);
        BankTransactionDTO bankTransactionDTO2 = new BankTransactionDTO();
        assertThat(bankTransactionDTO1).isNotEqualTo(bankTransactionDTO2);
        bankTransactionDTO2.setId(bankTransactionDTO1.getId());
        assertThat(bankTransactionDTO1).isEqualTo(bankTransactionDTO2);
        bankTransactionDTO2.setId(2L);
        assertThat(bankTransactionDTO1).isNotEqualTo(bankTransactionDTO2);
        bankTransactionDTO1.setId(null);
        assertThat(bankTransactionDTO1).isNotEqualTo(bankTransactionDTO2);
    }
}
