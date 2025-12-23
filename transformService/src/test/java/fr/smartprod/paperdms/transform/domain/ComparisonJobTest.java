package fr.smartprod.paperdms.transform.domain;

import static fr.smartprod.paperdms.transform.domain.ComparisonJobTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.transform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ComparisonJobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComparisonJob.class);
        ComparisonJob comparisonJob1 = getComparisonJobSample1();
        ComparisonJob comparisonJob2 = new ComparisonJob();
        assertThat(comparisonJob1).isNotEqualTo(comparisonJob2);

        comparisonJob2.setId(comparisonJob1.getId());
        assertThat(comparisonJob1).isEqualTo(comparisonJob2);

        comparisonJob2 = getComparisonJobSample2();
        assertThat(comparisonJob1).isNotEqualTo(comparisonJob2);
    }
}
