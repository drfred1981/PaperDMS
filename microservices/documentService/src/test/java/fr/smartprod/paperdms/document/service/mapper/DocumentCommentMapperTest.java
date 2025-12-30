package fr.smartprod.paperdms.document.service.mapper;

import static fr.smartprod.paperdms.document.domain.DocumentCommentAsserts.*;
import static fr.smartprod.paperdms.document.domain.DocumentCommentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentCommentMapperTest {

    private DocumentCommentMapper documentCommentMapper;

    @BeforeEach
    void setUp() {
        documentCommentMapper = new DocumentCommentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDocumentCommentSample1();
        var actual = documentCommentMapper.toEntity(documentCommentMapper.toDto(expected));
        assertDocumentCommentAllPropertiesEquals(expected, actual);
    }
}
