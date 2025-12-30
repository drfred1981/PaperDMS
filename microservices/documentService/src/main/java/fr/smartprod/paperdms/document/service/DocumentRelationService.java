package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.DocumentRelation;
import fr.smartprod.paperdms.document.repository.DocumentRelationRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentRelationSearchRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentRelationDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentRelationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.DocumentRelation}.
 */
@Service
@Transactional
public class DocumentRelationService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentRelationService.class);

    private final DocumentRelationRepository documentRelationRepository;

    private final DocumentRelationMapper documentRelationMapper;

    private final DocumentRelationSearchRepository documentRelationSearchRepository;

    public DocumentRelationService(
        DocumentRelationRepository documentRelationRepository,
        DocumentRelationMapper documentRelationMapper,
        DocumentRelationSearchRepository documentRelationSearchRepository
    ) {
        this.documentRelationRepository = documentRelationRepository;
        this.documentRelationMapper = documentRelationMapper;
        this.documentRelationSearchRepository = documentRelationSearchRepository;
    }

    /**
     * Save a documentRelation.
     *
     * @param documentRelationDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentRelationDTO save(DocumentRelationDTO documentRelationDTO) {
        LOG.debug("Request to save DocumentRelation : {}", documentRelationDTO);
        DocumentRelation documentRelation = documentRelationMapper.toEntity(documentRelationDTO);
        documentRelation = documentRelationRepository.save(documentRelation);
        documentRelationSearchRepository.index(documentRelation);
        return documentRelationMapper.toDto(documentRelation);
    }

    /**
     * Update a documentRelation.
     *
     * @param documentRelationDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentRelationDTO update(DocumentRelationDTO documentRelationDTO) {
        LOG.debug("Request to update DocumentRelation : {}", documentRelationDTO);
        DocumentRelation documentRelation = documentRelationMapper.toEntity(documentRelationDTO);
        documentRelation = documentRelationRepository.save(documentRelation);
        documentRelationSearchRepository.index(documentRelation);
        return documentRelationMapper.toDto(documentRelation);
    }

    /**
     * Partially update a documentRelation.
     *
     * @param documentRelationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocumentRelationDTO> partialUpdate(DocumentRelationDTO documentRelationDTO) {
        LOG.debug("Request to partially update DocumentRelation : {}", documentRelationDTO);

        return documentRelationRepository
            .findById(documentRelationDTO.getId())
            .map(existingDocumentRelation -> {
                documentRelationMapper.partialUpdate(existingDocumentRelation, documentRelationDTO);

                return existingDocumentRelation;
            })
            .map(documentRelationRepository::save)
            .map(savedDocumentRelation -> {
                documentRelationSearchRepository.index(savedDocumentRelation);
                return savedDocumentRelation;
            })
            .map(documentRelationMapper::toDto);
    }

    /**
     * Get one documentRelation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentRelationDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentRelation : {}", id);
        return documentRelationRepository.findById(id).map(documentRelationMapper::toDto);
    }

    /**
     * Delete the documentRelation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentRelation : {}", id);
        documentRelationRepository.deleteById(id);
        documentRelationSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the documentRelation corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentRelationDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of DocumentRelations for query {}", query);
        return documentRelationSearchRepository.search(query, pageable).map(documentRelationMapper::toDto);
    }
}
