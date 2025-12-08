package com.ged.ocr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Configuration Apache Tika
 */
@Entity
@Table(name = "tika_configuration")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TikaConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false, unique = true)
    private String name;

    @NotNull
    @Size(max = 500)
    @Column(name = "endpoint", length = 500, nullable = false)
    private String endpoint;

    @Size(max = 255)
    @Column(name = "api_key", length = 255)
    private String apiKey;

    @Column(name = "timeout")
    private Integer timeout;

    @Column(name = "max_file_size")
    private Long maxFileSize;

    @Size(max = 500)
    @Column(name = "supported_languages", length = 500)
    private String supportedLanguages;

    @NotNull
    @Column(name = "enable_ocr", nullable = false)
    private Boolean enableOcr;

    @Size(max = 50)
    @Column(name = "ocr_engine", length = 50)
    private String ocrEngine;

    @NotNull
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TikaConfiguration id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public TikaConfiguration name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndpoint() {
        return this.endpoint;
    }

    public TikaConfiguration endpoint(String endpoint) {
        this.setEndpoint(endpoint);
        return this;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public TikaConfiguration apiKey(String apiKey) {
        this.setApiKey(apiKey);
        return this;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Integer getTimeout() {
        return this.timeout;
    }

    public TikaConfiguration timeout(Integer timeout) {
        this.setTimeout(timeout);
        return this;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Long getMaxFileSize() {
        return this.maxFileSize;
    }

    public TikaConfiguration maxFileSize(Long maxFileSize) {
        this.setMaxFileSize(maxFileSize);
        return this;
    }

    public void setMaxFileSize(Long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public String getSupportedLanguages() {
        return this.supportedLanguages;
    }

    public TikaConfiguration supportedLanguages(String supportedLanguages) {
        this.setSupportedLanguages(supportedLanguages);
        return this;
    }

    public void setSupportedLanguages(String supportedLanguages) {
        this.supportedLanguages = supportedLanguages;
    }

    public Boolean getEnableOcr() {
        return this.enableOcr;
    }

    public TikaConfiguration enableOcr(Boolean enableOcr) {
        this.setEnableOcr(enableOcr);
        return this;
    }

    public void setEnableOcr(Boolean enableOcr) {
        this.enableOcr = enableOcr;
    }

    public String getOcrEngine() {
        return this.ocrEngine;
    }

    public TikaConfiguration ocrEngine(String ocrEngine) {
        this.setOcrEngine(ocrEngine);
        return this;
    }

    public void setOcrEngine(String ocrEngine) {
        this.ocrEngine = ocrEngine;
    }

    public Boolean getIsDefault() {
        return this.isDefault;
    }

    public TikaConfiguration isDefault(Boolean isDefault) {
        this.setIsDefault(isDefault);
        return this;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public TikaConfiguration isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public TikaConfiguration createdDate(Instant createdDate) {
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
        if (!(o instanceof TikaConfiguration)) {
            return false;
        }
        return getId() != null && getId().equals(((TikaConfiguration) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TikaConfiguration{" +
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
