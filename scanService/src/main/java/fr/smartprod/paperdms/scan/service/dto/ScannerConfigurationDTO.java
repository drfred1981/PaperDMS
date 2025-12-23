package fr.smartprod.paperdms.scan.service.dto;

import fr.smartprod.paperdms.scan.domain.enumeration.ColorMode;
import fr.smartprod.paperdms.scan.domain.enumeration.ScanFormat;
import fr.smartprod.paperdms.scan.domain.enumeration.ScannerType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.scan.domain.ScannerConfiguration} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ScannerConfigurationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private ScannerType scannerType;

    @Size(max = 45)
    private String ipAddress;

    private Integer port;

    @Size(max = 50)
    private String protocol;

    @Size(max = 100)
    private String manufacturer;

    @Size(max = 100)
    private String model;

    private ColorMode defaultColorMode;

    private Integer defaultResolution;

    private ScanFormat defaultFormat;

    @Lob
    private String capabilities;

    @NotNull
    private Boolean isActive;

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

    public ScannerType getScannerType() {
        return scannerType;
    }

    public void setScannerType(ScannerType scannerType) {
        this.scannerType = scannerType;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public ColorMode getDefaultColorMode() {
        return defaultColorMode;
    }

    public void setDefaultColorMode(ColorMode defaultColorMode) {
        this.defaultColorMode = defaultColorMode;
    }

    public Integer getDefaultResolution() {
        return defaultResolution;
    }

    public void setDefaultResolution(Integer defaultResolution) {
        this.defaultResolution = defaultResolution;
    }

    public ScanFormat getDefaultFormat() {
        return defaultFormat;
    }

    public void setDefaultFormat(ScanFormat defaultFormat) {
        this.defaultFormat = defaultFormat;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
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
        if (!(o instanceof ScannerConfigurationDTO)) {
            return false;
        }

        ScannerConfigurationDTO scannerConfigurationDTO = (ScannerConfigurationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, scannerConfigurationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScannerConfigurationDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", scannerType='" + getScannerType() + "'" +
            ", ipAddress='" + getIpAddress() + "'" +
            ", port=" + getPort() +
            ", protocol='" + getProtocol() + "'" +
            ", manufacturer='" + getManufacturer() + "'" +
            ", model='" + getModel() + "'" +
            ", defaultColorMode='" + getDefaultColorMode() + "'" +
            ", defaultResolution=" + getDefaultResolution() +
            ", defaultFormat='" + getDefaultFormat() + "'" +
            ", capabilities='" + getCapabilities() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
