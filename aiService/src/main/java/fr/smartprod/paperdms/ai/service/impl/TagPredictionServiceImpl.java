package fr.smartprod.paperdms.ai.service.impl;

import fr.smartprod.paperdms.ai.domain.TagPrediction;
import fr.smartprod.paperdms.ai.repository.TagPredictionRepository;
import fr.smartprod.paperdms.ai.service.TagPredictionService;
import fr.smartprod.paperdms.ai.service.dto.TagPredictionDTO;
import fr.smartprod.paperdms.ai.service.mapper.TagPredictionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.ai.domain.TagPrediction}.
 */
@Service
@Transactional
public class TagPredictionServiceImpl implements TagPredictionService {

    private static final Logger LOG = LoggerFactory.getLogger(TagPredictionServiceImpl.class);

    private final TagPredictionRepository tagPredictionRepository;

    private final TagPredictionMapper tagPredictionMapper;

    public TagPredictionServiceImpl(TagPredictionRepository tagPredictionRepository, TagPredictionMapper tagPredictionMapper) {
        this.tagPredictionRepository = tagPredictionRepository;
        this.tagPredictionMapper = tagPredictionMapper;
    }

    @Override
    public TagPredictionDTO save(TagPredictionDTO tagPredictionDTO) {
        LOG.debug("Request to save TagPrediction : {}", tagPredictionDTO);
        TagPrediction tagPrediction = tagPredictionMapper.toEntity(tagPredictionDTO);
        tagPrediction = tagPredictionRepository.save(tagPrediction);
        return tagPredictionMapper.toDto(tagPrediction);
    }

    @Override
    public TagPredictionDTO update(TagPredictionDTO tagPredictionDTO) {
        LOG.debug("Request to update TagPrediction : {}", tagPredictionDTO);
        TagPrediction tagPrediction = tagPredictionMapper.toEntity(tagPredictionDTO);
        tagPrediction = tagPredictionRepository.save(tagPrediction);
        return tagPredictionMapper.toDto(tagPrediction);
    }

    @Override
    public Optional<TagPredictionDTO> partialUpdate(TagPredictionDTO tagPredictionDTO) {
        LOG.debug("Request to partially update TagPrediction : {}", tagPredictionDTO);

        return tagPredictionRepository
            .findById(tagPredictionDTO.getId())
            .map(existingTagPrediction -> {
                tagPredictionMapper.partialUpdate(existingTagPrediction, tagPredictionDTO);

                return existingTagPrediction;
            })
            .map(tagPredictionRepository::save)
            .map(tagPredictionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TagPredictionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all TagPredictions");
        return tagPredictionRepository.findAll(pageable).map(tagPredictionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TagPredictionDTO> findOne(Long id) {
        LOG.debug("Request to get TagPrediction : {}", id);
        return tagPredictionRepository.findById(id).map(tagPredictionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete TagPrediction : {}", id);
        tagPredictionRepository.deleteById(id);
    }
}
