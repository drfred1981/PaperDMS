package fr.smartprod.paperdms.pdftoimage.domain;

import static fr.smartprod.paperdms.pdftoimage.domain.ImageConversionHistoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.pdftoimage.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImageConversionHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImageConversionHistory.class);
        ImageConversionHistory imageConversionHistory1 = getImageConversionHistorySample1();
        ImageConversionHistory imageConversionHistory2 = new ImageConversionHistory();
        assertThat(imageConversionHistory1).isNotEqualTo(imageConversionHistory2);

        imageConversionHistory2.setId(imageConversionHistory1.getId());
        assertThat(imageConversionHistory1).isEqualTo(imageConversionHistory2);

        imageConversionHistory2 = getImageConversionHistorySample2();
        assertThat(imageConversionHistory1).isNotEqualTo(imageConversionHistory2);
    }
}
