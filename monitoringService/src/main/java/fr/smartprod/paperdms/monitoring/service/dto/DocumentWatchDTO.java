package fr.smartprod.paperdms.monitoring.service.dto;

import fr.smartprod.paperdms.monitoring.domain.enumeration.WatchType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.monitoring.domain.DocumentWatch} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentWatchDTO implements Serializable {

    private Long id;

    @NotNull
    private Long documentId;

    @NotNull
    @Size(max = 50)
    private String userId;

    @NotNull
    private WatchType watchType;

    @NotNull
    private Boolean notifyOnView;

    @NotNull
    private Boolean notifyOnDownload;

    @NotNull
    private Boolean notifyOnModify;

    @NotNull
    private Boolean notifyOnShare;

    @NotNull
    private Boolean notifyOnDelete;

    @NotNull
    private Instant createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public WatchType getWatchType() {
        return watchType;
    }

    public void setWatchType(WatchType watchType) {
        this.watchType = watchType;
    }

    public Boolean getNotifyOnView() {
        return notifyOnView;
    }

    public void setNotifyOnView(Boolean notifyOnView) {
        this.notifyOnView = notifyOnView;
    }

    public Boolean getNotifyOnDownload() {
        return notifyOnDownload;
    }

    public void setNotifyOnDownload(Boolean notifyOnDownload) {
        this.notifyOnDownload = notifyOnDownload;
    }

    public Boolean getNotifyOnModify() {
        return notifyOnModify;
    }

    public void setNotifyOnModify(Boolean notifyOnModify) {
        this.notifyOnModify = notifyOnModify;
    }

    public Boolean getNotifyOnShare() {
        return notifyOnShare;
    }

    public void setNotifyOnShare(Boolean notifyOnShare) {
        this.notifyOnShare = notifyOnShare;
    }

    public Boolean getNotifyOnDelete() {
        return notifyOnDelete;
    }

    public void setNotifyOnDelete(Boolean notifyOnDelete) {
        this.notifyOnDelete = notifyOnDelete;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentWatchDTO)) {
            return false;
        }

        DocumentWatchDTO documentWatchDTO = (DocumentWatchDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentWatchDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentWatchDTO{" +
            "id=" + getId() +
            ", documentId=" + getDocumentId() +
            ", userId='" + getUserId() + "'" +
            ", watchType='" + getWatchType() + "'" +
            ", notifyOnView='" + getNotifyOnView() + "'" +
            ", notifyOnDownload='" + getNotifyOnDownload() + "'" +
            ", notifyOnModify='" + getNotifyOnModify() + "'" +
            ", notifyOnShare='" + getNotifyOnShare() + "'" +
            ", notifyOnDelete='" + getNotifyOnDelete() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
