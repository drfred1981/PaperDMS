package fr.smartprod.paperdms.pdftoimage.domain;

import static fr.smartprod.paperdms.pdftoimage.domain.ImageGeneratedImageTestSamples.*;
import static fr.smartprod.paperdms.pdftoimage.domain.ImagePdfConversionRequestTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.pdftoimage.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImageGeneratedImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImageGeneratedImage.class);
        ImageGeneratedImage imageGeneratedImage1 = getImageGeneratedImageSample1();
        ImageGeneratedImage imageGeneratedImage2 = new ImageGeneratedImage();
        assertThat(imageGeneratedImage1).isNotEqualTo(imageGeneratedImage2);

        imageGeneratedImage2.setId(imageGeneratedImage1.getId());
        assertThat(imageGeneratedImage1).isEqualTo(imageGeneratedImage2);

        imageGeneratedImage2 = getImageGeneratedImageSample2();
        assertThat(imageGeneratedImage1).isNotEqualTo(imageGeneratedImage2);
    }

    @Test
    void conversionRequestTest() {
        ImageGeneratedImage imageGeneratedImage = getImageGeneratedImageRandomSampleGenerator();
        ImagePdfConversionRequest imagePdfConversionRequestBack = getImagePdfConversionRequestRandomSampleGenerator();

        imageGeneratedImage.setConversionRequest(imagePdfConversionRequestBack);
        assertThat(imageGeneratedImage.getConversionRequest()).isEqualTo(imagePdfConversionRequestBack);

        imageGeneratedImage.conversionRequest(null);
        assertThat(imageGeneratedImage.getConversionRequest()).isNull();
    }
}
