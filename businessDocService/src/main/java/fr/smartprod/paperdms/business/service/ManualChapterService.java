package fr.smartprod.paperdms.business.service;

import fr.smartprod.paperdms.business.service.dto.ManualChapterDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.business.domain.ManualChapter}.
 */
public interface ManualChapterService {
    /**
     * Save a manualChapter.
     *
     * @param manualChapterDTO the entity to save.
     * @return the persisted entity.
     */
    ManualChapterDTO save(ManualChapterDTO manualChapterDTO);

    /**
     * Updates a manualChapter.
     *
     * @param manualChapterDTO the entity to update.
     * @return the persisted entity.
     */
    ManualChapterDTO update(ManualChapterDTO manualChapterDTO);

    /**
     * Partially updates a manualChapter.
     *
     * @param manualChapterDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ManualChapterDTO> partialUpdate(ManualChapterDTO manualChapterDTO);

    /**
     * Get all the manualChapters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ManualChapterDTO> findAll(Pageable pageable);

    /**
     * Get the "id" manualChapter.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ManualChapterDTO> findOne(Long id);

    /**
     * Delete the "id" manualChapter.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
