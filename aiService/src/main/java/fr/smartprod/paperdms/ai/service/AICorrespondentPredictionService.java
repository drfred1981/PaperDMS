package fr.smartprod.paperdms.ai.service;

import fr.smartprod.paperdms.ai.domain.AICorrespondentPrediction;
import fr.smartprod.paperdms.ai.repository.AICorrespondentPredictionRepository;
import fr.smartprod.paperdms.ai.repository.search.AICorrespondentPredictionSearchRepository;
import fr.smartprod.paperdms.ai.service.dto.AICorrespondentPredictionDTO;
import fr.smartprod.paperdms.ai.service.mapper.AICorrespondentPredictionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.ai.domain.AICorrespondentPrediction}.
 */
@Service
@Transactional
public class AICorrespondentPredictionService {

    private static final Logger LOG = LoggerFactory.getLogger(AICorrespondentPredictionService.class);

    private final AICorrespondentPredictionRepository aICorrespondentPredictionRepository;

    private final AICorrespondentPredictionMapper aICorrespondentPredictionMapper;

    private final AICorrespondentPredictionSearchRepository aICorrespondentPredictionSearchRepository;

    public AICorrespondentPredictionService(
        AICorrespondentPredictionRepository aICorrespondentPredictionRepository,
        AICorrespondentPredictionMapper aICorrespondentPredictionMapper,
        AICorrespondentPredictionSearchRepository aICorrespondentPredictionSearchRepository
    ) {
        this.aICorrespondentPredictionRepository = aICorrespondentPredictionRepository;
        this.aICorrespondentPredictionMapper = aICorrespondentPredictionMapper;
        this.aICorrespondentPredictionSearchRepository = aICorrespondentPredictionSearchRepository;
    }

    /**
     * Save a aICorrespondentPrediction.
     *
     * @param aICorrespondentPredictionDTO the entity to save.
     * @return the persisted entity.
     */
    public AICorrespondentPredictionDTO save(AICorrespondentPredictionDTO aICorrespondentPredictionDTO) {
        LOG.debug("Request to save AICorrespondentPrediction : {}", aICorrespondentPredictionDTO);
        AICorrespondentPrediction aICorrespondentPrediction = aICorrespondentPredictionMapper.toEntity(aICorrespondentPredictionDTO);
        aICorrespondentPrediction = aICorrespondentPredictionRepository.save(aICorrespondentPrediction);
        aICorrespondentPredictionSearchRepository.index(aICorrespondentPrediction);
        return aICorrespondentPredictionMapper.toDto(aICorrespondentPrediction);
    }

    /**
     * Update a aICorrespondentPrediction.
     *
     * @param aICorrespondentPredictionDTO the entity to save.
     * @return the persisted entity.
     */
    public AICorrespondentPredictionDTO update(AICorrespondentPredictionDTO aICorrespondentPredictionDTO) {
        LOG.debug("Request to update AICorrespondentPrediction : {}", aICorrespondentPredictionDTO);
        AICorrespondentPrediction aICorrespondentPrediction = aICorrespondentPredictionMapper.toEntity(aICorrespondentPredictionDTO);
        aICorrespondentPrediction = aICorrespondentPredictionRepository.save(aICorrespondentPrediction);
        aICorrespondentPredictionSearchRepository.index(aICorrespondentPrediction);
        return aICorrespondentPredictionMapper.toDto(aICorrespondentPrediction);
    }

    /**
     * Partially update a aICorrespondentPrediction.
     *
     * @param aICorrespondentPredictionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AICorrespondentPredictionDTO> partialUpdate(AICorrespondentPredictionDTO aICorrespondentPredictionDTO) {
        LOG.debug("Request to partially update AICorrespondentPrediction : {}", aICorrespondentPredictionDTO);

        return aICorrespondentPredictionRepository
            .findById(aICorrespondentPredictionDTO.getId())
            .map(existingAICorrespondentPrediction -> {
                aICorrespondentPredictionMapper.partialUpdate(existingAICorrespondentPrediction, aICorrespondentPredictionDTO);

                return existingAICorrespondentPrediction;
            })
            .map(aICorrespondentPredictionRepository::save)
            .map(savedAICorrespondentPrediction -> {
                aICorrespondentPredictionSearchRepository.index(savedAICorrespondentPrediction);
                return savedAICorrespondentPrediction;
            })
            .map(aICorrespondentPredictionMapper::toDto);
    }

    /**
     * Get one aICorrespondentPrediction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AICorrespondentPredictionDTO> findOne(Long id) {
        LOG.debug("Request to get AICorrespondentPrediction : {}", id);
        return aICorrespondentPredictionRepository.findById(id).map(aICorrespondentPredictionMapper::toDto);
    }

    /**
     * Delete the aICorrespondentPrediction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AICorrespondentPrediction : {}", id);
        aICorrespondentPredictionRepository.deleteById(id);
        aICorrespondentPredictionSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the aICorrespondentPrediction corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AICorrespondentPredictionDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of AICorrespondentPredictions for query {}", query);
        return aICorrespondentPredictionSearchRepository.search(query, pageable).map(aICorrespondentPredictionMapper::toDto);
    }
}
