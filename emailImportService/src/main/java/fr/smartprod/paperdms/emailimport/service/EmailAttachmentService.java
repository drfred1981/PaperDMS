package fr.smartprod.paperdms.emailimport.service;

import fr.smartprod.paperdms.emailimport.service.dto.EmailAttachmentDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.emailimport.domain.EmailAttachment}.
 */
public interface EmailAttachmentService {
    /**
     * Save a emailAttachment.
     *
     * @param emailAttachmentDTO the entity to save.
     * @return the persisted entity.
     */
    EmailAttachmentDTO save(EmailAttachmentDTO emailAttachmentDTO);

    /**
     * Updates a emailAttachment.
     *
     * @param emailAttachmentDTO the entity to update.
     * @return the persisted entity.
     */
    EmailAttachmentDTO update(EmailAttachmentDTO emailAttachmentDTO);

    /**
     * Partially updates a emailAttachment.
     *
     * @param emailAttachmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmailAttachmentDTO> partialUpdate(EmailAttachmentDTO emailAttachmentDTO);

    /**
     * Get all the emailAttachments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmailAttachmentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" emailAttachment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmailAttachmentDTO> findOne(Long id);

    /**
     * Delete the "id" emailAttachment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
