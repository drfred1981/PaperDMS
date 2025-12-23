package fr.smartprod.paperdms.transform.service.mapper;

import static fr.smartprod.paperdms.transform.domain.ConversionJobAsserts.*;
import static fr.smartprod.paperdms.transform.domain.ConversionJobTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConversionJobMapperTest {

    private ConversionJobMapper conversionJobMapper;

    @BeforeEach
    void setUp() {
        conversionJobMapper = new ConversionJobMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getConversionJobSample1();
        var actual = conversionJobMapper.toEntity(conversionJobMapper.toDto(expected));
        assertConversionJobAllPropertiesEquals(expected, actual);
    }
}
