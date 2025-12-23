package fr.smartprod.paperdms.ai.domain;

import static fr.smartprod.paperdms.ai.domain.AiCacheTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.ai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AiCacheTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AiCache.class);
        AiCache aiCache1 = getAiCacheSample1();
        AiCache aiCache2 = new AiCache();
        assertThat(aiCache1).isNotEqualTo(aiCache2);

        aiCache2.setId(aiCache1.getId());
        assertThat(aiCache1).isEqualTo(aiCache2);

        aiCache2 = getAiCacheSample2();
        assertThat(aiCache1).isNotEqualTo(aiCache2);
    }
}
