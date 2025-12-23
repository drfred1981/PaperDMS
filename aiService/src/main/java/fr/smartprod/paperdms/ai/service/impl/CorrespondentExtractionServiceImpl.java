package fr.smartprod.paperdms.ai.service.impl;

import fr.smartprod.paperdms.ai.domain.CorrespondentExtraction;
import fr.smartprod.paperdms.ai.repository.CorrespondentExtractionRepository;
import fr.smartprod.paperdms.ai.service.CorrespondentExtractionService;
import fr.smartprod.paperdms.ai.service.dto.CorrespondentExtractionDTO;
import fr.smartprod.paperdms.ai.service.mapper.CorrespondentExtractionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.ai.domain.CorrespondentExtraction}.
 */
@Service
@Transactional
public class CorrespondentExtractionServiceImpl implements CorrespondentExtractionService {

    private static final Logger LOG = LoggerFactory.getLogger(CorrespondentExtractionServiceImpl.class);

    private final CorrespondentExtractionRepository correspondentExtractionRepository;

    private final CorrespondentExtractionMapper correspondentExtractionMapper;

    public CorrespondentExtractionServiceImpl(
        CorrespondentExtractionRepository correspondentExtractionRepository,
        CorrespondentExtractionMapper correspondentExtractionMapper
    ) {
        this.correspondentExtractionRepository = correspondentExtractionRepository;
        this.correspondentExtractionMapper = correspondentExtractionMapper;
    }

    @Override
    public CorrespondentExtractionDTO save(CorrespondentExtractionDTO correspondentExtractionDTO) {
        LOG.debug("Request to save CorrespondentExtraction : {}", correspondentExtractionDTO);
        CorrespondentExtraction correspondentExtraction = correspondentExtractionMapper.toEntity(correspondentExtractionDTO);
        correspondentExtraction = correspondentExtractionRepository.save(correspondentExtraction);
        return correspondentExtractionMapper.toDto(correspondentExtraction);
    }

    @Override
    public CorrespondentExtractionDTO update(CorrespondentExtractionDTO correspondentExtractionDTO) {
        LOG.debug("Request to update CorrespondentExtraction : {}", correspondentExtractionDTO);
        CorrespondentExtraction correspondentExtraction = correspondentExtractionMapper.toEntity(correspondentExtractionDTO);
        correspondentExtraction = correspondentExtractionRepository.save(correspondentExtraction);
        return correspondentExtractionMapper.toDto(correspondentExtraction);
    }

    @Override
    public Optional<CorrespondentExtractionDTO> partialUpdate(CorrespondentExtractionDTO correspondentExtractionDTO) {
        LOG.debug("Request to partially update CorrespondentExtraction : {}", correspondentExtractionDTO);

        return correspondentExtractionRepository
            .findById(correspondentExtractionDTO.getId())
            .map(existingCorrespondentExtraction -> {
                correspondentExtractionMapper.partialUpdate(existingCorrespondentExtraction, correspondentExtractionDTO);

                return existingCorrespondentExtraction;
            })
            .map(correspondentExtractionRepository::save)
            .map(correspondentExtractionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CorrespondentExtractionDTO> findOne(Long id) {
        LOG.debug("Request to get CorrespondentExtraction : {}", id);
        return correspondentExtractionRepository.findById(id).map(correspondentExtractionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CorrespondentExtraction : {}", id);
        correspondentExtractionRepository.deleteById(id);
    }
}
