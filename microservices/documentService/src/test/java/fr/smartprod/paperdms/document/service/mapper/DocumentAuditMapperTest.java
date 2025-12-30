package fr.smartprod.paperdms.document.service.mapper;

import static fr.smartprod.paperdms.document.domain.DocumentAuditAsserts.*;
import static fr.smartprod.paperdms.document.domain.DocumentAuditTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentAuditMapperTest {

    private DocumentAuditMapper documentAuditMapper;

    @BeforeEach
    void setUp() {
        documentAuditMapper = new DocumentAuditMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDocumentAuditSample1();
        var actual = documentAuditMapper.toEntity(documentAuditMapper.toDto(expected));
        assertDocumentAuditAllPropertiesEquals(expected, actual);
    }
}
