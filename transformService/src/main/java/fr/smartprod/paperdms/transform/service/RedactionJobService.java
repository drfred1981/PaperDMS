package fr.smartprod.paperdms.transform.service;

import fr.smartprod.paperdms.transform.service.dto.RedactionJobDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.transform.domain.RedactionJob}.
 */
public interface RedactionJobService {
    /**
     * Save a redactionJob.
     *
     * @param redactionJobDTO the entity to save.
     * @return the persisted entity.
     */
    RedactionJobDTO save(RedactionJobDTO redactionJobDTO);

    /**
     * Updates a redactionJob.
     *
     * @param redactionJobDTO the entity to update.
     * @return the persisted entity.
     */
    RedactionJobDTO update(RedactionJobDTO redactionJobDTO);

    /**
     * Partially updates a redactionJob.
     *
     * @param redactionJobDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RedactionJobDTO> partialUpdate(RedactionJobDTO redactionJobDTO);

    /**
     * Get all the redactionJobs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RedactionJobDTO> findAll(Pageable pageable);

    /**
     * Get the "id" redactionJob.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RedactionJobDTO> findOne(Long id);

    /**
     * Delete the "id" redactionJob.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
