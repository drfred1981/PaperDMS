package fr.smartprod.paperdms.document.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SmartFolder.
 */
@Entity
@Table(name = "smart_folder")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SmartFolder implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Lob
    @Column(name = "query_json", nullable = false)
    private String queryJson;

    @NotNull
    @Column(name = "auto_refresh", nullable = false)
    private Boolean autoRefresh;

    @NotNull
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @NotNull
    @Size(max = 50)
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SmartFolder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public SmartFolder name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQueryJson() {
        return this.queryJson;
    }

    public SmartFolder queryJson(String queryJson) {
        this.setQueryJson(queryJson);
        return this;
    }

    public void setQueryJson(String queryJson) {
        this.queryJson = queryJson;
    }

    public Boolean getAutoRefresh() {
        return this.autoRefresh;
    }

    public SmartFolder autoRefresh(Boolean autoRefresh) {
        this.setAutoRefresh(autoRefresh);
        return this;
    }

    public void setAutoRefresh(Boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    public Boolean getIsPublic() {
        return this.isPublic;
    }

    public SmartFolder isPublic(Boolean isPublic) {
        this.setIsPublic(isPublic);
        return this;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public SmartFolder createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public SmartFolder createdDate(Instant createdDate) {
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
        if (!(o instanceof SmartFolder)) {
            return false;
        }
        return getId() != null && getId().equals(((SmartFolder) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SmartFolder{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", queryJson='" + getQueryJson() + "'" +
            ", autoRefresh='" + getAutoRefresh() + "'" +
            ", isPublic='" + getIsPublic() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
