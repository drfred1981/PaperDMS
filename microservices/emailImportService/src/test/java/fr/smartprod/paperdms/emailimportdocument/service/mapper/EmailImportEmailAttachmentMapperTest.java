package fr.smartprod.paperdms.emailimportdocument.service.mapper;

import static fr.smartprod.paperdms.emailimportdocument.domain.EmailImportEmailAttachmentAsserts.*;
import static fr.smartprod.paperdms.emailimportdocument.domain.EmailImportEmailAttachmentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailImportEmailAttachmentMapperTest {

    private EmailImportEmailAttachmentMapper emailImportEmailAttachmentMapper;

    @BeforeEach
    void setUp() {
        emailImportEmailAttachmentMapper = new EmailImportEmailAttachmentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEmailImportEmailAttachmentSample1();
        var actual = emailImportEmailAttachmentMapper.toEntity(emailImportEmailAttachmentMapper.toDto(expected));
        assertEmailImportEmailAttachmentAllPropertiesEquals(expected, actual);
    }
}
