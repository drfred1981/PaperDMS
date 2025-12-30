package fr.smartprod.paperdms.ai.domain;

import static fr.smartprod.paperdms.ai.domain.AICacheTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AICacheTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AICache.class);
        AICache aICache1 = getAICacheSample1();
        AICache aICache2 = new AICache();
        assertThat(aICache1).isNotEqualTo(aICache2);

        aICache2.setId(aICache1.getId());
        assertThat(aICache1).isEqualTo(aICache2);

        aICache2 = getAICacheSample2();
        assertThat(aICache1).isNotEqualTo(aICache2);
    }
}
