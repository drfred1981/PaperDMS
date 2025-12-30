package fr.smartprod.paperdms.similarity.domain;

import static fr.smartprod.paperdms.similarity.domain.SimilarityClusterTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.similarity.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SimilarityClusterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SimilarityCluster.class);
        SimilarityCluster similarityCluster1 = getSimilarityClusterSample1();
        SimilarityCluster similarityCluster2 = new SimilarityCluster();
        assertThat(similarityCluster1).isNotEqualTo(similarityCluster2);

        similarityCluster2.setId(similarityCluster1.getId());
        assertThat(similarityCluster1).isEqualTo(similarityCluster2);

        similarityCluster2 = getSimilarityClusterSample2();
        assertThat(similarityCluster1).isNotEqualTo(similarityCluster2);
    }
}
