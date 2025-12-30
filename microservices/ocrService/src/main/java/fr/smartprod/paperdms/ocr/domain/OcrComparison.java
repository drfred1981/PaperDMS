package fr.smartprod.paperdms.ocr.domain;

import fr.smartprod.paperdms.ocr.domain.enumeration.OcrEngine;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OcrComparison.
 */
@Entity
@Table(name = "ocr_comparison")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "ocrcomparison")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OcrComparison implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 64)
    @Column(name = "document_sha_256", length = 64, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String documentSha256;

    @NotNull
    @Column(name = "page_number", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer pageNumber;

    @Lob
    @Column(name = "tika_text")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String tikaText;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    @Column(name = "tika_confidence")
    private Double tikaConfidence;

    @Lob
    @Column(name = "ai_text")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String aiText;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    @Column(name = "ai_confidence")
    private Double aiConfidence;

    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    @Column(name = "similarity")
    private Double similarity;

    @Lob
    @Column(name = "differences")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String differences;

    @Size(max = 1000)
    @Column(name = "differences_s_3_key", length = 1000)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String differencesS3Key;

    @Enumerated(EnumType.STRING)
    @Column(name = "selected_engine")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private OcrEngine selectedEngine;

    @Size(max = 50)
    @Column(name = "selected_by", length = 50)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String selectedBy;

    @Column(name = "selected_date")
    private Instant selectedDate;

    @NotNull
    @Column(name = "comparison_date", nullable = false)
    private Instant comparisonDate;

    @Lob
    @Column(name = "metadata")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String metadata;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OcrComparison id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentSha256() {
        return this.documentSha256;
    }

    public OcrComparison documentSha256(String documentSha256) {
        this.setDocumentSha256(documentSha256);
        return this;
    }

    public void setDocumentSha256(String documentSha256) {
        this.documentSha256 = documentSha256;
    }

    public Integer getPageNumber() {
        return this.pageNumber;
    }

    public OcrComparison pageNumber(Integer pageNumber) {
        this.setPageNumber(pageNumber);
        return this;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getTikaText() {
        return this.tikaText;
    }

    public OcrComparison tikaText(String tikaText) {
        this.setTikaText(tikaText);
        return this;
    }

    public void setTikaText(String tikaText) {
        this.tikaText = tikaText;
    }

    public Double getTikaConfidence() {
        return this.tikaConfidence;
    }

    public OcrComparison tikaConfidence(Double tikaConfidence) {
        this.setTikaConfidence(tikaConfidence);
        return this;
    }

    public void setTikaConfidence(Double tikaConfidence) {
        this.tikaConfidence = tikaConfidence;
    }

    public String getAiText() {
        return this.aiText;
    }

    public OcrComparison aiText(String aiText) {
        this.setAiText(aiText);
        return this;
    }

    public void setAiText(String aiText) {
        this.aiText = aiText;
    }

    public Double getAiConfidence() {
        return this.aiConfidence;
    }

    public OcrComparison aiConfidence(Double aiConfidence) {
        this.setAiConfidence(aiConfidence);
        return this;
    }

    public void setAiConfidence(Double aiConfidence) {
        this.aiConfidence = aiConfidence;
    }

    public Double getSimilarity() {
        return this.similarity;
    }

    public OcrComparison similarity(Double similarity) {
        this.setSimilarity(similarity);
        return this;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

    public String getDifferences() {
        return this.differences;
    }

    public OcrComparison differences(String differences) {
        this.setDifferences(differences);
        return this;
    }

    public void setDifferences(String differences) {
        this.differences = differences;
    }

    public String getDifferencesS3Key() {
        return this.differencesS3Key;
    }

    public OcrComparison differencesS3Key(String differencesS3Key) {
        this.setDifferencesS3Key(differencesS3Key);
        return this;
    }

    public void setDifferencesS3Key(String differencesS3Key) {
        this.differencesS3Key = differencesS3Key;
    }

    public OcrEngine getSelectedEngine() {
        return this.selectedEngine;
    }

    public OcrComparison selectedEngine(OcrEngine selectedEngine) {
        this.setSelectedEngine(selectedEngine);
        return this;
    }

    public void setSelectedEngine(OcrEngine selectedEngine) {
        this.selectedEngine = selectedEngine;
    }

    public String getSelectedBy() {
        return this.selectedBy;
    }

    public OcrComparison selectedBy(String selectedBy) {
        this.setSelectedBy(selectedBy);
        return this;
    }

    public void setSelectedBy(String selectedBy) {
        this.selectedBy = selectedBy;
    }

    public Instant getSelectedDate() {
        return this.selectedDate;
    }

    public OcrComparison selectedDate(Instant selectedDate) {
        this.setSelectedDate(selectedDate);
        return this;
    }

    public void setSelectedDate(Instant selectedDate) {
        this.selectedDate = selectedDate;
    }

    public Instant getComparisonDate() {
        return this.comparisonDate;
    }

    public OcrComparison comparisonDate(Instant comparisonDate) {
        this.setComparisonDate(comparisonDate);
        return this;
    }

    public void setComparisonDate(Instant comparisonDate) {
        this.comparisonDate = comparisonDate;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public OcrComparison metadata(String metadata) {
        this.setMetadata(metadata);
        return this;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OcrComparison)) {
            return false;
        }
        return getId() != null && getId().equals(((OcrComparison) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OcrComparison{" +
            "id=" + getId() +
            ", documentSha256='" + getDocumentSha256() + "'" +
            ", pageNumber=" + getPageNumber() +
            ", tikaText='" + getTikaText() + "'" +
            ", tikaConfidence=" + getTikaConfidence() +
            ", aiText='" + getAiText() + "'" +
            ", aiConfidence=" + getAiConfidence() +
            ", similarity=" + getSimilarity() +
            ", differences='" + getDifferences() + "'" +
            ", differencesS3Key='" + getDifferencesS3Key() + "'" +
            ", selectedEngine='" + getSelectedEngine() + "'" +
            ", selectedBy='" + getSelectedBy() + "'" +
            ", selectedDate='" + getSelectedDate() + "'" +
            ", comparisonDate='" + getComparisonDate() + "'" +
            ", metadata='" + getMetadata() + "'" +
            "}";
    }
}
