package fr.smartprod.paperdms.pdftoimage.service.mapper;

import static fr.smartprod.paperdms.pdftoimage.domain.ImagePdfConversionRequestAsserts.*;
import static fr.smartprod.paperdms.pdftoimage.domain.ImagePdfConversionRequestTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImagePdfConversionRequestMapperTest {

    private ImagePdfConversionRequestMapper imagePdfConversionRequestMapper;

    @BeforeEach
    void setUp() {
        imagePdfConversionRequestMapper = new ImagePdfConversionRequestMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getImagePdfConversionRequestSample1();
        var actual = imagePdfConversionRequestMapper.toEntity(imagePdfConversionRequestMapper.toDto(expected));
        assertImagePdfConversionRequestAllPropertiesEquals(expected, actual);
    }
}
