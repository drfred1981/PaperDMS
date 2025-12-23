package fr.smartprod.paperdms.business.service;

import fr.smartprod.paperdms.business.service.dto.BankTransactionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.business.domain.BankTransaction}.
 */
public interface BankTransactionService {
    /**
     * Save a bankTransaction.
     *
     * @param bankTransactionDTO the entity to save.
     * @return the persisted entity.
     */
    BankTransactionDTO save(BankTransactionDTO bankTransactionDTO);

    /**
     * Updates a bankTransaction.
     *
     * @param bankTransactionDTO the entity to update.
     * @return the persisted entity.
     */
    BankTransactionDTO update(BankTransactionDTO bankTransactionDTO);

    /**
     * Partially updates a bankTransaction.
     *
     * @param bankTransactionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BankTransactionDTO> partialUpdate(BankTransactionDTO bankTransactionDTO);

    /**
     * Get all the bankTransactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BankTransactionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" bankTransaction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BankTransactionDTO> findOne(Long id);

    /**
     * Delete the "id" bankTransaction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
