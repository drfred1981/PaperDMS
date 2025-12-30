package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.DocumentType;
import fr.smartprod.paperdms.document.repository.DocumentTypeRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentTypeSearchRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentTypeDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.DocumentType}.
 */
@Service
@Transactional
public class DocumentTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentTypeService.class);

    private final DocumentTypeRepository documentTypeRepository;

    private final DocumentTypeMapper documentTypeMapper;

    private final DocumentTypeSearchRepository documentTypeSearchRepository;

    public DocumentTypeService(
        DocumentTypeRepository documentTypeRepository,
        DocumentTypeMapper documentTypeMapper,
        DocumentTypeSearchRepository documentTypeSearchRepository
    ) {
        this.documentTypeRepository = documentTypeRepository;
        this.documentTypeMapper = documentTypeMapper;
        this.documentTypeSearchRepository = documentTypeSearchRepository;
    }

    /**
     * Save a documentType.
     *
     * @param documentTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentTypeDTO save(DocumentTypeDTO documentTypeDTO) {
        LOG.debug("Request to save DocumentType : {}", documentTypeDTO);
        DocumentType documentType = documentTypeMapper.toEntity(documentTypeDTO);
        documentType = documentTypeRepository.save(documentType);
        documentTypeSearchRepository.index(documentType);
        return documentTypeMapper.toDto(documentType);
    }

    /**
     * Update a documentType.
     *
     * @param documentTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentTypeDTO update(DocumentTypeDTO documentTypeDTO) {
        LOG.debug("Request to update DocumentType : {}", documentTypeDTO);
        DocumentType documentType = documentTypeMapper.toEntity(documentTypeDTO);
        documentType = documentTypeRepository.save(documentType);
        documentTypeSearchRepository.index(documentType);
        return documentTypeMapper.toDto(documentType);
    }

    /**
     * Partially update a documentType.
     *
     * @param documentTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocumentTypeDTO> partialUpdate(DocumentTypeDTO documentTypeDTO) {
        LOG.debug("Request to partially update DocumentType : {}", documentTypeDTO);

        return documentTypeRepository
            .findById(documentTypeDTO.getId())
            .map(existingDocumentType -> {
                documentTypeMapper.partialUpdate(existingDocumentType, documentTypeDTO);

                return existingDocumentType;
            })
            .map(documentTypeRepository::save)
            .map(savedDocumentType -> {
                documentTypeSearchRepository.index(savedDocumentType);
                return savedDocumentType;
            })
            .map(documentTypeMapper::toDto);
    }

    /**
     * Get one documentType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentTypeDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentType : {}", id);
        return documentTypeRepository.findById(id).map(documentTypeMapper::toDto);
    }

    /**
     * Delete the documentType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentType : {}", id);
        documentTypeRepository.deleteById(id);
        documentTypeSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the documentType corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentTypeDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of DocumentTypes for query {}", query);
        return documentTypeSearchRepository.search(query, pageable).map(documentTypeMapper::toDto);
    }
}
