package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.DocumentTemplate;
import fr.smartprod.paperdms.document.repository.DocumentTemplateRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentTemplateSearchRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentTemplateDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentTemplateMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.DocumentTemplate}.
 */
@Service
@Transactional
public class DocumentTemplateService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentTemplateService.class);

    private final DocumentTemplateRepository documentTemplateRepository;

    private final DocumentTemplateMapper documentTemplateMapper;

    private final DocumentTemplateSearchRepository documentTemplateSearchRepository;

    public DocumentTemplateService(
        DocumentTemplateRepository documentTemplateRepository,
        DocumentTemplateMapper documentTemplateMapper,
        DocumentTemplateSearchRepository documentTemplateSearchRepository
    ) {
        this.documentTemplateRepository = documentTemplateRepository;
        this.documentTemplateMapper = documentTemplateMapper;
        this.documentTemplateSearchRepository = documentTemplateSearchRepository;
    }

    /**
     * Save a documentTemplate.
     *
     * @param documentTemplateDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentTemplateDTO save(DocumentTemplateDTO documentTemplateDTO) {
        LOG.debug("Request to save DocumentTemplate : {}", documentTemplateDTO);
        DocumentTemplate documentTemplate = documentTemplateMapper.toEntity(documentTemplateDTO);
        documentTemplate = documentTemplateRepository.save(documentTemplate);
        documentTemplateSearchRepository.index(documentTemplate);
        return documentTemplateMapper.toDto(documentTemplate);
    }

    /**
     * Update a documentTemplate.
     *
     * @param documentTemplateDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentTemplateDTO update(DocumentTemplateDTO documentTemplateDTO) {
        LOG.debug("Request to update DocumentTemplate : {}", documentTemplateDTO);
        DocumentTemplate documentTemplate = documentTemplateMapper.toEntity(documentTemplateDTO);
        documentTemplate = documentTemplateRepository.save(documentTemplate);
        documentTemplateSearchRepository.index(documentTemplate);
        return documentTemplateMapper.toDto(documentTemplate);
    }

    /**
     * Partially update a documentTemplate.
     *
     * @param documentTemplateDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocumentTemplateDTO> partialUpdate(DocumentTemplateDTO documentTemplateDTO) {
        LOG.debug("Request to partially update DocumentTemplate : {}", documentTemplateDTO);

        return documentTemplateRepository
            .findById(documentTemplateDTO.getId())
            .map(existingDocumentTemplate -> {
                documentTemplateMapper.partialUpdate(existingDocumentTemplate, documentTemplateDTO);

                return existingDocumentTemplate;
            })
            .map(documentTemplateRepository::save)
            .map(savedDocumentTemplate -> {
                documentTemplateSearchRepository.index(savedDocumentTemplate);
                return savedDocumentTemplate;
            })
            .map(documentTemplateMapper::toDto);
    }

    /**
     * Get one documentTemplate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentTemplateDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentTemplate : {}", id);
        return documentTemplateRepository.findById(id).map(documentTemplateMapper::toDto);
    }

    /**
     * Delete the documentTemplate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentTemplate : {}", id);
        documentTemplateRepository.deleteById(id);
        documentTemplateSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the documentTemplate corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentTemplateDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of DocumentTemplates for query {}", query);
        return documentTemplateSearchRepository.search(query, pageable).map(documentTemplateMapper::toDto);
    }
}
