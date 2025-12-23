package fr.smartprod.paperdms.business.service.mapper;

import static fr.smartprod.paperdms.business.domain.BankTransactionAsserts.*;
import static fr.smartprod.paperdms.business.domain.BankTransactionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BankTransactionMapperTest {

    private BankTransactionMapper bankTransactionMapper;

    @BeforeEach
    void setUp() {
        bankTransactionMapper = new BankTransactionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBankTransactionSample1();
        var actual = bankTransactionMapper.toEntity(bankTransactionMapper.toDto(expected));
        assertBankTransactionAllPropertiesEquals(expected, actual);
    }
}
