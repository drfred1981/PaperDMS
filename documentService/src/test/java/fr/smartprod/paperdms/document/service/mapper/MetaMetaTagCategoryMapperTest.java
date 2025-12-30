package fr.smartprod.paperdms.document.service.mapper;

import static fr.smartprod.paperdms.document.domain.MetaMetaTagCategoryAsserts.*;
import static fr.smartprod.paperdms.document.domain.MetaMetaTagCategoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MetaMetaTagCategoryMapperTest {

    private MetaMetaTagCategoryMapper metaMetaTagCategoryMapper;

    @BeforeEach
    void setUp() {
        metaMetaTagCategoryMapper = new MetaMetaTagCategoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMetaMetaTagCategorySample1();
        var actual = metaMetaTagCategoryMapper.toEntity(metaMetaTagCategoryMapper.toDto(expected));
        assertMetaMetaTagCategoryAllPropertiesEquals(expected, actual);
    }
}
