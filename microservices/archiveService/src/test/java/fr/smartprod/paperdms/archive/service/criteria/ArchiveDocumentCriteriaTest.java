package fr.smartprod.paperdms.archive.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ArchiveDocumentCriteriaTest {

    @Test
    void newArchiveDocumentCriteriaHasAllFiltersNullTest() {
        var archiveDocumentCriteria = new ArchiveDocumentCriteria();
        assertThat(archiveDocumentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void archiveDocumentCriteriaFluentMethodsCreatesFiltersTest() {
        var archiveDocumentCriteria = new ArchiveDocumentCriteria();

        setAllFilters(archiveDocumentCriteria);

        assertThat(archiveDocumentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void archiveDocumentCriteriaCopyCreatesNullFilterTest() {
        var archiveDocumentCriteria = new ArchiveDocumentCriteria();
        var copy = archiveDocumentCriteria.copy();

        assertThat(archiveDocumentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(archiveDocumentCriteria)
        );
    }

    @Test
    void archiveDocumentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var archiveDocumentCriteria = new ArchiveDocumentCriteria();
        setAllFilters(archiveDocumentCriteria);

        var copy = archiveDocumentCriteria.copy();

        assertThat(archiveDocumentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(archiveDocumentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var archiveDocumentCriteria = new ArchiveDocumentCriteria();

        assertThat(archiveDocumentCriteria).hasToString("ArchiveDocumentCriteria{}");
    }

    private static void setAllFilters(ArchiveDocumentCriteria archiveDocumentCriteria) {
        archiveDocumentCriteria.id();
        archiveDocumentCriteria.documentSha256();
        archiveDocumentCriteria.originalPath();
        archiveDocumentCriteria.archivePath();
        archiveDocumentCriteria.fileSize();
        archiveDocumentCriteria.addedDate();
        archiveDocumentCriteria.archiveJobId();
        archiveDocumentCriteria.distinct();
    }

    private static Condition<ArchiveDocumentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentSha256()) &&
                condition.apply(criteria.getOriginalPath()) &&
                condition.apply(criteria.getArchivePath()) &&
                condition.apply(criteria.getFileSize()) &&
                condition.apply(criteria.getAddedDate()) &&
                condition.apply(criteria.getArchiveJobId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ArchiveDocumentCriteria> copyFiltersAre(
        ArchiveDocumentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentSha256(), copy.getDocumentSha256()) &&
                condition.apply(criteria.getOriginalPath(), copy.getOriginalPath()) &&
                condition.apply(criteria.getArchivePath(), copy.getArchivePath()) &&
                condition.apply(criteria.getFileSize(), copy.getFileSize()) &&
                condition.apply(criteria.getAddedDate(), copy.getAddedDate()) &&
                condition.apply(criteria.getArchiveJobId(), copy.getArchiveJobId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
