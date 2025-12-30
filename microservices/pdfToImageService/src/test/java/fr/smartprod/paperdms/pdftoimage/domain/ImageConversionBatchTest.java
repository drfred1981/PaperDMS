package fr.smartprod.paperdms.pdftoimage.domain;

import static fr.smartprod.paperdms.pdftoimage.domain.ImageConversionBatchTestSamples.*;
import static fr.smartprod.paperdms.pdftoimage.domain.ImagePdfConversionRequestTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.pdftoimage.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ImageConversionBatchTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImageConversionBatch.class);
        ImageConversionBatch imageConversionBatch1 = getImageConversionBatchSample1();
        ImageConversionBatch imageConversionBatch2 = new ImageConversionBatch();
        assertThat(imageConversionBatch1).isNotEqualTo(imageConversionBatch2);

        imageConversionBatch2.setId(imageConversionBatch1.getId());
        assertThat(imageConversionBatch1).isEqualTo(imageConversionBatch2);

        imageConversionBatch2 = getImageConversionBatchSample2();
        assertThat(imageConversionBatch1).isNotEqualTo(imageConversionBatch2);
    }

    @Test
    void conversionsTest() {
        ImageConversionBatch imageConversionBatch = getImageConversionBatchRandomSampleGenerator();
        ImagePdfConversionRequest imagePdfConversionRequestBack = getImagePdfConversionRequestRandomSampleGenerator();

        imageConversionBatch.addConversions(imagePdfConversionRequestBack);
        assertThat(imageConversionBatch.getConversions()).containsOnly(imagePdfConversionRequestBack);
        assertThat(imagePdfConversionRequestBack.getBatch()).isEqualTo(imageConversionBatch);

        imageConversionBatch.removeConversions(imagePdfConversionRequestBack);
        assertThat(imageConversionBatch.getConversions()).doesNotContain(imagePdfConversionRequestBack);
        assertThat(imagePdfConversionRequestBack.getBatch()).isNull();

        imageConversionBatch.conversions(new HashSet<>(Set.of(imagePdfConversionRequestBack)));
        assertThat(imageConversionBatch.getConversions()).containsOnly(imagePdfConversionRequestBack);
        assertThat(imagePdfConversionRequestBack.getBatch()).isEqualTo(imageConversionBatch);

        imageConversionBatch.setConversions(new HashSet<>());
        assertThat(imageConversionBatch.getConversions()).doesNotContain(imagePdfConversionRequestBack);
        assertThat(imagePdfConversionRequestBack.getBatch()).isNull();
    }
}
