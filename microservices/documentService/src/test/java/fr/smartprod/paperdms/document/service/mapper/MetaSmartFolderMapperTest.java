package fr.smartprod.paperdms.document.service.mapper;

import static fr.smartprod.paperdms.document.domain.MetaSmartFolderAsserts.*;
import static fr.smartprod.paperdms.document.domain.MetaSmartFolderTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MetaSmartFolderMapperTest {

    private MetaSmartFolderMapper metaSmartFolderMapper;

    @BeforeEach
    void setUp() {
        metaSmartFolderMapper = new MetaSmartFolderMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMetaSmartFolderSample1();
        var actual = metaSmartFolderMapper.toEntity(metaSmartFolderMapper.toDto(expected));
        assertMetaSmartFolderAllPropertiesEquals(expected, actual);
    }
}
