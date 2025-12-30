package fr.smartprod.paperdms.similarity.domain;

import static fr.smartprod.paperdms.similarity.domain.SimilarityDocumentComparisonTestSamples.*;
import static fr.smartprod.paperdms.similarity.domain.SimilarityJobTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.similarity.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SimilarityJobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SimilarityJob.class);
        SimilarityJob similarityJob1 = getSimilarityJobSample1();
        SimilarityJob similarityJob2 = new SimilarityJob();
        assertThat(similarityJob1).isNotEqualTo(similarityJob2);

        similarityJob2.setId(similarityJob1.getId());
        assertThat(similarityJob1).isEqualTo(similarityJob2);

        similarityJob2 = getSimilarityJobSample2();
        assertThat(similarityJob1).isNotEqualTo(similarityJob2);
    }

    @Test
    void similaritiesTest() {
        SimilarityJob similarityJob = getSimilarityJobRandomSampleGenerator();
        SimilarityDocumentComparison similarityDocumentComparisonBack = getSimilarityDocumentComparisonRandomSampleGenerator();

        similarityJob.addSimilarities(similarityDocumentComparisonBack);
        assertThat(similarityJob.getSimilarities()).containsOnly(similarityDocumentComparisonBack);
        assertThat(similarityDocumentComparisonBack.getJob()).isEqualTo(similarityJob);

        similarityJob.removeSimilarities(similarityDocumentComparisonBack);
        assertThat(similarityJob.getSimilarities()).doesNotContain(similarityDocumentComparisonBack);
        assertThat(similarityDocumentComparisonBack.getJob()).isNull();

        similarityJob.similarities(new HashSet<>(Set.of(similarityDocumentComparisonBack)));
        assertThat(similarityJob.getSimilarities()).containsOnly(similarityDocumentComparisonBack);
        assertThat(similarityDocumentComparisonBack.getJob()).isEqualTo(similarityJob);

        similarityJob.setSimilarities(new HashSet<>());
        assertThat(similarityJob.getSimilarities()).doesNotContain(similarityDocumentComparisonBack);
        assertThat(similarityDocumentComparisonBack.getJob()).isNull();
    }
}
