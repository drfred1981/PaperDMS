package fr.smartprod.paperdms.scan.service;

import fr.smartprod.paperdms.scan.service.dto.ScannedPageDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.scan.domain.ScannedPage}.
 */
public interface ScannedPageService {
    /**
     * Save a scannedPage.
     *
     * @param scannedPageDTO the entity to save.
     * @return the persisted entity.
     */
    ScannedPageDTO save(ScannedPageDTO scannedPageDTO);

    /**
     * Updates a scannedPage.
     *
     * @param scannedPageDTO the entity to update.
     * @return the persisted entity.
     */
    ScannedPageDTO update(ScannedPageDTO scannedPageDTO);

    /**
     * Partially updates a scannedPage.
     *
     * @param scannedPageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ScannedPageDTO> partialUpdate(ScannedPageDTO scannedPageDTO);

    /**
     * Get all the scannedPages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ScannedPageDTO> findAll(Pageable pageable);

    /**
     * Get the "id" scannedPage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ScannedPageDTO> findOne(Long id);

    /**
     * Delete the "id" scannedPage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
