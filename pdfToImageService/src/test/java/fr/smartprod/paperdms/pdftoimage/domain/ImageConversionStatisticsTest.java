package fr.smartprod.paperdms.pdftoimage.domain;

import static fr.smartprod.paperdms.pdftoimage.domain.ImageConversionStatisticsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.pdftoimage.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImageConversionStatisticsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImageConversionStatistics.class);
        ImageConversionStatistics imageConversionStatistics1 = getImageConversionStatisticsSample1();
        ImageConversionStatistics imageConversionStatistics2 = new ImageConversionStatistics();
        assertThat(imageConversionStatistics1).isNotEqualTo(imageConversionStatistics2);

        imageConversionStatistics2.setId(imageConversionStatistics1.getId());
        assertThat(imageConversionStatistics1).isEqualTo(imageConversionStatistics2);

        imageConversionStatistics2 = getImageConversionStatisticsSample2();
        assertThat(imageConversionStatistics1).isNotEqualTo(imageConversionStatistics2);
    }
}
