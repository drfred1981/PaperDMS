package fr.smartprod.paperdms.gateway.service.impl;

import fr.smartprod.paperdms.gateway.domain.DocumentProcess;
import fr.smartprod.paperdms.gateway.repository.DocumentProcessRepository;
import fr.smartprod.paperdms.gateway.repository.search.DocumentProcessSearchRepository;
import fr.smartprod.paperdms.gateway.service.DocumentProcessService;
import fr.smartprod.paperdms.gateway.service.dto.DocumentProcessDTO;
import fr.smartprod.paperdms.gateway.service.mapper.DocumentProcessMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.gateway.domain.DocumentProcess}.
 */
@Service
@Transactional
public class DocumentProcessServiceImpl implements DocumentProcessService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentProcessServiceImpl.class);

    private final DocumentProcessRepository documentProcessRepository;

    private final DocumentProcessMapper documentProcessMapper;

    private final DocumentProcessSearchRepository documentProcessSearchRepository;

    public DocumentProcessServiceImpl(
        DocumentProcessRepository documentProcessRepository,
        DocumentProcessMapper documentProcessMapper,
        DocumentProcessSearchRepository documentProcessSearchRepository
    ) {
        this.documentProcessRepository = documentProcessRepository;
        this.documentProcessMapper = documentProcessMapper;
        this.documentProcessSearchRepository = documentProcessSearchRepository;
    }

    @Override
    public DocumentProcessDTO save(DocumentProcessDTO documentProcessDTO) {
        LOG.debug("Request to save DocumentProcess : {}", documentProcessDTO);
        DocumentProcess documentProcess = documentProcessMapper.toEntity(documentProcessDTO);
        documentProcess = documentProcessRepository.save(documentProcess);
        documentProcessSearchRepository.index(documentProcess);
        return documentProcessMapper.toDto(documentProcess);
    }

    @Override
    public DocumentProcessDTO update(DocumentProcessDTO documentProcessDTO) {
        LOG.debug("Request to update DocumentProcess : {}", documentProcessDTO);
        DocumentProcess documentProcess = documentProcessMapper.toEntity(documentProcessDTO);
        documentProcess = documentProcessRepository.save(documentProcess);
        documentProcessSearchRepository.index(documentProcess);
        return documentProcessMapper.toDto(documentProcess);
    }

    @Override
    public Optional<DocumentProcessDTO> partialUpdate(DocumentProcessDTO documentProcessDTO) {
        LOG.debug("Request to partially update DocumentProcess : {}", documentProcessDTO);

        return documentProcessRepository
            .findById(documentProcessDTO.getId())
            .map(existingDocumentProcess -> {
                documentProcessMapper.partialUpdate(existingDocumentProcess, documentProcessDTO);

                return existingDocumentProcess;
            })
            .map(documentProcessRepository::save)
            .map(savedDocumentProcess -> {
                documentProcessSearchRepository.index(savedDocumentProcess);
                return savedDocumentProcess;
            })
            .map(documentProcessMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentProcessDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all DocumentProcesses");
        return documentProcessRepository.findAll(pageable).map(documentProcessMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentProcessDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentProcess : {}", id);
        return documentProcessRepository.findById(id).map(documentProcessMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentProcess : {}", id);
        documentProcessRepository.deleteById(id);
        documentProcessSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentProcessDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of DocumentProcesses for query {}", query);
        return documentProcessSearchRepository.search(query, pageable).map(documentProcessMapper::toDto);
    }
}
