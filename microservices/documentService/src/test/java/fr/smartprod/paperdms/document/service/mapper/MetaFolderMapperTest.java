package fr.smartprod.paperdms.document.service.mapper;

import static fr.smartprod.paperdms.document.domain.MetaFolderAsserts.*;
import static fr.smartprod.paperdms.document.domain.MetaFolderTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MetaFolderMapperTest {

    private MetaFolderMapper metaFolderMapper;

    @BeforeEach
    void setUp() {
        metaFolderMapper = new MetaFolderMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMetaFolderSample1();
        var actual = metaFolderMapper.toEntity(metaFolderMapper.toDto(expected));
        assertMetaFolderAllPropertiesEquals(expected, actual);
    }
}
