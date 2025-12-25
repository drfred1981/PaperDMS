package fr.smartprod.paperdms.document.service.criteria;

import fr.smartprod.paperdms.document.domain.enumeration.AlertFrequency;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.document.domain.SavedSearch} entity. This class is used
 * in {@link fr.smartprod.paperdms.document.web.rest.SavedSearchResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /saved-searches?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SavedSearchCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AlertFrequency
     */
    public static class AlertFrequencyFilter extends Filter<AlertFrequency> {

        public AlertFrequencyFilter() {}

        public AlertFrequencyFilter(AlertFrequencyFilter filter) {
            super(filter);
        }

        @Override
        public AlertFrequencyFilter copy() {
            return new AlertFrequencyFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private BooleanFilter isPublic;

    private BooleanFilter isAlert;

    private AlertFrequencyFilter alertFrequency;

    private StringFilter userId;

    private InstantFilter createdDate;

    private Boolean distinct;

    public SavedSearchCriteria() {}

    public SavedSearchCriteria(SavedSearchCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.isPublic = other.optionalIsPublic().map(BooleanFilter::copy).orElse(null);
        this.isAlert = other.optionalIsAlert().map(BooleanFilter::copy).orElse(null);
        this.alertFrequency = other.optionalAlertFrequency().map(AlertFrequencyFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SavedSearchCriteria copy() {
        return new SavedSearchCriteria(this);
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

    public BooleanFilter getIsPublic() {
        return isPublic;
    }

    public Optional<BooleanFilter> optionalIsPublic() {
        return Optional.ofNullable(isPublic);
    }

    public BooleanFilter isPublic() {
        if (isPublic == null) {
            setIsPublic(new BooleanFilter());
        }
        return isPublic;
    }

    public void setIsPublic(BooleanFilter isPublic) {
        this.isPublic = isPublic;
    }

    public BooleanFilter getIsAlert() {
        return isAlert;
    }

    public Optional<BooleanFilter> optionalIsAlert() {
        return Optional.ofNullable(isAlert);
    }

    public BooleanFilter isAlert() {
        if (isAlert == null) {
            setIsAlert(new BooleanFilter());
        }
        return isAlert;
    }

    public void setIsAlert(BooleanFilter isAlert) {
        this.isAlert = isAlert;
    }

    public AlertFrequencyFilter getAlertFrequency() {
        return alertFrequency;
    }

    public Optional<AlertFrequencyFilter> optionalAlertFrequency() {
        return Optional.ofNullable(alertFrequency);
    }

    public AlertFrequencyFilter alertFrequency() {
        if (alertFrequency == null) {
            setAlertFrequency(new AlertFrequencyFilter());
        }
        return alertFrequency;
    }

    public void setAlertFrequency(AlertFrequencyFilter alertFrequency) {
        this.alertFrequency = alertFrequency;
    }

    public StringFilter getUserId() {
        return userId;
    }

    public Optional<StringFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public StringFilter userId() {
        if (userId == null) {
            setUserId(new StringFilter());
        }
        return userId;
    }

    public void setUserId(StringFilter userId) {
        this.userId = userId;
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
        final SavedSearchCriteria that = (SavedSearchCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(isPublic, that.isPublic) &&
            Objects.equals(isAlert, that.isAlert) &&
            Objects.equals(alertFrequency, that.alertFrequency) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isPublic, isAlert, alertFrequency, userId, createdDate, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SavedSearchCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalIsPublic().map(f -> "isPublic=" + f + ", ").orElse("") +
            optionalIsAlert().map(f -> "isAlert=" + f + ", ").orElse("") +
            optionalAlertFrequency().map(f -> "alertFrequency=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
