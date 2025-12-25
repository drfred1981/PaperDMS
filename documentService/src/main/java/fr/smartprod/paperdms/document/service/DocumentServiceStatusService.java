package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.service.dto.DocumentServiceStatusDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.document.domain.DocumentServiceStatus}.
 */
public interface DocumentServiceStatusService {
    /**
     * Save a documentServiceStatus.
     *
     * @param documentServiceStatusDTO the entity to save.
     * @return the persisted entity.
     */
    DocumentServiceStatusDTO save(DocumentServiceStatusDTO documentServiceStatusDTO);

    /**
     * Updates a documentServiceStatus.
     *
     * @param documentServiceStatusDTO the entity to update.
     * @return the persisted entity.
     */
    DocumentServiceStatusDTO update(DocumentServiceStatusDTO documentServiceStatusDTO);

    /**
     * Partially updates a documentServiceStatus.
     *
     * @param documentServiceStatusDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentServiceStatusDTO> partialUpdate(DocumentServiceStatusDTO documentServiceStatusDTO);

    /**
     * Get the "id" documentServiceStatus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentServiceStatusDTO> findOne(Long id);

    /**
     * Delete the "id" documentServiceStatus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the documentServiceStatus corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DocumentServiceStatusDTO> search(String query, Pageable pageable);
}
