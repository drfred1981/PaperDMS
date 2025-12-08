package com.ged.notification.service.criteria;

import com.ged.notification.domain.enumeration.NotificationChannel;
import com.ged.notification.domain.enumeration.NotificationPriority;
import com.ged.notification.domain.enumeration.NotificationType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.ged.notification.domain.Notification} entity. This class is used
 * in {@link com.ged.notification.web.rest.NotificationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering NotificationType
     */
    public static class NotificationTypeFilter extends Filter<NotificationType> {

        public NotificationTypeFilter() {}

        public NotificationTypeFilter(NotificationTypeFilter filter) {
            super(filter);
        }

        @Override
        public NotificationTypeFilter copy() {
            return new NotificationTypeFilter(this);
        }
    }

    /**
     * Class for filtering NotificationPriority
     */
    public static class NotificationPriorityFilter extends Filter<NotificationPriority> {

        public NotificationPriorityFilter() {}

        public NotificationPriorityFilter(NotificationPriorityFilter filter) {
            super(filter);
        }

        @Override
        public NotificationPriorityFilter copy() {
            return new NotificationPriorityFilter(this);
        }
    }

    /**
     * Class for filtering NotificationChannel
     */
    public static class NotificationChannelFilter extends Filter<NotificationChannel> {

        public NotificationChannelFilter() {}

        public NotificationChannelFilter(NotificationChannelFilter filter) {
            super(filter);
        }

        @Override
        public NotificationChannelFilter copy() {
            return new NotificationChannelFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private NotificationTypeFilter type;

    private NotificationPriorityFilter priority;

    private StringFilter recipientId;

    private BooleanFilter isRead;

    private InstantFilter readDate;

    private NotificationChannelFilter channel;

    private StringFilter relatedEntityType;

    private LongFilter relatedEntityId;

    private StringFilter actionUrl;

    private InstantFilter expirationDate;

    private InstantFilter sentDate;

    private InstantFilter createdDate;

    private LongFilter templateId;

    private Boolean distinct;

    public NotificationCriteria() {}

    public NotificationCriteria(NotificationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.type = other.optionalType().map(NotificationTypeFilter::copy).orElse(null);
        this.priority = other.optionalPriority().map(NotificationPriorityFilter::copy).orElse(null);
        this.recipientId = other.optionalRecipientId().map(StringFilter::copy).orElse(null);
        this.isRead = other.optionalIsRead().map(BooleanFilter::copy).orElse(null);
        this.readDate = other.optionalReadDate().map(InstantFilter::copy).orElse(null);
        this.channel = other.optionalChannel().map(NotificationChannelFilter::copy).orElse(null);
        this.relatedEntityType = other.optionalRelatedEntityType().map(StringFilter::copy).orElse(null);
        this.relatedEntityId = other.optionalRelatedEntityId().map(LongFilter::copy).orElse(null);
        this.actionUrl = other.optionalActionUrl().map(StringFilter::copy).orElse(null);
        this.expirationDate = other.optionalExpirationDate().map(InstantFilter::copy).orElse(null);
        this.sentDate = other.optionalSentDate().map(InstantFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.templateId = other.optionalTemplateId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public NotificationCriteria copy() {
        return new NotificationCriteria(this);
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

    public NotificationTypeFilter getType() {
        return type;
    }

    public Optional<NotificationTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public NotificationTypeFilter type() {
        if (type == null) {
            setType(new NotificationTypeFilter());
        }
        return type;
    }

    public void setType(NotificationTypeFilter type) {
        this.type = type;
    }

    public NotificationPriorityFilter getPriority() {
        return priority;
    }

    public Optional<NotificationPriorityFilter> optionalPriority() {
        return Optional.ofNullable(priority);
    }

    public NotificationPriorityFilter priority() {
        if (priority == null) {
            setPriority(new NotificationPriorityFilter());
        }
        return priority;
    }

    public void setPriority(NotificationPriorityFilter priority) {
        this.priority = priority;
    }

    public StringFilter getRecipientId() {
        return recipientId;
    }

    public Optional<StringFilter> optionalRecipientId() {
        return Optional.ofNullable(recipientId);
    }

    public StringFilter recipientId() {
        if (recipientId == null) {
            setRecipientId(new StringFilter());
        }
        return recipientId;
    }

    public void setRecipientId(StringFilter recipientId) {
        this.recipientId = recipientId;
    }

    public BooleanFilter getIsRead() {
        return isRead;
    }

    public Optional<BooleanFilter> optionalIsRead() {
        return Optional.ofNullable(isRead);
    }

    public BooleanFilter isRead() {
        if (isRead == null) {
            setIsRead(new BooleanFilter());
        }
        return isRead;
    }

    public void setIsRead(BooleanFilter isRead) {
        this.isRead = isRead;
    }

    public InstantFilter getReadDate() {
        return readDate;
    }

    public Optional<InstantFilter> optionalReadDate() {
        return Optional.ofNullable(readDate);
    }

    public InstantFilter readDate() {
        if (readDate == null) {
            setReadDate(new InstantFilter());
        }
        return readDate;
    }

    public void setReadDate(InstantFilter readDate) {
        this.readDate = readDate;
    }

    public NotificationChannelFilter getChannel() {
        return channel;
    }

    public Optional<NotificationChannelFilter> optionalChannel() {
        return Optional.ofNullable(channel);
    }

    public NotificationChannelFilter channel() {
        if (channel == null) {
            setChannel(new NotificationChannelFilter());
        }
        return channel;
    }

    public void setChannel(NotificationChannelFilter channel) {
        this.channel = channel;
    }

    public StringFilter getRelatedEntityType() {
        return relatedEntityType;
    }

    public Optional<StringFilter> optionalRelatedEntityType() {
        return Optional.ofNullable(relatedEntityType);
    }

    public StringFilter relatedEntityType() {
        if (relatedEntityType == null) {
            setRelatedEntityType(new StringFilter());
        }
        return relatedEntityType;
    }

    public void setRelatedEntityType(StringFilter relatedEntityType) {
        this.relatedEntityType = relatedEntityType;
    }

    public LongFilter getRelatedEntityId() {
        return relatedEntityId;
    }

    public Optional<LongFilter> optionalRelatedEntityId() {
        return Optional.ofNullable(relatedEntityId);
    }

    public LongFilter relatedEntityId() {
        if (relatedEntityId == null) {
            setRelatedEntityId(new LongFilter());
        }
        return relatedEntityId;
    }

    public void setRelatedEntityId(LongFilter relatedEntityId) {
        this.relatedEntityId = relatedEntityId;
    }

    public StringFilter getActionUrl() {
        return actionUrl;
    }

    public Optional<StringFilter> optionalActionUrl() {
        return Optional.ofNullable(actionUrl);
    }

    public StringFilter actionUrl() {
        if (actionUrl == null) {
            setActionUrl(new StringFilter());
        }
        return actionUrl;
    }

    public void setActionUrl(StringFilter actionUrl) {
        this.actionUrl = actionUrl;
    }

    public InstantFilter getExpirationDate() {
        return expirationDate;
    }

    public Optional<InstantFilter> optionalExpirationDate() {
        return Optional.ofNullable(expirationDate);
    }

    public InstantFilter expirationDate() {
        if (expirationDate == null) {
            setExpirationDate(new InstantFilter());
        }
        return expirationDate;
    }

    public void setExpirationDate(InstantFilter expirationDate) {
        this.expirationDate = expirationDate;
    }

    public InstantFilter getSentDate() {
        return sentDate;
    }

    public Optional<InstantFilter> optionalSentDate() {
        return Optional.ofNullable(sentDate);
    }

    public InstantFilter sentDate() {
        if (sentDate == null) {
            setSentDate(new InstantFilter());
        }
        return sentDate;
    }

    public void setSentDate(InstantFilter sentDate) {
        this.sentDate = sentDate;
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

    public LongFilter getTemplateId() {
        return templateId;
    }

    public Optional<LongFilter> optionalTemplateId() {
        return Optional.ofNullable(templateId);
    }

    public LongFilter templateId() {
        if (templateId == null) {
            setTemplateId(new LongFilter());
        }
        return templateId;
    }

    public void setTemplateId(LongFilter templateId) {
        this.templateId = templateId;
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
        final NotificationCriteria that = (NotificationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(type, that.type) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(recipientId, that.recipientId) &&
            Objects.equals(isRead, that.isRead) &&
            Objects.equals(readDate, that.readDate) &&
            Objects.equals(channel, that.channel) &&
            Objects.equals(relatedEntityType, that.relatedEntityType) &&
            Objects.equals(relatedEntityId, that.relatedEntityId) &&
            Objects.equals(actionUrl, that.actionUrl) &&
            Objects.equals(expirationDate, that.expirationDate) &&
            Objects.equals(sentDate, that.sentDate) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(templateId, that.templateId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            type,
            priority,
            recipientId,
            isRead,
            readDate,
            channel,
            relatedEntityType,
            relatedEntityId,
            actionUrl,
            expirationDate,
            sentDate,
            createdDate,
            templateId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalPriority().map(f -> "priority=" + f + ", ").orElse("") +
            optionalRecipientId().map(f -> "recipientId=" + f + ", ").orElse("") +
            optionalIsRead().map(f -> "isRead=" + f + ", ").orElse("") +
            optionalReadDate().map(f -> "readDate=" + f + ", ").orElse("") +
            optionalChannel().map(f -> "channel=" + f + ", ").orElse("") +
            optionalRelatedEntityType().map(f -> "relatedEntityType=" + f + ", ").orElse("") +
            optionalRelatedEntityId().map(f -> "relatedEntityId=" + f + ", ").orElse("") +
            optionalActionUrl().map(f -> "actionUrl=" + f + ", ").orElse("") +
            optionalExpirationDate().map(f -> "expirationDate=" + f + ", ").orElse("") +
            optionalSentDate().map(f -> "sentDate=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalTemplateId().map(f -> "templateId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
