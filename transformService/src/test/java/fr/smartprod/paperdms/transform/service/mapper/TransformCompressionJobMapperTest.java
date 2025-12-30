package fr.smartprod.paperdms.transform.service.mapper;

import static fr.smartprod.paperdms.transform.domain.TransformCompressionJobAsserts.*;
import static fr.smartprod.paperdms.transform.domain.TransformCompressionJobTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransformCompressionJobMapperTest {

    private TransformCompressionJobMapper transformCompressionJobMapper;

    @BeforeEach
    void setUp() {
        transformCompressionJobMapper = new TransformCompressionJobMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTransformCompressionJobSample1();
        var actual = transformCompressionJobMapper.toEntity(transformCompressionJobMapper.toDto(expected));
        assertTransformCompressionJobAllPropertiesEquals(expected, actual);
    }
}
