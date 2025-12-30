package fr.smartprod.paperdms.monitoring.domain;

import fr.smartprod.paperdms.monitoring.domain.enumeration.WatchType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MonitoringDocumentWatch.
 */
@Entity
@Table(name = "monitoring_document_watch")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MonitoringDocumentWatch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 64)
    @Column(name = "document_sha_256", length = 64)
    private String documentSha256;

    @NotNull
    @Size(max = 50)
    @Column(name = "user_id", length = 50, nullable = false)
    private String userId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "watch_type", nullable = false)
    private WatchType watchType;

    @NotNull
    @Column(name = "notify_on_view", nullable = false)
    private Boolean notifyOnView;

    @NotNull
    @Column(name = "notify_on_download", nullable = false)
    private Boolean notifyOnDownload;

    @NotNull
    @Column(name = "notify_on_modify", nullable = false)
    private Boolean notifyOnModify;

    @NotNull
    @Column(name = "notify_on_share", nullable = false)
    private Boolean notifyOnShare;

    @NotNull
    @Column(name = "notify_on_delete", nullable = false)
    private Boolean notifyOnDelete;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MonitoringDocumentWatch id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentSha256() {
        return this.documentSha256;
    }

    public MonitoringDocumentWatch documentSha256(String documentSha256) {
        this.setDocumentSha256(documentSha256);
        return this;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public String getUserId() {
        return this.userId;
    }

    public MonitoringDocumentWatch userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public WatchType getWatchType() {
        return this.watchType;
    }

    public MonitoringDocumentWatch watchType(WatchType watchType) {
        this.setWatchType(watchType);
        return this;
    }

    public void setWatchType(WatchType watchType) {
        this.watchType = watchType;
    }

    public Boolean getNotifyOnView() {
        return this.notifyOnView;
    }

    public MonitoringDocumentWatch notifyOnView(Boolean notifyOnView) {
        this.setNotifyOnView(notifyOnView);
        return this;
    }

    public void setNotifyOnView(Boolean notifyOnView) {
        this.notifyOnView = notifyOnView;
    }

    public Boolean getNotifyOnDownload() {
        return this.notifyOnDownload;
    }

    public MonitoringDocumentWatch notifyOnDownload(Boolean notifyOnDownload) {
        this.setNotifyOnDownload(notifyOnDownload);
        return this;
    }

    public void setNotifyOnDownload(Boolean notifyOnDownload) {
        this.notifyOnDownload = notifyOnDownload;
    }

    public Boolean getNotifyOnModify() {
        return this.notifyOnModify;
    }

    public MonitoringDocumentWatch notifyOnModify(Boolean notifyOnModify) {
        this.setNotifyOnModify(notifyOnModify);
        return this;
    }

    public void setNotifyOnModify(Boolean notifyOnModify) {
        this.notifyOnModify = notifyOnModify;
    }

    public Boolean getNotifyOnShare() {
        return this.notifyOnShare;
    }

    public MonitoringDocumentWatch notifyOnShare(Boolean notifyOnShare) {
        this.setNotifyOnShare(notifyOnShare);
        return this;
    }

    public void setNotifyOnShare(Boolean notifyOnShare) {
        this.notifyOnShare = notifyOnShare;
    }

    public Boolean getNotifyOnDelete() {
        return this.notifyOnDelete;
    }

    public MonitoringDocumentWatch notifyOnDelete(Boolean notifyOnDelete) {
        this.setNotifyOnDelete(notifyOnDelete);
        return this;
    }

    public void setNotifyOnDelete(Boolean notifyOnDelete) {
        this.notifyOnDelete = notifyOnDelete;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public MonitoringDocumentWatch createdDate(Instant createdDate) {
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
        if (!(o instanceof MonitoringDocumentWatch)) {
            return false;
        }
        return getId() != null && getId().equals(((MonitoringDocumentWatch) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MonitoringDocumentWatch{" +
            "id=" + getId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
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
