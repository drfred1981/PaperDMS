package fr.smartprod.paperdms.document.service.mapper;

import static fr.smartprod.paperdms.document.domain.MetaSavedSearchAsserts.*;
import static fr.smartprod.paperdms.document.domain.MetaSavedSearchTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MetaSavedSearchMapperTest {

    private MetaSavedSearchMapper metaSavedSearchMapper;

    @BeforeEach
    void setUp() {
        metaSavedSearchMapper = new MetaSavedSearchMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMetaSavedSearchSample1();
        var actual = metaSavedSearchMapper.toEntity(metaSavedSearchMapper.toDto(expected));
        assertMetaSavedSearchAllPropertiesEquals(expected, actual);
    }
}
