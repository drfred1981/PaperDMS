package fr.smartprod.paperdms.document.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.smartprod.paperdms.document.domain.enumeration.PermissionType;
import fr.smartprod.paperdms.document.domain.enumeration.PrincipalType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DocumentPermission.
 */
@Entity
@Table(name = "document_permission")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "documentpermission")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "principal_type", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private PrincipalType principalType;

    @NotNull
    @Size(max = 100)
    @Column(name = "principal_id", length = 100, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String principalId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "permission", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private PermissionType permission;

    @NotNull
    @Column(name = "can_delegate", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean canDelegate;

    @NotNull
    @Size(max = 50)
    @Column(name = "granted_by", length = 50, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String grantedBy;

    @NotNull
    @Column(name = "granted_date", nullable = false)
    private Instant grantedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "documentVersions",
            "documentTags",
            "statuses",
            "documentExtractedFields",
            "permissions",
            "audits",
            "comments",
            "metadatas",
            "statistics",
            "documentType",
            "folder",
        },
        allowSetters = true
    )
    private Document document;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "documentPermissions" }, allowSetters = true)
    private MetaPermissionGroup metaPermissionGroup;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocumentPermission id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PrincipalType getPrincipalType() {
        return this.principalType;
    }

    public DocumentPermission principalType(PrincipalType principalType) {
        this.setPrincipalType(principalType);
        return this;
    }

    public void setPrincipalType(PrincipalType principalType) {
        this.principalType = principalType;
    }

    public String getPrincipalId() {
        return this.principalId;
    }

    public DocumentPermission principalId(String principalId) {
        this.setPrincipalId(principalId);
        return this;
    }

    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }

    public PermissionType getPermission() {
        return this.permission;
    }

    public DocumentPermission permission(PermissionType permission) {
        this.setPermission(permission);
        return this;
    }

    public void setPermission(PermissionType permission) {
        this.permission = permission;
    }

    public Boolean getCanDelegate() {
        return this.canDelegate;
    }

    public DocumentPermission canDelegate(Boolean canDelegate) {
        this.setCanDelegate(canDelegate);
        return this;
    }

    public void setCanDelegate(Boolean canDelegate) {
        this.canDelegate = canDelegate;
    }

    public String getGrantedBy() {
        return this.grantedBy;
    }

    public DocumentPermission grantedBy(String grantedBy) {
        this.setGrantedBy(grantedBy);
        return this;
    }

    public void setGrantedBy(String grantedBy) {
        this.grantedBy = grantedBy;
    }

    public Instant getGrantedDate() {
        return this.grantedDate;
    }

    public DocumentPermission grantedDate(Instant grantedDate) {
        this.setGrantedDate(grantedDate);
        return this;
    }

    public void setGrantedDate(Instant grantedDate) {
        this.grantedDate = grantedDate;
    }

    public Document getDocument() {
        return this.document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public DocumentPermission document(Document document) {
        this.setDocument(document);
        return this;
    }

    public MetaPermissionGroup getMetaPermissionGroup() {
        return this.metaPermissionGroup;
    }

    public void setMetaPermissionGroup(MetaPermissionGroup metaPermissionGroup) {
        this.metaPermissionGroup = metaPermissionGroup;
    }

    public DocumentPermission metaPermissionGroup(MetaPermissionGroup metaPermissionGroup) {
        this.setMetaPermissionGroup(metaPermissionGroup);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentPermission)) {
            return false;
        }
        return getId() != null && getId().equals(((DocumentPermission) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentPermission{" +
            "id=" + getId() +
            ", principalType='" + getPrincipalType() + "'" +
            ", principalId='" + getPrincipalId() + "'" +
            ", permission='" + getPermission() + "'" +
            ", canDelegate='" + getCanDelegate() + "'" +
            ", grantedBy='" + getGrantedBy() + "'" +
            ", grantedDate='" + getGrantedDate() + "'" +
            "}";
    }
}
