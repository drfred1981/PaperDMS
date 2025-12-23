package fr.smartprod.paperdms.business.service;

import fr.smartprod.paperdms.business.service.dto.BankStatementDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.business.domain.BankStatement}.
 */
public interface BankStatementService {
    /**
     * Save a bankStatement.
     *
     * @param bankStatementDTO the entity to save.
     * @return the persisted entity.
     */
    BankStatementDTO save(BankStatementDTO bankStatementDTO);

    /**
     * Updates a bankStatement.
     *
     * @param bankStatementDTO the entity to update.
     * @return the persisted entity.
     */
    BankStatementDTO update(BankStatementDTO bankStatementDTO);

    /**
     * Partially updates a bankStatement.
     *
     * @param bankStatementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BankStatementDTO> partialUpdate(BankStatementDTO bankStatementDTO);

    /**
     * Get the "id" bankStatement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BankStatementDTO> findOne(Long id);

    /**
     * Delete the "id" bankStatement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
