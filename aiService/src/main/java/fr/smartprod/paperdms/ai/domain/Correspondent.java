package fr.smartprod.paperdms.ai.domain;

import fr.smartprod.paperdms.ai.domain.enumeration.CorrespondentRole;
import fr.smartprod.paperdms.ai.domain.enumeration.CorrespondentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Correspondent.
 */
@Entity
@Table(name = "correspondent")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "correspondent")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Correspondent implements Serializable {

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
    @org.springframework.data.elasticsearch.annotations.MultiField(
        mainField = @org.springframework.data.elasticsearch.annotations.Field(
            type = org.springframework.data.elasticsearch.annotations.FieldType.Text
        ),
        otherFields = {
            @org.springframework.data.elasticsearch.annotations.InnerField(
                suffix = "keyword",
                type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword,
                ignoreAbove = 256
            ),
        }
    )
    private String name;

    @Size(max = 255)
    @Column(name = "email", length = 255)
    @org.springframework.data.elasticsearch.annotations.MultiField(
        mainField = @org.springframework.data.elasticsearch.annotations.Field(
            type = org.springframework.data.elasticsearch.annotations.FieldType.Text
        ),
        otherFields = {
            @org.springframework.data.elasticsearch.annotations.InnerField(
                suffix = "keyword",
                type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword,
                ignoreAbove = 256
            ),
        }
    )
    private String email;

    @Size(max = 50)
    @Column(name = "phone", length = 50)
    @org.springframework.data.elasticsearch.annotations.MultiField(
        mainField = @org.springframework.data.elasticsearch.annotations.Field(
            type = org.springframework.data.elasticsearch.annotations.FieldType.Text
        ),
        otherFields = {
            @org.springframework.data.elasticsearch.annotations.InnerField(
                suffix = "keyword",
                type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword,
                ignoreAbove = 256
            ),
        }
    )
    private String phone;

    @Lob
    @Column(name = "address")
    @org.springframework.data.elasticsearch.annotations.MultiField(
        mainField = @org.springframework.data.elasticsearch.annotations.Field(
            type = org.springframework.data.elasticsearch.annotations.FieldType.Text
        ),
        otherFields = {
            @org.springframework.data.elasticsearch.annotations.InnerField(
                suffix = "keyword",
                type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword,
                ignoreAbove = 256
            ),
        }
    )
    private String address;

    @Size(max = 255)
    @Column(name = "company", length = 255)
    @org.springframework.data.elasticsearch.annotations.MultiField(
        mainField = @org.springframework.data.elasticsearch.annotations.Field(
            type = org.springframework.data.elasticsearch.annotations.FieldType.Text
        ),
        otherFields = {
            @org.springframework.data.elasticsearch.annotations.InnerField(
                suffix = "keyword",
                type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword,
                ignoreAbove = 256
            ),
        }
    )
    private String company;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private CorrespondentType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private CorrespondentRole role;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    @Column(name = "confidence")
    private Double confidence;

    @NotNull
    @Column(name = "is_verified", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isVerified;

    @Size(max = 50)
    @Column(name = "verified_by", length = 50)
    @org.springframework.data.elasticsearch.annotations.MultiField(
        mainField = @org.springframework.data.elasticsearch.annotations.Field(
            type = org.springframework.data.elasticsearch.annotations.FieldType.Text
        ),
        otherFields = {
            @org.springframework.data.elasticsearch.annotations.InnerField(
                suffix = "keyword",
                type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword,
                ignoreAbove = 256
            ),
        }
    )
    private String verifiedBy;

    @Column(name = "verified_date")
    private Instant verifiedDate;

    @Lob
    @Column(name = "metadata")
    @org.springframework.data.elasticsearch.annotations.MultiField(
        mainField = @org.springframework.data.elasticsearch.annotations.Field(
            type = org.springframework.data.elasticsearch.annotations.FieldType.Text
        ),
        otherFields = {
            @org.springframework.data.elasticsearch.annotations.InnerField(
                suffix = "keyword",
                type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword,
                ignoreAbove = 256
            ),
        }
    )
    private String metadata;

    @NotNull
    @Column(name = "extracted_date", nullable = false)
    private Instant extractedDate;

    @ManyToOne(optional = false)
    @NotNull
    private CorrespondentExtraction extraction;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Correspondent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Correspondent name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public Correspondent email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public Correspondent phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return this.address;
    }

    public Correspondent address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany() {
        return this.company;
    }

    public Correspondent company(String company) {
        this.setCompany(company);
        return this;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public CorrespondentType getType() {
        return this.type;
    }

    public Correspondent type(CorrespondentType type) {
        this.setType(type);
        return this;
    }

    public void setType(CorrespondentType type) {
        this.type = type;
    }

    public CorrespondentRole getRole() {
        return this.role;
    }

    public Correspondent role(CorrespondentRole role) {
        this.setRole(role);
        return this;
    }

    public void setRole(CorrespondentRole role) {
        this.role = role;
    }

    public Double getConfidence() {
        return this.confidence;
    }

    public Correspondent confidence(Double confidence) {
        this.setConfidence(confidence);
        return this;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public Boolean getIsVerified() {
        return this.isVerified;
    }

    public Correspondent isVerified(Boolean isVerified) {
        this.setIsVerified(isVerified);
        return this;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public String getVerifiedBy() {
        return this.verifiedBy;
    }

    public Correspondent verifiedBy(String verifiedBy) {
        this.setVerifiedBy(verifiedBy);
        return this;
    }

    public void setVerifiedBy(String verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public Instant getVerifiedDate() {
        return this.verifiedDate;
    }

    public Correspondent verifiedDate(Instant verifiedDate) {
        this.setVerifiedDate(verifiedDate);
        return this;
    }

    public void setVerifiedDate(Instant verifiedDate) {
        this.verifiedDate = verifiedDate;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public Correspondent metadata(String metadata) {
        this.setMetadata(metadata);
        return this;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Instant getExtractedDate() {
        return this.extractedDate;
    }

    public Correspondent extractedDate(Instant extractedDate) {
        this.setExtractedDate(extractedDate);
        return this;
    }

    public void setExtractedDate(Instant extractedDate) {
        this.extractedDate = extractedDate;
    }

    public CorrespondentExtraction getExtraction() {
        return this.extraction;
    }

    public void setExtraction(CorrespondentExtraction correspondentExtraction) {
        this.extraction = correspondentExtraction;
    }

    public Correspondent extraction(CorrespondentExtraction correspondentExtraction) {
        this.setExtraction(correspondentExtraction);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Correspondent)) {
            return false;
        }
        return getId() != null && getId().equals(((Correspondent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Correspondent{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            ", company='" + getCompany() + "'" +
            ", type='" + getType() + "'" +
            ", role='" + getRole() + "'" +
            ", confidence=" + getConfidence() +
            ", isVerified='" + getIsVerified() + "'" +
            ", verifiedBy='" + getVerifiedBy() + "'" +
            ", verifiedDate='" + getVerifiedDate() + "'" +
            ", metadata='" + getMetadata() + "'" +
            ", extractedDate='" + getExtractedDate() + "'" +
            "}";
    }
}
