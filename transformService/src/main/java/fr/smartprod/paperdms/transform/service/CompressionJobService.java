package fr.smartprod.paperdms.transform.service;

import fr.smartprod.paperdms.transform.service.dto.CompressionJobDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.transform.domain.CompressionJob}.
 */
public interface CompressionJobService {
    /**
     * Save a compressionJob.
     *
     * @param compressionJobDTO the entity to save.
     * @return the persisted entity.
     */
    CompressionJobDTO save(CompressionJobDTO compressionJobDTO);

    /**
     * Updates a compressionJob.
     *
     * @param compressionJobDTO the entity to update.
     * @return the persisted entity.
     */
    CompressionJobDTO update(CompressionJobDTO compressionJobDTO);

    /**
     * Partially updates a compressionJob.
     *
     * @param compressionJobDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CompressionJobDTO> partialUpdate(CompressionJobDTO compressionJobDTO);

    /**
     * Get all the compressionJobs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CompressionJobDTO> findAll(Pageable pageable);

    /**
     * Get the "id" compressionJob.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CompressionJobDTO> findOne(Long id);

    /**
     * Delete the "id" compressionJob.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
