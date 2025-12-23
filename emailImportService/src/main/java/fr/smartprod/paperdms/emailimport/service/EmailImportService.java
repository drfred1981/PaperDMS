package fr.smartprod.paperdms.emailimport.service;

import fr.smartprod.paperdms.emailimport.service.dto.EmailImportDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.emailimport.domain.EmailImport}.
 */
public interface EmailImportService {
    /**
     * Save a emailImport.
     *
     * @param emailImportDTO the entity to save.
     * @return the persisted entity.
     */
    EmailImportDTO save(EmailImportDTO emailImportDTO);

    /**
     * Updates a emailImport.
     *
     * @param emailImportDTO the entity to update.
     * @return the persisted entity.
     */
    EmailImportDTO update(EmailImportDTO emailImportDTO);

    /**
     * Partially updates a emailImport.
     *
     * @param emailImportDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmailImportDTO> partialUpdate(EmailImportDTO emailImportDTO);

    /**
     * Get the "id" emailImport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmailImportDTO> findOne(Long id);

    /**
     * Delete the "id" emailImport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
