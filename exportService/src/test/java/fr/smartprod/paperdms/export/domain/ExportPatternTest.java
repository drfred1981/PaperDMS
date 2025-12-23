package fr.smartprod.paperdms.export.domain;

import static fr.smartprod.paperdms.export.domain.ExportPatternTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.export.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExportPatternTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExportPattern.class);
        ExportPattern exportPattern1 = getExportPatternSample1();
        ExportPattern exportPattern2 = new ExportPattern();
        assertThat(exportPattern1).isNotEqualTo(exportPattern2);

        exportPattern2.setId(exportPattern1.getId());
        assertThat(exportPattern1).isEqualTo(exportPattern2);

        exportPattern2 = getExportPatternSample2();
        assertThat(exportPattern1).isNotEqualTo(exportPattern2);
    }
}
