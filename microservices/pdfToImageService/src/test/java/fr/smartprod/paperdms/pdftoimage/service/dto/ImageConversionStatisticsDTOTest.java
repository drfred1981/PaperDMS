package fr.smartprod.paperdms.pdftoimage.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.pdftoimage.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImageConversionStatisticsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImageConversionStatisticsDTO.class);
        ImageConversionStatisticsDTO imageConversionStatisticsDTO1 = new ImageConversionStatisticsDTO();
        imageConversionStatisticsDTO1.setId(1L);
        ImageConversionStatisticsDTO imageConversionStatisticsDTO2 = new ImageConversionStatisticsDTO();
        assertThat(imageConversionStatisticsDTO1).isNotEqualTo(imageConversionStatisticsDTO2);
        imageConversionStatisticsDTO2.setId(imageConversionStatisticsDTO1.getId());
        assertThat(imageConversionStatisticsDTO1).isEqualTo(imageConversionStatisticsDTO2);
        imageConversionStatisticsDTO2.setId(2L);
        assertThat(imageConversionStatisticsDTO1).isNotEqualTo(imageConversionStatisticsDTO2);
        imageConversionStatisticsDTO1.setId(null);
        assertThat(imageConversionStatisticsDTO1).isNotEqualTo(imageConversionStatisticsDTO2);
    }
}
