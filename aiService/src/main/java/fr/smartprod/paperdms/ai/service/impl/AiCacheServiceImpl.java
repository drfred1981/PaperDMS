package fr.smartprod.paperdms.ai.service.impl;

import fr.smartprod.paperdms.ai.domain.AiCache;
import fr.smartprod.paperdms.ai.repository.AiCacheRepository;
import fr.smartprod.paperdms.ai.service.AiCacheService;
import fr.smartprod.paperdms.ai.service.dto.AiCacheDTO;
import fr.smartprod.paperdms.ai.service.mapper.AiCacheMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.ai.domain.AiCache}.
 */
@Service
@Transactional
public class AiCacheServiceImpl implements AiCacheService {

    private static final Logger LOG = LoggerFactory.getLogger(AiCacheServiceImpl.class);

    private final AiCacheRepository aiCacheRepository;

    private final AiCacheMapper aiCacheMapper;

    public AiCacheServiceImpl(AiCacheRepository aiCacheRepository, AiCacheMapper aiCacheMapper) {
        this.aiCacheRepository = aiCacheRepository;
        this.aiCacheMapper = aiCacheMapper;
    }

    @Override
    public AiCacheDTO save(AiCacheDTO aiCacheDTO) {
        LOG.debug("Request to save AiCache : {}", aiCacheDTO);
        AiCache aiCache = aiCacheMapper.toEntity(aiCacheDTO);
        aiCache = aiCacheRepository.save(aiCache);
        return aiCacheMapper.toDto(aiCache);
    }

    @Override
    public AiCacheDTO update(AiCacheDTO aiCacheDTO) {
        LOG.debug("Request to update AiCache : {}", aiCacheDTO);
        AiCache aiCache = aiCacheMapper.toEntity(aiCacheDTO);
        aiCache = aiCacheRepository.save(aiCache);
        return aiCacheMapper.toDto(aiCache);
    }

    @Override
    public Optional<AiCacheDTO> partialUpdate(AiCacheDTO aiCacheDTO) {
        LOG.debug("Request to partially update AiCache : {}", aiCacheDTO);

        return aiCacheRepository
            .findById(aiCacheDTO.getId())
            .map(existingAiCache -> {
                aiCacheMapper.partialUpdate(existingAiCache, aiCacheDTO);

                return existingAiCache;
            })
            .map(aiCacheRepository::save)
            .map(aiCacheMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AiCacheDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all AiCaches");
        return aiCacheRepository.findAll(pageable).map(aiCacheMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AiCacheDTO> findOne(Long id) {
        LOG.debug("Request to get AiCache : {}", id);
        return aiCacheRepository.findById(id).map(aiCacheMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete AiCache : {}", id);
        aiCacheRepository.deleteById(id);
    }
}
