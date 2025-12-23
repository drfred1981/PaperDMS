package fr.smartprod.paperdms.document.service.mapper;

import static fr.smartprod.paperdms.document.domain.SmartFolderAsserts.*;
import static fr.smartprod.paperdms.document.domain.SmartFolderTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SmartFolderMapperTest {

    private SmartFolderMapper smartFolderMapper;

    @BeforeEach
    void setUp() {
        smartFolderMapper = new SmartFolderMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSmartFolderSample1();
        var actual = smartFolderMapper.toEntity(smartFolderMapper.toDto(expected));
        assertSmartFolderAllPropertiesEquals(expected, actual);
    }
}
