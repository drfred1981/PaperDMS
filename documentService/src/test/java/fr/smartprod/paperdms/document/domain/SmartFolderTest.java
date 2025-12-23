package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.SmartFolderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SmartFolderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmartFolder.class);
        SmartFolder smartFolder1 = getSmartFolderSample1();
        SmartFolder smartFolder2 = new SmartFolder();
        assertThat(smartFolder1).isNotEqualTo(smartFolder2);

        smartFolder2.setId(smartFolder1.getId());
        assertThat(smartFolder1).isEqualTo(smartFolder2);

        smartFolder2 = getSmartFolderSample2();
        assertThat(smartFolder1).isNotEqualTo(smartFolder2);
    }
}
