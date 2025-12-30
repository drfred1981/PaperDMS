package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.DocumentAudit;
import fr.smartprod.paperdms.document.repository.DocumentAuditRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentAuditSearchRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentAuditDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentAuditMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.DocumentAudit}.
 */
@Service
@Transactional
public class DocumentAuditService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentAuditService.class);

    private final DocumentAuditRepository documentAuditRepository;

    private final DocumentAuditMapper documentAuditMapper;

    private final DocumentAuditSearchRepository documentAuditSearchRepository;

    public DocumentAuditService(
        DocumentAuditRepository documentAuditRepository,
        DocumentAuditMapper documentAuditMapper,
        DocumentAuditSearchRepository documentAuditSearchRepository
    ) {
        this.documentAuditRepository = documentAuditRepository;
        this.documentAuditMapper = documentAuditMapper;
        this.documentAuditSearchRepository = documentAuditSearchRepository;
    }

    /**
     * Save a documentAudit.
     *
     * @param documentAuditDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentAuditDTO save(DocumentAuditDTO documentAuditDTO) {
        LOG.debug("Request to save DocumentAudit : {}", documentAuditDTO);
        DocumentAudit documentAudit = documentAuditMapper.toEntity(documentAuditDTO);
        documentAudit = documentAuditRepository.save(documentAudit);
        documentAuditSearchRepository.index(documentAudit);
        return documentAuditMapper.toDto(documentAudit);
    }

    /**
     * Update a documentAudit.
     *
     * @param documentAuditDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentAuditDTO update(DocumentAuditDTO documentAuditDTO) {
        LOG.debug("Request to update DocumentAudit : {}", documentAuditDTO);
        DocumentAudit documentAudit = documentAuditMapper.toEntity(documentAuditDTO);
        documentAudit = documentAuditRepository.save(documentAudit);
        documentAuditSearchRepository.index(documentAudit);
        return documentAuditMapper.toDto(documentAudit);
    }

    /**
     * Partially update a documentAudit.
     *
     * @param documentAuditDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocumentAuditDTO> partialUpdate(DocumentAuditDTO documentAuditDTO) {
        LOG.debug("Request to partially update DocumentAudit : {}", documentAuditDTO);

        return documentAuditRepository
            .findById(documentAuditDTO.getId())
            .map(existingDocumentAudit -> {
                documentAuditMapper.partialUpdate(existingDocumentAudit, documentAuditDTO);

                return existingDocumentAudit;
            })
            .map(documentAuditRepository::save)
            .map(savedDocumentAudit -> {
                documentAuditSearchRepository.index(savedDocumentAudit);
                return savedDocumentAudit;
            })
            .map(documentAuditMapper::toDto);
    }

    /**
     * Get one documentAudit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentAuditDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentAudit : {}", id);
        return documentAuditRepository.findById(id).map(documentAuditMapper::toDto);
    }

    /**
     * Delete the documentAudit by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentAudit : {}", id);
        documentAuditRepository.deleteById(id);
        documentAuditSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the documentAudit corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentAuditDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of DocumentAudits for query {}", query);
        return documentAuditSearchRepository.search(query, pageable).map(documentAuditMapper::toDto);
    }
}
