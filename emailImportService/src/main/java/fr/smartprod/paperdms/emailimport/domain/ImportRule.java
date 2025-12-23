package fr.smartprod.paperdms.emailimport.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ImportRule.
 */
@Entity
@Table(name = "import_rule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImportRule implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false, unique = true)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "priority", nullable = false)
    private Integer priority;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Lob
    @Column(name = "conditions", nullable = false)
    private String conditions;

    @Lob
    @Column(name = "actions", nullable = false)
    private String actions;

    @Column(name = "folder_id")
    private Long folderId;

    @Column(name = "document_type_id")
    private Long documentTypeId;

    @Lob
    @Column(name = "apply_tags")
    private String applyTags;

    @Lob
    @Column(name = "notify_users")
    private String notifyUsers;

    @Column(name = "match_count")
    private Integer matchCount;

    @Column(name = "last_match_date")
    private Instant lastMatchDate;

    @NotNull
    @Size(max = 50)
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ImportRule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ImportRule name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ImportRule description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public ImportRule priority(Integer priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public ImportRule isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getConditions() {
        return this.conditions;
    }

    public ImportRule conditions(String conditions) {
        this.setConditions(conditions);
        return this;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getActions() {
        return this.actions;
    }

    public ImportRule actions(String actions) {
        this.setActions(actions);
        return this;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public Long getFolderId() {
        return this.folderId;
    }

    public ImportRule folderId(Long folderId) {
        this.setFolderId(folderId);
        return this;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Long getDocumentTypeId() {
        return this.documentTypeId;
    }

    public ImportRule documentTypeId(Long documentTypeId) {
        this.setDocumentTypeId(documentTypeId);
        return this;
    }

    public void setDocumentTypeId(Long documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public String getApplyTags() {
        return this.applyTags;
    }

    public ImportRule applyTags(String applyTags) {
        this.setApplyTags(applyTags);
        return this;
    }

    public void setApplyTags(String applyTags) {
        this.applyTags = applyTags;
    }

    public String getNotifyUsers() {
        return this.notifyUsers;
    }

    public ImportRule notifyUsers(String notifyUsers) {
        this.setNotifyUsers(notifyUsers);
        return this;
    }

    public void setNotifyUsers(String notifyUsers) {
        this.notifyUsers = notifyUsers;
    }

    public Integer getMatchCount() {
        return this.matchCount;
    }

    public ImportRule matchCount(Integer matchCount) {
        this.setMatchCount(matchCount);
        return this;
    }

    public void setMatchCount(Integer matchCount) {
        this.matchCount = matchCount;
    }

    public Instant getLastMatchDate() {
        return this.lastMatchDate;
    }

    public ImportRule lastMatchDate(Instant lastMatchDate) {
        this.setLastMatchDate(lastMatchDate);
        return this;
    }

    public void setLastMatchDate(Instant lastMatchDate) {
        this.lastMatchDate = lastMatchDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public ImportRule createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public ImportRule createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public ImportRule lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImportRule)) {
            return false;
        }
        return getId() != null && getId().equals(((ImportRule) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImportRule{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", priority=" + getPriority() +
            ", isActive='" + getIsActive() + "'" +
            ", conditions='" + getConditions() + "'" +
            ", actions='" + getActions() + "'" +
            ", folderId=" + getFolderId() +
            ", documentTypeId=" + getDocumentTypeId() +
            ", applyTags='" + getApplyTags() + "'" +
            ", notifyUsers='" + getNotifyUsers() + "'" +
            ", matchCount=" + getMatchCount() +
            ", lastMatchDate='" + getLastMatchDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
