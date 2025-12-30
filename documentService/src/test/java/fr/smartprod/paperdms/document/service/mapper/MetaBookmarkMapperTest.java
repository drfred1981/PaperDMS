package fr.smartprod.paperdms.document.service.mapper;

import static fr.smartprod.paperdms.document.domain.MetaBookmarkAsserts.*;
import static fr.smartprod.paperdms.document.domain.MetaBookmarkTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MetaBookmarkMapperTest {

    private MetaBookmarkMapper metaBookmarkMapper;

    @BeforeEach
    void setUp() {
        metaBookmarkMapper = new MetaBookmarkMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMetaBookmarkSample1();
        var actual = metaBookmarkMapper.toEntity(metaBookmarkMapper.toDto(expected));
        assertMetaBookmarkAllPropertiesEquals(expected, actual);
    }
}
