package fr.smartprod.paperdms.emailimport.service.mapper;

import static fr.smartprod.paperdms.emailimport.domain.ImportRuleAsserts.*;
import static fr.smartprod.paperdms.emailimport.domain.ImportRuleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImportRuleMapperTest {

    private ImportRuleMapper importRuleMapper;

    @BeforeEach
    void setUp() {
        importRuleMapper = new ImportRuleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getImportRuleSample1();
        var actual = importRuleMapper.toEntity(importRuleMapper.toDto(expected));
        assertImportRuleAllPropertiesEquals(expected, actual);
    }
}
