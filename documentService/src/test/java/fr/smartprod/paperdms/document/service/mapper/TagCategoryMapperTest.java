package fr.smartprod.paperdms.document.service.mapper;

import static fr.smartprod.paperdms.document.domain.TagCategoryAsserts.*;
import static fr.smartprod.paperdms.document.domain.TagCategoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TagCategoryMapperTest {

    private TagCategoryMapper tagCategoryMapper;

    @BeforeEach
    void setUp() {
        tagCategoryMapper = new TagCategoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTagCategorySample1();
        var actual = tagCategoryMapper.toEntity(tagCategoryMapper.toDto(expected));
        assertTagCategoryAllPropertiesEquals(expected, actual);
    }
}
