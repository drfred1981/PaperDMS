package fr.smartprod.paperdms.business.service.criteria;

import fr.smartprod.paperdms.business.domain.enumeration.StatementStatus;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.business.domain.BankStatement} entity. This class is used
 * in {@link fr.smartprod.paperdms.business.web.rest.BankStatementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bank-statements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BankStatementCriteria implements Serializable, Criteria {

    /**
     * Class for filtering StatementStatus
     */
    public static class StatementStatusFilter extends Filter<StatementStatus> {

        public StatementStatusFilter() {}

        public StatementStatusFilter(StatementStatusFilter filter) {
            super(filter);
        }

        @Override
        public StatementStatusFilter copy() {
            return new StatementStatusFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter documentId;

    private StringFilter accountNumber;

    private StringFilter bankName;

    private LocalDateFilter statementDate;

    private LocalDateFilter statementPeriodStart;

    private LocalDateFilter statementPeriodEnd;

    private DoubleFilter openingBalance;

    private DoubleFilter closingBalance;

    private StringFilter currency;

    private StatementStatusFilter status;

    private BooleanFilter isReconciled;

    private InstantFilter createdDate;

    private Boolean distinct;

    public BankStatementCriteria() {}

    public BankStatementCriteria(BankStatementCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentId = other.optionalDocumentId().map(LongFilter::copy).orElse(null);
        this.accountNumber = other.optionalAccountNumber().map(StringFilter::copy).orElse(null);
        this.bankName = other.optionalBankName().map(StringFilter::copy).orElse(null);
        this.statementDate = other.optionalStatementDate().map(LocalDateFilter::copy).orElse(null);
        this.statementPeriodStart = other.optionalStatementPeriodStart().map(LocalDateFilter::copy).orElse(null);
        this.statementPeriodEnd = other.optionalStatementPeriodEnd().map(LocalDateFilter::copy).orElse(null);
        this.openingBalance = other.optionalOpeningBalance().map(DoubleFilter::copy).orElse(null);
        this.closingBalance = other.optionalClosingBalance().map(DoubleFilter::copy).orElse(null);
        this.currency = other.optionalCurrency().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(StatementStatusFilter::copy).orElse(null);
        this.isReconciled = other.optionalIsReconciled().map(BooleanFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public BankStatementCriteria copy() {
        return new BankStatementCriteria(this);
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

    public StringFilter getAccountNumber() {
        return accountNumber;
    }

    public Optional<StringFilter> optionalAccountNumber() {
        return Optional.ofNullable(accountNumber);
    }

    public StringFilter accountNumber() {
        if (accountNumber == null) {
            setAccountNumber(new StringFilter());
        }
        return accountNumber;
    }

    public void setAccountNumber(StringFilter accountNumber) {
        this.accountNumber = accountNumber;
    }

    public StringFilter getBankName() {
        return bankName;
    }

    public Optional<StringFilter> optionalBankName() {
        return Optional.ofNullable(bankName);
    }

    public StringFilter bankName() {
        if (bankName == null) {
            setBankName(new StringFilter());
        }
        return bankName;
    }

    public void setBankName(StringFilter bankName) {
        this.bankName = bankName;
    }

    public LocalDateFilter getStatementDate() {
        return statementDate;
    }

    public Optional<LocalDateFilter> optionalStatementDate() {
        return Optional.ofNullable(statementDate);
    }

    public LocalDateFilter statementDate() {
        if (statementDate == null) {
            setStatementDate(new LocalDateFilter());
        }
        return statementDate;
    }

    public void setStatementDate(LocalDateFilter statementDate) {
        this.statementDate = statementDate;
    }

    public LocalDateFilter getStatementPeriodStart() {
        return statementPeriodStart;
    }

    public Optional<LocalDateFilter> optionalStatementPeriodStart() {
        return Optional.ofNullable(statementPeriodStart);
    }

    public LocalDateFilter statementPeriodStart() {
        if (statementPeriodStart == null) {
            setStatementPeriodStart(new LocalDateFilter());
        }
        return statementPeriodStart;
    }

    public void setStatementPeriodStart(LocalDateFilter statementPeriodStart) {
        this.statementPeriodStart = statementPeriodStart;
    }

    public LocalDateFilter getStatementPeriodEnd() {
        return statementPeriodEnd;
    }

    public Optional<LocalDateFilter> optionalStatementPeriodEnd() {
        return Optional.ofNullable(statementPeriodEnd);
    }

    public LocalDateFilter statementPeriodEnd() {
        if (statementPeriodEnd == null) {
            setStatementPeriodEnd(new LocalDateFilter());
        }
        return statementPeriodEnd;
    }

    public void setStatementPeriodEnd(LocalDateFilter statementPeriodEnd) {
        this.statementPeriodEnd = statementPeriodEnd;
    }

    public DoubleFilter getOpeningBalance() {
        return openingBalance;
    }

    public Optional<DoubleFilter> optionalOpeningBalance() {
        return Optional.ofNullable(openingBalance);
    }

    public DoubleFilter openingBalance() {
        if (openingBalance == null) {
            setOpeningBalance(new DoubleFilter());
        }
        return openingBalance;
    }

    public void setOpeningBalance(DoubleFilter openingBalance) {
        this.openingBalance = openingBalance;
    }

    public DoubleFilter getClosingBalance() {
        return closingBalance;
    }

    public Optional<DoubleFilter> optionalClosingBalance() {
        return Optional.ofNullable(closingBalance);
    }

    public DoubleFilter closingBalance() {
        if (closingBalance == null) {
            setClosingBalance(new DoubleFilter());
        }
        return closingBalance;
    }

    public void setClosingBalance(DoubleFilter closingBalance) {
        this.closingBalance = closingBalance;
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

    public StatementStatusFilter getStatus() {
        return status;
    }

    public Optional<StatementStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public StatementStatusFilter status() {
        if (status == null) {
            setStatus(new StatementStatusFilter());
        }
        return status;
    }

    public void setStatus(StatementStatusFilter status) {
        this.status = status;
    }

    public BooleanFilter getIsReconciled() {
        return isReconciled;
    }

    public Optional<BooleanFilter> optionalIsReconciled() {
        return Optional.ofNullable(isReconciled);
    }

    public BooleanFilter isReconciled() {
        if (isReconciled == null) {
            setIsReconciled(new BooleanFilter());
        }
        return isReconciled;
    }

    public void setIsReconciled(BooleanFilter isReconciled) {
        this.isReconciled = isReconciled;
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
        final BankStatementCriteria that = (BankStatementCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(accountNumber, that.accountNumber) &&
            Objects.equals(bankName, that.bankName) &&
            Objects.equals(statementDate, that.statementDate) &&
            Objects.equals(statementPeriodStart, that.statementPeriodStart) &&
            Objects.equals(statementPeriodEnd, that.statementPeriodEnd) &&
            Objects.equals(openingBalance, that.openingBalance) &&
            Objects.equals(closingBalance, that.closingBalance) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(status, that.status) &&
            Objects.equals(isReconciled, that.isReconciled) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            documentId,
            accountNumber,
            bankName,
            statementDate,
            statementPeriodStart,
            statementPeriodEnd,
            openingBalance,
            closingBalance,
            currency,
            status,
            isReconciled,
            createdDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BankStatementCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentId().map(f -> "documentId=" + f + ", ").orElse("") +
            optionalAccountNumber().map(f -> "accountNumber=" + f + ", ").orElse("") +
            optionalBankName().map(f -> "bankName=" + f + ", ").orElse("") +
            optionalStatementDate().map(f -> "statementDate=" + f + ", ").orElse("") +
            optionalStatementPeriodStart().map(f -> "statementPeriodStart=" + f + ", ").orElse("") +
            optionalStatementPeriodEnd().map(f -> "statementPeriodEnd=" + f + ", ").orElse("") +
            optionalOpeningBalance().map(f -> "openingBalance=" + f + ", ").orElse("") +
            optionalClosingBalance().map(f -> "closingBalance=" + f + ", ").orElse("") +
            optionalCurrency().map(f -> "currency=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalIsReconciled().map(f -> "isReconciled=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
