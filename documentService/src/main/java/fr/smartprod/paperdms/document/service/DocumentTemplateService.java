package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.service.dto.DocumentTemplateDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.document.domain.DocumentTemplate}.
 */
public interface DocumentTemplateService {
    /**
     * Save a documentTemplate.
     *
     * @param documentTemplateDTO the entity to save.
     * @return the persisted entity.
     */
    DocumentTemplateDTO save(DocumentTemplateDTO documentTemplateDTO);

    /**
     * Updates a documentTemplate.
     *
     * @param documentTemplateDTO the entity to update.
     * @return the persisted entity.
     */
    DocumentTemplateDTO update(DocumentTemplateDTO documentTemplateDTO);

    /**
     * Partially updates a documentTemplate.
     *
     * @param documentTemplateDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentTemplateDTO> partialUpdate(DocumentTemplateDTO documentTemplateDTO);

    /**
     * Get all the documentTemplates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DocumentTemplateDTO> findAll(Pageable pageable);

    /**
     * Get the "id" documentTemplate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentTemplateDTO> findOne(Long id);

    /**
     * Delete the "id" documentTemplate.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
