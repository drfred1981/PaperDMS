package com.ged.similarity.service.impl;

import com.ged.similarity.domain.SimilarityCluster;
import com.ged.similarity.repository.SimilarityClusterRepository;
import com.ged.similarity.service.SimilarityClusterService;
import com.ged.similarity.service.dto.SimilarityClusterDTO;
import com.ged.similarity.service.mapper.SimilarityClusterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ged.similarity.domain.SimilarityCluster}.
 */
@Service
@Transactional
public class SimilarityClusterServiceImpl implements SimilarityClusterService {

    private static final Logger LOG = LoggerFactory.getLogger(SimilarityClusterServiceImpl.class);

    private final SimilarityClusterRepository similarityClusterRepository;

    private final SimilarityClusterMapper similarityClusterMapper;

    public SimilarityClusterServiceImpl(
        SimilarityClusterRepository similarityClusterRepository,
        SimilarityClusterMapper similarityClusterMapper
    ) {
        this.similarityClusterRepository = similarityClusterRepository;
        this.similarityClusterMapper = similarityClusterMapper;
    }

    @Override
    public SimilarityClusterDTO save(SimilarityClusterDTO similarityClusterDTO) {
        LOG.debug("Request to save SimilarityCluster : {}", similarityClusterDTO);
        SimilarityCluster similarityCluster = similarityClusterMapper.toEntity(similarityClusterDTO);
        similarityCluster = similarityClusterRepository.save(similarityCluster);
        return similarityClusterMapper.toDto(similarityCluster);
    }

    @Override
    public SimilarityClusterDTO update(SimilarityClusterDTO similarityClusterDTO) {
        LOG.debug("Request to update SimilarityCluster : {}", similarityClusterDTO);
        SimilarityCluster similarityCluster = similarityClusterMapper.toEntity(similarityClusterDTO);
        similarityCluster = similarityClusterRepository.save(similarityCluster);
        return similarityClusterMapper.toDto(similarityCluster);
    }

    @Override
    public Optional<SimilarityClusterDTO> partialUpdate(SimilarityClusterDTO similarityClusterDTO) {
        LOG.debug("Request to partially update SimilarityCluster : {}", similarityClusterDTO);

        return similarityClusterRepository
            .findById(similarityClusterDTO.getId())
            .map(existingSimilarityCluster -> {
                similarityClusterMapper.partialUpdate(existingSimilarityCluster, similarityClusterDTO);

                return existingSimilarityCluster;
            })
            .map(similarityClusterRepository::save)
            .map(similarityClusterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SimilarityClusterDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SimilarityClusters");
        return similarityClusterRepository.findAll(pageable).map(similarityClusterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SimilarityClusterDTO> findOne(Long id) {
        LOG.debug("Request to get SimilarityCluster : {}", id);
        return similarityClusterRepository.findById(id).map(similarityClusterMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SimilarityCluster : {}", id);
        similarityClusterRepository.deleteById(id);
    }
}
