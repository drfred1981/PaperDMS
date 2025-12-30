package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.DocumentMetadata;
import fr.smartprod.paperdms.document.repository.DocumentMetadataRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentMetadataSearchRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentMetadataDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentMetadataMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.DocumentMetadata}.
 */
@Service
@Transactional
public class DocumentMetadataService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentMetadataService.class);

    private final DocumentMetadataRepository documentMetadataRepository;

    private final DocumentMetadataMapper documentMetadataMapper;

    private final DocumentMetadataSearchRepository documentMetadataSearchRepository;

    public DocumentMetadataService(
        DocumentMetadataRepository documentMetadataRepository,
        DocumentMetadataMapper documentMetadataMapper,
        DocumentMetadataSearchRepository documentMetadataSearchRepository
    ) {
        this.documentMetadataRepository = documentMetadataRepository;
        this.documentMetadataMapper = documentMetadataMapper;
        this.documentMetadataSearchRepository = documentMetadataSearchRepository;
    }

    /**
     * Save a documentMetadata.
     *
     * @param documentMetadataDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentMetadataDTO save(DocumentMetadataDTO documentMetadataDTO) {
        LOG.debug("Request to save DocumentMetadata : {}", documentMetadataDTO);
        DocumentMetadata documentMetadata = documentMetadataMapper.toEntity(documentMetadataDTO);
        documentMetadata = documentMetadataRepository.save(documentMetadata);
        documentMetadataSearchRepository.index(documentMetadata);
        return documentMetadataMapper.toDto(documentMetadata);
    }

    /**
     * Update a documentMetadata.
     *
     * @param documentMetadataDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentMetadataDTO update(DocumentMetadataDTO documentMetadataDTO) {
        LOG.debug("Request to update DocumentMetadata : {}", documentMetadataDTO);
        DocumentMetadata documentMetadata = documentMetadataMapper.toEntity(documentMetadataDTO);
        documentMetadata = documentMetadataRepository.save(documentMetadata);
        documentMetadataSearchRepository.index(documentMetadata);
        return documentMetadataMapper.toDto(documentMetadata);
    }

    /**
     * Partially update a documentMetadata.
     *
     * @param documentMetadataDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocumentMetadataDTO> partialUpdate(DocumentMetadataDTO documentMetadataDTO) {
        LOG.debug("Request to partially update DocumentMetadata : {}", documentMetadataDTO);

        return documentMetadataRepository
            .findById(documentMetadataDTO.getId())
            .map(existingDocumentMetadata -> {
                documentMetadataMapper.partialUpdate(existingDocumentMetadata, documentMetadataDTO);

                return existingDocumentMetadata;
            })
            .map(documentMetadataRepository::save)
            .map(savedDocumentMetadata -> {
                documentMetadataSearchRepository.index(savedDocumentMetadata);
                return savedDocumentMetadata;
            })
            .map(documentMetadataMapper::toDto);
    }

    /**
     * Get one documentMetadata by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentMetadataDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentMetadata : {}", id);
        return documentMetadataRepository.findById(id).map(documentMetadataMapper::toDto);
    }

    /**
     * Delete the documentMetadata by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentMetadata : {}", id);
        documentMetadataRepository.deleteById(id);
        documentMetadataSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the documentMetadata corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentMetadataDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of DocumentMetadata for query {}", query);
        return documentMetadataSearchRepository.search(query, pageable).map(documentMetadataMapper::toDto);
    }
}
