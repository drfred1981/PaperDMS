package com.ged.ai.service.criteria;

import com.ged.ai.domain.enumeration.CorrespondentRole;
import com.ged.ai.domain.enumeration.CorrespondentType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.ged.ai.domain.Correspondent} entity. This class is used
 * in {@link com.ged.ai.web.rest.CorrespondentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /correspondents?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CorrespondentCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CorrespondentType
     */
    public static class CorrespondentTypeFilter extends Filter<CorrespondentType> {

        public CorrespondentTypeFilter() {}

        public CorrespondentTypeFilter(CorrespondentTypeFilter filter) {
            super(filter);
        }

        @Override
        public CorrespondentTypeFilter copy() {
            return new CorrespondentTypeFilter(this);
        }
    }

    /**
     * Class for filtering CorrespondentRole
     */
    public static class CorrespondentRoleFilter extends Filter<CorrespondentRole> {

        public CorrespondentRoleFilter() {}

        public CorrespondentRoleFilter(CorrespondentRoleFilter filter) {
            super(filter);
        }

        @Override
        public CorrespondentRoleFilter copy() {
            return new CorrespondentRoleFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter email;

    private StringFilter phone;

    private StringFilter company;

    private CorrespondentTypeFilter type;

    private CorrespondentRoleFilter role;

    private DoubleFilter confidence;

    private BooleanFilter isVerified;

    private StringFilter verifiedBy;

    private InstantFilter verifiedDate;

    private InstantFilter extractedDate;

    private LongFilter extractionId;

    private Boolean distinct;

    public CorrespondentCriteria() {}

    public CorrespondentCriteria(CorrespondentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.phone = other.optionalPhone().map(StringFilter::copy).orElse(null);
        this.company = other.optionalCompany().map(StringFilter::copy).orElse(null);
        this.type = other.optionalType().map(CorrespondentTypeFilter::copy).orElse(null);
        this.role = other.optionalRole().map(CorrespondentRoleFilter::copy).orElse(null);
        this.confidence = other.optionalConfidence().map(DoubleFilter::copy).orElse(null);
        this.isVerified = other.optionalIsVerified().map(BooleanFilter::copy).orElse(null);
        this.verifiedBy = other.optionalVerifiedBy().map(StringFilter::copy).orElse(null);
        this.verifiedDate = other.optionalVerifiedDate().map(InstantFilter::copy).orElse(null);
        this.extractedDate = other.optionalExtractedDate().map(InstantFilter::copy).orElse(null);
        this.extractionId = other.optionalExtractionId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CorrespondentCriteria copy() {
        return new CorrespondentCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public Optional<StringFilter> optionalPhone() {
        return Optional.ofNullable(phone);
    }

    public StringFilter phone() {
        if (phone == null) {
            setPhone(new StringFilter());
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getCompany() {
        return company;
    }

    public Optional<StringFilter> optionalCompany() {
        return Optional.ofNullable(company);
    }

    public StringFilter company() {
        if (company == null) {
            setCompany(new StringFilter());
        }
        return company;
    }

    public void setCompany(StringFilter company) {
        this.company = company;
    }

    public CorrespondentTypeFilter getType() {
        return type;
    }

    public Optional<CorrespondentTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public CorrespondentTypeFilter type() {
        if (type == null) {
            setType(new CorrespondentTypeFilter());
        }
        return type;
    }

    public void setType(CorrespondentTypeFilter type) {
        this.type = type;
    }

    public CorrespondentRoleFilter getRole() {
        return role;
    }

    public Optional<CorrespondentRoleFilter> optionalRole() {
        return Optional.ofNullable(role);
    }

    public CorrespondentRoleFilter role() {
        if (role == null) {
            setRole(new CorrespondentRoleFilter());
        }
        return role;
    }

    public void setRole(CorrespondentRoleFilter role) {
        this.role = role;
    }

    public DoubleFilter getConfidence() {
        return confidence;
    }

    public Optional<DoubleFilter> optionalConfidence() {
        return Optional.ofNullable(confidence);
    }

    public DoubleFilter confidence() {
        if (confidence == null) {
            setConfidence(new DoubleFilter());
        }
        return confidence;
    }

    public void setConfidence(DoubleFilter confidence) {
        this.confidence = confidence;
    }

    public BooleanFilter getIsVerified() {
        return isVerified;
    }

    public Optional<BooleanFilter> optionalIsVerified() {
        return Optional.ofNullable(isVerified);
    }

    public BooleanFilter isVerified() {
        if (isVerified == null) {
            setIsVerified(new BooleanFilter());
        }
        return isVerified;
    }

    public void setIsVerified(BooleanFilter isVerified) {
        this.isVerified = isVerified;
    }

    public StringFilter getVerifiedBy() {
        return verifiedBy;
    }

    public Optional<StringFilter> optionalVerifiedBy() {
        return Optional.ofNullable(verifiedBy);
    }

    public StringFilter verifiedBy() {
        if (verifiedBy == null) {
            setVerifiedBy(new StringFilter());
        }
        return verifiedBy;
    }

    public void setVerifiedBy(StringFilter verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public InstantFilter getVerifiedDate() {
        return verifiedDate;
    }

    public Optional<InstantFilter> optionalVerifiedDate() {
        return Optional.ofNullable(verifiedDate);
    }

    public InstantFilter verifiedDate() {
        if (verifiedDate == null) {
            setVerifiedDate(new InstantFilter());
        }
        return verifiedDate;
    }

    public void setVerifiedDate(InstantFilter verifiedDate) {
        this.verifiedDate = verifiedDate;
    }

    public InstantFilter getExtractedDate() {
        return extractedDate;
    }

    public Optional<InstantFilter> optionalExtractedDate() {
        return Optional.ofNullable(extractedDate);
    }

    public InstantFilter extractedDate() {
        if (extractedDate == null) {
            setExtractedDate(new InstantFilter());
        }
        return extractedDate;
    }

    public void setExtractedDate(InstantFilter extractedDate) {
        this.extractedDate = extractedDate;
    }

    public LongFilter getExtractionId() {
        return extractionId;
    }

    public Optional<LongFilter> optionalExtractionId() {
        return Optional.ofNullable(extractionId);
    }

    public LongFilter extractionId() {
        if (extractionId == null) {
            setExtractionId(new LongFilter());
        }
        return extractionId;
    }

    public void setExtractionId(LongFilter extractionId) {
        this.extractionId = extractionId;
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
        final CorrespondentCriteria that = (CorrespondentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(company, that.company) &&
            Objects.equals(type, that.type) &&
            Objects.equals(role, that.role) &&
            Objects.equals(confidence, that.confidence) &&
            Objects.equals(isVerified, that.isVerified) &&
            Objects.equals(verifiedBy, that.verifiedBy) &&
            Objects.equals(verifiedDate, that.verifiedDate) &&
            Objects.equals(extractedDate, that.extractedDate) &&
            Objects.equals(extractionId, that.extractionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            email,
            phone,
            company,
            type,
            role,
            confidence,
            isVerified,
            verifiedBy,
            verifiedDate,
            extractedDate,
            extractionId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CorrespondentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalPhone().map(f -> "phone=" + f + ", ").orElse("") +
            optionalCompany().map(f -> "company=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalRole().map(f -> "role=" + f + ", ").orElse("") +
            optionalConfidence().map(f -> "confidence=" + f + ", ").orElse("") +
            optionalIsVerified().map(f -> "isVerified=" + f + ", ").orElse("") +
            optionalVerifiedBy().map(f -> "verifiedBy=" + f + ", ").orElse("") +
            optionalVerifiedDate().map(f -> "verifiedDate=" + f + ", ").orElse("") +
            optionalExtractedDate().map(f -> "extractedDate=" + f + ", ").orElse("") +
            optionalExtractionId().map(f -> "extractionId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
