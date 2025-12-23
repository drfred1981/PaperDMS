package fr.smartprod.paperdms.archive.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ArchiveJobCriteriaTest {

    @Test
    void newArchiveJobCriteriaHasAllFiltersNullTest() {
        var archiveJobCriteria = new ArchiveJobCriteria();
        assertThat(archiveJobCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void archiveJobCriteriaFluentMethodsCreatesFiltersTest() {
        var archiveJobCriteria = new ArchiveJobCriteria();

        setAllFilters(archiveJobCriteria);

        assertThat(archiveJobCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void archiveJobCriteriaCopyCreatesNullFilterTest() {
        var archiveJobCriteria = new ArchiveJobCriteria();
        var copy = archiveJobCriteria.copy();

        assertThat(archiveJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(archiveJobCriteria)
        );
    }

    @Test
    void archiveJobCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var archiveJobCriteria = new ArchiveJobCriteria();
        setAllFilters(archiveJobCriteria);

        var copy = archiveJobCriteria.copy();

        assertThat(archiveJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(archiveJobCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var archiveJobCriteria = new ArchiveJobCriteria();

        assertThat(archiveJobCriteria).hasToString("ArchiveJobCriteria{}");
    }

    private static void setAllFilters(ArchiveJobCriteria archiveJobCriteria) {
        archiveJobCriteria.id();
        archiveJobCriteria.name();
        archiveJobCriteria.archiveFormat();
        archiveJobCriteria.compressionLevel();
        archiveJobCriteria.encryptionEnabled();
        archiveJobCriteria.encryptionAlgorithm();
        archiveJobCriteria.password();
        archiveJobCriteria.s3ArchiveKey();
        archiveJobCriteria.archiveSha256();
        archiveJobCriteria.archiveSize();
        archiveJobCriteria.documentCount();
        archiveJobCriteria.status();
        archiveJobCriteria.startDate();
        archiveJobCriteria.endDate();
        archiveJobCriteria.createdBy();
        archiveJobCriteria.createdDate();
        archiveJobCriteria.distinct();
    }

    private static Condition<ArchiveJobCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getArchiveFormat()) &&
                condition.apply(criteria.getCompressionLevel()) &&
                condition.apply(criteria.getEncryptionEnabled()) &&
                condition.apply(criteria.getEncryptionAlgorithm()) &&
                condition.apply(criteria.getPassword()) &&
                condition.apply(criteria.gets3ArchiveKey()) &&
                condition.apply(criteria.getArchiveSha256()) &&
                condition.apply(criteria.getArchiveSize()) &&
                condition.apply(criteria.getDocumentCount()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ArchiveJobCriteria> copyFiltersAre(ArchiveJobCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getArchiveFormat(), copy.getArchiveFormat()) &&
                condition.apply(criteria.getCompressionLevel(), copy.getCompressionLevel()) &&
                condition.apply(criteria.getEncryptionEnabled(), copy.getEncryptionEnabled()) &&
                condition.apply(criteria.getEncryptionAlgorithm(), copy.getEncryptionAlgorithm()) &&
                condition.apply(criteria.getPassword(), copy.getPassword()) &&
                condition.apply(criteria.gets3ArchiveKey(), copy.gets3ArchiveKey()) &&
                condition.apply(criteria.getArchiveSha256(), copy.getArchiveSha256()) &&
                condition.apply(criteria.getArchiveSize(), copy.getArchiveSize()) &&
                condition.apply(criteria.getDocumentCount(), copy.getDocumentCount()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
