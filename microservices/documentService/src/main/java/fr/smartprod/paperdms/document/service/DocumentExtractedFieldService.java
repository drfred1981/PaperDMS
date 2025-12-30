package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.DocumentExtractedField;
import fr.smartprod.paperdms.document.repository.DocumentExtractedFieldRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentExtractedFieldSearchRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentExtractedFieldDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentExtractedFieldMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.DocumentExtractedField}.
 */
@Service
@Transactional
public class DocumentExtractedFieldService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentExtractedFieldService.class);

    private final DocumentExtractedFieldRepository documentExtractedFieldRepository;

    private final DocumentExtractedFieldMapper documentExtractedFieldMapper;

    private final DocumentExtractedFieldSearchRepository documentExtractedFieldSearchRepository;

    public DocumentExtractedFieldService(
        DocumentExtractedFieldRepository documentExtractedFieldRepository,
        DocumentExtractedFieldMapper documentExtractedFieldMapper,
        DocumentExtractedFieldSearchRepository documentExtractedFieldSearchRepository
    ) {
        this.documentExtractedFieldRepository = documentExtractedFieldRepository;
        this.documentExtractedFieldMapper = documentExtractedFieldMapper;
        this.documentExtractedFieldSearchRepository = documentExtractedFieldSearchRepository;
    }

    /**
     * Save a documentExtractedField.
     *
     * @param documentExtractedFieldDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentExtractedFieldDTO save(DocumentExtractedFieldDTO documentExtractedFieldDTO) {
        LOG.debug("Request to save DocumentExtractedField : {}", documentExtractedFieldDTO);
        DocumentExtractedField documentExtractedField = documentExtractedFieldMapper.toEntity(documentExtractedFieldDTO);
        documentExtractedField = documentExtractedFieldRepository.save(documentExtractedField);
        documentExtractedFieldSearchRepository.index(documentExtractedField);
        return documentExtractedFieldMapper.toDto(documentExtractedField);
    }

    /**
     * Update a documentExtractedField.
     *
     * @param documentExtractedFieldDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentExtractedFieldDTO update(DocumentExtractedFieldDTO documentExtractedFieldDTO) {
        LOG.debug("Request to update DocumentExtractedField : {}", documentExtractedFieldDTO);
        DocumentExtractedField documentExtractedField = documentExtractedFieldMapper.toEntity(documentExtractedFieldDTO);
        documentExtractedField = documentExtractedFieldRepository.save(documentExtractedField);
        documentExtractedFieldSearchRepository.index(documentExtractedField);
        return documentExtractedFieldMapper.toDto(documentExtractedField);
    }

    /**
     * Partially update a documentExtractedField.
     *
     * @param documentExtractedFieldDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocumentExtractedFieldDTO> partialUpdate(DocumentExtractedFieldDTO documentExtractedFieldDTO) {
        LOG.debug("Request to partially update DocumentExtractedField : {}", documentExtractedFieldDTO);

        return documentExtractedFieldRepository
            .findById(documentExtractedFieldDTO.getId())
            .map(existingDocumentExtractedField -> {
                documentExtractedFieldMapper.partialUpdate(existingDocumentExtractedField, documentExtractedFieldDTO);

                return existingDocumentExtractedField;
            })
            .map(documentExtractedFieldRepository::save)
            .map(savedDocumentExtractedField -> {
                documentExtractedFieldSearchRepository.index(savedDocumentExtractedField);
                return savedDocumentExtractedField;
            })
            .map(documentExtractedFieldMapper::toDto);
    }

    /**
     * Get one documentExtractedField by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentExtractedFieldDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentExtractedField : {}", id);
        return documentExtractedFieldRepository.findById(id).map(documentExtractedFieldMapper::toDto);
    }

    /**
     * Delete the documentExtractedField by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentExtractedField : {}", id);
        documentExtractedFieldRepository.deleteById(id);
        documentExtractedFieldSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the documentExtractedField corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentExtractedFieldDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of DocumentExtractedFields for query {}", query);
        return documentExtractedFieldSearchRepository.search(query, pageable).map(documentExtractedFieldMapper::toDto);
    }
}
