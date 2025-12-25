package fr.smartprod.paperdms.business.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BankTransaction.
 */
@Entity
@Table(name = "bank_transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BankTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "statement_id", nullable = false)
    private Long statementId;

    @NotNull
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @NotNull
    @Size(max = 500)
    @Column(name = "description", length = 500, nullable = false)
    private String description;

    @Column(name = "debit_amount")
    private Double debitAmount;

    @Column(name = "credit_amount")
    private Double creditAmount;

    @NotNull
    @Column(name = "balance", nullable = false)
    private Double balance;

    @NotNull
    @Column(name = "is_reconciled", nullable = false)
    private Boolean isReconciled;

    @ManyToOne(optional = false)
    @NotNull
    private BankStatement statement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BankTransaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStatementId() {
        return this.statementId;
    }

    public BankTransaction statementId(Long statementId) {
        this.setStatementId(statementId);
        return this;
    }

    public void setStatementId(Long statementId) {
        this.statementId = statementId;
    }

    public LocalDate getTransactionDate() {
        return this.transactionDate;
    }

    public BankTransaction transactionDate(LocalDate transactionDate) {
        this.setTransactionDate(transactionDate);
        return this;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDescription() {
        return this.description;
    }

    public BankTransaction description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDebitAmount() {
        return this.debitAmount;
    }

    public BankTransaction debitAmount(Double debitAmount) {
        this.setDebitAmount(debitAmount);
        return this;
    }

    public void setDebitAmount(Double debitAmount) {
        this.debitAmount = debitAmount;
    }

    public Double getCreditAmount() {
        return this.creditAmount;
    }

    public BankTransaction creditAmount(Double creditAmount) {
        this.setCreditAmount(creditAmount);
        return this;
    }

    public void setCreditAmount(Double creditAmount) {
        this.creditAmount = creditAmount;
    }

    public Double getBalance() {
        return this.balance;
    }

    public BankTransaction balance(Double balance) {
        this.setBalance(balance);
        return this;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Boolean getIsReconciled() {
        return this.isReconciled;
    }

    public BankTransaction isReconciled(Boolean isReconciled) {
        this.setIsReconciled(isReconciled);
        return this;
    }

    public void setIsReconciled(Boolean isReconciled) {
        this.isReconciled = isReconciled;
    }

    public BankStatement getStatement() {
        return this.statement;
    }

    public void setStatement(BankStatement bankStatement) {
        this.statement = bankStatement;
    }

    public BankTransaction statement(BankStatement bankStatement) {
        this.setStatement(bankStatement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BankTransaction)) {
            return false;
        }
        return getId() != null && getId().equals(((BankTransaction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BankTransaction{" +
            "id=" + getId() +
            ", statementId=" + getStatementId() +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", debitAmount=" + getDebitAmount() +
            ", creditAmount=" + getCreditAmount() +
            ", balance=" + getBalance() +
            ", isReconciled='" + getIsReconciled() + "'" +
            "}";
    }
}
