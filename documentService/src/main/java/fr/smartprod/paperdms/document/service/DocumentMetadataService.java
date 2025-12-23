package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.service.dto.DocumentMetadataDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.document.domain.DocumentMetadata}.
 */
public interface DocumentMetadataService {
    /**
     * Save a documentMetadata.
     *
     * @param documentMetadataDTO the entity to save.
     * @return the persisted entity.
     */
    DocumentMetadataDTO save(DocumentMetadataDTO documentMetadataDTO);

    /**
     * Updates a documentMetadata.
     *
     * @param documentMetadataDTO the entity to update.
     * @return the persisted entity.
     */
    DocumentMetadataDTO update(DocumentMetadataDTO documentMetadataDTO);

    /**
     * Partially updates a documentMetadata.
     *
     * @param documentMetadataDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentMetadataDTO> partialUpdate(DocumentMetadataDTO documentMetadataDTO);

    /**
     * Get all the documentMetadata.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DocumentMetadataDTO> findAll(Pageable pageable);

    /**
     * Get the "id" documentMetadata.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentMetadataDTO> findOne(Long id);

    /**
     * Delete the "id" documentMetadata.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
