package fr.smartprod.paperdms.archive.service;

import fr.smartprod.paperdms.archive.service.dto.ArchiveJobDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.archive.domain.ArchiveJob}.
 */
public interface ArchiveJobService {
    /**
     * Save a archiveJob.
     *
     * @param archiveJobDTO the entity to save.
     * @return the persisted entity.
     */
    ArchiveJobDTO save(ArchiveJobDTO archiveJobDTO);

    /**
     * Updates a archiveJob.
     *
     * @param archiveJobDTO the entity to update.
     * @return the persisted entity.
     */
    ArchiveJobDTO update(ArchiveJobDTO archiveJobDTO);

    /**
     * Partially updates a archiveJob.
     *
     * @param archiveJobDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ArchiveJobDTO> partialUpdate(ArchiveJobDTO archiveJobDTO);

    /**
     * Get the "id" archiveJob.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ArchiveJobDTO> findOne(Long id);

    /**
     * Delete the "id" archiveJob.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
