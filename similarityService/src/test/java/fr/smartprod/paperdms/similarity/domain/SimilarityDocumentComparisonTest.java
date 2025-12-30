package fr.smartprod.paperdms.similarity.domain;

import static fr.smartprod.paperdms.similarity.domain.SimilarityDocumentComparisonTestSamples.*;
import static fr.smartprod.paperdms.similarity.domain.SimilarityJobTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.similarity.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SimilarityDocumentComparisonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SimilarityDocumentComparison.class);
        SimilarityDocumentComparison similarityDocumentComparison1 = getSimilarityDocumentComparisonSample1();
        SimilarityDocumentComparison similarityDocumentComparison2 = new SimilarityDocumentComparison();
        assertThat(similarityDocumentComparison1).isNotEqualTo(similarityDocumentComparison2);

        similarityDocumentComparison2.setId(similarityDocumentComparison1.getId());
        assertThat(similarityDocumentComparison1).isEqualTo(similarityDocumentComparison2);

        similarityDocumentComparison2 = getSimilarityDocumentComparisonSample2();
        assertThat(similarityDocumentComparison1).isNotEqualTo(similarityDocumentComparison2);
    }

    @Test
    void jobTest() {
        SimilarityDocumentComparison similarityDocumentComparison = getSimilarityDocumentComparisonRandomSampleGenerator();
        SimilarityJob similarityJobBack = getSimilarityJobRandomSampleGenerator();

        similarityDocumentComparison.setJob(similarityJobBack);
        assertThat(similarityDocumentComparison.getJob()).isEqualTo(similarityJobBack);

        similarityDocumentComparison.job(null);
        assertThat(similarityDocumentComparison.getJob()).isNull();
    }
}
