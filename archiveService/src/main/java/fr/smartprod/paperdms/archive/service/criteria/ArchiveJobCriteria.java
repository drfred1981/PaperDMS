package fr.smartprod.paperdms.archive.service.criteria;

import fr.smartprod.paperdms.archive.domain.enumeration.ArchiveFormat;
import fr.smartprod.paperdms.archive.domain.enumeration.ArchiveStatus;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.smartprod.paperdms.archive.domain.ArchiveJob} entity. This class is used
 * in {@link fr.smartprod.paperdms.archive.web.rest.ArchiveJobResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /archive-jobs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArchiveJobCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ArchiveFormat
     */
    public static class ArchiveFormatFilter extends Filter<ArchiveFormat> {

        public ArchiveFormatFilter() {}

        public ArchiveFormatFilter(ArchiveFormatFilter filter) {
            super(filter);
        }

        @Override
        public ArchiveFormatFilter copy() {
            return new ArchiveFormatFilter(this);
        }
    }

    /**
     * Class for filtering ArchiveStatus
     */
    public static class ArchiveStatusFilter extends Filter<ArchiveStatus> {

        public ArchiveStatusFilter() {}

        public ArchiveStatusFilter(ArchiveStatusFilter filter) {
            super(filter);
        }

        @Override
        public ArchiveStatusFilter copy() {
            return new ArchiveStatusFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private ArchiveFormatFilter archiveFormat;

    private IntegerFilter compressionLevel;

    private BooleanFilter encryptionEnabled;

    private StringFilter encryptionAlgorithm;

    private StringFilter password;

    private StringFilter s3ArchiveKey;

    private StringFilter archiveSha256;

    private LongFilter archiveSize;

    private IntegerFilter documentCount;

    private ArchiveStatusFilter status;

    private InstantFilter startDate;

    private InstantFilter endDate;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private Boolean distinct;

    public ArchiveJobCriteria() {}

    public ArchiveJobCriteria(ArchiveJobCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.archiveFormat = other.optionalArchiveFormat().map(ArchiveFormatFilter::copy).orElse(null);
        this.compressionLevel = other.optionalCompressionLevel().map(IntegerFilter::copy).orElse(null);
        this.encryptionEnabled = other.optionalEncryptionEnabled().map(BooleanFilter::copy).orElse(null);
        this.encryptionAlgorithm = other.optionalEncryptionAlgorithm().map(StringFilter::copy).orElse(null);
        this.password = other.optionalPassword().map(StringFilter::copy).orElse(null);
        this.s3ArchiveKey = other.optionals3ArchiveKey().map(StringFilter::copy).orElse(null);
        this.archiveSha256 = other.optionalArchiveSha256().map(StringFilter::copy).orElse(null);
        this.archiveSize = other.optionalArchiveSize().map(LongFilter::copy).orElse(null);
        this.documentCount = other.optionalDocumentCount().map(IntegerFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ArchiveStatusFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(InstantFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(InstantFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ArchiveJobCriteria copy() {
        return new ArchiveJobCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public ArchiveFormatFilter getArchiveFormat() {
        return archiveFormat;
    }

    public Optional<ArchiveFormatFilter> optionalArchiveFormat() {
        return Optional.ofNullable(archiveFormat);
    }

    public ArchiveFormatFilter archiveFormat() {
        if (archiveFormat == null) {
            setArchiveFormat(new ArchiveFormatFilter());
        }
        return archiveFormat;
    }

    public void setArchiveFormat(ArchiveFormatFilter archiveFormat) {
        this.archiveFormat = archiveFormat;
    }

    public IntegerFilter getCompressionLevel() {
        return compressionLevel;
    }

    public Optional<IntegerFilter> optionalCompressionLevel() {
        return Optional.ofNullable(compressionLevel);
    }

    public IntegerFilter compressionLevel() {
        if (compressionLevel == null) {
            setCompressionLevel(new IntegerFilter());
        }
        return compressionLevel;
    }

    public void setCompressionLevel(IntegerFilter compressionLevel) {
        this.compressionLevel = compressionLevel;
    }

    public BooleanFilter getEncryptionEnabled() {
        return encryptionEnabled;
    }

    public Optional<BooleanFilter> optionalEncryptionEnabled() {
        return Optional.ofNullable(encryptionEnabled);
    }

    public BooleanFilter encryptionEnabled() {
        if (encryptionEnabled == null) {
            setEncryptionEnabled(new BooleanFilter());
        }
        return encryptionEnabled;
    }

    public void setEncryptionEnabled(BooleanFilter encryptionEnabled) {
        this.encryptionEnabled = encryptionEnabled;
    }

    public StringFilter getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    public Optional<StringFilter> optionalEncryptionAlgorithm() {
        return Optional.ofNullable(encryptionAlgorithm);
    }

    public StringFilter encryptionAlgorithm() {
        if (encryptionAlgorithm == null) {
            setEncryptionAlgorithm(new StringFilter());
        }
        return encryptionAlgorithm;
    }

    public void setEncryptionAlgorithm(StringFilter encryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm;
    }

    public StringFilter getPassword() {
        return password;
    }

    public Optional<StringFilter> optionalPassword() {
        return Optional.ofNullable(password);
    }

    public StringFilter password() {
        if (password == null) {
            setPassword(new StringFilter());
        }
        return password;
    }

    public void setPassword(StringFilter password) {
        this.password = password;
    }

    public StringFilter gets3ArchiveKey() {
        return s3ArchiveKey;
    }

    public Optional<StringFilter> optionals3ArchiveKey() {
        return Optional.ofNullable(s3ArchiveKey);
    }

    public StringFilter s3ArchiveKey() {
        if (s3ArchiveKey == null) {
            sets3ArchiveKey(new StringFilter());
        }
        return s3ArchiveKey;
    }

    public void sets3ArchiveKey(StringFilter s3ArchiveKey) {
        this.s3ArchiveKey = s3ArchiveKey;
    }

    public StringFilter getArchiveSha256() {
        return archiveSha256;
    }

    public Optional<StringFilter> optionalArchiveSha256() {
        return Optional.ofNullable(archiveSha256);
    }

    public StringFilter archiveSha256() {
        if (archiveSha256 == null) {
            setArchiveSha256(new StringFilter());
        }
        return archiveSha256;
    }

    public void setArchiveSha256(StringFilter archiveSha256) {
        this.archiveSha256 = archiveSha256;
    }

    public LongFilter getArchiveSize() {
        return archiveSize;
    }

    public Optional<LongFilter> optionalArchiveSize() {
        return Optional.ofNullable(archiveSize);
    }

    public LongFilter archiveSize() {
        if (archiveSize == null) {
            setArchiveSize(new LongFilter());
        }
        return archiveSize;
    }

    public void setArchiveSize(LongFilter archiveSize) {
        this.archiveSize = archiveSize;
    }

    public IntegerFilter getDocumentCount() {
        return documentCount;
    }

    public Optional<IntegerFilter> optionalDocumentCount() {
        return Optional.ofNullable(documentCount);
    }

    public IntegerFilter documentCount() {
        if (documentCount == null) {
            setDocumentCount(new IntegerFilter());
        }
        return documentCount;
    }

    public void setDocumentCount(IntegerFilter documentCount) {
        this.documentCount = documentCount;
    }

    public ArchiveStatusFilter getStatus() {
        return status;
    }

    public Optional<ArchiveStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ArchiveStatusFilter status() {
        if (status == null) {
            setStatus(new ArchiveStatusFilter());
        }
        return status;
    }

    public void setStatus(ArchiveStatusFilter status) {
        this.status = status;
    }

    public InstantFilter getStartDate() {
        return startDate;
    }

    public Optional<InstantFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public InstantFilter startDate() {
        if (startDate == null) {
            setStartDate(new InstantFilter());
        }
        return startDate;
    }

    public void setStartDate(InstantFilter startDate) {
        this.startDate = startDate;
    }

    public InstantFilter getEndDate() {
        return endDate;
    }

    public Optional<InstantFilter> optionalEndDate() {
        return Optional.ofNullable(endDate);
    }

    public InstantFilter endDate() {
        if (endDate == null) {
            setEndDate(new InstantFilter());
        }
        return endDate;
    }

    public void setEndDate(InstantFilter endDate) {
        this.endDate = endDate;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public Optional<StringFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new StringFilter());
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<InstantFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new InstantFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ArchiveJobCriteria that = (ArchiveJobCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(archiveFormat, that.archiveFormat) &&
            Objects.equals(compressionLevel, that.compressionLevel) &&
            Objects.equals(encryptionEnabled, that.encryptionEnabled) &&
            Objects.equals(encryptionAlgorithm, that.encryptionAlgorithm) &&
            Objects.equals(password, that.password) &&
            Objects.equals(s3ArchiveKey, that.s3ArchiveKey) &&
            Objects.equals(archiveSha256, that.archiveSha256) &&
            Objects.equals(archiveSize, that.archiveSize) &&
            Objects.equals(documentCount, that.documentCount) &&
            Objects.equals(status, that.status) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            archiveFormat,
            compressionLevel,
            encryptionEnabled,
            encryptionAlgorithm,
            password,
            s3ArchiveKey,
            archiveSha256,
            archiveSize,
            documentCount,
            status,
            startDate,
            endDate,
            createdBy,
            createdDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArchiveJobCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalArchiveFormat().map(f -> "archiveFormat=" + f + ", ").orElse("") +
            optionalCompressionLevel().map(f -> "compressionLevel=" + f + ", ").orElse("") +
            optionalEncryptionEnabled().map(f -> "encryptionEnabled=" + f + ", ").orElse("") +
            optionalEncryptionAlgorithm().map(f -> "encryptionAlgorithm=" + f + ", ").orElse("") +
            optionalPassword().map(f -> "password=" + f + ", ").orElse("") +
            optionals3ArchiveKey().map(f -> "s3ArchiveKey=" + f + ", ").orElse("") +
            optionalArchiveSha256().map(f -> "archiveSha256=" + f + ", ").orElse("") +
            optionalArchiveSize().map(f -> "archiveSize=" + f + ", ").orElse("") +
            optionalDocumentCount().map(f -> "documentCount=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
