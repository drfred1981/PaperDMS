package fr.smartprod.paperdms.ai.service;

import fr.smartprod.paperdms.ai.service.dto.TagPredictionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.ai.domain.TagPrediction}.
 */
public interface TagPredictionService {
    /**
     * Save a tagPrediction.
     *
     * @param tagPredictionDTO the entity to save.
     * @return the persisted entity.
     */
    TagPredictionDTO save(TagPredictionDTO tagPredictionDTO);

    /**
     * Updates a tagPrediction.
     *
     * @param tagPredictionDTO the entity to update.
     * @return the persisted entity.
     */
    TagPredictionDTO update(TagPredictionDTO tagPredictionDTO);

    /**
     * Partially updates a tagPrediction.
     *
     * @param tagPredictionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TagPredictionDTO> partialUpdate(TagPredictionDTO tagPredictionDTO);

    /**
     * Get all the tagPredictions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TagPredictionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" tagPrediction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TagPredictionDTO> findOne(Long id);

    /**
     * Delete the "id" tagPrediction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
