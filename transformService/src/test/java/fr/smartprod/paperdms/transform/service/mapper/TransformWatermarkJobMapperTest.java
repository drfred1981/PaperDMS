package fr.smartprod.paperdms.transform.service.mapper;

import static fr.smartprod.paperdms.transform.domain.TransformWatermarkJobAsserts.*;
import static fr.smartprod.paperdms.transform.domain.TransformWatermarkJobTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransformWatermarkJobMapperTest {

    private TransformWatermarkJobMapper transformWatermarkJobMapper;

    @BeforeEach
    void setUp() {
        transformWatermarkJobMapper = new TransformWatermarkJobMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTransformWatermarkJobSample1();
        var actual = transformWatermarkJobMapper.toEntity(transformWatermarkJobMapper.toDto(expected));
        assertTransformWatermarkJobAllPropertiesEquals(expected, actual);
    }
}
