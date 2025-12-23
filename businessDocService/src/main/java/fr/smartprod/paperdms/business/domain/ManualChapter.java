package fr.smartprod.paperdms.business.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ManualChapter.
 */
@Entity
@Table(name = "manual_chapter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ManualChapter implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "manual_id", nullable = false)
    private Long manualId;

    @NotNull
    @Size(max = 50)
    @Column(name = "chapter_number", length = 50, nullable = false)
    private String chapterNumber;

    @NotNull
    @Size(max = 500)
    @Column(name = "title", length = 500, nullable = false)
    private String title;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "page_start")
    private Integer pageStart;

    @Column(name = "page_end")
    private Integer pageEnd;

    @NotNull
    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "display_order")
    private Integer displayOrder;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentChapter")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subchapters", "manual", "parentChapter" }, allowSetters = true)
    private Set<ManualChapter> subchapters = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private Manual manual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "subchapters", "manual", "parentChapter" }, allowSetters = true)
    private ManualChapter parentChapter;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ManualChapter id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getManualId() {
        return this.manualId;
    }

    public ManualChapter manualId(Long manualId) {
        this.setManualId(manualId);
        return this;
    }

    public void setManualId(Long manualId) {
        this.manualId = manualId;
    }

    public String getChapterNumber() {
        return this.chapterNumber;
    }

    public ManualChapter chapterNumber(String chapterNumber) {
        this.setChapterNumber(chapterNumber);
        return this;
    }

    public void setChapterNumber(String chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public String getTitle() {
        return this.title;
    }

    public ManualChapter title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public ManualChapter content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPageStart() {
        return this.pageStart;
    }

    public ManualChapter pageStart(Integer pageStart) {
        this.setPageStart(pageStart);
        return this;
    }

    public void setPageStart(Integer pageStart) {
        this.pageStart = pageStart;
    }

    public Integer getPageEnd() {
        return this.pageEnd;
    }

    public ManualChapter pageEnd(Integer pageEnd) {
        this.setPageEnd(pageEnd);
        return this;
    }

    public void setPageEnd(Integer pageEnd) {
        this.pageEnd = pageEnd;
    }

    public Integer getLevel() {
        return this.level;
    }

    public ManualChapter level(Integer level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public ManualChapter displayOrder(Integer displayOrder) {
        this.setDisplayOrder(displayOrder);
        return this;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Set<ManualChapter> getSubchapters() {
        return this.subchapters;
    }

    public void setSubchapters(Set<ManualChapter> manualChapters) {
        if (this.subchapters != null) {
            this.subchapters.forEach(i -> i.setParentChapter(null));
        }
        if (manualChapters != null) {
            manualChapters.forEach(i -> i.setParentChapter(this));
        }
        this.subchapters = manualChapters;
    }

    public ManualChapter subchapters(Set<ManualChapter> manualChapters) {
        this.setSubchapters(manualChapters);
        return this;
    }

    public ManualChapter addSubchapters(ManualChapter manualChapter) {
        this.subchapters.add(manualChapter);
        manualChapter.setParentChapter(this);
        return this;
    }

    public ManualChapter removeSubchapters(ManualChapter manualChapter) {
        this.subchapters.remove(manualChapter);
        manualChapter.setParentChapter(null);
        return this;
    }

    public Manual getManual() {
        return this.manual;
    }

    public void setManual(Manual manual) {
        this.manual = manual;
    }

    public ManualChapter manual(Manual manual) {
        this.setManual(manual);
        return this;
    }

    public ManualChapter getParentChapter() {
        return this.parentChapter;
    }

    public void setParentChapter(ManualChapter manualChapter) {
        this.parentChapter = manualChapter;
    }

    public ManualChapter parentChapter(ManualChapter manualChapter) {
        this.setParentChapter(manualChapter);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ManualChapter)) {
            return false;
        }
        return getId() != null && getId().equals(((ManualChapter) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManualChapter{" +
            "id=" + getId() +
            ", manualId=" + getManualId() +
            ", chapterNumber='" + getChapterNumber() + "'" +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", pageStart=" + getPageStart() +
            ", pageEnd=" + getPageEnd() +
            ", level=" + getLevel() +
            ", displayOrder=" + getDisplayOrder() +
            "}";
    }
}
