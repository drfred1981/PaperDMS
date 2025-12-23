package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.service.dto.DocumentStatisticsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.document.domain.DocumentStatistics}.
 */
public interface DocumentStatisticsService {
    /**
     * Save a documentStatistics.
     *
     * @param documentStatisticsDTO the entity to save.
     * @return the persisted entity.
     */
    DocumentStatisticsDTO save(DocumentStatisticsDTO documentStatisticsDTO);

    /**
     * Updates a documentStatistics.
     *
     * @param documentStatisticsDTO the entity to update.
     * @return the persisted entity.
     */
    DocumentStatisticsDTO update(DocumentStatisticsDTO documentStatisticsDTO);

    /**
     * Partially updates a documentStatistics.
     *
     * @param documentStatisticsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentStatisticsDTO> partialUpdate(DocumentStatisticsDTO documentStatisticsDTO);

    /**
     * Get all the documentStatistics.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DocumentStatisticsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" documentStatistics.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentStatisticsDTO> findOne(Long id);

    /**
     * Delete the "id" documentStatistics.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
