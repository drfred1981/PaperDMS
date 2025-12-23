package fr.smartprod.paperdms.document.service.mapper;

import static fr.smartprod.paperdms.document.domain.PermissionGroupAsserts.*;
import static fr.smartprod.paperdms.document.domain.PermissionGroupTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PermissionGroupMapperTest {

    private PermissionGroupMapper permissionGroupMapper;

    @BeforeEach
    void setUp() {
        permissionGroupMapper = new PermissionGroupMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPermissionGroupSample1();
        var actual = permissionGroupMapper.toEntity(permissionGroupMapper.toDto(expected));
        assertPermissionGroupAllPropertiesEquals(expected, actual);
    }
}
