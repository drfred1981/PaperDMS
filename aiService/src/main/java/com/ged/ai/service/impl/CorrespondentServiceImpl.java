package com.ged.ai.service.impl;

import com.ged.ai.domain.Correspondent;
import com.ged.ai.repository.CorrespondentRepository;
import com.ged.ai.repository.search.CorrespondentSearchRepository;
import com.ged.ai.service.CorrespondentService;
import com.ged.ai.service.dto.CorrespondentDTO;
import com.ged.ai.service.mapper.CorrespondentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ged.ai.domain.Correspondent}.
 */
@Service
@Transactional
public class CorrespondentServiceImpl implements CorrespondentService {

    private static final Logger LOG = LoggerFactory.getLogger(CorrespondentServiceImpl.class);

    private final CorrespondentRepository correspondentRepository;

    private final CorrespondentMapper correspondentMapper;

    private final CorrespondentSearchRepository correspondentSearchRepository;

    public CorrespondentServiceImpl(
        CorrespondentRepository correspondentRepository,
        CorrespondentMapper correspondentMapper,
        CorrespondentSearchRepository correspondentSearchRepository
    ) {
        this.correspondentRepository = correspondentRepository;
        this.correspondentMapper = correspondentMapper;
        this.correspondentSearchRepository = correspondentSearchRepository;
    }

    @Override
    public CorrespondentDTO save(CorrespondentDTO correspondentDTO) {
        LOG.debug("Request to save Correspondent : {}", correspondentDTO);
        Correspondent correspondent = correspondentMapper.toEntity(correspondentDTO);
        correspondent = correspondentRepository.save(correspondent);
        correspondentSearchRepository.index(correspondent);
        return correspondentMapper.toDto(correspondent);
    }

    @Override
    public CorrespondentDTO update(CorrespondentDTO correspondentDTO) {
        LOG.debug("Request to update Correspondent : {}", correspondentDTO);
        Correspondent correspondent = correspondentMapper.toEntity(correspondentDTO);
        correspondent = correspondentRepository.save(correspondent);
        correspondentSearchRepository.index(correspondent);
        return correspondentMapper.toDto(correspondent);
    }

    @Override
    public Optional<CorrespondentDTO> partialUpdate(CorrespondentDTO correspondentDTO) {
        LOG.debug("Request to partially update Correspondent : {}", correspondentDTO);

        return correspondentRepository
            .findById(correspondentDTO.getId())
            .map(existingCorrespondent -> {
                correspondentMapper.partialUpdate(existingCorrespondent, correspondentDTO);

                return existingCorrespondent;
            })
            .map(correspondentRepository::save)
            .map(savedCorrespondent -> {
                correspondentSearchRepository.index(savedCorrespondent);
                return savedCorrespondent;
            })
            .map(correspondentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CorrespondentDTO> findOne(Long id) {
        LOG.debug("Request to get Correspondent : {}", id);
        return correspondentRepository.findById(id).map(correspondentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Correspondent : {}", id);
        correspondentRepository.deleteById(id);
        correspondentSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CorrespondentDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Correspondents for query {}", query);
        return correspondentSearchRepository.search(query, pageable).map(correspondentMapper::toDto);
    }
}
