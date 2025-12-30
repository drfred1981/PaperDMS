package fr.smartprod.paperdms.similarity.service;

import fr.smartprod.paperdms.similarity.domain.SimilarityDocumentComparison;
import fr.smartprod.paperdms.similarity.repository.SimilarityDocumentComparisonRepository;
import fr.smartprod.paperdms.similarity.repository.search.SimilarityDocumentComparisonSearchRepository;
import fr.smartprod.paperdms.similarity.service.dto.SimilarityDocumentComparisonDTO;
import fr.smartprod.paperdms.similarity.service.mapper.SimilarityDocumentComparisonMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.similarity.domain.SimilarityDocumentComparison}.
 */
@Service
@Transactional
public class SimilarityDocumentComparisonService {

    private static final Logger LOG = LoggerFactory.getLogger(SimilarityDocumentComparisonService.class);

    private final SimilarityDocumentComparisonRepository similarityDocumentComparisonRepository;

    private final SimilarityDocumentComparisonMapper similarityDocumentComparisonMapper;

    private final SimilarityDocumentComparisonSearchRepository similarityDocumentComparisonSearchRepository;

    public SimilarityDocumentComparisonService(
        SimilarityDocumentComparisonRepository similarityDocumentComparisonRepository,
        SimilarityDocumentComparisonMapper similarityDocumentComparisonMapper,
        SimilarityDocumentComparisonSearchRepository similarityDocumentComparisonSearchRepository
    ) {
        this.similarityDocumentComparisonRepository = similarityDocumentComparisonRepository;
        this.similarityDocumentComparisonMapper = similarityDocumentComparisonMapper;
        this.similarityDocumentComparisonSearchRepository = similarityDocumentComparisonSearchRepository;
    }

    /**
     * Save a similarityDocumentComparison.
     *
     * @param similarityDocumentComparisonDTO the entity to save.
     * @return the persisted entity.
     */
    public SimilarityDocumentComparisonDTO save(SimilarityDocumentComparisonDTO similarityDocumentComparisonDTO) {
        LOG.debug("Request to save SimilarityDocumentComparison : {}", similarityDocumentComparisonDTO);
        SimilarityDocumentComparison similarityDocumentComparison = similarityDocumentComparisonMapper.toEntity(
            similarityDocumentComparisonDTO
        );
        similarityDocumentComparison = similarityDocumentComparisonRepository.save(similarityDocumentComparison);
        similarityDocumentComparisonSearchRepository.index(similarityDocumentComparison);
        return similarityDocumentComparisonMapper.toDto(similarityDocumentComparison);
    }

    /**
     * Update a similarityDocumentComparison.
     *
     * @param similarityDocumentComparisonDTO the entity to save.
     * @return the persisted entity.
     */
    public SimilarityDocumentComparisonDTO update(SimilarityDocumentComparisonDTO similarityDocumentComparisonDTO) {
        LOG.debug("Request to update SimilarityDocumentComparison : {}", similarityDocumentComparisonDTO);
        SimilarityDocumentComparison similarityDocumentComparison = similarityDocumentComparisonMapper.toEntity(
            similarityDocumentComparisonDTO
        );
        similarityDocumentComparison = similarityDocumentComparisonRepository.save(similarityDocumentComparison);
        similarityDocumentComparisonSearchRepository.index(similarityDocumentComparison);
        return similarityDocumentComparisonMapper.toDto(similarityDocumentComparison);
    }

    /**
     * Partially update a similarityDocumentComparison.
     *
     * @param similarityDocumentComparisonDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SimilarityDocumentComparisonDTO> partialUpdate(SimilarityDocumentComparisonDTO similarityDocumentComparisonDTO) {
        LOG.debug("Request to partially update SimilarityDocumentComparison : {}", similarityDocumentComparisonDTO);

        return similarityDocumentComparisonRepository
            .findById(similarityDocumentComparisonDTO.getId())
            .map(existingSimilarityDocumentComparison -> {
                similarityDocumentComparisonMapper.partialUpdate(existingSimilarityDocumentComparison, similarityDocumentComparisonDTO);

                return existingSimilarityDocumentComparison;
            })
            .map(similarityDocumentComparisonRepository::save)
            .map(savedSimilarityDocumentComparison -> {
                similarityDocumentComparisonSearchRepository.index(savedSimilarityDocumentComparison);
                return savedSimilarityDocumentComparison;
            })
            .map(similarityDocumentComparisonMapper::toDto);
    }

    /**
     * Get one similarityDocumentComparison by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SimilarityDocumentComparisonDTO> findOne(Long id) {
        LOG.debug("Request to get SimilarityDocumentComparison : {}", id);
        return similarityDocumentComparisonRepository.findById(id).map(similarityDocumentComparisonMapper::toDto);
    }

    /**
     * Delete the similarityDocumentComparison by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SimilarityDocumentComparison : {}", id);
        similarityDocumentComparisonRepository.deleteById(id);
        similarityDocumentComparisonSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the similarityDocumentComparison corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SimilarityDocumentComparisonDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of SimilarityDocumentComparisons for query {}", query);
        return similarityDocumentComparisonSearchRepository.search(query, pageable).map(similarityDocumentComparisonMapper::toDto);
    }
}
