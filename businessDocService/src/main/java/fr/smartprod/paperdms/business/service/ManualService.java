package fr.smartprod.paperdms.business.service;

import fr.smartprod.paperdms.business.service.dto.ManualDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.business.domain.Manual}.
 */
public interface ManualService {
    /**
     * Save a manual.
     *
     * @param manualDTO the entity to save.
     * @return the persisted entity.
     */
    ManualDTO save(ManualDTO manualDTO);

    /**
     * Updates a manual.
     *
     * @param manualDTO the entity to update.
     * @return the persisted entity.
     */
    ManualDTO update(ManualDTO manualDTO);

    /**
     * Partially updates a manual.
     *
     * @param manualDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ManualDTO> partialUpdate(ManualDTO manualDTO);

    /**
     * Get the "id" manual.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ManualDTO> findOne(Long id);

    /**
     * Delete the "id" manual.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the manual corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ManualDTO> search(String query, Pageable pageable);
}
