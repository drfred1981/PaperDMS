package fr.smartprod.paperdms.business.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InvoiceCriteriaTest {

    @Test
    void newInvoiceCriteriaHasAllFiltersNullTest() {
        var invoiceCriteria = new InvoiceCriteria();
        assertThat(invoiceCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void invoiceCriteriaFluentMethodsCreatesFiltersTest() {
        var invoiceCriteria = new InvoiceCriteria();

        setAllFilters(invoiceCriteria);

        assertThat(invoiceCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void invoiceCriteriaCopyCreatesNullFilterTest() {
        var invoiceCriteria = new InvoiceCriteria();
        var copy = invoiceCriteria.copy();

        assertThat(invoiceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(invoiceCriteria)
        );
    }

    @Test
    void invoiceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var invoiceCriteria = new InvoiceCriteria();
        setAllFilters(invoiceCriteria);

        var copy = invoiceCriteria.copy();

        assertThat(invoiceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(invoiceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var invoiceCriteria = new InvoiceCriteria();

        assertThat(invoiceCriteria).hasToString("InvoiceCriteria{}");
    }

    private static void setAllFilters(InvoiceCriteria invoiceCriteria) {
        invoiceCriteria.id();
        invoiceCriteria.documentId();
        invoiceCriteria.invoiceNumber();
        invoiceCriteria.invoiceType();
        invoiceCriteria.supplierName();
        invoiceCriteria.customerName();
        invoiceCriteria.issueDate();
        invoiceCriteria.dueDate();
        invoiceCriteria.paymentDate();
        invoiceCriteria.totalAmountExclTax();
        invoiceCriteria.taxAmount();
        invoiceCriteria.totalAmountInclTax();
        invoiceCriteria.currency();
        invoiceCriteria.status();
        invoiceCriteria.paymentMethod();
        invoiceCriteria.createdDate();
        invoiceCriteria.distinct();
    }

    private static Condition<InvoiceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentId()) &&
                condition.apply(criteria.getInvoiceNumber()) &&
                condition.apply(criteria.getInvoiceType()) &&
                condition.apply(criteria.getSupplierName()) &&
                condition.apply(criteria.getCustomerName()) &&
                condition.apply(criteria.getIssueDate()) &&
                condition.apply(criteria.getDueDate()) &&
                condition.apply(criteria.getPaymentDate()) &&
                condition.apply(criteria.getTotalAmountExclTax()) &&
                condition.apply(criteria.getTaxAmount()) &&
                condition.apply(criteria.getTotalAmountInclTax()) &&
                condition.apply(criteria.getCurrency()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getPaymentMethod()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InvoiceCriteria> copyFiltersAre(InvoiceCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentId(), copy.getDocumentId()) &&
                condition.apply(criteria.getInvoiceNumber(), copy.getInvoiceNumber()) &&
                condition.apply(criteria.getInvoiceType(), copy.getInvoiceType()) &&
                condition.apply(criteria.getSupplierName(), copy.getSupplierName()) &&
                condition.apply(criteria.getCustomerName(), copy.getCustomerName()) &&
                condition.apply(criteria.getIssueDate(), copy.getIssueDate()) &&
                condition.apply(criteria.getDueDate(), copy.getDueDate()) &&
                condition.apply(criteria.getPaymentDate(), copy.getPaymentDate()) &&
                condition.apply(criteria.getTotalAmountExclTax(), copy.getTotalAmountExclTax()) &&
                condition.apply(criteria.getTaxAmount(), copy.getTaxAmount()) &&
                condition.apply(criteria.getTotalAmountInclTax(), copy.getTotalAmountInclTax()) &&
                condition.apply(criteria.getCurrency(), copy.getCurrency()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getPaymentMethod(), copy.getPaymentMethod()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
