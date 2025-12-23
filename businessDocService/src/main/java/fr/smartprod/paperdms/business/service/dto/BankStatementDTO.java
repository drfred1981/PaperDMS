package fr.smartprod.paperdms.business.service.dto;

import fr.smartprod.paperdms.business.domain.enumeration.StatementStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.business.domain.BankStatement} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BankStatementDTO implements Serializable {

    private Long id;

    @NotNull
    private Long documentId;

    @NotNull
    @Size(max = 50)
    private String accountNumber;

    @NotNull
    @Size(max = 255)
    private String bankName;

    @NotNull
    private LocalDate statementDate;

    @NotNull
    private LocalDate statementPeriodStart;

    @NotNull
    private LocalDate statementPeriodEnd;

    @NotNull
    private Double openingBalance;

    @NotNull
    private Double closingBalance;

    @NotNull
    @Size(max = 3)
    private String currency;

    @NotNull
    private StatementStatus status;

    @NotNull
    private Boolean isReconciled;

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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public LocalDate getStatementDate() {
        return statementDate;
    }

    public void setStatementDate(LocalDate statementDate) {
        this.statementDate = statementDate;
    }

    public LocalDate getStatementPeriodStart() {
        return statementPeriodStart;
    }

    public void setStatementPeriodStart(LocalDate statementPeriodStart) {
        this.statementPeriodStart = statementPeriodStart;
    }

    public LocalDate getStatementPeriodEnd() {
        return statementPeriodEnd;
    }

    public void setStatementPeriodEnd(LocalDate statementPeriodEnd) {
        this.statementPeriodEnd = statementPeriodEnd;
    }

    public Double getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(Double openingBalance) {
        this.openingBalance = openingBalance;
    }

    public Double getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(Double closingBalance) {
        this.closingBalance = closingBalance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public StatementStatus getStatus() {
        return status;
    }

    public void setStatus(StatementStatus status) {
        this.status = status;
    }

    public Boolean getIsReconciled() {
        return isReconciled;
    }

    public void setIsReconciled(Boolean isReconciled) {
        this.isReconciled = isReconciled;
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
        if (!(o instanceof BankStatementDTO)) {
            return false;
        }

        BankStatementDTO bankStatementDTO = (BankStatementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bankStatementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BankStatementDTO{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
            ", accountNumber='" + getAccountNumber() + "'" +
            ", bankName='" + getBankName() + "'" +
            ", statementDate='" + getStatementDate() + "'" +
            ", statementPeriodStart='" + getStatementPeriodStart() + "'" +
            ", statementPeriodEnd='" + getStatementPeriodEnd() + "'" +
            ", openingBalance=" + getOpeningBalance() +
            ", closingBalance=" + getClosingBalance() +
            ", currency='" + getCurrency() + "'" +
            ", status='" + getStatus() + "'" +
            ", isReconciled='" + getIsReconciled() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
