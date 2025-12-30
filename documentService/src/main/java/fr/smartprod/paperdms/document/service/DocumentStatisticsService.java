package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.document.domain.DocumentStatistics;
import fr.smartprod.paperdms.document.repository.DocumentStatisticsRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentStatisticsSearchRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentStatisticsDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentStatisticsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.DocumentStatistics}.
 */
@Service
@Transactional
public class DocumentStatisticsService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentStatisticsService.class);

    private final DocumentStatisticsRepository documentStatisticsRepository;

    private final DocumentStatisticsMapper documentStatisticsMapper;

    private final DocumentStatisticsSearchRepository documentStatisticsSearchRepository;

    public DocumentStatisticsService(
        DocumentStatisticsRepository documentStatisticsRepository,
        DocumentStatisticsMapper documentStatisticsMapper,
        DocumentStatisticsSearchRepository documentStatisticsSearchRepository
    ) {
        this.documentStatisticsRepository = documentStatisticsRepository;
        this.documentStatisticsMapper = documentStatisticsMapper;
        this.documentStatisticsSearchRepository = documentStatisticsSearchRepository;
    }

    /**
     * Save a documentStatistics.
     *
     * @param documentStatisticsDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentStatisticsDTO save(DocumentStatisticsDTO documentStatisticsDTO) {
        LOG.debug("Request to save DocumentStatistics : {}", documentStatisticsDTO);
        DocumentStatistics documentStatistics = documentStatisticsMapper.toEntity(documentStatisticsDTO);
        documentStatistics = documentStatisticsRepository.save(documentStatistics);
        documentStatisticsSearchRepository.index(documentStatistics);
        return documentStatisticsMapper.toDto(documentStatistics);
    }

    /**
     * Update a documentStatistics.
     *
     * @param documentStatisticsDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentStatisticsDTO update(DocumentStatisticsDTO documentStatisticsDTO) {
        LOG.debug("Request to update DocumentStatistics : {}", documentStatisticsDTO);
        DocumentStatistics documentStatistics = documentStatisticsMapper.toEntity(documentStatisticsDTO);
        documentStatistics = documentStatisticsRepository.save(documentStatistics);
        documentStatisticsSearchRepository.index(documentStatistics);
        return documentStatisticsMapper.toDto(documentStatistics);
    }

    /**
     * Partially update a documentStatistics.
     *
     * @param documentStatisticsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocumentStatisticsDTO> partialUpdate(DocumentStatisticsDTO documentStatisticsDTO) {
        LOG.debug("Request to partially update DocumentStatistics : {}", documentStatisticsDTO);

        return documentStatisticsRepository
            .findById(documentStatisticsDTO.getId())
            .map(existingDocumentStatistics -> {
                documentStatisticsMapper.partialUpdate(existingDocumentStatistics, documentStatisticsDTO);

                return existingDocumentStatistics;
            })
            .map(documentStatisticsRepository::save)
            .map(savedDocumentStatistics -> {
                documentStatisticsSearchRepository.index(savedDocumentStatistics);
                return savedDocumentStatistics;
            })
            .map(documentStatisticsMapper::toDto);
    }

    /**
     * Get one documentStatistics by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentStatisticsDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentStatistics : {}", id);
        return documentStatisticsRepository.findById(id).map(documentStatisticsMapper::toDto);
    }

    /**
     * Delete the documentStatistics by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentStatistics : {}", id);
        documentStatisticsRepository.deleteById(id);
        documentStatisticsSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the documentStatistics corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentStatisticsDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of DocumentStatistics for query {}", query);
        return documentStatisticsSearchRepository.search(query, pageable).map(documentStatisticsMapper::toDto);
    }
}
