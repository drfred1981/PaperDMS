package fr.smartprod.paperdms.export.domain;

import static fr.smartprod.paperdms.export.domain.ExportJobTestSamples.*;
import static fr.smartprod.paperdms.export.domain.ExportPatternTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.export.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
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

    @Test
    void exportJobsTest() {
        ExportPattern exportPattern = getExportPatternRandomSampleGenerator();
        ExportJob exportJobBack = getExportJobRandomSampleGenerator();

        exportPattern.addExportJobs(exportJobBack);
        assertThat(exportPattern.getExportJobs()).containsOnly(exportJobBack);
        assertThat(exportJobBack.getExportPattern()).isEqualTo(exportPattern);

        exportPattern.removeExportJobs(exportJobBack);
        assertThat(exportPattern.getExportJobs()).doesNotContain(exportJobBack);
        assertThat(exportJobBack.getExportPattern()).isNull();

        exportPattern.exportJobs(new HashSet<>(Set.of(exportJobBack)));
        assertThat(exportPattern.getExportJobs()).containsOnly(exportJobBack);
        assertThat(exportJobBack.getExportPattern()).isEqualTo(exportPattern);

        exportPattern.setExportJobs(new HashSet<>());
        assertThat(exportPattern.getExportJobs()).doesNotContain(exportJobBack);
        assertThat(exportJobBack.getExportPattern()).isNull();
    }
}
