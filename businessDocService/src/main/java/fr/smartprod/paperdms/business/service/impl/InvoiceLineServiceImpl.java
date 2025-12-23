package fr.smartprod.paperdms.business.service.impl;

import fr.smartprod.paperdms.business.domain.InvoiceLine;
import fr.smartprod.paperdms.business.repository.InvoiceLineRepository;
import fr.smartprod.paperdms.business.service.InvoiceLineService;
import fr.smartprod.paperdms.business.service.dto.InvoiceLineDTO;
import fr.smartprod.paperdms.business.service.mapper.InvoiceLineMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.business.domain.InvoiceLine}.
 */
@Service
@Transactional
public class InvoiceLineServiceImpl implements InvoiceLineService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceLineServiceImpl.class);

    private final InvoiceLineRepository invoiceLineRepository;

    private final InvoiceLineMapper invoiceLineMapper;

    public InvoiceLineServiceImpl(InvoiceLineRepository invoiceLineRepository, InvoiceLineMapper invoiceLineMapper) {
        this.invoiceLineRepository = invoiceLineRepository;
        this.invoiceLineMapper = invoiceLineMapper;
    }

    @Override
    public InvoiceLineDTO save(InvoiceLineDTO invoiceLineDTO) {
        LOG.debug("Request to save InvoiceLine : {}", invoiceLineDTO);
        InvoiceLine invoiceLine = invoiceLineMapper.toEntity(invoiceLineDTO);
        invoiceLine = invoiceLineRepository.save(invoiceLine);
        return invoiceLineMapper.toDto(invoiceLine);
    }

    @Override
    public InvoiceLineDTO update(InvoiceLineDTO invoiceLineDTO) {
        LOG.debug("Request to update InvoiceLine : {}", invoiceLineDTO);
        InvoiceLine invoiceLine = invoiceLineMapper.toEntity(invoiceLineDTO);
        invoiceLine = invoiceLineRepository.save(invoiceLine);
        return invoiceLineMapper.toDto(invoiceLine);
    }

    @Override
    public Optional<InvoiceLineDTO> partialUpdate(InvoiceLineDTO invoiceLineDTO) {
        LOG.debug("Request to partially update InvoiceLine : {}", invoiceLineDTO);

        return invoiceLineRepository
            .findById(invoiceLineDTO.getId())
            .map(existingInvoiceLine -> {
                invoiceLineMapper.partialUpdate(existingInvoiceLine, invoiceLineDTO);

                return existingInvoiceLine;
            })
            .map(invoiceLineRepository::save)
            .map(invoiceLineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceLineDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all InvoiceLines");
        return invoiceLineRepository.findAll(pageable).map(invoiceLineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InvoiceLineDTO> findOne(Long id) {
        LOG.debug("Request to get InvoiceLine : {}", id);
        return invoiceLineRepository.findById(id).map(invoiceLineMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceLine : {}", id);
        invoiceLineRepository.deleteById(id);
    }
}
