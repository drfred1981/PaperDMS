package fr.smartprod.paperdms.document.service.mapper;

import static fr.smartprod.paperdms.document.domain.SavedSearchAsserts.*;
import static fr.smartprod.paperdms.document.domain.SavedSearchTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SavedSearchMapperTest {

    private SavedSearchMapper savedSearchMapper;

    @BeforeEach
    void setUp() {
        savedSearchMapper = new SavedSearchMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSavedSearchSample1();
        var actual = savedSearchMapper.toEntity(savedSearchMapper.toDto(expected));
        assertSavedSearchAllPropertiesEquals(expected, actual);
    }
}
