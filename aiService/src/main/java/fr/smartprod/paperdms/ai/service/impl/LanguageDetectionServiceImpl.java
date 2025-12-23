package fr.smartprod.paperdms.ai.service.impl;

import fr.smartprod.paperdms.ai.domain.LanguageDetection;
import fr.smartprod.paperdms.ai.repository.LanguageDetectionRepository;
import fr.smartprod.paperdms.ai.service.LanguageDetectionService;
import fr.smartprod.paperdms.ai.service.dto.LanguageDetectionDTO;
import fr.smartprod.paperdms.ai.service.mapper.LanguageDetectionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.ai.domain.LanguageDetection}.
 */
@Service
@Transactional
public class LanguageDetectionServiceImpl implements LanguageDetectionService {

    private static final Logger LOG = LoggerFactory.getLogger(LanguageDetectionServiceImpl.class);

    private final LanguageDetectionRepository languageDetectionRepository;

    private final LanguageDetectionMapper languageDetectionMapper;

    public LanguageDetectionServiceImpl(
        LanguageDetectionRepository languageDetectionRepository,
        LanguageDetectionMapper languageDetectionMapper
    ) {
        this.languageDetectionRepository = languageDetectionRepository;
        this.languageDetectionMapper = languageDetectionMapper;
    }

    @Override
    public LanguageDetectionDTO save(LanguageDetectionDTO languageDetectionDTO) {
        LOG.debug("Request to save LanguageDetection : {}", languageDetectionDTO);
        LanguageDetection languageDetection = languageDetectionMapper.toEntity(languageDetectionDTO);
        languageDetection = languageDetectionRepository.save(languageDetection);
        return languageDetectionMapper.toDto(languageDetection);
    }

    @Override
    public LanguageDetectionDTO update(LanguageDetectionDTO languageDetectionDTO) {
        LOG.debug("Request to update LanguageDetection : {}", languageDetectionDTO);
        LanguageDetection languageDetection = languageDetectionMapper.toEntity(languageDetectionDTO);
        languageDetection = languageDetectionRepository.save(languageDetection);
        return languageDetectionMapper.toDto(languageDetection);
    }

    @Override
    public Optional<LanguageDetectionDTO> partialUpdate(LanguageDetectionDTO languageDetectionDTO) {
        LOG.debug("Request to partially update LanguageDetection : {}", languageDetectionDTO);

        return languageDetectionRepository
            .findById(languageDetectionDTO.getId())
            .map(existingLanguageDetection -> {
                languageDetectionMapper.partialUpdate(existingLanguageDetection, languageDetectionDTO);

                return existingLanguageDetection;
            })
            .map(languageDetectionRepository::save)
            .map(languageDetectionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LanguageDetectionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all LanguageDetections");
        return languageDetectionRepository.findAll(pageable).map(languageDetectionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LanguageDetectionDTO> findOne(Long id) {
        LOG.debug("Request to get LanguageDetection : {}", id);
        return languageDetectionRepository.findById(id).map(languageDetectionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete LanguageDetection : {}", id);
        languageDetectionRepository.deleteById(id);
    }
}
