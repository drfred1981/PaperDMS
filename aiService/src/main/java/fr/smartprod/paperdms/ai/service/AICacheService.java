package fr.smartprod.paperdms.ai.service;

import fr.smartprod.paperdms.ai.domain.AICache;
import fr.smartprod.paperdms.ai.repository.AICacheRepository;
import fr.smartprod.paperdms.ai.repository.search.AICacheSearchRepository;
import fr.smartprod.paperdms.ai.service.dto.AICacheDTO;
import fr.smartprod.paperdms.ai.service.mapper.AICacheMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.ai.domain.AICache}.
 */
@Service
@Transactional
public class AICacheService {

    private static final Logger LOG = LoggerFactory.getLogger(AICacheService.class);

    private final AICacheRepository aICacheRepository;

    private final AICacheMapper aICacheMapper;

    private final AICacheSearchRepository aICacheSearchRepository;

    public AICacheService(
        AICacheRepository aICacheRepository,
        AICacheMapper aICacheMapper,
        AICacheSearchRepository aICacheSearchRepository
    ) {
        this.aICacheRepository = aICacheRepository;
        this.aICacheMapper = aICacheMapper;
        this.aICacheSearchRepository = aICacheSearchRepository;
    }

    /**
     * Save a aICache.
     *
     * @param aICacheDTO the entity to save.
     * @return the persisted entity.
     */
    public AICacheDTO save(AICacheDTO aICacheDTO) {
        LOG.debug("Request to save AICache : {}", aICacheDTO);
        AICache aICache = aICacheMapper.toEntity(aICacheDTO);
        aICache = aICacheRepository.save(aICache);
        aICacheSearchRepository.index(aICache);
        return aICacheMapper.toDto(aICache);
    }

    /**
     * Update a aICache.
     *
     * @param aICacheDTO the entity to save.
     * @return the persisted entity.
     */
    public AICacheDTO update(AICacheDTO aICacheDTO) {
        LOG.debug("Request to update AICache : {}", aICacheDTO);
        AICache aICache = aICacheMapper.toEntity(aICacheDTO);
        aICache = aICacheRepository.save(aICache);
        aICacheSearchRepository.index(aICache);
        return aICacheMapper.toDto(aICache);
    }

    /**
     * Partially update a aICache.
     *
     * @param aICacheDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AICacheDTO> partialUpdate(AICacheDTO aICacheDTO) {
        LOG.debug("Request to partially update AICache : {}", aICacheDTO);

        return aICacheRepository
            .findById(aICacheDTO.getId())
            .map(existingAICache -> {
                aICacheMapper.partialUpdate(existingAICache, aICacheDTO);

                return existingAICache;
            })
            .map(aICacheRepository::save)
            .map(savedAICache -> {
                aICacheSearchRepository.index(savedAICache);
                return savedAICache;
            })
            .map(aICacheMapper::toDto);
    }

    /**
     * Get one aICache by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AICacheDTO> findOne(Long id) {
        LOG.debug("Request to get AICache : {}", id);
        return aICacheRepository.findById(id).map(aICacheMapper::toDto);
    }

    /**
     * Delete the aICache by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AICache : {}", id);
        aICacheRepository.deleteById(id);
        aICacheSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the aICache corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AICacheDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of AICaches for query {}", query);
        return aICacheSearchRepository.search(query, pageable).map(aICacheMapper::toDto);
    }
}
