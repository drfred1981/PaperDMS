package fr.smartprod.paperdms.business.service.mapper;

import static fr.smartprod.paperdms.business.domain.BankStatementAsserts.*;
import static fr.smartprod.paperdms.business.domain.BankStatementTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BankStatementMapperTest {

    private BankStatementMapper bankStatementMapper;

    @BeforeEach
    void setUp() {
        bankStatementMapper = new BankStatementMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBankStatementSample1();
        var actual = bankStatementMapper.toEntity(bankStatementMapper.toDto(expected));
        assertBankStatementAllPropertiesEquals(expected, actual);
    }
}
