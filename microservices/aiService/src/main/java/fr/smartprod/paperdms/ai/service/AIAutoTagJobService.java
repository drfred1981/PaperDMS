package fr.smartprod.paperdms.ai.service;

import fr.smartprod.paperdms.ai.domain.AIAutoTagJob;
import fr.smartprod.paperdms.ai.repository.AIAutoTagJobRepository;
import fr.smartprod.paperdms.ai.repository.search.AIAutoTagJobSearchRepository;
import fr.smartprod.paperdms.ai.service.dto.AIAutoTagJobDTO;
import fr.smartprod.paperdms.ai.service.mapper.AIAutoTagJobMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.ai.domain.AIAutoTagJob}.
 */
@Service
@Transactional
public class AIAutoTagJobService {

    private static final Logger LOG = LoggerFactory.getLogger(AIAutoTagJobService.class);

    private final AIAutoTagJobRepository aIAutoTagJobRepository;

    private final AIAutoTagJobMapper aIAutoTagJobMapper;

    private final AIAutoTagJobSearchRepository aIAutoTagJobSearchRepository;

    public AIAutoTagJobService(
        AIAutoTagJobRepository aIAutoTagJobRepository,
        AIAutoTagJobMapper aIAutoTagJobMapper,
        AIAutoTagJobSearchRepository aIAutoTagJobSearchRepository
    ) {
        this.aIAutoTagJobRepository = aIAutoTagJobRepository;
        this.aIAutoTagJobMapper = aIAutoTagJobMapper;
        this.aIAutoTagJobSearchRepository = aIAutoTagJobSearchRepository;
    }

    /**
     * Save a aIAutoTagJob.
     *
     * @param aIAutoTagJobDTO the entity to save.
     * @return the persisted entity.
     */
    public AIAutoTagJobDTO save(AIAutoTagJobDTO aIAutoTagJobDTO) {
        LOG.debug("Request to save AIAutoTagJob : {}", aIAutoTagJobDTO);
        AIAutoTagJob aIAutoTagJob = aIAutoTagJobMapper.toEntity(aIAutoTagJobDTO);
        aIAutoTagJob = aIAutoTagJobRepository.save(aIAutoTagJob);
        aIAutoTagJobSearchRepository.index(aIAutoTagJob);
        return aIAutoTagJobMapper.toDto(aIAutoTagJob);
    }

    /**
     * Update a aIAutoTagJob.
     *
     * @param aIAutoTagJobDTO the entity to save.
     * @return the persisted entity.
     */
    public AIAutoTagJobDTO update(AIAutoTagJobDTO aIAutoTagJobDTO) {
        LOG.debug("Request to update AIAutoTagJob : {}", aIAutoTagJobDTO);
        AIAutoTagJob aIAutoTagJob = aIAutoTagJobMapper.toEntity(aIAutoTagJobDTO);
        aIAutoTagJob = aIAutoTagJobRepository.save(aIAutoTagJob);
        aIAutoTagJobSearchRepository.index(aIAutoTagJob);
        return aIAutoTagJobMapper.toDto(aIAutoTagJob);
    }

    /**
     * Partially update a aIAutoTagJob.
     *
     * @param aIAutoTagJobDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AIAutoTagJobDTO> partialUpdate(AIAutoTagJobDTO aIAutoTagJobDTO) {
        LOG.debug("Request to partially update AIAutoTagJob : {}", aIAutoTagJobDTO);

        return aIAutoTagJobRepository
            .findById(aIAutoTagJobDTO.getId())
            .map(existingAIAutoTagJob -> {
                aIAutoTagJobMapper.partialUpdate(existingAIAutoTagJob, aIAutoTagJobDTO);

                return existingAIAutoTagJob;
            })
            .map(aIAutoTagJobRepository::save)
            .map(savedAIAutoTagJob -> {
                aIAutoTagJobSearchRepository.index(savedAIAutoTagJob);
                return savedAIAutoTagJob;
            })
            .map(aIAutoTagJobMapper::toDto);
    }

    /**
     * Get one aIAutoTagJob by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AIAutoTagJobDTO> findOne(Long id) {
        LOG.debug("Request to get AIAutoTagJob : {}", id);
        return aIAutoTagJobRepository.findById(id).map(aIAutoTagJobMapper::toDto);
    }

    /**
     * Delete the aIAutoTagJob by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AIAutoTagJob : {}", id);
        aIAutoTagJobRepository.deleteById(id);
        aIAutoTagJobSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the aIAutoTagJob corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AIAutoTagJobDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of AIAutoTagJobs for query {}", query);
        return aIAutoTagJobSearchRepository.search(query, pageable).map(aIAutoTagJobMapper::toDto);
    }
}
