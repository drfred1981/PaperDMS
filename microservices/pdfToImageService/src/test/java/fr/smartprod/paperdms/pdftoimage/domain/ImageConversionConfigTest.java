package fr.smartprod.paperdms.pdftoimage.domain;

import static fr.smartprod.paperdms.pdftoimage.domain.ImageConversionConfigTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.pdftoimage.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImageConversionConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImageConversionConfig.class);
        ImageConversionConfig imageConversionConfig1 = getImageConversionConfigSample1();
        ImageConversionConfig imageConversionConfig2 = new ImageConversionConfig();
        assertThat(imageConversionConfig1).isNotEqualTo(imageConversionConfig2);

        imageConversionConfig2.setId(imageConversionConfig1.getId());
        assertThat(imageConversionConfig1).isEqualTo(imageConversionConfig2);

        imageConversionConfig2 = getImageConversionConfigSample2();
        assertThat(imageConversionConfig1).isNotEqualTo(imageConversionConfig2);
    }
}
