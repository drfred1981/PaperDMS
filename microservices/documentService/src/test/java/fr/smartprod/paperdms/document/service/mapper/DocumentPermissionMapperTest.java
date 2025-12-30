package fr.smartprod.paperdms.document.service.mapper;

import static fr.smartprod.paperdms.document.domain.DocumentPermissionAsserts.*;
import static fr.smartprod.paperdms.document.domain.DocumentPermissionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentPermissionMapperTest {

    private DocumentPermissionMapper documentPermissionMapper;

    @BeforeEach
    void setUp() {
        documentPermissionMapper = new DocumentPermissionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDocumentPermissionSample1();
        var actual = documentPermissionMapper.toEntity(documentPermissionMapper.toDto(expected));
        assertDocumentPermissionAllPropertiesEquals(expected, actual);
    }
}
