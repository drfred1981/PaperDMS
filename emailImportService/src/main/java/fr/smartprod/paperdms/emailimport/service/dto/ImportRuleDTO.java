package fr.smartprod.paperdms.emailimport.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.emailimport.domain.ImportRule} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImportRuleDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Lob
    private String description;

    @NotNull
    private Integer priority;

    @NotNull
    private Boolean isActive;

    @Lob
    private String conditions;

    @Lob
    private String actions;

    private Long folderId;

    private Long documentTypeId;

    @Lob
    private String applyTags;

    @Lob
    private String notifyUsers;

    private Integer matchCount;

    private Instant lastMatchDate;

    @NotNull
    @Size(max = 50)
    private String createdBy;

    @NotNull
    private Instant createdDate;

    private Instant lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Long getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(Long documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public String getApplyTags() {
        return applyTags;
    }

    public void setApplyTags(String applyTags) {
        this.applyTags = applyTags;
    }

    public String getNotifyUsers() {
        return notifyUsers;
    }

    public void setNotifyUsers(String notifyUsers) {
        this.notifyUsers = notifyUsers;
    }

    public Integer getMatchCount() {
        return matchCount;
    }

    public void setMatchCount(Integer matchCount) {
        this.matchCount = matchCount;
    }

    public Instant getLastMatchDate() {
        return lastMatchDate;
    }

    public void setLastMatchDate(Instant lastMatchDate) {
        this.lastMatchDate = lastMatchDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImportRuleDTO)) {
            return false;
        }

        ImportRuleDTO importRuleDTO = (ImportRuleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, importRuleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImportRuleDTO{" +
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
