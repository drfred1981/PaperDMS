package fr.smartprod.paperdms.pdftoimage.service.mapper;

import static fr.smartprod.paperdms.pdftoimage.domain.ImageConversionConfigAsserts.*;
import static fr.smartprod.paperdms.pdftoimage.domain.ImageConversionConfigTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImageConversionConfigMapperTest {

    private ImageConversionConfigMapper imageConversionConfigMapper;

    @BeforeEach
    void setUp() {
        imageConversionConfigMapper = new ImageConversionConfigMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getImageConversionConfigSample1();
        var actual = imageConversionConfigMapper.toEntity(imageConversionConfigMapper.toDto(expected));
        assertImageConversionConfigAllPropertiesEquals(expected, actual);
    }
}
