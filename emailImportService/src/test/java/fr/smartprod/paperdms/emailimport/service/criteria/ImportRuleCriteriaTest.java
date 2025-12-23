package fr.smartprod.paperdms.emailimport.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ImportRuleCriteriaTest {

    @Test
    void newImportRuleCriteriaHasAllFiltersNullTest() {
        var importRuleCriteria = new ImportRuleCriteria();
        assertThat(importRuleCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void importRuleCriteriaFluentMethodsCreatesFiltersTest() {
        var importRuleCriteria = new ImportRuleCriteria();

        setAllFilters(importRuleCriteria);

        assertThat(importRuleCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void importRuleCriteriaCopyCreatesNullFilterTest() {
        var importRuleCriteria = new ImportRuleCriteria();
        var copy = importRuleCriteria.copy();

        assertThat(importRuleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(importRuleCriteria)
        );
    }

    @Test
    void importRuleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var importRuleCriteria = new ImportRuleCriteria();
        setAllFilters(importRuleCriteria);

        var copy = importRuleCriteria.copy();

        assertThat(importRuleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(importRuleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var importRuleCriteria = new ImportRuleCriteria();

        assertThat(importRuleCriteria).hasToString("ImportRuleCriteria{}");
    }

    private static void setAllFilters(ImportRuleCriteria importRuleCriteria) {
        importRuleCriteria.id();
        importRuleCriteria.name();
        importRuleCriteria.priority();
        importRuleCriteria.isActive();
        importRuleCriteria.folderId();
        importRuleCriteria.documentTypeId();
        importRuleCriteria.matchCount();
        importRuleCriteria.lastMatchDate();
        importRuleCriteria.createdBy();
        importRuleCriteria.createdDate();
        importRuleCriteria.lastModifiedDate();
        importRuleCriteria.distinct();
    }

    private static Condition<ImportRuleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getPriority()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getFolderId()) &&
                condition.apply(criteria.getDocumentTypeId()) &&
                condition.apply(criteria.getMatchCount()) &&
                condition.apply(criteria.getLastMatchDate()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ImportRuleCriteria> copyFiltersAre(ImportRuleCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getPriority(), copy.getPriority()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getFolderId(), copy.getFolderId()) &&
                condition.apply(criteria.getDocumentTypeId(), copy.getDocumentTypeId()) &&
                condition.apply(criteria.getMatchCount(), copy.getMatchCount()) &&
                condition.apply(criteria.getLastMatchDate(), copy.getLastMatchDate()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
