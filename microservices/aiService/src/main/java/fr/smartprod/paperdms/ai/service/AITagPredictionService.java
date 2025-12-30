package fr.smartprod.paperdms.ai.service;

import fr.smartprod.paperdms.ai.domain.AITagPrediction;
import fr.smartprod.paperdms.ai.repository.AITagPredictionRepository;
import fr.smartprod.paperdms.ai.repository.search.AITagPredictionSearchRepository;
import fr.smartprod.paperdms.ai.service.dto.AITagPredictionDTO;
import fr.smartprod.paperdms.ai.service.mapper.AITagPredictionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.ai.domain.AITagPrediction}.
 */
@Service
@Transactional
public class AITagPredictionService {

    private static final Logger LOG = LoggerFactory.getLogger(AITagPredictionService.class);

    private final AITagPredictionRepository aITagPredictionRepository;

    private final AITagPredictionMapper aITagPredictionMapper;

    private final AITagPredictionSearchRepository aITagPredictionSearchRepository;

    public AITagPredictionService(
        AITagPredictionRepository aITagPredictionRepository,
        AITagPredictionMapper aITagPredictionMapper,
        AITagPredictionSearchRepository aITagPredictionSearchRepository
    ) {
        this.aITagPredictionRepository = aITagPredictionRepository;
        this.aITagPredictionMapper = aITagPredictionMapper;
        this.aITagPredictionSearchRepository = aITagPredictionSearchRepository;
    }

    /**
     * Save a aITagPrediction.
     *
     * @param aITagPredictionDTO the entity to save.
     * @return the persisted entity.
     */
    public AITagPredictionDTO save(AITagPredictionDTO aITagPredictionDTO) {
        LOG.debug("Request to save AITagPrediction : {}", aITagPredictionDTO);
        AITagPrediction aITagPrediction = aITagPredictionMapper.toEntity(aITagPredictionDTO);
        aITagPrediction = aITagPredictionRepository.save(aITagPrediction);
        aITagPredictionSearchRepository.index(aITagPrediction);
        return aITagPredictionMapper.toDto(aITagPrediction);
    }

    /**
     * Update a aITagPrediction.
     *
     * @param aITagPredictionDTO the entity to save.
     * @return the persisted entity.
     */
    public AITagPredictionDTO update(AITagPredictionDTO aITagPredictionDTO) {
        LOG.debug("Request to update AITagPrediction : {}", aITagPredictionDTO);
        AITagPrediction aITagPrediction = aITagPredictionMapper.toEntity(aITagPredictionDTO);
        aITagPrediction = aITagPredictionRepository.save(aITagPrediction);
        aITagPredictionSearchRepository.index(aITagPrediction);
        return aITagPredictionMapper.toDto(aITagPrediction);
    }

    /**
     * Partially update a aITagPrediction.
     *
     * @param aITagPredictionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AITagPredictionDTO> partialUpdate(AITagPredictionDTO aITagPredictionDTO) {
        LOG.debug("Request to partially update AITagPrediction : {}", aITagPredictionDTO);

        return aITagPredictionRepository
            .findById(aITagPredictionDTO.getId())
            .map(existingAITagPrediction -> {
                aITagPredictionMapper.partialUpdate(existingAITagPrediction, aITagPredictionDTO);

                return existingAITagPrediction;
            })
            .map(aITagPredictionRepository::save)
            .map(savedAITagPrediction -> {
                aITagPredictionSearchRepository.index(savedAITagPrediction);
                return savedAITagPrediction;
            })
            .map(aITagPredictionMapper::toDto);
    }

    /**
     * Get one aITagPrediction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AITagPredictionDTO> findOne(Long id) {
        LOG.debug("Request to get AITagPrediction : {}", id);
        return aITagPredictionRepository.findById(id).map(aITagPredictionMapper::toDto);
    }

    /**
     * Delete the aITagPrediction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AITagPrediction : {}", id);
        aITagPredictionRepository.deleteById(id);
        aITagPredictionSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the aITagPrediction corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AITagPredictionDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of AITagPredictions for query {}", query);
        return aITagPredictionSearchRepository.search(query, pageable).map(aITagPredictionMapper::toDto);
    }
}
