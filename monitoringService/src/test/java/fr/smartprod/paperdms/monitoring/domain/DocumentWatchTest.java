package fr.smartprod.paperdms.monitoring.domain;

import static fr.smartprod.paperdms.monitoring.domain.DocumentWatchTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.monitoring.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentWatchTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentWatch.class);
        DocumentWatch documentWatch1 = getDocumentWatchSample1();
        DocumentWatch documentWatch2 = new DocumentWatch();
        assertThat(documentWatch1).isNotEqualTo(documentWatch2);

        documentWatch2.setId(documentWatch1.getId());
        assertThat(documentWatch1).isEqualTo(documentWatch2);

        documentWatch2 = getDocumentWatchSample2();
        assertThat(documentWatch1).isNotEqualTo(documentWatch2);
    }
}
