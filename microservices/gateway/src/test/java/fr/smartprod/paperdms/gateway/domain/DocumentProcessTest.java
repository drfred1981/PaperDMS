package fr.smartprod.paperdms.gateway.domain;

import static fr.smartprod.paperdms.gateway.domain.DocumentProcessTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentProcessTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentProcess.class);
        DocumentProcess documentProcess1 = getDocumentProcessSample1();
        DocumentProcess documentProcess2 = new DocumentProcess();
        assertThat(documentProcess1).isNotEqualTo(documentProcess2);

        documentProcess2.setId(documentProcess1.getId());
        assertThat(documentProcess1).isEqualTo(documentProcess2);

        documentProcess2 = getDocumentProcessSample2();
        assertThat(documentProcess1).isNotEqualTo(documentProcess2);
    }
}
