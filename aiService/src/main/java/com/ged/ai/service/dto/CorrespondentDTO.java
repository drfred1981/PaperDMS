package com.ged.ai.service.dto;

import com.ged.ai.domain.enumeration.CorrespondentRole;
import com.ged.ai.domain.enumeration.CorrespondentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ged.ai.domain.Correspondent} entity.
 */
@Schema(description = "Correspondant (exp�diteur/destinataire)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CorrespondentDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String email;

    @Size(max = 50)
    private String phone;

    @Lob
    private String address;

    @Size(max = 255)
    private String company;

    @NotNull
    private CorrespondentType type;

    @NotNull
    private CorrespondentRole role;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    private Double confidence;

    @NotNull
    private Boolean isVerified;

    @Size(max = 50)
    private String verifiedBy;

    private Instant verifiedDate;

    @Lob
    private String metadata;

    @NotNull
    private Instant extractedDate;

    @NotNull
    private CorrespondentExtractionDTO extraction;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public CorrespondentType getType() {
        return type;
    }

    public void setType(CorrespondentType type) {
        this.type = type;
    }

    public CorrespondentRole getRole() {
        return role;
    }

    public void setRole(CorrespondentRole role) {
        this.role = role;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public String getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(String verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public Instant getVerifiedDate() {
        return verifiedDate;
    }

    public void setVerifiedDate(Instant verifiedDate) {
        this.verifiedDate = verifiedDate;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Instant getExtractedDate() {
        return extractedDate;
    }

    public void setExtractedDate(Instant extractedDate) {
        this.extractedDate = extractedDate;
    }

    public CorrespondentExtractionDTO getExtraction() {
        return extraction;
    }

    public void setExtraction(CorrespondentExtractionDTO extraction) {
        this.extraction = extraction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CorrespondentDTO)) {
            return false;
        }

        CorrespondentDTO correspondentDTO = (CorrespondentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, correspondentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CorrespondentDTO{" +
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
            ", extraction=" + getExtraction() +
            "}";
    }
}
