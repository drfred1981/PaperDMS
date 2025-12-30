package fr.smartprod.paperdms.document.domain;

import fr.smartprod.paperdms.document.domain.enumeration.AlertFrequency;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MetaSavedSearch.
 */
@Entity
@Table(name = "meta_saved_search")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "metasavedsearch")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MetaSavedSearch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String name;

    @Lob
    @Column(name = "query", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String query;

    @NotNull
    @Column(name = "is_public", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isPublic;

    @NotNull
    @Column(name = "is_alert", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isAlert;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_frequency")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private AlertFrequency alertFrequency;

    @NotNull
    @Size(max = 50)
    @Column(name = "user_id", length = 50, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String userId;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MetaSavedSearch id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MetaSavedSearch name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuery() {
        return this.query;
    }

    public MetaSavedSearch query(String query) {
        this.setQuery(query);
        return this;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Boolean getIsPublic() {
        return this.isPublic;
    }

    public MetaSavedSearch isPublic(Boolean isPublic) {
        this.setIsPublic(isPublic);
        return this;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Boolean getIsAlert() {
        return this.isAlert;
    }

    public MetaSavedSearch isAlert(Boolean isAlert) {
        this.setIsAlert(isAlert);
        return this;
    }

    public void setIsAlert(Boolean isAlert) {
        this.isAlert = isAlert;
    }

    public AlertFrequency getAlertFrequency() {
        return this.alertFrequency;
    }

    public MetaSavedSearch alertFrequency(AlertFrequency alertFrequency) {
        this.setAlertFrequency(alertFrequency);
        return this;
    }

    public void setAlertFrequency(AlertFrequency alertFrequency) {
        this.alertFrequency = alertFrequency;
    }

    public String getUserId() {
        return this.userId;
    }

    public MetaSavedSearch userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public MetaSavedSearch createdDate(Instant createdDate) {
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
        if (!(o instanceof MetaSavedSearch)) {
            return false;
        }
        return getId() != null && getId().equals(((MetaSavedSearch) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MetaSavedSearch{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", query='" + getQuery() + "'" +
            ", isPublic='" + getIsPublic() + "'" +
            ", isAlert='" + getIsAlert() + "'" +
            ", alertFrequency='" + getAlertFrequency() + "'" +
            ", userId='" + getUserId() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
