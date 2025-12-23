package fr.smartprod.paperdms.business.domain;

import static fr.smartprod.paperdms.business.domain.BankStatementTestSamples.*;
import static fr.smartprod.paperdms.business.domain.BankTransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.business.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BankTransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BankTransaction.class);
        BankTransaction bankTransaction1 = getBankTransactionSample1();
        BankTransaction bankTransaction2 = new BankTransaction();
        assertThat(bankTransaction1).isNotEqualTo(bankTransaction2);

        bankTransaction2.setId(bankTransaction1.getId());
        assertThat(bankTransaction1).isEqualTo(bankTransaction2);

        bankTransaction2 = getBankTransactionSample2();
        assertThat(bankTransaction1).isNotEqualTo(bankTransaction2);
    }

    @Test
    void statementTest() {
        BankTransaction bankTransaction = getBankTransactionRandomSampleGenerator();
        BankStatement bankStatementBack = getBankStatementRandomSampleGenerator();

        bankTransaction.setStatement(bankStatementBack);
        assertThat(bankTransaction.getStatement()).isEqualTo(bankStatementBack);

        bankTransaction.statement(null);
        assertThat(bankTransaction.getStatement()).isNull();
    }
}
