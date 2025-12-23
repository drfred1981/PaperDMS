package fr.smartprod.paperdms.archive.service.mapper;

import static fr.smartprod.paperdms.archive.domain.ArchiveDocumentAsserts.*;
import static fr.smartprod.paperdms.archive.domain.ArchiveDocumentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArchiveDocumentMapperTest {

    private ArchiveDocumentMapper archiveDocumentMapper;

    @BeforeEach
    void setUp() {
        archiveDocumentMapper = new ArchiveDocumentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getArchiveDocumentSample1();
        var actual = archiveDocumentMapper.toEntity(archiveDocumentMapper.toDto(expected));
        assertArchiveDocumentAllPropertiesEquals(expected, actual);
    }
}
