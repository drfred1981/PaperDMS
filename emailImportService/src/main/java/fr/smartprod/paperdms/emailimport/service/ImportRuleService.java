package fr.smartprod.paperdms.emailimport.service;

import fr.smartprod.paperdms.emailimport.service.dto.ImportRuleDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.emailimport.domain.ImportRule}.
 */
public interface ImportRuleService {
    /**
     * Save a importRule.
     *
     * @param importRuleDTO the entity to save.
     * @return the persisted entity.
     */
    ImportRuleDTO save(ImportRuleDTO importRuleDTO);

    /**
     * Updates a importRule.
     *
     * @param importRuleDTO the entity to update.
     * @return the persisted entity.
     */
    ImportRuleDTO update(ImportRuleDTO importRuleDTO);

    /**
     * Partially updates a importRule.
     *
     * @param importRuleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ImportRuleDTO> partialUpdate(ImportRuleDTO importRuleDTO);

    /**
     * Get the "id" importRule.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ImportRuleDTO> findOne(Long id);

    /**
     * Delete the "id" importRule.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
