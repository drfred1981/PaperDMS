package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.service.dto.DocumentTypeFieldDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.document.domain.DocumentTypeField}.
 */
public interface DocumentTypeFieldService {
    /**
     * Save a documentTypeField.
     *
     * @param documentTypeFieldDTO the entity to save.
     * @return the persisted entity.
     */
    DocumentTypeFieldDTO save(DocumentTypeFieldDTO documentTypeFieldDTO);

    /**
     * Updates a documentTypeField.
     *
     * @param documentTypeFieldDTO the entity to update.
     * @return the persisted entity.
     */
    DocumentTypeFieldDTO update(DocumentTypeFieldDTO documentTypeFieldDTO);

    /**
     * Partially updates a documentTypeField.
     *
     * @param documentTypeFieldDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentTypeFieldDTO> partialUpdate(DocumentTypeFieldDTO documentTypeFieldDTO);

    /**
     * Get all the documentTypeFields.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DocumentTypeFieldDTO> findAll(Pageable pageable);

    /**
     * Get the "id" documentTypeField.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentTypeFieldDTO> findOne(Long id);

    /**
     * Delete the "id" documentTypeField.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
