package fr.smartprod.paperdms.transform.service.mapper;

import static fr.smartprod.paperdms.transform.domain.WatermarkJobAsserts.*;
import static fr.smartprod.paperdms.transform.domain.WatermarkJobTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WatermarkJobMapperTest {

    private WatermarkJobMapper watermarkJobMapper;

    @BeforeEach
    void setUp() {
        watermarkJobMapper = new WatermarkJobMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getWatermarkJobSample1();
        var actual = watermarkJobMapper.toEntity(watermarkJobMapper.toDto(expected));
        assertWatermarkJobAllPropertiesEquals(expected, actual);
    }
}
