package fr.smartprod.paperdms.similarity.domain;

import static fr.smartprod.paperdms.similarity.domain.SimilarityDocumentFingerprintTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.similarity.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SimilarityDocumentFingerprintTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SimilarityDocumentFingerprint.class);
        SimilarityDocumentFingerprint similarityDocumentFingerprint1 = getSimilarityDocumentFingerprintSample1();
        SimilarityDocumentFingerprint similarityDocumentFingerprint2 = new SimilarityDocumentFingerprint();
        assertThat(similarityDocumentFingerprint1).isNotEqualTo(similarityDocumentFingerprint2);

        similarityDocumentFingerprint2.setId(similarityDocumentFingerprint1.getId());
        assertThat(similarityDocumentFingerprint1).isEqualTo(similarityDocumentFingerprint2);

        similarityDocumentFingerprint2 = getSimilarityDocumentFingerprintSample2();
        assertThat(similarityDocumentFingerprint1).isNotEqualTo(similarityDocumentFingerprint2);
    }
}
