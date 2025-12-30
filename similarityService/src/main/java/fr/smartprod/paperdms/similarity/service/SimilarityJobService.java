package fr.smartprod.paperdms.similarity.service;

import fr.smartprod.paperdms.similarity.domain.SimilarityJob;
import fr.smartprod.paperdms.similarity.repository.SimilarityJobRepository;
import fr.smartprod.paperdms.similarity.repository.search.SimilarityJobSearchRepository;
import fr.smartprod.paperdms.similarity.service.dto.SimilarityJobDTO;
import fr.smartprod.paperdms.similarity.service.mapper.SimilarityJobMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.similarity.domain.SimilarityJob}.
 */
@Service
@Transactional
public class SimilarityJobService {

    private static final Logger LOG = LoggerFactory.getLogger(SimilarityJobService.class);

    private final SimilarityJobRepository similarityJobRepository;

    private final SimilarityJobMapper similarityJobMapper;

    private final SimilarityJobSearchRepository similarityJobSearchRepository;

    public SimilarityJobService(
        SimilarityJobRepository similarityJobRepository,
        SimilarityJobMapper similarityJobMapper,
        SimilarityJobSearchRepository similarityJobSearchRepository
    ) {
        this.similarityJobRepository = similarityJobRepository;
        this.similarityJobMapper = similarityJobMapper;
        this.similarityJobSearchRepository = similarityJobSearchRepository;
    }

    /**
     * Save a similarityJob.
     *
     * @param similarityJobDTO the entity to save.
     * @return the persisted entity.
     */
    public SimilarityJobDTO save(SimilarityJobDTO similarityJobDTO) {
        LOG.debug("Request to save SimilarityJob : {}", similarityJobDTO);
        SimilarityJob similarityJob = similarityJobMapper.toEntity(similarityJobDTO);
        similarityJob = similarityJobRepository.save(similarityJob);
        similarityJobSearchRepository.index(similarityJob);
        return similarityJobMapper.toDto(similarityJob);
    }

    /**
     * Update a similarityJob.
     *
     * @param similarityJobDTO the entity to save.
     * @return the persisted entity.
     */
    public SimilarityJobDTO update(SimilarityJobDTO similarityJobDTO) {
        LOG.debug("Request to update SimilarityJob : {}", similarityJobDTO);
        SimilarityJob similarityJob = similarityJobMapper.toEntity(similarityJobDTO);
        similarityJob = similarityJobRepository.save(similarityJob);
        similarityJobSearchRepository.index(similarityJob);
        return similarityJobMapper.toDto(similarityJob);
    }

    /**
     * Partially update a similarityJob.
     *
     * @param similarityJobDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SimilarityJobDTO> partialUpdate(SimilarityJobDTO similarityJobDTO) {
        LOG.debug("Request to partially update SimilarityJob : {}", similarityJobDTO);

        return similarityJobRepository
            .findById(similarityJobDTO.getId())
            .map(existingSimilarityJob -> {
                similarityJobMapper.partialUpdate(existingSimilarityJob, similarityJobDTO);

                return existingSimilarityJob;
            })
            .map(similarityJobRepository::save)
            .map(savedSimilarityJob -> {
                similarityJobSearchRepository.index(savedSimilarityJob);
                return savedSimilarityJob;
            })
            .map(similarityJobMapper::toDto);
    }

    /**
     * Get one similarityJob by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SimilarityJobDTO> findOne(Long id) {
        LOG.debug("Request to get SimilarityJob : {}", id);
        return similarityJobRepository.findById(id).map(similarityJobMapper::toDto);
    }

    /**
     * Delete the similarityJob by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SimilarityJob : {}", id);
        similarityJobRepository.deleteById(id);
        similarityJobSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the similarityJob corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SimilarityJobDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of SimilarityJobs for query {}", query);
        return similarityJobSearchRepository.search(query, pageable).map(similarityJobMapper::toDto);
    }
}
