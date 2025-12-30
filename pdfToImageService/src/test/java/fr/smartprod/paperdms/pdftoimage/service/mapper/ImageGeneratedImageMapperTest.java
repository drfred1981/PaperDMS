package fr.smartprod.paperdms.pdftoimage.service.mapper;

import static fr.smartprod.paperdms.pdftoimage.domain.ImageGeneratedImageAsserts.*;
import static fr.smartprod.paperdms.pdftoimage.domain.ImageGeneratedImageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImageGeneratedImageMapperTest {

    private ImageGeneratedImageMapper imageGeneratedImageMapper;

    @BeforeEach
    void setUp() {
        imageGeneratedImageMapper = new ImageGeneratedImageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getImageGeneratedImageSample1();
        var actual = imageGeneratedImageMapper.toEntity(imageGeneratedImageMapper.toDto(expected));
        assertImageGeneratedImageAllPropertiesEquals(expected, actual);
    }
}
