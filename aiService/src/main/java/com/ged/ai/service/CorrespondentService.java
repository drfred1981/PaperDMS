package com.ged.ai.service;

import com.ged.ai.service.dto.CorrespondentDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ged.ai.domain.Correspondent}.
 */
public interface CorrespondentService {
    /**
     * Save a correspondent.
     *
     * @param correspondentDTO the entity to save.
     * @return the persisted entity.
     */
    CorrespondentDTO save(CorrespondentDTO correspondentDTO);

    /**
     * Updates a correspondent.
     *
     * @param correspondentDTO the entity to update.
     * @return the persisted entity.
     */
    CorrespondentDTO update(CorrespondentDTO correspondentDTO);

    /**
     * Partially updates a correspondent.
     *
     * @param correspondentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CorrespondentDTO> partialUpdate(CorrespondentDTO correspondentDTO);

    /**
     * Get the "id" correspondent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CorrespondentDTO> findOne(Long id);

    /**
     * Delete the "id" correspondent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the correspondent corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CorrespondentDTO> search(String query, Pageable pageable);
}
