package fr.smartprod.paperdms.transform.service;

import fr.smartprod.paperdms.transform.service.dto.ComparisonJobDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.transform.domain.ComparisonJob}.
 */
public interface ComparisonJobService {
    /**
     * Save a comparisonJob.
     *
     * @param comparisonJobDTO the entity to save.
     * @return the persisted entity.
     */
    ComparisonJobDTO save(ComparisonJobDTO comparisonJobDTO);

    /**
     * Updates a comparisonJob.
     *
     * @param comparisonJobDTO the entity to update.
     * @return the persisted entity.
     */
    ComparisonJobDTO update(ComparisonJobDTO comparisonJobDTO);

    /**
     * Partially updates a comparisonJob.
     *
     * @param comparisonJobDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ComparisonJobDTO> partialUpdate(ComparisonJobDTO comparisonJobDTO);

    /**
     * Get all the comparisonJobs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ComparisonJobDTO> findAll(Pageable pageable);

    /**
     * Get the "id" comparisonJob.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ComparisonJobDTO> findOne(Long id);

    /**
     * Delete the "id" comparisonJob.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
