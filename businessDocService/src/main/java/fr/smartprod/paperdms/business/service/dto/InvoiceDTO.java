package fr.smartprod.paperdms.business.service.dto;

import fr.smartprod.paperdms.business.domain.enumeration.InvoiceStatus;
import fr.smartprod.paperdms.business.domain.enumeration.InvoiceType;
import fr.smartprod.paperdms.business.domain.enumeration.PaymentMethod;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.business.domain.Invoice} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceDTO implements Serializable {

    private Long id;

    @NotNull
    private Long documentId;

    @NotNull
    @Size(max = 100)
    private String invoiceNumber;

    @NotNull
    private InvoiceType invoiceType;

    @Size(max = 255)
    private String supplierName;

    @Size(max = 255)
    private String customerName;

    @NotNull
    private LocalDate issueDate;

    private LocalDate dueDate;

    private LocalDate paymentDate;

    @NotNull
    private Double totalAmountExclTax;

    @NotNull
    private Double taxAmount;

    @NotNull
    private Double totalAmountInclTax;

    @NotNull
    @Size(max = 3)
    private String currency;

    @NotNull
    private InvoiceStatus status;

    private PaymentMethod paymentMethod;

    @NotNull
    private Instant createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public InvoiceType getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(InvoiceType invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Double getTotalAmountExclTax() {
        return totalAmountExclTax;
    }

    public void setTotalAmountExclTax(Double totalAmountExclTax) {
        this.totalAmountExclTax = totalAmountExclTax;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Double getTotalAmountInclTax() {
        return totalAmountInclTax;
    }

    public void setTotalAmountInclTax(Double totalAmountInclTax) {
        this.totalAmountInclTax = totalAmountInclTax;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceDTO)) {
            return false;
        }

        InvoiceDTO invoiceDTO = (InvoiceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, invoiceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceDTO{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
            ", invoiceNumber='" + getInvoiceNumber() + "'" +
            ", invoiceType='" + getInvoiceType() + "'" +
            ", supplierName='" + getSupplierName() + "'" +
            ", customerName='" + getCustomerName() + "'" +
            ", issueDate='" + getIssueDate() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", totalAmountExclTax=" + getTotalAmountExclTax() +
            ", taxAmount=" + getTaxAmount() +
            ", totalAmountInclTax=" + getTotalAmountInclTax() +
            ", currency='" + getCurrency() + "'" +
            ", status='" + getStatus() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
