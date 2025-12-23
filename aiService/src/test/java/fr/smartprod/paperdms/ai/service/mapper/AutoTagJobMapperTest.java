package fr.smartprod.paperdms.ai.service.mapper;

import static fr.smartprod.paperdms.ai.domain.AutoTagJobAsserts.*;
import static fr.smartprod.paperdms.ai.domain.AutoTagJobTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AutoTagJobMapperTest {

    private AutoTagJobMapper autoTagJobMapper;

    @BeforeEach
    void setUp() {
        autoTagJobMapper = new AutoTagJobMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAutoTagJobSample1();
        var actual = autoTagJobMapper.toEntity(autoTagJobMapper.toDto(expected));
        assertAutoTagJobAllPropertiesEquals(expected, actual);
    }
}
