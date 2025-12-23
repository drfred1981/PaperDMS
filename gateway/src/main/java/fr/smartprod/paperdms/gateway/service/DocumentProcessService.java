package fr.smartprod.paperdms.gateway.service;

import fr.smartprod.paperdms.gateway.service.dto.DocumentProcessDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.gateway.domain.DocumentProcess}.
 */
public interface DocumentProcessService {
    /**
     * Save a documentProcess.
     *
     * @param documentProcessDTO the entity to save.
     * @return the persisted entity.
     */
    DocumentProcessDTO save(DocumentProcessDTO documentProcessDTO);

    /**
     * Updates a documentProcess.
     *
     * @param documentProcessDTO the entity to update.
     * @return the persisted entity.
     */
    DocumentProcessDTO update(DocumentProcessDTO documentProcessDTO);

    /**
     * Partially updates a documentProcess.
     *
     * @param documentProcessDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentProcessDTO> partialUpdate(DocumentProcessDTO documentProcessDTO);

    /**
     * Get all the documentProcesses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DocumentProcessDTO> findAll(Pageable pageable);

    /**
     * Get the "id" documentProcess.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentProcessDTO> findOne(Long id);

    /**
     * Delete the "id" documentProcess.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the documentProcess corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DocumentProcessDTO> search(String query, Pageable pageable);
}
