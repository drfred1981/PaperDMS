package fr.smartprod.paperdms.ai.service.dto;

import fr.smartprod.paperdms.ai.domain.enumeration.CorrespondentRole;
import fr.smartprod.paperdms.ai.domain.enumeration.CorrespondentType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.ai.domain.AICorrespondentPrediction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AICorrespondentPredictionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String correspondentName;

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

    private CorrespondentType type;

    private CorrespondentRole role;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    private Double confidence;

    @Size(max = 500)
    private String reason;

    @Size(max = 50)
    private String modelVersion;

    @Size(max = 1000)
    private String predictionS3Key;

    private Boolean isAccepted;

    @Size(max = 50)
    private String acceptedBy;

    private Instant acceptedDate;

    @NotNull
    private Instant predictionDate;

    private AIAutoTagJobDTO job;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCorrespondentName() {
        return correspondentName;
    }

    public void setCorrespondentName(String correspondentName) {
        this.correspondentName = correspondentName;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getPredictionS3Key() {
        return predictionS3Key;
    }

    public void setPredictionS3Key(String predictionS3Key) {
        this.predictionS3Key = predictionS3Key;
    }

    public Boolean getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(Boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    public String getAcceptedBy() {
        return acceptedBy;
    }

    public void setAcceptedBy(String acceptedBy) {
        this.acceptedBy = acceptedBy;
    }

    public Instant getAcceptedDate() {
        return acceptedDate;
    }

    public void setAcceptedDate(Instant acceptedDate) {
        this.acceptedDate = acceptedDate;
    }

    public Instant getPredictionDate() {
        return predictionDate;
    }

    public void setPredictionDate(Instant predictionDate) {
        this.predictionDate = predictionDate;
    }

    public AIAutoTagJobDTO getJob() {
        return job;
    }

    public void setJob(AIAutoTagJobDTO job) {
        this.job = job;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AICorrespondentPredictionDTO)) {
            return false;
        }

        AICorrespondentPredictionDTO aICorrespondentPredictionDTO = (AICorrespondentPredictionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, aICorrespondentPredictionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AICorrespondentPredictionDTO{" +
            "id=" + getId() +
            ", correspondentName='" + getCorrespondentName() + "'" +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            ", company='" + getCompany() + "'" +
            ", type='" + getType() + "'" +
            ", role='" + getRole() + "'" +
            ", confidence=" + getConfidence() +
            ", reason='" + getReason() + "'" +
            ", modelVersion='" + getModelVersion() + "'" +
            ", predictionS3Key='" + getPredictionS3Key() + "'" +
            ", isAccepted='" + getIsAccepted() + "'" +
            ", acceptedBy='" + getAcceptedBy() + "'" +
            ", acceptedDate='" + getAcceptedDate() + "'" +
            ", predictionDate='" + getPredictionDate() + "'" +
            ", job=" + getJob() +
            "}";
    }
}
