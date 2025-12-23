package fr.smartprod.paperdms.business.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ManualCriteriaTest {

    @Test
    void newManualCriteriaHasAllFiltersNullTest() {
        var manualCriteria = new ManualCriteria();
        assertThat(manualCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void manualCriteriaFluentMethodsCreatesFiltersTest() {
        var manualCriteria = new ManualCriteria();

        setAllFilters(manualCriteria);

        assertThat(manualCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void manualCriteriaCopyCreatesNullFilterTest() {
        var manualCriteria = new ManualCriteria();
        var copy = manualCriteria.copy();

        assertThat(manualCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(manualCriteria)
        );
    }

    @Test
    void manualCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var manualCriteria = new ManualCriteria();
        setAllFilters(manualCriteria);

        var copy = manualCriteria.copy();

        assertThat(manualCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(manualCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var manualCriteria = new ManualCriteria();

        assertThat(manualCriteria).hasToString("ManualCriteria{}");
    }

    private static void setAllFilters(ManualCriteria manualCriteria) {
        manualCriteria.id();
        manualCriteria.documentId();
        manualCriteria.title();
        manualCriteria.manualType();
        manualCriteria.version();
        manualCriteria.language();
        manualCriteria.publicationDate();
        manualCriteria.pageCount();
        manualCriteria.status();
        manualCriteria.isPublic();
        manualCriteria.createdDate();
        manualCriteria.distinct();
    }

    private static Condition<ManualCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentId()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getManualType()) &&
                condition.apply(criteria.getVersion()) &&
                condition.apply(criteria.getLanguage()) &&
                condition.apply(criteria.getPublicationDate()) &&
                condition.apply(criteria.getPageCount()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getIsPublic()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ManualCriteria> copyFiltersAre(ManualCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentId(), copy.getDocumentId()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getManualType(), copy.getManualType()) &&
                condition.apply(criteria.getVersion(), copy.getVersion()) &&
                condition.apply(criteria.getLanguage(), copy.getLanguage()) &&
                condition.apply(criteria.getPublicationDate(), copy.getPublicationDate()) &&
                condition.apply(criteria.getPageCount(), copy.getPageCount()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getIsPublic(), copy.getIsPublic()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
