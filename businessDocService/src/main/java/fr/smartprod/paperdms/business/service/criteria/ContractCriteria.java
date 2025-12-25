package fr.smartprod.paperdms.business.service.criteria;

import fr.smartprod.paperdms.business.domain.enumeration.ContractStatus;
import fr.smartprod.paperdms.business.domain.enumeration.ContractType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.business.domain.Contract} entity. This class is used
 * in {@link fr.smartprod.paperdms.business.web.rest.ContractResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contracts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContractCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ContractType
     */
    public static class ContractTypeFilter extends Filter<ContractType> {

        public ContractTypeFilter() {}

        public ContractTypeFilter(ContractTypeFilter filter) {
            super(filter);
        }

        @Override
        public ContractTypeFilter copy() {
            return new ContractTypeFilter(this);
        }
    }

    /**
     * Class for filtering ContractStatus
     */
    public static class ContractStatusFilter extends Filter<ContractStatus> {

        public ContractStatusFilter() {}

        public ContractStatusFilter(ContractStatusFilter filter) {
            super(filter);
        }

        @Override
        public ContractStatusFilter copy() {
            return new ContractStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter documentId;

    private StringFilter contractNumber;

    private ContractTypeFilter contractType;

    private StringFilter title;

    private StringFilter partyA;

    private StringFilter partyB;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private BooleanFilter autoRenew;

    private DoubleFilter contractValue;

    private StringFilter currency;

    private ContractStatusFilter status;

    private InstantFilter createdDate;

    private Boolean distinct;

    public ContractCriteria() {}

    public ContractCriteria(ContractCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentId = other.optionalDocumentId().map(LongFilter::copy).orElse(null);
        this.contractNumber = other.optionalContractNumber().map(StringFilter::copy).orElse(null);
        this.contractType = other.optionalContractType().map(ContractTypeFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.partyA = other.optionalPartyA().map(StringFilter::copy).orElse(null);
        this.partyB = other.optionalPartyB().map(StringFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(LocalDateFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(LocalDateFilter::copy).orElse(null);
        this.autoRenew = other.optionalAutoRenew().map(BooleanFilter::copy).orElse(null);
        this.contractValue = other.optionalContractValue().map(DoubleFilter::copy).orElse(null);
        this.currency = other.optionalCurrency().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ContractStatusFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ContractCriteria copy() {
        return new ContractCriteria(this);
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

    public StringFilter getContractNumber() {
        return contractNumber;
    }

    public Optional<StringFilter> optionalContractNumber() {
        return Optional.ofNullable(contractNumber);
    }

    public StringFilter contractNumber() {
        if (contractNumber == null) {
            setContractNumber(new StringFilter());
        }
        return contractNumber;
    }

    public void setContractNumber(StringFilter contractNumber) {
        this.contractNumber = contractNumber;
    }

    public ContractTypeFilter getContractType() {
        return contractType;
    }

    public Optional<ContractTypeFilter> optionalContractType() {
        return Optional.ofNullable(contractType);
    }

    public ContractTypeFilter contractType() {
        if (contractType == null) {
            setContractType(new ContractTypeFilter());
        }
        return contractType;
    }

    public void setContractType(ContractTypeFilter contractType) {
        this.contractType = contractType;
    }

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getPartyA() {
        return partyA;
    }

    public Optional<StringFilter> optionalPartyA() {
        return Optional.ofNullable(partyA);
    }

    public StringFilter partyA() {
        if (partyA == null) {
            setPartyA(new StringFilter());
        }
        return partyA;
    }

    public void setPartyA(StringFilter partyA) {
        this.partyA = partyA;
    }

    public StringFilter getPartyB() {
        return partyB;
    }

    public Optional<StringFilter> optionalPartyB() {
        return Optional.ofNullable(partyB);
    }

    public StringFilter partyB() {
        if (partyB == null) {
            setPartyB(new StringFilter());
        }
        return partyB;
    }

    public void setPartyB(StringFilter partyB) {
        this.partyB = partyB;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public Optional<LocalDateFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            setStartDate(new LocalDateFilter());
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public Optional<LocalDateFilter> optionalEndDate() {
        return Optional.ofNullable(endDate);
    }

    public LocalDateFilter endDate() {
        if (endDate == null) {
            setEndDate(new LocalDateFilter());
        }
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public BooleanFilter getAutoRenew() {
        return autoRenew;
    }

    public Optional<BooleanFilter> optionalAutoRenew() {
        return Optional.ofNullable(autoRenew);
    }

    public BooleanFilter autoRenew() {
        if (autoRenew == null) {
            setAutoRenew(new BooleanFilter());
        }
        return autoRenew;
    }

    public void setAutoRenew(BooleanFilter autoRenew) {
        this.autoRenew = autoRenew;
    }

    public DoubleFilter getContractValue() {
        return contractValue;
    }

    public Optional<DoubleFilter> optionalContractValue() {
        return Optional.ofNullable(contractValue);
    }

    public DoubleFilter contractValue() {
        if (contractValue == null) {
            setContractValue(new DoubleFilter());
        }
        return contractValue;
    }

    public void setContractValue(DoubleFilter contractValue) {
        this.contractValue = contractValue;
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

    public ContractStatusFilter getStatus() {
        return status;
    }

    public Optional<ContractStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ContractStatusFilter status() {
        if (status == null) {
            setStatus(new ContractStatusFilter());
        }
        return status;
    }

    public void setStatus(ContractStatusFilter status) {
        this.status = status;
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
        final ContractCriteria that = (ContractCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(contractNumber, that.contractNumber) &&
            Objects.equals(contractType, that.contractType) &&
            Objects.equals(title, that.title) &&
            Objects.equals(partyA, that.partyA) &&
            Objects.equals(partyB, that.partyB) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(autoRenew, that.autoRenew) &&
            Objects.equals(contractValue, that.contractValue) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(status, that.status) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            documentId,
            contractNumber,
            contractType,
            title,
            partyA,
            partyB,
            startDate,
            endDate,
            autoRenew,
            contractValue,
            currency,
            status,
            createdDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContractCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentId().map(f -> "documentId=" + f + ", ").orElse("") +
            optionalContractNumber().map(f -> "contractNumber=" + f + ", ").orElse("") +
            optionalContractType().map(f -> "contractType=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalPartyA().map(f -> "partyA=" + f + ", ").orElse("") +
            optionalPartyB().map(f -> "partyB=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalAutoRenew().map(f -> "autoRenew=" + f + ", ").orElse("") +
            optionalContractValue().map(f -> "contractValue=" + f + ", ").orElse("") +
            optionalCurrency().map(f -> "currency=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
