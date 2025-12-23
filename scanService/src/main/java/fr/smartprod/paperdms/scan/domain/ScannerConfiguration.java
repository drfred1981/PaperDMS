package fr.smartprod.paperdms.scan.domain;

import fr.smartprod.paperdms.scan.domain.enumeration.ColorMode;
import fr.smartprod.paperdms.scan.domain.enumeration.ScanFormat;
import fr.smartprod.paperdms.scan.domain.enumeration.ScannerType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ScannerConfiguration.
 */
@Entity
@Table(name = "scanner_configuration")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ScannerConfiguration implements Serializable {

    @Serial
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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "scanner_type", nullable = false)
    private ScannerType scannerType;

    @Size(max = 45)
    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "port")
    private Integer port;

    @Size(max = 50)
    @Column(name = "protocol", length = 50)
    private String protocol;

    @Size(max = 100)
    @Column(name = "manufacturer", length = 100)
    private String manufacturer;

    @Size(max = 100)
    @Column(name = "model", length = 100)
    private String model;

    @Enumerated(EnumType.STRING)
    @Column(name = "default_color_mode")
    private ColorMode defaultColorMode;

    @Column(name = "default_resolution")
    private Integer defaultResolution;

    @Enumerated(EnumType.STRING)
    @Column(name = "default_format")
    private ScanFormat defaultFormat;

    @Lob
    @Column(name = "capabilities")
    private String capabilities;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ScannerConfiguration id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ScannerConfiguration name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ScannerType getScannerType() {
        return this.scannerType;
    }

    public ScannerConfiguration scannerType(ScannerType scannerType) {
        this.setScannerType(scannerType);
        return this;
    }

    public void setScannerType(ScannerType scannerType) {
        this.scannerType = scannerType;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public ScannerConfiguration ipAddress(String ipAddress) {
        this.setIpAddress(ipAddress);
        return this;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getPort() {
        return this.port;
    }

    public ScannerConfiguration port(Integer port) {
        this.setPort(port);
        return this;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public ScannerConfiguration protocol(String protocol) {
        this.setProtocol(protocol);
        return this;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public ScannerConfiguration manufacturer(String manufacturer) {
        this.setManufacturer(manufacturer);
        return this;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return this.model;
    }

    public ScannerConfiguration model(String model) {
        this.setModel(model);
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public ColorMode getDefaultColorMode() {
        return this.defaultColorMode;
    }

    public ScannerConfiguration defaultColorMode(ColorMode defaultColorMode) {
        this.setDefaultColorMode(defaultColorMode);
        return this;
    }

    public void setDefaultColorMode(ColorMode defaultColorMode) {
        this.defaultColorMode = defaultColorMode;
    }

    public Integer getDefaultResolution() {
        return this.defaultResolution;
    }

    public ScannerConfiguration defaultResolution(Integer defaultResolution) {
        this.setDefaultResolution(defaultResolution);
        return this;
    }

    public void setDefaultResolution(Integer defaultResolution) {
        this.defaultResolution = defaultResolution;
    }

    public ScanFormat getDefaultFormat() {
        return this.defaultFormat;
    }

    public ScannerConfiguration defaultFormat(ScanFormat defaultFormat) {
        this.setDefaultFormat(defaultFormat);
        return this;
    }

    public void setDefaultFormat(ScanFormat defaultFormat) {
        this.defaultFormat = defaultFormat;
    }

    public String getCapabilities() {
        return this.capabilities;
    }

    public ScannerConfiguration capabilities(String capabilities) {
        this.setCapabilities(capabilities);
        return this;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public ScannerConfiguration isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public ScannerConfiguration createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public ScannerConfiguration lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ScannerConfiguration)) {
            return false;
        }
        return getId() != null && getId().equals(((ScannerConfiguration) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScannerConfiguration{" +
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
