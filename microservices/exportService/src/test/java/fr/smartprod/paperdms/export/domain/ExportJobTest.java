package fr.smartprod.paperdms.export.domain;

import static fr.smartprod.paperdms.export.domain.ExportJobTestSamples.*;
import static fr.smartprod.paperdms.export.domain.ExportPatternTestSamples.*;
import static fr.smartprod.paperdms.export.domain.ExportResultTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.smartprod.paperdms.export.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ExportJobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExportJob.class);
        ExportJob exportJob1 = getExportJobSample1();
        ExportJob exportJob2 = new ExportJob();
        assertThat(exportJob1).isNotEqualTo(exportJob2);

        exportJob2.setId(exportJob1.getId());
        assertThat(exportJob1).isEqualTo(exportJob2);

        exportJob2 = getExportJobSample2();
        assertThat(exportJob1).isNotEqualTo(exportJob2);
    }

    @Test
    void exportResultsTest() {
        ExportJob exportJob = getExportJobRandomSampleGenerator();
        ExportResult exportResultBack = getExportResultRandomSampleGenerator();

        exportJob.addExportResults(exportResultBack);
        assertThat(exportJob.getExportResults()).containsOnly(exportResultBack);
        assertThat(exportResultBack.getExportJob()).isEqualTo(exportJob);

        exportJob.removeExportResults(exportResultBack);
        assertThat(exportJob.getExportResults()).doesNotContain(exportResultBack);
        assertThat(exportResultBack.getExportJob()).isNull();

        exportJob.exportResults(new HashSet<>(Set.of(exportResultBack)));
        assertThat(exportJob.getExportResults()).containsOnly(exportResultBack);
        assertThat(exportResultBack.getExportJob()).isEqualTo(exportJob);

        exportJob.setExportResults(new HashSet<>());
        assertThat(exportJob.getExportResults()).doesNotContain(exportResultBack);
        assertThat(exportResultBack.getExportJob()).isNull();
    }

    @Test
    void exportPatternTest() {
        ExportJob exportJob = getExportJobRandomSampleGenerator();
        ExportPattern exportPatternBack = getExportPatternRandomSampleGenerator();

        exportJob.setExportPattern(exportPatternBack);
        assertThat(exportJob.getExportPattern()).isEqualTo(exportPatternBack);

        exportJob.exportPattern(null);
        assertThat(exportJob.getExportPattern()).isNull();
    }
}
