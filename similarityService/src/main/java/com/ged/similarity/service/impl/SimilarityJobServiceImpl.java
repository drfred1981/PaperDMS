package com.ged.similarity.service.impl;

import com.ged.similarity.domain.SimilarityJob;
import com.ged.similarity.repository.SimilarityJobRepository;
import com.ged.similarity.service.SimilarityJobService;
import com.ged.similarity.service.dto.SimilarityJobDTO;
import com.ged.similarity.service.mapper.SimilarityJobMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ged.similarity.domain.SimilarityJob}.
 */
@Service
@Transactional
public class SimilarityJobServiceImpl implements SimilarityJobService {

    private static final Logger LOG = LoggerFactory.getLogger(SimilarityJobServiceImpl.class);

    private final SimilarityJobRepository similarityJobRepository;

    private final SimilarityJobMapper similarityJobMapper;

    public SimilarityJobServiceImpl(SimilarityJobRepository similarityJobRepository, SimilarityJobMapper similarityJobMapper) {
        this.similarityJobRepository = similarityJobRepository;
        this.similarityJobMapper = similarityJobMapper;
    }

    @Override
    public SimilarityJobDTO save(SimilarityJobDTO similarityJobDTO) {
        LOG.debug("Request to save SimilarityJob : {}", similarityJobDTO);
        SimilarityJob similarityJob = similarityJobMapper.toEntity(similarityJobDTO);
        similarityJob = similarityJobRepository.save(similarityJob);
        return similarityJobMapper.toDto(similarityJob);
    }

    @Override
    public SimilarityJobDTO update(SimilarityJobDTO similarityJobDTO) {
        LOG.debug("Request to update SimilarityJob : {}", similarityJobDTO);
        SimilarityJob similarityJob = similarityJobMapper.toEntity(similarityJobDTO);
        similarityJob = similarityJobRepository.save(similarityJob);
        return similarityJobMapper.toDto(similarityJob);
    }

    @Override
    public Optional<SimilarityJobDTO> partialUpdate(SimilarityJobDTO similarityJobDTO) {
        LOG.debug("Request to partially update SimilarityJob : {}", similarityJobDTO);

        return similarityJobRepository
            .findById(similarityJobDTO.getId())
            .map(existingSimilarityJob -> {
                similarityJobMapper.partialUpdate(existingSimilarityJob, similarityJobDTO);

                return existingSimilarityJob;
            })
            .map(similarityJobRepository::save)
            .map(similarityJobMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SimilarityJobDTO> findOne(Long id) {
        LOG.debug("Request to get SimilarityJob : {}", id);
        return similarityJobRepository.findById(id).map(similarityJobMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SimilarityJob : {}", id);
        similarityJobRepository.deleteById(id);
    }
}
