package fr.smartprod.paperdms.emailimportdocument.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EmailImportImportRule.
 */
@Entity
@Table(name = "email_import_import_rule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmailImportImportRule implements Serializable {

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rule")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "rule" }, allowSetters = true)
    private Set<EmailImportImportMapping> emailImportImportMappings = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "appliedRule")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "emailImportEmailAttachments", "appliedRule" }, allowSetters = true)
    private Set<EmailImportDocument> emailImportDocuments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EmailImportImportRule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public EmailImportImportRule name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public EmailImportImportRule description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public EmailImportImportRule priority(Integer priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public EmailImportImportRule isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getConditions() {
        return this.conditions;
    }

    public EmailImportImportRule conditions(String conditions) {
        this.setConditions(conditions);
        return this;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getActions() {
        return this.actions;
    }

    public EmailImportImportRule actions(String actions) {
        this.setActions(actions);
        return this;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public String getNotifyUsers() {
        return this.notifyUsers;
    }

    public EmailImportImportRule notifyUsers(String notifyUsers) {
        this.setNotifyUsers(notifyUsers);
        return this;
    }

    public void setNotifyUsers(String notifyUsers) {
        this.notifyUsers = notifyUsers;
    }

    public Integer getMatchCount() {
        return this.matchCount;
    }

    public EmailImportImportRule matchCount(Integer matchCount) {
        this.setMatchCount(matchCount);
        return this;
    }

    public void setMatchCount(Integer matchCount) {
        this.matchCount = matchCount;
    }

    public Instant getLastMatchDate() {
        return this.lastMatchDate;
    }

    public EmailImportImportRule lastMatchDate(Instant lastMatchDate) {
        this.setLastMatchDate(lastMatchDate);
        return this;
    }

    public void setLastMatchDate(Instant lastMatchDate) {
        this.lastMatchDate = lastMatchDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public EmailImportImportRule createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public EmailImportImportRule createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public EmailImportImportRule lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<EmailImportImportMapping> getEmailImportImportMappings() {
        return this.emailImportImportMappings;
    }

    public void setEmailImportImportMappings(Set<EmailImportImportMapping> emailImportImportMappings) {
        if (this.emailImportImportMappings != null) {
            this.emailImportImportMappings.forEach(i -> i.setRule(null));
        }
        if (emailImportImportMappings != null) {
            emailImportImportMappings.forEach(i -> i.setRule(this));
        }
        this.emailImportImportMappings = emailImportImportMappings;
    }

    public EmailImportImportRule emailImportImportMappings(Set<EmailImportImportMapping> emailImportImportMappings) {
        this.setEmailImportImportMappings(emailImportImportMappings);
        return this;
    }

    public EmailImportImportRule addEmailImportImportMappings(EmailImportImportMapping emailImportImportMapping) {
        this.emailImportImportMappings.add(emailImportImportMapping);
        emailImportImportMapping.setRule(this);
        return this;
    }

    public EmailImportImportRule removeEmailImportImportMappings(EmailImportImportMapping emailImportImportMapping) {
        this.emailImportImportMappings.remove(emailImportImportMapping);
        emailImportImportMapping.setRule(null);
        return this;
    }

    public Set<EmailImportDocument> getEmailImportDocuments() {
        return this.emailImportDocuments;
    }

    public void setEmailImportDocuments(Set<EmailImportDocument> emailImportDocuments) {
        if (this.emailImportDocuments != null) {
            this.emailImportDocuments.forEach(i -> i.setAppliedRule(null));
        }
        if (emailImportDocuments != null) {
            emailImportDocuments.forEach(i -> i.setAppliedRule(this));
        }
        this.emailImportDocuments = emailImportDocuments;
    }

    public EmailImportImportRule emailImportDocuments(Set<EmailImportDocument> emailImportDocuments) {
        this.setEmailImportDocuments(emailImportDocuments);
        return this;
    }

    public EmailImportImportRule addEmailImportDocuments(EmailImportDocument emailImportDocument) {
        this.emailImportDocuments.add(emailImportDocument);
        emailImportDocument.setAppliedRule(this);
        return this;
    }

    public EmailImportImportRule removeEmailImportDocuments(EmailImportDocument emailImportDocument) {
        this.emailImportDocuments.remove(emailImportDocument);
        emailImportDocument.setAppliedRule(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailImportImportRule)) {
            return false;
        }
        return getId() != null && getId().equals(((EmailImportImportRule) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailImportImportRule{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", priority=" + getPriority() +
            ", isActive='" + getIsActive() + "'" +
            ", conditions='" + getConditions() + "'" +
            ", actions='" + getActions() + "'" +
            ", notifyUsers='" + getNotifyUsers() + "'" +
            ", matchCount=" + getMatchCount() +
            ", lastMatchDate='" + getLastMatchDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
