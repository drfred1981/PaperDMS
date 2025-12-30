package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.DocumentTag;
import fr.smartprod.paperdms.document.repository.DocumentTagRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentTagSearchRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentTagDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentTagMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.DocumentTag}.
 */
@Service
@Transactional
public class DocumentTagService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentTagService.class);

    private final DocumentTagRepository documentTagRepository;

    private final DocumentTagMapper documentTagMapper;

    private final DocumentTagSearchRepository documentTagSearchRepository;

    public DocumentTagService(
        DocumentTagRepository documentTagRepository,
        DocumentTagMapper documentTagMapper,
        DocumentTagSearchRepository documentTagSearchRepository
    ) {
        this.documentTagRepository = documentTagRepository;
        this.documentTagMapper = documentTagMapper;
        this.documentTagSearchRepository = documentTagSearchRepository;
    }

    /**
     * Save a documentTag.
     *
     * @param documentTagDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentTagDTO save(DocumentTagDTO documentTagDTO) {
        LOG.debug("Request to save DocumentTag : {}", documentTagDTO);
        DocumentTag documentTag = documentTagMapper.toEntity(documentTagDTO);
        documentTag = documentTagRepository.save(documentTag);
        documentTagSearchRepository.index(documentTag);
        return documentTagMapper.toDto(documentTag);
    }

    /**
     * Update a documentTag.
     *
     * @param documentTagDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentTagDTO update(DocumentTagDTO documentTagDTO) {
        LOG.debug("Request to update DocumentTag : {}", documentTagDTO);
        DocumentTag documentTag = documentTagMapper.toEntity(documentTagDTO);
        documentTag = documentTagRepository.save(documentTag);
        documentTagSearchRepository.index(documentTag);
        return documentTagMapper.toDto(documentTag);
    }

    /**
     * Partially update a documentTag.
     *
     * @param documentTagDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocumentTagDTO> partialUpdate(DocumentTagDTO documentTagDTO) {
        LOG.debug("Request to partially update DocumentTag : {}", documentTagDTO);

        return documentTagRepository
            .findById(documentTagDTO.getId())
            .map(existingDocumentTag -> {
                documentTagMapper.partialUpdate(existingDocumentTag, documentTagDTO);

                return existingDocumentTag;
            })
            .map(documentTagRepository::save)
            .map(savedDocumentTag -> {
                documentTagSearchRepository.index(savedDocumentTag);
                return savedDocumentTag;
            })
            .map(documentTagMapper::toDto);
    }

    /**
     * Get one documentTag by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentTagDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentTag : {}", id);
        return documentTagRepository.findById(id).map(documentTagMapper::toDto);
    }

    /**
     * Delete the documentTag by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentTag : {}", id);
        documentTagRepository.deleteById(id);
        documentTagSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the documentTag corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentTagDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of DocumentTags for query {}", query);
        return documentTagSearchRepository.search(query, pageable).map(documentTagMapper::toDto);
    }
}
