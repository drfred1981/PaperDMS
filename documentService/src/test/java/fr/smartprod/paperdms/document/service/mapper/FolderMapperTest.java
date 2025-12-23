package fr.smartprod.paperdms.document.service.mapper;

import static fr.smartprod.paperdms.document.domain.FolderAsserts.*;
import static fr.smartprod.paperdms.document.domain.FolderTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FolderMapperTest {

    private FolderMapper folderMapper;

    @BeforeEach
    void setUp() {
        folderMapper = new FolderMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFolderSample1();
        var actual = folderMapper.toEntity(folderMapper.toDto(expected));
        assertFolderAllPropertiesEquals(expected, actual);
    }
}
