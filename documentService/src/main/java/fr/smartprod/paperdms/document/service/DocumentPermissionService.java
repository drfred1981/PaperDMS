package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.DocumentPermission;
import fr.smartprod.paperdms.document.repository.DocumentPermissionRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentPermissionSearchRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentPermissionDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentPermissionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.DocumentPermission}.
 */
@Service
@Transactional
public class DocumentPermissionService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentPermissionService.class);

    private final DocumentPermissionRepository documentPermissionRepository;

    private final DocumentPermissionMapper documentPermissionMapper;

    private final DocumentPermissionSearchRepository documentPermissionSearchRepository;

    public DocumentPermissionService(
        DocumentPermissionRepository documentPermissionRepository,
        DocumentPermissionMapper documentPermissionMapper,
        DocumentPermissionSearchRepository documentPermissionSearchRepository
    ) {
        this.documentPermissionRepository = documentPermissionRepository;
        this.documentPermissionMapper = documentPermissionMapper;
        this.documentPermissionSearchRepository = documentPermissionSearchRepository;
    }

    /**
     * Save a documentPermission.
     *
     * @param documentPermissionDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentPermissionDTO save(DocumentPermissionDTO documentPermissionDTO) {
        LOG.debug("Request to save DocumentPermission : {}", documentPermissionDTO);
        DocumentPermission documentPermission = documentPermissionMapper.toEntity(documentPermissionDTO);
        documentPermission = documentPermissionRepository.save(documentPermission);
        documentPermissionSearchRepository.index(documentPermission);
        return documentPermissionMapper.toDto(documentPermission);
    }

    /**
     * Update a documentPermission.
     *
     * @param documentPermissionDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentPermissionDTO update(DocumentPermissionDTO documentPermissionDTO) {
        LOG.debug("Request to update DocumentPermission : {}", documentPermissionDTO);
        DocumentPermission documentPermission = documentPermissionMapper.toEntity(documentPermissionDTO);
        documentPermission = documentPermissionRepository.save(documentPermission);
        documentPermissionSearchRepository.index(documentPermission);
        return documentPermissionMapper.toDto(documentPermission);
    }

    /**
     * Partially update a documentPermission.
     *
     * @param documentPermissionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocumentPermissionDTO> partialUpdate(DocumentPermissionDTO documentPermissionDTO) {
        LOG.debug("Request to partially update DocumentPermission : {}", documentPermissionDTO);

        return documentPermissionRepository
            .findById(documentPermissionDTO.getId())
            .map(existingDocumentPermission -> {
                documentPermissionMapper.partialUpdate(existingDocumentPermission, documentPermissionDTO);

                return existingDocumentPermission;
            })
            .map(documentPermissionRepository::save)
            .map(savedDocumentPermission -> {
                documentPermissionSearchRepository.index(savedDocumentPermission);
                return savedDocumentPermission;
            })
            .map(documentPermissionMapper::toDto);
    }

    /**
     * Get one documentPermission by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentPermissionDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentPermission : {}", id);
        return documentPermissionRepository.findById(id).map(documentPermissionMapper::toDto);
    }

    /**
     * Delete the documentPermission by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentPermission : {}", id);
        documentPermissionRepository.deleteById(id);
        documentPermissionSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the documentPermission corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentPermissionDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of DocumentPermissions for query {}", query);
        return documentPermissionSearchRepository.search(query, pageable).map(documentPermissionMapper::toDto);
    }
}
