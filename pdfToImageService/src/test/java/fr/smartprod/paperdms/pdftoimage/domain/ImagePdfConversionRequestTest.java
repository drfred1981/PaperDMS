package fr.smartprod.paperdms.pdftoimage.domain;

import static fr.smartprod.paperdms.pdftoimage.domain.ImageConversionBatchTestSamples.*;
import static fr.smartprod.paperdms.pdftoimage.domain.ImageGeneratedImageTestSamples.*;
import static fr.smartprod.paperdms.pdftoimage.domain.ImagePdfConversionRequestTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.pdftoimage.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ImagePdfConversionRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImagePdfConversionRequest.class);
        ImagePdfConversionRequest imagePdfConversionRequest1 = getImagePdfConversionRequestSample1();
        ImagePdfConversionRequest imagePdfConversionRequest2 = new ImagePdfConversionRequest();
        assertThat(imagePdfConversionRequest1).isNotEqualTo(imagePdfConversionRequest2);

        imagePdfConversionRequest2.setId(imagePdfConversionRequest1.getId());
        assertThat(imagePdfConversionRequest1).isEqualTo(imagePdfConversionRequest2);

        imagePdfConversionRequest2 = getImagePdfConversionRequestSample2();
        assertThat(imagePdfConversionRequest1).isNotEqualTo(imagePdfConversionRequest2);
    }

    @Test
    void generatedImagesTest() {
        ImagePdfConversionRequest imagePdfConversionRequest = getImagePdfConversionRequestRandomSampleGenerator();
        ImageGeneratedImage imageGeneratedImageBack = getImageGeneratedImageRandomSampleGenerator();

        imagePdfConversionRequest.addGeneratedImages(imageGeneratedImageBack);
        assertThat(imagePdfConversionRequest.getGeneratedImages()).containsOnly(imageGeneratedImageBack);
        assertThat(imageGeneratedImageBack.getConversionRequest()).isEqualTo(imagePdfConversionRequest);

        imagePdfConversionRequest.removeGeneratedImages(imageGeneratedImageBack);
        assertThat(imagePdfConversionRequest.getGeneratedImages()).doesNotContain(imageGeneratedImageBack);
        assertThat(imageGeneratedImageBack.getConversionRequest()).isNull();

        imagePdfConversionRequest.generatedImages(new HashSet<>(Set.of(imageGeneratedImageBack)));
        assertThat(imagePdfConversionRequest.getGeneratedImages()).containsOnly(imageGeneratedImageBack);
        assertThat(imageGeneratedImageBack.getConversionRequest()).isEqualTo(imagePdfConversionRequest);

        imagePdfConversionRequest.setGeneratedImages(new HashSet<>());
        assertThat(imagePdfConversionRequest.getGeneratedImages()).doesNotContain(imageGeneratedImageBack);
        assertThat(imageGeneratedImageBack.getConversionRequest()).isNull();
    }

    @Test
    void batchTest() {
        ImagePdfConversionRequest imagePdfConversionRequest = getImagePdfConversionRequestRandomSampleGenerator();
        ImageConversionBatch imageConversionBatchBack = getImageConversionBatchRandomSampleGenerator();

        imagePdfConversionRequest.setBatch(imageConversionBatchBack);
        assertThat(imagePdfConversionRequest.getBatch()).isEqualTo(imageConversionBatchBack);

        imagePdfConversionRequest.batch(null);
        assertThat(imagePdfConversionRequest.getBatch()).isNull();
    }
}
