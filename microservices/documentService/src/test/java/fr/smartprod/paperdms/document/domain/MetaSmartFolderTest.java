package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.MetaSmartFolderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MetaSmartFolderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetaSmartFolder.class);
        MetaSmartFolder metaSmartFolder1 = getMetaSmartFolderSample1();
        MetaSmartFolder metaSmartFolder2 = new MetaSmartFolder();
        assertThat(metaSmartFolder1).isNotEqualTo(metaSmartFolder2);

        metaSmartFolder2.setId(metaSmartFolder1.getId());
        assertThat(metaSmartFolder1).isEqualTo(metaSmartFolder2);

        metaSmartFolder2 = getMetaSmartFolderSample2();
        assertThat(metaSmartFolder1).isNotEqualTo(metaSmartFolder2);
    }
}
