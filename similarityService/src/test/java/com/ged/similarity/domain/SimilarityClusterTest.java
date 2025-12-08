package com.ged.similarity.domain;

import static com.ged.similarity.domain.SimilarityClusterTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ged.similarity.web.rest.TestUtil;
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
