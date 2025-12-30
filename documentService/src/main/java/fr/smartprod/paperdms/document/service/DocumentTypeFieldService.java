package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.DocumentTypeField;
import fr.smartprod.paperdms.document.repository.DocumentTypeFieldRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentTypeFieldSearchRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentTypeFieldDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentTypeFieldMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.DocumentTypeField}.
 */
@Service
@Transactional
public class DocumentTypeFieldService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentTypeFieldService.class);

    private final DocumentTypeFieldRepository documentTypeFieldRepository;

    private final DocumentTypeFieldMapper documentTypeFieldMapper;

    private final DocumentTypeFieldSearchRepository documentTypeFieldSearchRepository;

    public DocumentTypeFieldService(
        DocumentTypeFieldRepository documentTypeFieldRepository,
        DocumentTypeFieldMapper documentTypeFieldMapper,
        DocumentTypeFieldSearchRepository documentTypeFieldSearchRepository
    ) {
        this.documentTypeFieldRepository = documentTypeFieldRepository;
        this.documentTypeFieldMapper = documentTypeFieldMapper;
        this.documentTypeFieldSearchRepository = documentTypeFieldSearchRepository;
    }

    /**
     * Save a documentTypeField.
     *
     * @param documentTypeFieldDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentTypeFieldDTO save(DocumentTypeFieldDTO documentTypeFieldDTO) {
        LOG.debug("Request to save DocumentTypeField : {}", documentTypeFieldDTO);
        DocumentTypeField documentTypeField = documentTypeFieldMapper.toEntity(documentTypeFieldDTO);
        documentTypeField = documentTypeFieldRepository.save(documentTypeField);
        documentTypeFieldSearchRepository.index(documentTypeField);
        return documentTypeFieldMapper.toDto(documentTypeField);
    }

    /**
     * Update a documentTypeField.
     *
     * @param documentTypeFieldDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentTypeFieldDTO update(DocumentTypeFieldDTO documentTypeFieldDTO) {
        LOG.debug("Request to update DocumentTypeField : {}", documentTypeFieldDTO);
        DocumentTypeField documentTypeField = documentTypeFieldMapper.toEntity(documentTypeFieldDTO);
        documentTypeField = documentTypeFieldRepository.save(documentTypeField);
        documentTypeFieldSearchRepository.index(documentTypeField);
        return documentTypeFieldMapper.toDto(documentTypeField);
    }

    /**
     * Partially update a documentTypeField.
     *
     * @param documentTypeFieldDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocumentTypeFieldDTO> partialUpdate(DocumentTypeFieldDTO documentTypeFieldDTO) {
        LOG.debug("Request to partially update DocumentTypeField : {}", documentTypeFieldDTO);

        return documentTypeFieldRepository
            .findById(documentTypeFieldDTO.getId())
            .map(existingDocumentTypeField -> {
                documentTypeFieldMapper.partialUpdate(existingDocumentTypeField, documentTypeFieldDTO);

                return existingDocumentTypeField;
            })
            .map(documentTypeFieldRepository::save)
            .map(savedDocumentTypeField -> {
                documentTypeFieldSearchRepository.index(savedDocumentTypeField);
                return savedDocumentTypeField;
            })
            .map(documentTypeFieldMapper::toDto);
    }

    /**
     * Get one documentTypeField by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentTypeFieldDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentTypeField : {}", id);
        return documentTypeFieldRepository.findById(id).map(documentTypeFieldMapper::toDto);
    }

    /**
     * Delete the documentTypeField by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentTypeField : {}", id);
        documentTypeFieldRepository.deleteById(id);
        documentTypeFieldSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the documentTypeField corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentTypeFieldDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of DocumentTypeFields for query {}", query);
        return documentTypeFieldSearchRepository.search(query, pageable).map(documentTypeFieldMapper::toDto);
    }
}
