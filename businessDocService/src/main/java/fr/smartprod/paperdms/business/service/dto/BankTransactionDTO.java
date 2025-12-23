package fr.smartprod.paperdms.business.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.business.domain.BankTransaction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BankTransactionDTO implements Serializable {

    private Long id;

    @NotNull
    private Long statementId;

    @NotNull
    private LocalDate transactionDate;

    @NotNull
    @Size(max = 500)
    private String description;

    private Double debitAmount;

    private Double creditAmount;

    @NotNull
    private Double balance;

    @NotNull
    private Boolean isReconciled;

    @NotNull
    private BankStatementDTO statement;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStatementId() {
        return statementId;
    }

    public void setStatementId(Long statementId) {
        this.statementId = statementId;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(Double debitAmount) {
        this.debitAmount = debitAmount;
    }

    public Double getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(Double creditAmount) {
        this.creditAmount = creditAmount;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Boolean getIsReconciled() {
        return isReconciled;
    }

    public void setIsReconciled(Boolean isReconciled) {
        this.isReconciled = isReconciled;
    }

    public BankStatementDTO getStatement() {
        return statement;
    }

    public void setStatement(BankStatementDTO statement) {
        this.statement = statement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BankTransactionDTO)) {
            return false;
        }

        BankTransactionDTO bankTransactionDTO = (BankTransactionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bankTransactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BankTransactionDTO{" +
            "id=" + getId() +
            ", statementId=" + getStatementId() +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", debitAmount=" + getDebitAmount() +
            ", creditAmount=" + getCreditAmount() +
            ", balance=" + getBalance() +
            ", isReconciled='" + getIsReconciled() + "'" +
            ", statement=" + getStatement() +
            "}";
    }
}
