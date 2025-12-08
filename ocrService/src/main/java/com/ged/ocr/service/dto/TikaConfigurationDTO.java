package com.ged.ocr.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.ged.ocr.domain.TikaConfiguration} entity.
 */
@Schema(description = "Configuration Apache Tika")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TikaConfigurationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Size(max = 500)
    private String endpoint;

    @Size(max = 255)
    private String apiKey;

    private Integer timeout;

    private Long maxFileSize;

    @Size(max = 500)
    private String supportedLanguages;

    @NotNull
    private Boolean enableOcr;

    @Size(max = 50)
    private String ocrEngine;

    @NotNull
    private Boolean isDefault;

    @NotNull
    private Boolean isActive;

    @NotNull
    private Instant createdDate;

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

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Long getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(Long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public String getSupportedLanguages() {
        return supportedLanguages;
    }

    public void setSupportedLanguages(String supportedLanguages) {
        this.supportedLanguages = supportedLanguages;
    }

    public Boolean getEnableOcr() {
        return enableOcr;
    }

    public void setEnableOcr(Boolean enableOcr) {
        this.enableOcr = enableOcr;
    }

    public String getOcrEngine() {
        return ocrEngine;
    }

    public void setOcrEngine(String ocrEngine) {
        this.ocrEngine = ocrEngine;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TikaConfigurationDTO)) {
            return false;
        }

        TikaConfigurationDTO tikaConfigurationDTO = (TikaConfigurationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tikaConfigurationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TikaConfigurationDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", endpoint='" + getEndpoint() + "'" +
            ", apiKey='" + getApiKey() + "'" +
            ", timeout=" + getTimeout() +
            ", maxFileSize=" + getMaxFileSize() +
            ", supportedLanguages='" + getSupportedLanguages() + "'" +
            ", enableOcr='" + getEnableOcr() + "'" +
            ", ocrEngine='" + getOcrEngine() + "'" +
            ", isDefault='" + getIsDefault() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
