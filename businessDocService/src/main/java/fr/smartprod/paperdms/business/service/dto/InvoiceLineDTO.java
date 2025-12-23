package fr.smartprod.paperdms.business.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.business.domain.InvoiceLine} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceLineDTO implements Serializable {

    private Long id;

    @NotNull
    private Long invoiceId;

    @NotNull
    private Integer lineNumber;

    @NotNull
    @Size(max = 500)
    private String description;

    @NotNull
    private Double quantity;

    @NotNull
    private Double unitPrice;

    @NotNull
    private Double taxRate;

    @NotNull
    private Double totalAmountExclTax;

    @NotNull
    private InvoiceDTO invoice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public Double getTotalAmountExclTax() {
        return totalAmountExclTax;
    }

    public void setTotalAmountExclTax(Double totalAmountExclTax) {
        this.totalAmountExclTax = totalAmountExclTax;
    }

    public InvoiceDTO getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceDTO invoice) {
        this.invoice = invoice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceLineDTO)) {
            return false;
        }

        InvoiceLineDTO invoiceLineDTO = (InvoiceLineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, invoiceLineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceLineDTO{" +
            "id=" + getId() +
            ", invoiceId=" + getInvoiceId() +
            ", lineNumber=" + getLineNumber() +
            ", description='" + getDescription() + "'" +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", taxRate=" + getTaxRate() +
            ", totalAmountExclTax=" + getTotalAmountExclTax() +
            ", invoice=" + getInvoice() +
            "}";
    }
}
