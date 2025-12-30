package fr.smartprod.paperdms.emailimportdocument.service.mapper;

import static fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportRuleAsserts.*;
import static fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportRuleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailImportImportRuleMapperTest {

    private EmailImportImportRuleMapper emailImportImportRuleMapper;

    @BeforeEach
    void setUp() {
        emailImportImportRuleMapper = new EmailImportImportRuleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEmailImportImportRuleSample1();
        var actual = emailImportImportRuleMapper.toEntity(emailImportImportRuleMapper.toDto(expected));
        assertEmailImportImportRuleAllPropertiesEquals(expected, actual);
    }
}
