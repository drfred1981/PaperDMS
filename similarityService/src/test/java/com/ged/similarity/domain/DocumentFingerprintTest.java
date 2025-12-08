package com.ged.similarity.domain;

import static com.ged.similarity.domain.DocumentFingerprintTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ged.similarity.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentFingerprintTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentFingerprint.class);
        DocumentFingerprint documentFingerprint1 = getDocumentFingerprintSample1();
        DocumentFingerprint documentFingerprint2 = new DocumentFingerprint();
        assertThat(documentFingerprint1).isNotEqualTo(documentFingerprint2);

        documentFingerprint2.setId(documentFingerprint1.getId());
        assertThat(documentFingerprint1).isEqualTo(documentFingerprint2);

        documentFingerprint2 = getDocumentFingerprintSample2();
        assertThat(documentFingerprint1).isNotEqualTo(documentFingerprint2);
    }
}
