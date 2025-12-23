package fr.smartprod.paperdms.business.domain;

import static fr.smartprod.paperdms.business.domain.BankStatementTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.business.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BankStatementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BankStatement.class);
        BankStatement bankStatement1 = getBankStatementSample1();
        BankStatement bankStatement2 = new BankStatement();
        assertThat(bankStatement1).isNotEqualTo(bankStatement2);

        bankStatement2.setId(bankStatement1.getId());
        assertThat(bankStatement1).isEqualTo(bankStatement2);

        bankStatement2 = getBankStatementSample2();
        assertThat(bankStatement1).isNotEqualTo(bankStatement2);
    }
}
