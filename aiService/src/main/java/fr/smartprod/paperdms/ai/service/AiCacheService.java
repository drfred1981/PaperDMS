package fr.smartprod.paperdms.ai.service;

import fr.smartprod.paperdms.ai.service.dto.AiCacheDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.ai.domain.AiCache}.
 */
public interface AiCacheService {
    /**
     * Save a aiCache.
     *
     * @param aiCacheDTO the entity to save.
     * @return the persisted entity.
     */
    AiCacheDTO save(AiCacheDTO aiCacheDTO);

    /**
     * Updates a aiCache.
     *
     * @param aiCacheDTO the entity to update.
     * @return the persisted entity.
     */
    AiCacheDTO update(AiCacheDTO aiCacheDTO);

    /**
     * Partially updates a aiCache.
     *
     * @param aiCacheDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AiCacheDTO> partialUpdate(AiCacheDTO aiCacheDTO);

    /**
     * Get all the aiCaches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AiCacheDTO> findAll(Pageable pageable);

    /**
     * Get the "id" aiCache.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AiCacheDTO> findOne(Long id);

    /**
     * Delete the "id" aiCache.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
