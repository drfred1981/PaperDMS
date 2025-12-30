package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.DocumentServiceStatus;
import fr.smartprod.paperdms.document.repository.DocumentServiceStatusRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentServiceStatusSearchRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentServiceStatusDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentServiceStatusMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.DocumentServiceStatus}.
 */
@Service
@Transactional
public class DocumentServiceStatusService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentServiceStatusService.class);

    private final DocumentServiceStatusRepository documentServiceStatusRepository;

    private final DocumentServiceStatusMapper documentServiceStatusMapper;

    private final DocumentServiceStatusSearchRepository documentServiceStatusSearchRepository;

    public DocumentServiceStatusService(
        DocumentServiceStatusRepository documentServiceStatusRepository,
        DocumentServiceStatusMapper documentServiceStatusMapper,
        DocumentServiceStatusSearchRepository documentServiceStatusSearchRepository
    ) {
        this.documentServiceStatusRepository = documentServiceStatusRepository;
        this.documentServiceStatusMapper = documentServiceStatusMapper;
        this.documentServiceStatusSearchRepository = documentServiceStatusSearchRepository;
    }

    /**
     * Save a documentServiceStatus.
     *
     * @param documentServiceStatusDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentServiceStatusDTO save(DocumentServiceStatusDTO documentServiceStatusDTO) {
        LOG.debug("Request to save DocumentServiceStatus : {}", documentServiceStatusDTO);
        DocumentServiceStatus documentServiceStatus = documentServiceStatusMapper.toEntity(documentServiceStatusDTO);
        documentServiceStatus = documentServiceStatusRepository.save(documentServiceStatus);
        documentServiceStatusSearchRepository.index(documentServiceStatus);
        return documentServiceStatusMapper.toDto(documentServiceStatus);
    }

    /**
     * Update a documentServiceStatus.
     *
     * @param documentServiceStatusDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentServiceStatusDTO update(DocumentServiceStatusDTO documentServiceStatusDTO) {
        LOG.debug("Request to update DocumentServiceStatus : {}", documentServiceStatusDTO);
        DocumentServiceStatus documentServiceStatus = documentServiceStatusMapper.toEntity(documentServiceStatusDTO);
        documentServiceStatus = documentServiceStatusRepository.save(documentServiceStatus);
        documentServiceStatusSearchRepository.index(documentServiceStatus);
        return documentServiceStatusMapper.toDto(documentServiceStatus);
    }

    /**
     * Partially update a documentServiceStatus.
     *
     * @param documentServiceStatusDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocumentServiceStatusDTO> partialUpdate(DocumentServiceStatusDTO documentServiceStatusDTO) {
        LOG.debug("Request to partially update DocumentServiceStatus : {}", documentServiceStatusDTO);

        return documentServiceStatusRepository
            .findById(documentServiceStatusDTO.getId())
            .map(existingDocumentServiceStatus -> {
                documentServiceStatusMapper.partialUpdate(existingDocumentServiceStatus, documentServiceStatusDTO);

                return existingDocumentServiceStatus;
            })
            .map(documentServiceStatusRepository::save)
            .map(savedDocumentServiceStatus -> {
                documentServiceStatusSearchRepository.index(savedDocumentServiceStatus);
                return savedDocumentServiceStatus;
            })
            .map(documentServiceStatusMapper::toDto);
    }

    /**
     * Get one documentServiceStatus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentServiceStatusDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentServiceStatus : {}", id);
        return documentServiceStatusRepository.findById(id).map(documentServiceStatusMapper::toDto);
    }

    /**
     * Delete the documentServiceStatus by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentServiceStatus : {}", id);
        documentServiceStatusRepository.deleteById(id);
        documentServiceStatusSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the documentServiceStatus corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentServiceStatusDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of DocumentServiceStatuses for query {}", query);
        return documentServiceStatusSearchRepository.search(query, pageable).map(documentServiceStatusMapper::toDto);
    }
}
