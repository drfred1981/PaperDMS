package fr.smartprod.paperdms.ai.service;

import fr.smartprod.paperdms.ai.service.dto.CorrespondentExtractionDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.ai.domain.CorrespondentExtraction}.
 */
public interface CorrespondentExtractionService {
    /**
     * Save a correspondentExtraction.
     *
     * @param correspondentExtractionDTO the entity to save.
     * @return the persisted entity.
     */
    CorrespondentExtractionDTO save(CorrespondentExtractionDTO correspondentExtractionDTO);

    /**
     * Updates a correspondentExtraction.
     *
     * @param correspondentExtractionDTO the entity to update.
     * @return the persisted entity.
     */
    CorrespondentExtractionDTO update(CorrespondentExtractionDTO correspondentExtractionDTO);

    /**
     * Partially updates a correspondentExtraction.
     *
     * @param correspondentExtractionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CorrespondentExtractionDTO> partialUpdate(CorrespondentExtractionDTO correspondentExtractionDTO);

    /**
     * Get the "id" correspondentExtraction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CorrespondentExtractionDTO> findOne(Long id);

    /**
     * Delete the "id" correspondentExtraction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
