package fr.smartprod.paperdms.document.domain;

import static fr.smartprod.paperdms.document.domain.DocumentStatisticsTestSamples.*;
import static fr.smartprod.paperdms.document.domain.DocumentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.document.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentStatisticsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentStatistics.class);
        DocumentStatistics documentStatistics1 = getDocumentStatisticsSample1();
        DocumentStatistics documentStatistics2 = new DocumentStatistics();
        assertThat(documentStatistics1).isNotEqualTo(documentStatistics2);

        documentStatistics2.setId(documentStatistics1.getId());
        assertThat(documentStatistics1).isEqualTo(documentStatistics2);

        documentStatistics2 = getDocumentStatisticsSample2();
        assertThat(documentStatistics1).isNotEqualTo(documentStatistics2);
    }

    @Test
    void documentTest() {
        DocumentStatistics documentStatistics = getDocumentStatisticsRandomSampleGenerator();
        Document documentBack = getDocumentRandomSampleGenerator();

        documentStatistics.setDocument(documentBack);
        assertThat(documentStatistics.getDocument()).isEqualTo(documentBack);

        documentStatistics.document(null);
        assertThat(documentStatistics.getDocument()).isNull();
    }
}
