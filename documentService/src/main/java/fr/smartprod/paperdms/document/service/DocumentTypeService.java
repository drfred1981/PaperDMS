package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.service.dto.DocumentTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.document.domain.DocumentType}.
 */
public interface DocumentTypeService {
    /**
     * Save a documentType.
     *
     * @param documentTypeDTO the entity to save.
     * @return the persisted entity.
     */
    DocumentTypeDTO save(DocumentTypeDTO documentTypeDTO);

    /**
     * Updates a documentType.
     *
     * @param documentTypeDTO the entity to update.
     * @return the persisted entity.
     */
    DocumentTypeDTO update(DocumentTypeDTO documentTypeDTO);

    /**
     * Partially updates a documentType.
     *
     * @param documentTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentTypeDTO> partialUpdate(DocumentTypeDTO documentTypeDTO);

    /**
     * Get the "id" documentType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentTypeDTO> findOne(Long id);

    /**
     * Delete the "id" documentType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the documentType corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DocumentTypeDTO> search(String query, Pageable pageable);
}
