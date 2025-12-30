package fr.smartprod.paperdms.document.service.mapper;

import static fr.smartprod.paperdms.document.domain.MetaTagAsserts.*;
import static fr.smartprod.paperdms.document.domain.MetaTagTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MetaTagMapperTest {

    private MetaTagMapper metaTagMapper;

    @BeforeEach
    void setUp() {
        metaTagMapper = new MetaTagMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMetaTagSample1();
        var actual = metaTagMapper.toEntity(metaTagMapper.toDto(expected));
        assertMetaTagAllPropertiesEquals(expected, actual);
    }
}
