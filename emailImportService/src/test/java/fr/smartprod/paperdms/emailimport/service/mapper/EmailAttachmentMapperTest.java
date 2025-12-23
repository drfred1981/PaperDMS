package fr.smartprod.paperdms.emailimport.service.mapper;

import static fr.smartprod.paperdms.emailimport.domain.EmailAttachmentAsserts.*;
import static fr.smartprod.paperdms.emailimport.domain.EmailAttachmentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailAttachmentMapperTest {

    private EmailAttachmentMapper emailAttachmentMapper;

    @BeforeEach
    void setUp() {
        emailAttachmentMapper = new EmailAttachmentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEmailAttachmentSample1();
        var actual = emailAttachmentMapper.toEntity(emailAttachmentMapper.toDto(expected));
        assertEmailAttachmentAllPropertiesEquals(expected, actual);
    }
}
