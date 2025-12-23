package fr.smartprod.paperdms.transform.service;

import fr.smartprod.paperdms.transform.service.dto.MergeJobDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.transform.domain.MergeJob}.
 */
public interface MergeJobService {
    /**
     * Save a mergeJob.
     *
     * @param mergeJobDTO the entity to save.
     * @return the persisted entity.
     */
    MergeJobDTO save(MergeJobDTO mergeJobDTO);

    /**
     * Updates a mergeJob.
     *
     * @param mergeJobDTO the entity to update.
     * @return the persisted entity.
     */
    MergeJobDTO update(MergeJobDTO mergeJobDTO);

    /**
     * Partially updates a mergeJob.
     *
     * @param mergeJobDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MergeJobDTO> partialUpdate(MergeJobDTO mergeJobDTO);

    /**
     * Get all the mergeJobs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MergeJobDTO> findAll(Pageable pageable);

    /**
     * Get the "id" mergeJob.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MergeJobDTO> findOne(Long id);

    /**
     * Delete the "id" mergeJob.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
