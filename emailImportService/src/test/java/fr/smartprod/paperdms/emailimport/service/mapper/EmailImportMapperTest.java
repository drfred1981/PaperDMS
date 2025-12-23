package fr.smartprod.paperdms.emailimport.service.mapper;

import static fr.smartprod.paperdms.emailimport.domain.EmailImportAsserts.*;
import static fr.smartprod.paperdms.emailimport.domain.EmailImportTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailImportMapperTest {

    private EmailImportMapper emailImportMapper;

    @BeforeEach
    void setUp() {
        emailImportMapper = new EmailImportMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEmailImportSample1();
        var actual = emailImportMapper.toEntity(emailImportMapper.toDto(expected));
        assertEmailImportAllPropertiesEquals(expected, actual);
    }
}
