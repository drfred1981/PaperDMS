package fr.smartprod.paperdms.business.service;

import fr.smartprod.paperdms.business.service.dto.ContractClauseDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.business.domain.ContractClause}.
 */
public interface ContractClauseService {
    /**
     * Save a contractClause.
     *
     * @param contractClauseDTO the entity to save.
     * @return the persisted entity.
     */
    ContractClauseDTO save(ContractClauseDTO contractClauseDTO);

    /**
     * Updates a contractClause.
     *
     * @param contractClauseDTO the entity to update.
     * @return the persisted entity.
     */
    ContractClauseDTO update(ContractClauseDTO contractClauseDTO);

    /**
     * Partially updates a contractClause.
     *
     * @param contractClauseDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContractClauseDTO> partialUpdate(ContractClauseDTO contractClauseDTO);

    /**
     * Get all the contractClauses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContractClauseDTO> findAll(Pageable pageable);

    /**
     * Get the "id" contractClause.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContractClauseDTO> findOne(Long id);

    /**
     * Delete the "id" contractClause.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
