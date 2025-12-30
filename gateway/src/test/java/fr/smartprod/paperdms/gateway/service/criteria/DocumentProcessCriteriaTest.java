package fr.smartprod.paperdms.gateway.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DocumentProcessCriteriaTest {

    @Test
    void newDocumentProcessCriteriaHasAllFiltersNullTest() {
        var documentProcessCriteria = new DocumentProcessCriteria();
        assertThat(documentProcessCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void documentProcessCriteriaFluentMethodsCreatesFiltersTest() {
        var documentProcessCriteria = new DocumentProcessCriteria();

        setAllFilters(documentProcessCriteria);

        assertThat(documentProcessCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void documentProcessCriteriaCopyCreatesNullFilterTest() {
        var documentProcessCriteria = new DocumentProcessCriteria();
        var copy = documentProcessCriteria.copy();

        assertThat(documentProcessCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(documentProcessCriteria)
        );
    }

    @Test
    void documentProcessCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var documentProcessCriteria = new DocumentProcessCriteria();
        setAllFilters(documentProcessCriteria);

        var copy = documentProcessCriteria.copy();

        assertThat(documentProcessCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(documentProcessCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var documentProcessCriteria = new DocumentProcessCriteria();

        assertThat(documentProcessCriteria).hasToString("DocumentProcessCriteria{}");
    }

    private static void setAllFilters(DocumentProcessCriteria documentProcessCriteria) {
        documentProcessCriteria.id();
        documentProcessCriteria.status();
        documentProcessCriteria.documentSha256();
        documentProcessCriteria.distinct();
    }

    private static Condition<DocumentProcessCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getDocumentSha256()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DocumentProcessCriteria> copyFiltersAre(
        DocumentProcessCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getDocumentSha256(), copy.getDocumentSha256()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
