package fr.smartprod.paperdms.business.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.smartprod.paperdms.business.domain.ManualChapter} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ManualChapterDTO implements Serializable {

    private Long id;

    @NotNull
    private Long manualId;

    @NotNull
    @Size(max = 50)
    private String chapterNumber;

    @NotNull
    @Size(max = 500)
    private String title;

    @Lob
    private String content;

    private Integer pageStart;

    private Integer pageEnd;

    @NotNull
    private Integer level;

    private Integer displayOrder;

    @NotNull
    private ManualDTO manual;

    private ManualChapterDTO parentChapter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getManualId() {
        return manualId;
    }

    public void setManualId(Long manualId) {
        this.manualId = manualId;
    }

    public String getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(String chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPageStart() {
        return pageStart;
    }

    public void setPageStart(Integer pageStart) {
        this.pageStart = pageStart;
    }

    public Integer getPageEnd() {
        return pageEnd;
    }

    public void setPageEnd(Integer pageEnd) {
        this.pageEnd = pageEnd;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public ManualDTO getManual() {
        return manual;
    }

    public void setManual(ManualDTO manual) {
        this.manual = manual;
    }

    public ManualChapterDTO getParentChapter() {
        return parentChapter;
    }

    public void setParentChapter(ManualChapterDTO parentChapter) {
        this.parentChapter = parentChapter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ManualChapterDTO)) {
            return false;
        }

        ManualChapterDTO manualChapterDTO = (ManualChapterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, manualChapterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManualChapterDTO{" +
            "id=" + getId() +
            ", manualId=" + getManualId() +
            ", chapterNumber='" + getChapterNumber() + "'" +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", pageStart=" + getPageStart() +
            ", pageEnd=" + getPageEnd() +
            ", level=" + getLevel() +
            ", displayOrder=" + getDisplayOrder() +
            ", manual=" + getManual() +
            ", parentChapter=" + getParentChapter() +
            "}";
    }
}
