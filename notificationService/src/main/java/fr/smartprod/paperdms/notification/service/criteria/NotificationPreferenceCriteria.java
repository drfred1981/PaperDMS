package fr.smartprod.paperdms.notification.service.criteria;

import fr.smartprod.paperdms.notification.domain.enumeration.NotificationFrequency;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.notification.domain.NotificationPreference} entity. This class is used
 * in {@link fr.smartprod.paperdms.notification.web.rest.NotificationPreferenceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notification-preferences?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationPreferenceCriteria implements Serializable, Criteria {

    /**
     * Class for filtering NotificationFrequency
     */
    public static class NotificationFrequencyFilter extends Filter<NotificationFrequency> {

        public NotificationFrequencyFilter() {}

        public NotificationFrequencyFilter(NotificationFrequencyFilter filter) {
            super(filter);
        }

        @Override
        public NotificationFrequencyFilter copy() {
            return new NotificationFrequencyFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter userId;

    private BooleanFilter emailEnabled;

    private BooleanFilter pushEnabled;

    private BooleanFilter inAppEnabled;

    private StringFilter quietHoursStart;

    private StringFilter quietHoursEnd;

    private NotificationFrequencyFilter frequency;

    private InstantFilter lastModifiedDate;

    private Boolean distinct;

    public NotificationPreferenceCriteria() {}

    public NotificationPreferenceCriteria(NotificationPreferenceCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(StringFilter::copy).orElse(null);
        this.emailEnabled = other.optionalEmailEnabled().map(BooleanFilter::copy).orElse(null);
        this.pushEnabled = other.optionalPushEnabled().map(BooleanFilter::copy).orElse(null);
        this.inAppEnabled = other.optionalInAppEnabled().map(BooleanFilter::copy).orElse(null);
        this.quietHoursStart = other.optionalQuietHoursStart().map(StringFilter::copy).orElse(null);
        this.quietHoursEnd = other.optionalQuietHoursEnd().map(StringFilter::copy).orElse(null);
        this.frequency = other.optionalFrequency().map(NotificationFrequencyFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public NotificationPreferenceCriteria copy() {
        return new NotificationPreferenceCriteria(this);
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

    public BooleanFilter getEmailEnabled() {
        return emailEnabled;
    }

    public Optional<BooleanFilter> optionalEmailEnabled() {
        return Optional.ofNullable(emailEnabled);
    }

    public BooleanFilter emailEnabled() {
        if (emailEnabled == null) {
            setEmailEnabled(new BooleanFilter());
        }
        return emailEnabled;
    }

    public void setEmailEnabled(BooleanFilter emailEnabled) {
        this.emailEnabled = emailEnabled;
    }

    public BooleanFilter getPushEnabled() {
        return pushEnabled;
    }

    public Optional<BooleanFilter> optionalPushEnabled() {
        return Optional.ofNullable(pushEnabled);
    }

    public BooleanFilter pushEnabled() {
        if (pushEnabled == null) {
            setPushEnabled(new BooleanFilter());
        }
        return pushEnabled;
    }

    public void setPushEnabled(BooleanFilter pushEnabled) {
        this.pushEnabled = pushEnabled;
    }

    public BooleanFilter getInAppEnabled() {
        return inAppEnabled;
    }

    public Optional<BooleanFilter> optionalInAppEnabled() {
        return Optional.ofNullable(inAppEnabled);
    }

    public BooleanFilter inAppEnabled() {
        if (inAppEnabled == null) {
            setInAppEnabled(new BooleanFilter());
        }
        return inAppEnabled;
    }

    public void setInAppEnabled(BooleanFilter inAppEnabled) {
        this.inAppEnabled = inAppEnabled;
    }

    public StringFilter getQuietHoursStart() {
        return quietHoursStart;
    }

    public Optional<StringFilter> optionalQuietHoursStart() {
        return Optional.ofNullable(quietHoursStart);
    }

    public StringFilter quietHoursStart() {
        if (quietHoursStart == null) {
            setQuietHoursStart(new StringFilter());
        }
        return quietHoursStart;
    }

    public void setQuietHoursStart(StringFilter quietHoursStart) {
        this.quietHoursStart = quietHoursStart;
    }

    public StringFilter getQuietHoursEnd() {
        return quietHoursEnd;
    }

    public Optional<StringFilter> optionalQuietHoursEnd() {
        return Optional.ofNullable(quietHoursEnd);
    }

    public StringFilter quietHoursEnd() {
        if (quietHoursEnd == null) {
            setQuietHoursEnd(new StringFilter());
        }
        return quietHoursEnd;
    }

    public void setQuietHoursEnd(StringFilter quietHoursEnd) {
        this.quietHoursEnd = quietHoursEnd;
    }

    public NotificationFrequencyFilter getFrequency() {
        return frequency;
    }

    public Optional<NotificationFrequencyFilter> optionalFrequency() {
        return Optional.ofNullable(frequency);
    }

    public NotificationFrequencyFilter frequency() {
        if (frequency == null) {
            setFrequency(new NotificationFrequencyFilter());
        }
        return frequency;
    }

    public void setFrequency(NotificationFrequencyFilter frequency) {
        this.frequency = frequency;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Optional<InstantFilter> optionalLastModifiedDate() {
        return Optional.ofNullable(lastModifiedDate);
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            setLastModifiedDate(new InstantFilter());
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
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
        final NotificationPreferenceCriteria that = (NotificationPreferenceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(emailEnabled, that.emailEnabled) &&
            Objects.equals(pushEnabled, that.pushEnabled) &&
            Objects.equals(inAppEnabled, that.inAppEnabled) &&
            Objects.equals(quietHoursStart, that.quietHoursStart) &&
            Objects.equals(quietHoursEnd, that.quietHoursEnd) &&
            Objects.equals(frequency, that.frequency) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            userId,
            emailEnabled,
            pushEnabled,
            inAppEnabled,
            quietHoursStart,
            quietHoursEnd,
            frequency,
            lastModifiedDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationPreferenceCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalEmailEnabled().map(f -> "emailEnabled=" + f + ", ").orElse("") +
            optionalPushEnabled().map(f -> "pushEnabled=" + f + ", ").orElse("") +
            optionalInAppEnabled().map(f -> "inAppEnabled=" + f + ", ").orElse("") +
            optionalQuietHoursStart().map(f -> "quietHoursStart=" + f + ", ").orElse("") +
            optionalQuietHoursEnd().map(f -> "quietHoursEnd=" + f + ", ").orElse("") +
            optionalFrequency().map(f -> "frequency=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
