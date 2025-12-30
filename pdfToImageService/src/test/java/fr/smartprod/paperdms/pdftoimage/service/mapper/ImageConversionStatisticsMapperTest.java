package fr.smartprod.paperdms.pdftoimage.service.mapper;

import static fr.smartprod.paperdms.pdftoimage.domain.ImageConversionStatisticsAsserts.*;
import static fr.smartprod.paperdms.pdftoimage.domain.ImageConversionStatisticsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImageConversionStatisticsMapperTest {

    private ImageConversionStatisticsMapper imageConversionStatisticsMapper;

    @BeforeEach
    void setUp() {
        imageConversionStatisticsMapper = new ImageConversionStatisticsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getImageConversionStatisticsSample1();
        var actual = imageConversionStatisticsMapper.toEntity(imageConversionStatisticsMapper.toDto(expected));
        assertImageConversionStatisticsAllPropertiesEquals(expected, actual);
    }
}
