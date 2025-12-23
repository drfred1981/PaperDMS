package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.service.dto.DocumentAuditDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.document.domain.DocumentAudit}.
 */
public interface DocumentAuditService {
    /**
     * Save a documentAudit.
     *
     * @param documentAuditDTO the entity to save.
     * @return the persisted entity.
     */
    DocumentAuditDTO save(DocumentAuditDTO documentAuditDTO);

    /**
     * Updates a documentAudit.
     *
     * @param documentAuditDTO the entity to update.
     * @return the persisted entity.
     */
    DocumentAuditDTO update(DocumentAuditDTO documentAuditDTO);

    /**
     * Partially updates a documentAudit.
     *
     * @param documentAuditDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentAuditDTO> partialUpdate(DocumentAuditDTO documentAuditDTO);

    /**
     * Get the "id" documentAudit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentAuditDTO> findOne(Long id);

    /**
     * Delete the "id" documentAudit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
