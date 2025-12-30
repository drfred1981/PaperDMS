package fr.smartprod.paperdms.transform.service.mapper;

import static fr.smartprod.paperdms.transform.domain.TransformConversionJobAsserts.*;
import static fr.smartprod.paperdms.transform.domain.TransformConversionJobTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransformConversionJobMapperTest {

    private TransformConversionJobMapper transformConversionJobMapper;

    @BeforeEach
    void setUp() {
        transformConversionJobMapper = new TransformConversionJobMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTransformConversionJobSample1();
        var actual = transformConversionJobMapper.toEntity(transformConversionJobMapper.toDto(expected));
        assertTransformConversionJobAllPropertiesEquals(expected, actual);
    }
}
