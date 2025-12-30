package fr.smartprod.paperdms.pdftoimage.service.mapper;

import static fr.smartprod.paperdms.pdftoimage.domain.ImageConversionBatchAsserts.*;
import static fr.smartprod.paperdms.pdftoimage.domain.ImageConversionBatchTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImageConversionBatchMapperTest {

    private ImageConversionBatchMapper imageConversionBatchMapper;

    @BeforeEach
    void setUp() {
        imageConversionBatchMapper = new ImageConversionBatchMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getImageConversionBatchSample1();
        var actual = imageConversionBatchMapper.toEntity(imageConversionBatchMapper.toDto(expected));
        assertImageConversionBatchAllPropertiesEquals(expected, actual);
    }
}
