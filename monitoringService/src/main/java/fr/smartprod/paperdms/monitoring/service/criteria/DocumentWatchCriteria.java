package fr.smartprod.paperdms.monitoring.service.criteria;

import fr.smartprod.paperdms.monitoring.domain.enumeration.WatchType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.monitoring.domain.DocumentWatch} entity. This class is used
 * in {@link fr.smartprod.paperdms.monitoring.web.rest.DocumentWatchResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /document-watches?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentWatchCriteria implements Serializable, Criteria {

    /**
     * Class for filtering WatchType
     */
    public static class WatchTypeFilter extends Filter<WatchType> {

        public WatchTypeFilter() {}

        public WatchTypeFilter(WatchTypeFilter filter) {
            super(filter);
        }

        @Override
        public WatchTypeFilter copy() {
            return new WatchTypeFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter documentId;

    private StringFilter userId;

    private WatchTypeFilter watchType;

    private BooleanFilter notifyOnView;

    private BooleanFilter notifyOnDownload;

    private BooleanFilter notifyOnModify;

    private BooleanFilter notifyOnShare;

    private BooleanFilter notifyOnDelete;

    private InstantFilter createdDate;

    private Boolean distinct;

    public DocumentWatchCriteria() {}

    public DocumentWatchCriteria(DocumentWatchCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentId = other.optionalDocumentId().map(LongFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(StringFilter::copy).orElse(null);
        this.watchType = other.optionalWatchType().map(WatchTypeFilter::copy).orElse(null);
        this.notifyOnView = other.optionalNotifyOnView().map(BooleanFilter::copy).orElse(null);
        this.notifyOnDownload = other.optionalNotifyOnDownload().map(BooleanFilter::copy).orElse(null);
        this.notifyOnModify = other.optionalNotifyOnModify().map(BooleanFilter::copy).orElse(null);
        this.notifyOnShare = other.optionalNotifyOnShare().map(BooleanFilter::copy).orElse(null);
        this.notifyOnDelete = other.optionalNotifyOnDelete().map(BooleanFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DocumentWatchCriteria copy() {
        return new DocumentWatchCriteria(this);
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

    public WatchTypeFilter getWatchType() {
        return watchType;
    }

    public Optional<WatchTypeFilter> optionalWatchType() {
        return Optional.ofNullable(watchType);
    }

    public WatchTypeFilter watchType() {
        if (watchType == null) {
            setWatchType(new WatchTypeFilter());
        }
        return watchType;
    }

    public void setWatchType(WatchTypeFilter watchType) {
        this.watchType = watchType;
    }

    public BooleanFilter getNotifyOnView() {
        return notifyOnView;
    }

    public Optional<BooleanFilter> optionalNotifyOnView() {
        return Optional.ofNullable(notifyOnView);
    }

    public BooleanFilter notifyOnView() {
        if (notifyOnView == null) {
            setNotifyOnView(new BooleanFilter());
        }
        return notifyOnView;
    }

    public void setNotifyOnView(BooleanFilter notifyOnView) {
        this.notifyOnView = notifyOnView;
    }

    public BooleanFilter getNotifyOnDownload() {
        return notifyOnDownload;
    }

    public Optional<BooleanFilter> optionalNotifyOnDownload() {
        return Optional.ofNullable(notifyOnDownload);
    }

    public BooleanFilter notifyOnDownload() {
        if (notifyOnDownload == null) {
            setNotifyOnDownload(new BooleanFilter());
        }
        return notifyOnDownload;
    }

    public void setNotifyOnDownload(BooleanFilter notifyOnDownload) {
        this.notifyOnDownload = notifyOnDownload;
    }

    public BooleanFilter getNotifyOnModify() {
        return notifyOnModify;
    }

    public Optional<BooleanFilter> optionalNotifyOnModify() {
        return Optional.ofNullable(notifyOnModify);
    }

    public BooleanFilter notifyOnModify() {
        if (notifyOnModify == null) {
            setNotifyOnModify(new BooleanFilter());
        }
        return notifyOnModify;
    }

    public void setNotifyOnModify(BooleanFilter notifyOnModify) {
        this.notifyOnModify = notifyOnModify;
    }

    public BooleanFilter getNotifyOnShare() {
        return notifyOnShare;
    }

    public Optional<BooleanFilter> optionalNotifyOnShare() {
        return Optional.ofNullable(notifyOnShare);
    }

    public BooleanFilter notifyOnShare() {
        if (notifyOnShare == null) {
            setNotifyOnShare(new BooleanFilter());
        }
        return notifyOnShare;
    }

    public void setNotifyOnShare(BooleanFilter notifyOnShare) {
        this.notifyOnShare = notifyOnShare;
    }

    public BooleanFilter getNotifyOnDelete() {
        return notifyOnDelete;
    }

    public Optional<BooleanFilter> optionalNotifyOnDelete() {
        return Optional.ofNullable(notifyOnDelete);
    }

    public BooleanFilter notifyOnDelete() {
        if (notifyOnDelete == null) {
            setNotifyOnDelete(new BooleanFilter());
        }
        return notifyOnDelete;
    }

    public void setNotifyOnDelete(BooleanFilter notifyOnDelete) {
        this.notifyOnDelete = notifyOnDelete;
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
        final DocumentWatchCriteria that = (DocumentWatchCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(watchType, that.watchType) &&
            Objects.equals(notifyOnView, that.notifyOnView) &&
            Objects.equals(notifyOnDownload, that.notifyOnDownload) &&
            Objects.equals(notifyOnModify, that.notifyOnModify) &&
            Objects.equals(notifyOnShare, that.notifyOnShare) &&
            Objects.equals(notifyOnDelete, that.notifyOnDelete) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            documentId,
            userId,
            watchType,
            notifyOnView,
            notifyOnDownload,
            notifyOnModify,
            notifyOnShare,
            notifyOnDelete,
            createdDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentWatchCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentId().map(f -> "documentId=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalWatchType().map(f -> "watchType=" + f + ", ").orElse("") +
            optionalNotifyOnView().map(f -> "notifyOnView=" + f + ", ").orElse("") +
            optionalNotifyOnDownload().map(f -> "notifyOnDownload=" + f + ", ").orElse("") +
            optionalNotifyOnModify().map(f -> "notifyOnModify=" + f + ", ").orElse("") +
            optionalNotifyOnShare().map(f -> "notifyOnShare=" + f + ", ").orElse("") +
            optionalNotifyOnDelete().map(f -> "notifyOnDelete=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
