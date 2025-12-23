package fr.smartprod.paperdms.business.service.mapper;

import static fr.smartprod.paperdms.business.domain.ContractClauseAsserts.*;
import static fr.smartprod.paperdms.business.domain.ContractClauseTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContractClauseMapperTest {

    private ContractClauseMapper contractClauseMapper;

    @BeforeEach
    void setUp() {
        contractClauseMapper = new ContractClauseMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getContractClauseSample1();
        var actual = contractClauseMapper.toEntity(contractClauseMapper.toDto(expected));
        assertContractClauseAllPropertiesEquals(expected, actual);
    }
}
