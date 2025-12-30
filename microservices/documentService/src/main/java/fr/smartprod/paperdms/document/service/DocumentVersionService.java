package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.DocumentVersion;
import fr.smartprod.paperdms.document.repository.DocumentVersionRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentVersionSearchRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentVersionDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentVersionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.DocumentVersion}.
 */
@Service
@Transactional
public class DocumentVersionService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentVersionService.class);

    private final DocumentVersionRepository documentVersionRepository;

    private final DocumentVersionMapper documentVersionMapper;

    private final DocumentVersionSearchRepository documentVersionSearchRepository;

    public DocumentVersionService(
        DocumentVersionRepository documentVersionRepository,
        DocumentVersionMapper documentVersionMapper,
        DocumentVersionSearchRepository documentVersionSearchRepository
    ) {
        this.documentVersionRepository = documentVersionRepository;
        this.documentVersionMapper = documentVersionMapper;
        this.documentVersionSearchRepository = documentVersionSearchRepository;
    }

    /**
     * Save a documentVersion.
     *
     * @param documentVersionDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentVersionDTO save(DocumentVersionDTO documentVersionDTO) {
        LOG.debug("Request to save DocumentVersion : {}", documentVersionDTO);
        DocumentVersion documentVersion = documentVersionMapper.toEntity(documentVersionDTO);
        documentVersion = documentVersionRepository.save(documentVersion);
        documentVersionSearchRepository.index(documentVersion);
        return documentVersionMapper.toDto(documentVersion);
    }

    /**
     * Update a documentVersion.
     *
     * @param documentVersionDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentVersionDTO update(DocumentVersionDTO documentVersionDTO) {
        LOG.debug("Request to update DocumentVersion : {}", documentVersionDTO);
        DocumentVersion documentVersion = documentVersionMapper.toEntity(documentVersionDTO);
        documentVersion = documentVersionRepository.save(documentVersion);
        documentVersionSearchRepository.index(documentVersion);
        return documentVersionMapper.toDto(documentVersion);
    }

    /**
     * Partially update a documentVersion.
     *
     * @param documentVersionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocumentVersionDTO> partialUpdate(DocumentVersionDTO documentVersionDTO) {
        LOG.debug("Request to partially update DocumentVersion : {}", documentVersionDTO);

        return documentVersionRepository
            .findById(documentVersionDTO.getId())
            .map(existingDocumentVersion -> {
                documentVersionMapper.partialUpdate(existingDocumentVersion, documentVersionDTO);

                return existingDocumentVersion;
            })
            .map(documentVersionRepository::save)
            .map(savedDocumentVersion -> {
                documentVersionSearchRepository.index(savedDocumentVersion);
                return savedDocumentVersion;
            })
            .map(documentVersionMapper::toDto);
    }

    /**
     * Get one documentVersion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentVersionDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentVersion : {}", id);
        return documentVersionRepository.findById(id).map(documentVersionMapper::toDto);
    }

    /**
     * Delete the documentVersion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentVersion : {}", id);
        documentVersionRepository.deleteById(id);
        documentVersionSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the documentVersion corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentVersionDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of DocumentVersions for query {}", query);
        return documentVersionSearchRepository.search(query, pageable).map(documentVersionMapper::toDto);
    }
}
