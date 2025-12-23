package fr.smartprod.paperdms.business.service;

import fr.smartprod.paperdms.business.service.dto.InvoiceLineDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.business.domain.InvoiceLine}.
 */
public interface InvoiceLineService {
    /**
     * Save a invoiceLine.
     *
     * @param invoiceLineDTO the entity to save.
     * @return the persisted entity.
     */
    InvoiceLineDTO save(InvoiceLineDTO invoiceLineDTO);

    /**
     * Updates a invoiceLine.
     *
     * @param invoiceLineDTO the entity to update.
     * @return the persisted entity.
     */
    InvoiceLineDTO update(InvoiceLineDTO invoiceLineDTO);

    /**
     * Partially updates a invoiceLine.
     *
     * @param invoiceLineDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InvoiceLineDTO> partialUpdate(InvoiceLineDTO invoiceLineDTO);

    /**
     * Get all the invoiceLines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InvoiceLineDTO> findAll(Pageable pageable);

    /**
     * Get the "id" invoiceLine.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InvoiceLineDTO> findOne(Long id);

    /**
     * Delete the "id" invoiceLine.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
