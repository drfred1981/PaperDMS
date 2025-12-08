package com.ged.similarity.domain;

import static com.ged.similarity.domain.DocumentSimilarityTestSamples.*;
import static com.ged.similarity.domain.SimilarityJobTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ged.similarity.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentSimilarityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentSimilarity.class);
        DocumentSimilarity documentSimilarity1 = getDocumentSimilaritySample1();
        DocumentSimilarity documentSimilarity2 = new DocumentSimilarity();
        assertThat(documentSimilarity1).isNotEqualTo(documentSimilarity2);

        documentSimilarity2.setId(documentSimilarity1.getId());
        assertThat(documentSimilarity1).isEqualTo(documentSimilarity2);

        documentSimilarity2 = getDocumentSimilaritySample2();
        assertThat(documentSimilarity1).isNotEqualTo(documentSimilarity2);
    }

    @Test
    void jobTest() {
        DocumentSimilarity documentSimilarity = getDocumentSimilarityRandomSampleGenerator();
        SimilarityJob similarityJobBack = getSimilarityJobRandomSampleGenerator();

        documentSimilarity.setJob(similarityJobBack);
        assertThat(documentSimilarity.getJob()).isEqualTo(similarityJobBack);

        documentSimilarity.job(null);
        assertThat(documentSimilarity.getJob()).isNull();
    }
}
