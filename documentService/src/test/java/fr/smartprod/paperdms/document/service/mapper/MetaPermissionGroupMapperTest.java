package fr.smartprod.paperdms.document.service.mapper;

import static fr.smartprod.paperdms.document.domain.MetaPermissionGroupAsserts.*;
import static fr.smartprod.paperdms.document.domain.MetaPermissionGroupTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MetaPermissionGroupMapperTest {

    private MetaPermissionGroupMapper metaPermissionGroupMapper;

    @BeforeEach
    void setUp() {
        metaPermissionGroupMapper = new MetaPermissionGroupMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMetaPermissionGroupSample1();
        var actual = metaPermissionGroupMapper.toEntity(metaPermissionGroupMapper.toDto(expected));
        assertMetaPermissionGroupAllPropertiesEquals(expected, actual);
    }
}
