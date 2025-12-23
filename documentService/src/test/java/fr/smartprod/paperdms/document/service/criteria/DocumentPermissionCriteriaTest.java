package fr.smartprod.paperdms.document.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DocumentPermissionCriteriaTest {

    @Test
    void newDocumentPermissionCriteriaHasAllFiltersNullTest() {
        var documentPermissionCriteria = new DocumentPermissionCriteria();
        assertThat(documentPermissionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void documentPermissionCriteriaFluentMethodsCreatesFiltersTest() {
        var documentPermissionCriteria = new DocumentPermissionCriteria();

        setAllFilters(documentPermissionCriteria);

        assertThat(documentPermissionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void documentPermissionCriteriaCopyCreatesNullFilterTest() {
        var documentPermissionCriteria = new DocumentPermissionCriteria();
        var copy = documentPermissionCriteria.copy();

        assertThat(documentPermissionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(documentPermissionCriteria)
        );
    }

    @Test
    void documentPermissionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var documentPermissionCriteria = new DocumentPermissionCriteria();
        setAllFilters(documentPermissionCriteria);

        var copy = documentPermissionCriteria.copy();

        assertThat(documentPermissionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(documentPermissionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var documentPermissionCriteria = new DocumentPermissionCriteria();

        assertThat(documentPermissionCriteria).hasToString("DocumentPermissionCriteria{}");
    }

    private static void setAllFilters(DocumentPermissionCriteria documentPermissionCriteria) {
        documentPermissionCriteria.id();
        documentPermissionCriteria.documentId();
        documentPermissionCriteria.principalType();
        documentPermissionCriteria.principalId();
        documentPermissionCriteria.permission();
        documentPermissionCriteria.canDelegate();
        documentPermissionCriteria.grantedBy();
        documentPermissionCriteria.grantedDate();
        documentPermissionCriteria.permissionGroupId();
        documentPermissionCriteria.distinct();
    }

    private static Condition<DocumentPermissionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentId()) &&
                condition.apply(criteria.getPrincipalType()) &&
                condition.apply(criteria.getPrincipalId()) &&
                condition.apply(criteria.getPermission()) &&
                condition.apply(criteria.getCanDelegate()) &&
                condition.apply(criteria.getGrantedBy()) &&
                condition.apply(criteria.getGrantedDate()) &&
                condition.apply(criteria.getPermissionGroupId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DocumentPermissionCriteria> copyFiltersAre(
        DocumentPermissionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentId(), copy.getDocumentId()) &&
                condition.apply(criteria.getPrincipalType(), copy.getPrincipalType()) &&
                condition.apply(criteria.getPrincipalId(), copy.getPrincipalId()) &&
                condition.apply(criteria.getPermission(), copy.getPermission()) &&
                condition.apply(criteria.getCanDelegate(), copy.getCanDelegate()) &&
                condition.apply(criteria.getGrantedBy(), copy.getGrantedBy()) &&
                condition.apply(criteria.getGrantedDate(), copy.getGrantedDate()) &&
                condition.apply(criteria.getPermissionGroupId(), copy.getPermissionGroupId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
