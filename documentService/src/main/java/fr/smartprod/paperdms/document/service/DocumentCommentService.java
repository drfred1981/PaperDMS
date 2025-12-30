package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.DocumentComment;
import fr.smartprod.paperdms.document.repository.DocumentCommentRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentCommentSearchRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentCommentDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentCommentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.DocumentComment}.
 */
@Service
@Transactional
public class DocumentCommentService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentCommentService.class);

    private final DocumentCommentRepository documentCommentRepository;

    private final DocumentCommentMapper documentCommentMapper;

    private final DocumentCommentSearchRepository documentCommentSearchRepository;

    public DocumentCommentService(
        DocumentCommentRepository documentCommentRepository,
        DocumentCommentMapper documentCommentMapper,
        DocumentCommentSearchRepository documentCommentSearchRepository
    ) {
        this.documentCommentRepository = documentCommentRepository;
        this.documentCommentMapper = documentCommentMapper;
        this.documentCommentSearchRepository = documentCommentSearchRepository;
    }

    /**
     * Save a documentComment.
     *
     * @param documentCommentDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentCommentDTO save(DocumentCommentDTO documentCommentDTO) {
        LOG.debug("Request to save DocumentComment : {}", documentCommentDTO);
        DocumentComment documentComment = documentCommentMapper.toEntity(documentCommentDTO);
        documentComment = documentCommentRepository.save(documentComment);
        documentCommentSearchRepository.index(documentComment);
        return documentCommentMapper.toDto(documentComment);
    }

    /**
     * Update a documentComment.
     *
     * @param documentCommentDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentCommentDTO update(DocumentCommentDTO documentCommentDTO) {
        LOG.debug("Request to update DocumentComment : {}", documentCommentDTO);
        DocumentComment documentComment = documentCommentMapper.toEntity(documentCommentDTO);
        documentComment = documentCommentRepository.save(documentComment);
        documentCommentSearchRepository.index(documentComment);
        return documentCommentMapper.toDto(documentComment);
    }

    /**
     * Partially update a documentComment.
     *
     * @param documentCommentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocumentCommentDTO> partialUpdate(DocumentCommentDTO documentCommentDTO) {
        LOG.debug("Request to partially update DocumentComment : {}", documentCommentDTO);

        return documentCommentRepository
            .findById(documentCommentDTO.getId())
            .map(existingDocumentComment -> {
                documentCommentMapper.partialUpdate(existingDocumentComment, documentCommentDTO);

                return existingDocumentComment;
            })
            .map(documentCommentRepository::save)
            .map(savedDocumentComment -> {
                documentCommentSearchRepository.index(savedDocumentComment);
                return savedDocumentComment;
            })
            .map(documentCommentMapper::toDto);
    }

    /**
     * Get one documentComment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentCommentDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentComment : {}", id);
        return documentCommentRepository.findById(id).map(documentCommentMapper::toDto);
    }

    /**
     * Delete the documentComment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentComment : {}", id);
        documentCommentRepository.deleteById(id);
        documentCommentSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the documentComment corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentCommentDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of DocumentComments for query {}", query);
        return documentCommentSearchRepository.search(query, pageable).map(documentCommentMapper::toDto);
    }
}
