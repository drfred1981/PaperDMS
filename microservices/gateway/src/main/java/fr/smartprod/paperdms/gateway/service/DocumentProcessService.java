package fr.smartprod.paperdms.gateway.service;

import fr.smartprod.paperdms.gateway.domain.DocumentProcess;
import fr.smartprod.paperdms.gateway.repository.DocumentProcessRepository;
import fr.smartprod.paperdms.gateway.repository.search.DocumentProcessSearchRepository;
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
public class DocumentProcessService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentProcessService.class);

    private final DocumentProcessRepository documentProcessRepository;

    private final DocumentProcessMapper documentProcessMapper;

    private final DocumentProcessSearchRepository documentProcessSearchRepository;

    public DocumentProcessService(
        DocumentProcessRepository documentProcessRepository,
        DocumentProcessMapper documentProcessMapper,
        DocumentProcessSearchRepository documentProcessSearchRepository
    ) {
        this.documentProcessRepository = documentProcessRepository;
        this.documentProcessMapper = documentProcessMapper;
        this.documentProcessSearchRepository = documentProcessSearchRepository;
    }

    /**
     * Save a documentProcess.
     *
     * @param documentProcessDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentProcessDTO save(DocumentProcessDTO documentProcessDTO) {
        LOG.debug("Request to save DocumentProcess : {}", documentProcessDTO);
        DocumentProcess documentProcess = documentProcessMapper.toEntity(documentProcessDTO);
        documentProcess = documentProcessRepository.save(documentProcess);
        documentProcessSearchRepository.index(documentProcess);
        return documentProcessMapper.toDto(documentProcess);
    }

    /**
     * Update a documentProcess.
     *
     * @param documentProcessDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentProcessDTO update(DocumentProcessDTO documentProcessDTO) {
        LOG.debug("Request to update DocumentProcess : {}", documentProcessDTO);
        DocumentProcess documentProcess = documentProcessMapper.toEntity(documentProcessDTO);
        documentProcess = documentProcessRepository.save(documentProcess);
        documentProcessSearchRepository.index(documentProcess);
        return documentProcessMapper.toDto(documentProcess);
    }

    /**
     * Partially update a documentProcess.
     *
     * @param documentProcessDTO the entity to update partially.
     * @return the persisted entity.
     */
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

    /**
     * Get one documentProcess by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentProcessDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentProcess : {}", id);
        return documentProcessRepository.findById(id).map(documentProcessMapper::toDto);
    }

    /**
     * Delete the documentProcess by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentProcess : {}", id);
        documentProcessRepository.deleteById(id);
        documentProcessSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the documentProcess corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentProcessDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of DocumentProcesses for query {}", query);
        return documentProcessSearchRepository.search(query, pageable).map(documentProcessMapper::toDto);
    }
}
