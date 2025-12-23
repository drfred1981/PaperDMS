package fr.smartprod.paperdms.business.service.criteria;

import fr.smartprod.paperdms.business.domain.enumeration.InvoiceStatus;
import fr.smartprod.paperdms.business.domain.enumeration.InvoiceType;
import fr.smartprod.paperdms.business.domain.enumeration.PaymentMethod;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.business.domain.Invoice} entity. This class is used
 * in {@link fr.smartprod.paperdms.business.web.rest.InvoiceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /invoices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceCriteria implements Serializable, Criteria {

    /**
     * Class for filtering InvoiceType
     */
    public static class InvoiceTypeFilter extends Filter<InvoiceType> {

        public InvoiceTypeFilter() {}

        public InvoiceTypeFilter(InvoiceTypeFilter filter) {
            super(filter);
        }

        @Override
        public InvoiceTypeFilter copy() {
            return new InvoiceTypeFilter(this);
        }
    }

    /**
     * Class for filtering InvoiceStatus
     */
    public static class InvoiceStatusFilter extends Filter<InvoiceStatus> {

        public InvoiceStatusFilter() {}

        public InvoiceStatusFilter(InvoiceStatusFilter filter) {
            super(filter);
        }

        @Override
        public InvoiceStatusFilter copy() {
            return new InvoiceStatusFilter(this);
        }
    }

    /**
     * Class for filtering PaymentMethod
     */
    public static class PaymentMethodFilter extends Filter<PaymentMethod> {

        public PaymentMethodFilter() {}

        public PaymentMethodFilter(PaymentMethodFilter filter) {
            super(filter);
        }

        @Override
        public PaymentMethodFilter copy() {
            return new PaymentMethodFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter documentId;

    private StringFilter invoiceNumber;

    private InvoiceTypeFilter invoiceType;

    private StringFilter supplierName;

    private StringFilter customerName;

    private LocalDateFilter issueDate;

    private LocalDateFilter dueDate;

    private LocalDateFilter paymentDate;

    private DoubleFilter totalAmountExclTax;

    private DoubleFilter taxAmount;

    private DoubleFilter totalAmountInclTax;

    private StringFilter currency;

    private InvoiceStatusFilter status;

    private PaymentMethodFilter paymentMethod;

    private InstantFilter createdDate;

    private Boolean distinct;

    public InvoiceCriteria() {}

    public InvoiceCriteria(InvoiceCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentId = other.optionalDocumentId().map(LongFilter::copy).orElse(null);
        this.invoiceNumber = other.optionalInvoiceNumber().map(StringFilter::copy).orElse(null);
        this.invoiceType = other.optionalInvoiceType().map(InvoiceTypeFilter::copy).orElse(null);
        this.supplierName = other.optionalSupplierName().map(StringFilter::copy).orElse(null);
        this.customerName = other.optionalCustomerName().map(StringFilter::copy).orElse(null);
        this.issueDate = other.optionalIssueDate().map(LocalDateFilter::copy).orElse(null);
        this.dueDate = other.optionalDueDate().map(LocalDateFilter::copy).orElse(null);
        this.paymentDate = other.optionalPaymentDate().map(LocalDateFilter::copy).orElse(null);
        this.totalAmountExclTax = other.optionalTotalAmountExclTax().map(DoubleFilter::copy).orElse(null);
        this.taxAmount = other.optionalTaxAmount().map(DoubleFilter::copy).orElse(null);
        this.totalAmountInclTax = other.optionalTotalAmountInclTax().map(DoubleFilter::copy).orElse(null);
        this.currency = other.optionalCurrency().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(InvoiceStatusFilter::copy).orElse(null);
        this.paymentMethod = other.optionalPaymentMethod().map(PaymentMethodFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InvoiceCriteria copy() {
        return new InvoiceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getDocumentId() {
        return documentId;
    }

    public Optional<LongFilter> optionalDocumentId() {
        return Optional.ofNullable(documentId);
    }

    public LongFilter documentId() {
        if (documentId == null) {
            setDocumentId(new LongFilter());
        }
        return documentId;
    }

    public void setDocumentId(LongFilter documentId) {
        this.documentId = documentId;
    }

    public StringFilter getInvoiceNumber() {
        return invoiceNumber;
    }

    public Optional<StringFilter> optionalInvoiceNumber() {
        return Optional.ofNullable(invoiceNumber);
    }

    public StringFilter invoiceNumber() {
        if (invoiceNumber == null) {
            setInvoiceNumber(new StringFilter());
        }
        return invoiceNumber;
    }

    public void setInvoiceNumber(StringFilter invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public InvoiceTypeFilter getInvoiceType() {
        return invoiceType;
    }

    public Optional<InvoiceTypeFilter> optionalInvoiceType() {
        return Optional.ofNullable(invoiceType);
    }

    public InvoiceTypeFilter invoiceType() {
        if (invoiceType == null) {
            setInvoiceType(new InvoiceTypeFilter());
        }
        return invoiceType;
    }

    public void setInvoiceType(InvoiceTypeFilter invoiceType) {
        this.invoiceType = invoiceType;
    }

    public StringFilter getSupplierName() {
        return supplierName;
    }

    public Optional<StringFilter> optionalSupplierName() {
        return Optional.ofNullable(supplierName);
    }

    public StringFilter supplierName() {
        if (supplierName == null) {
            setSupplierName(new StringFilter());
        }
        return supplierName;
    }

    public void setSupplierName(StringFilter supplierName) {
        this.supplierName = supplierName;
    }

    public StringFilter getCustomerName() {
        return customerName;
    }

    public Optional<StringFilter> optionalCustomerName() {
        return Optional.ofNullable(customerName);
    }

    public StringFilter customerName() {
        if (customerName == null) {
            setCustomerName(new StringFilter());
        }
        return customerName;
    }

    public void setCustomerName(StringFilter customerName) {
        this.customerName = customerName;
    }

    public LocalDateFilter getIssueDate() {
        return issueDate;
    }

    public Optional<LocalDateFilter> optionalIssueDate() {
        return Optional.ofNullable(issueDate);
    }

    public LocalDateFilter issueDate() {
        if (issueDate == null) {
            setIssueDate(new LocalDateFilter());
        }
        return issueDate;
    }

    public void setIssueDate(LocalDateFilter issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDateFilter getDueDate() {
        return dueDate;
    }

    public Optional<LocalDateFilter> optionalDueDate() {
        return Optional.ofNullable(dueDate);
    }

    public LocalDateFilter dueDate() {
        if (dueDate == null) {
            setDueDate(new LocalDateFilter());
        }
        return dueDate;
    }

    public void setDueDate(LocalDateFilter dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateFilter getPaymentDate() {
        return paymentDate;
    }

    public Optional<LocalDateFilter> optionalPaymentDate() {
        return Optional.ofNullable(paymentDate);
    }

    public LocalDateFilter paymentDate() {
        if (paymentDate == null) {
            setPaymentDate(new LocalDateFilter());
        }
        return paymentDate;
    }

    public void setPaymentDate(LocalDateFilter paymentDate) {
        this.paymentDate = paymentDate;
    }

    public DoubleFilter getTotalAmountExclTax() {
        return totalAmountExclTax;
    }

    public Optional<DoubleFilter> optionalTotalAmountExclTax() {
        return Optional.ofNullable(totalAmountExclTax);
    }

    public DoubleFilter totalAmountExclTax() {
        if (totalAmountExclTax == null) {
            setTotalAmountExclTax(new DoubleFilter());
        }
        return totalAmountExclTax;
    }

    public void setTotalAmountExclTax(DoubleFilter totalAmountExclTax) {
        this.totalAmountExclTax = totalAmountExclTax;
    }

    public DoubleFilter getTaxAmount() {
        return taxAmount;
    }

    public Optional<DoubleFilter> optionalTaxAmount() {
        return Optional.ofNullable(taxAmount);
    }

    public DoubleFilter taxAmount() {
        if (taxAmount == null) {
            setTaxAmount(new DoubleFilter());
        }
        return taxAmount;
    }

    public void setTaxAmount(DoubleFilter taxAmount) {
        this.taxAmount = taxAmount;
    }

    public DoubleFilter getTotalAmountInclTax() {
        return totalAmountInclTax;
    }

    public Optional<DoubleFilter> optionalTotalAmountInclTax() {
        return Optional.ofNullable(totalAmountInclTax);
    }

    public DoubleFilter totalAmountInclTax() {
        if (totalAmountInclTax == null) {
            setTotalAmountInclTax(new DoubleFilter());
        }
        return totalAmountInclTax;
    }

    public void setTotalAmountInclTax(DoubleFilter totalAmountInclTax) {
        this.totalAmountInclTax = totalAmountInclTax;
    }

    public StringFilter getCurrency() {
        return currency;
    }

    public Optional<StringFilter> optionalCurrency() {
        return Optional.ofNullable(currency);
    }

    public StringFilter currency() {
        if (currency == null) {
            setCurrency(new StringFilter());
        }
        return currency;
    }

    public void setCurrency(StringFilter currency) {
        this.currency = currency;
    }

    public InvoiceStatusFilter getStatus() {
        return status;
    }

    public Optional<InvoiceStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public InvoiceStatusFilter status() {
        if (status == null) {
            setStatus(new InvoiceStatusFilter());
        }
        return status;
    }

    public void setStatus(InvoiceStatusFilter status) {
        this.status = status;
    }

    public PaymentMethodFilter getPaymentMethod() {
        return paymentMethod;
    }

    public Optional<PaymentMethodFilter> optionalPaymentMethod() {
        return Optional.ofNullable(paymentMethod);
    }

    public PaymentMethodFilter paymentMethod() {
        if (paymentMethod == null) {
            setPaymentMethod(new PaymentMethodFilter());
        }
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethodFilter paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<InstantFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new InstantFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final InvoiceCriteria that = (InvoiceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(invoiceNumber, that.invoiceNumber) &&
            Objects.equals(invoiceType, that.invoiceType) &&
            Objects.equals(supplierName, that.supplierName) &&
            Objects.equals(customerName, that.customerName) &&
            Objects.equals(issueDate, that.issueDate) &&
            Objects.equals(dueDate, that.dueDate) &&
            Objects.equals(paymentDate, that.paymentDate) &&
            Objects.equals(totalAmountExclTax, that.totalAmountExclTax) &&
            Objects.equals(taxAmount, that.taxAmount) &&
            Objects.equals(totalAmountInclTax, that.totalAmountInclTax) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(status, that.status) &&
            Objects.equals(paymentMethod, that.paymentMethod) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            documentId,
            invoiceNumber,
            invoiceType,
            supplierName,
            customerName,
            issueDate,
            dueDate,
            paymentDate,
            totalAmountExclTax,
            taxAmount,
            totalAmountInclTax,
            currency,
            status,
            paymentMethod,
            createdDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentId().map(f -> "documentId=" + f + ", ").orElse("") +
            optionalInvoiceNumber().map(f -> "invoiceNumber=" + f + ", ").orElse("") +
            optionalInvoiceType().map(f -> "invoiceType=" + f + ", ").orElse("") +
            optionalSupplierName().map(f -> "supplierName=" + f + ", ").orElse("") +
            optionalCustomerName().map(f -> "customerName=" + f + ", ").orElse("") +
            optionalIssueDate().map(f -> "issueDate=" + f + ", ").orElse("") +
            optionalDueDate().map(f -> "dueDate=" + f + ", ").orElse("") +
            optionalPaymentDate().map(f -> "paymentDate=" + f + ", ").orElse("") +
            optionalTotalAmountExclTax().map(f -> "totalAmountExclTax=" + f + ", ").orElse("") +
            optionalTaxAmount().map(f -> "taxAmount=" + f + ", ").orElse("") +
            optionalTotalAmountInclTax().map(f -> "totalAmountInclTax=" + f + ", ").orElse("") +
            optionalCurrency().map(f -> "currency=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalPaymentMethod().map(f -> "paymentMethod=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
