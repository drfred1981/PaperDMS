package fr.smartprod.paperdms.pdftoimage.service.mapper;

import static fr.smartprod.paperdms.pdftoimage.domain.ImageConversionHistoryAsserts.*;
import static fr.smartprod.paperdms.pdftoimage.domain.ImageConversionHistoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImageConversionHistoryMapperTest {

    private ImageConversionHistoryMapper imageConversionHistoryMapper;

    @BeforeEach
    void setUp() {
        imageConversionHistoryMapper = new ImageConversionHistoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getImageConversionHistorySample1();
        var actual = imageConversionHistoryMapper.toEntity(imageConversionHistoryMapper.toDto(expected));
        assertImageConversionHistoryAllPropertiesEquals(expected, actual);
    }
}
