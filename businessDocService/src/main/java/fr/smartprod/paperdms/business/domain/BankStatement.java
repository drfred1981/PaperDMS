package fr.smartprod.paperdms.business.domain;

import fr.smartprod.paperdms.business.domain.enumeration.StatementStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BankStatement.
 */
@Entity
@Table(name = "bank_statement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BankStatement implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @NotNull
    @Size(max = 50)
    @Column(name = "account_number", length = 50, nullable = false)
    private String accountNumber;

    @NotNull
    @Size(max = 255)
    @Column(name = "bank_name", length = 255, nullable = false)
    private String bankName;

    @NotNull
    @Column(name = "statement_date", nullable = false)
    private LocalDate statementDate;

    @NotNull
    @Column(name = "statement_period_start", nullable = false)
    private LocalDate statementPeriodStart;

    @NotNull
    @Column(name = "statement_period_end", nullable = false)
    private LocalDate statementPeriodEnd;

    @NotNull
    @Column(name = "opening_balance", nullable = false)
    private Double openingBalance;

    @NotNull
    @Column(name = "closing_balance", nullable = false)
    private Double closingBalance;

    @NotNull
    @Size(max = 3)
    @Column(name = "currency", length = 3, nullable = false)
    private String currency;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatementStatus status;

    @NotNull
    @Column(name = "is_reconciled", nullable = false)
    private Boolean isReconciled;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BankStatement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return this.documentId;
    }

    public BankStatement documentId(Long documentId) {
        this.setDocumentId(documentId);
        return this;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public BankStatement accountNumber(String accountNumber) {
        this.setAccountNumber(accountNumber);
        return this;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankName() {
        return this.bankName;
    }

    public BankStatement bankName(String bankName) {
        this.setBankName(bankName);
        return this;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public LocalDate getStatementDate() {
        return this.statementDate;
    }

    public BankStatement statementDate(LocalDate statementDate) {
        this.setStatementDate(statementDate);
        return this;
    }

    public void setStatementDate(LocalDate statementDate) {
        this.statementDate = statementDate;
    }

    public LocalDate getStatementPeriodStart() {
        return this.statementPeriodStart;
    }

    public BankStatement statementPeriodStart(LocalDate statementPeriodStart) {
        this.setStatementPeriodStart(statementPeriodStart);
        return this;
    }

    public void setStatementPeriodStart(LocalDate statementPeriodStart) {
        this.statementPeriodStart = statementPeriodStart;
    }

    public LocalDate getStatementPeriodEnd() {
        return this.statementPeriodEnd;
    }

    public BankStatement statementPeriodEnd(LocalDate statementPeriodEnd) {
        this.setStatementPeriodEnd(statementPeriodEnd);
        return this;
    }

    public void setStatementPeriodEnd(LocalDate statementPeriodEnd) {
        this.statementPeriodEnd = statementPeriodEnd;
    }

    public Double getOpeningBalance() {
        return this.openingBalance;
    }

    public BankStatement openingBalance(Double openingBalance) {
        this.setOpeningBalance(openingBalance);
        return this;
    }

    public void setOpeningBalance(Double openingBalance) {
        this.openingBalance = openingBalance;
    }

    public Double getClosingBalance() {
        return this.closingBalance;
    }

    public BankStatement closingBalance(Double closingBalance) {
        this.setClosingBalance(closingBalance);
        return this;
    }

    public void setClosingBalance(Double closingBalance) {
        this.closingBalance = closingBalance;
    }

    public String getCurrency() {
        return this.currency;
    }

    public BankStatement currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public StatementStatus getStatus() {
        return this.status;
    }

    public BankStatement status(StatementStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(StatementStatus status) {
        this.status = status;
    }

    public Boolean getIsReconciled() {
        return this.isReconciled;
    }

    public BankStatement isReconciled(Boolean isReconciled) {
        this.setIsReconciled(isReconciled);
        return this;
    }

    public void setIsReconciled(Boolean isReconciled) {
        this.isReconciled = isReconciled;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public BankStatement createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BankStatement)) {
            return false;
        }
        return getId() != null && getId().equals(((BankStatement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BankStatement{" +
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
