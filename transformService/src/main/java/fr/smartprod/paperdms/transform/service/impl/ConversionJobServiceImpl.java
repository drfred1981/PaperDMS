package fr.smartprod.paperdms.transform.service.impl;

import fr.smartprod.paperdms.transform.domain.ConversionJob;
import fr.smartprod.paperdms.transform.repository.ConversionJobRepository;
import fr.smartprod.paperdms.transform.service.ConversionJobService;
import fr.smartprod.paperdms.transform.service.dto.ConversionJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.ConversionJobMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.transform.domain.ConversionJob}.
 */
@Service
@Transactional
public class ConversionJobServiceImpl implements ConversionJobService {

    private static final Logger LOG = LoggerFactory.getLogger(ConversionJobServiceImpl.class);

    private final ConversionJobRepository conversionJobRepository;

    private final ConversionJobMapper conversionJobMapper;

    public ConversionJobServiceImpl(ConversionJobRepository conversionJobRepository, ConversionJobMapper conversionJobMapper) {
        this.conversionJobRepository = conversionJobRepository;
        this.conversionJobMapper = conversionJobMapper;
    }

    @Override
    public ConversionJobDTO save(ConversionJobDTO conversionJobDTO) {
        LOG.debug("Request to save ConversionJob : {}", conversionJobDTO);
        ConversionJob conversionJob = conversionJobMapper.toEntity(conversionJobDTO);
        conversionJob = conversionJobRepository.save(conversionJob);
        return conversionJobMapper.toDto(conversionJob);
    }

    @Override
    public ConversionJobDTO update(ConversionJobDTO conversionJobDTO) {
        LOG.debug("Request to update ConversionJob : {}", conversionJobDTO);
        ConversionJob conversionJob = conversionJobMapper.toEntity(conversionJobDTO);
        conversionJob = conversionJobRepository.save(conversionJob);
        return conversionJobMapper.toDto(conversionJob);
    }

    @Override
    public Optional<ConversionJobDTO> partialUpdate(ConversionJobDTO conversionJobDTO) {
        LOG.debug("Request to partially update ConversionJob : {}", conversionJobDTO);

        return conversionJobRepository
            .findById(conversionJobDTO.getId())
            .map(existingConversionJob -> {
                conversionJobMapper.partialUpdate(existingConversionJob, conversionJobDTO);

                return existingConversionJob;
            })
            .map(conversionJobRepository::save)
            .map(conversionJobMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConversionJobDTO> findOne(Long id) {
        LOG.debug("Request to get ConversionJob : {}", id);
        return conversionJobRepository.findById(id).map(conversionJobMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ConversionJob : {}", id);
        conversionJobRepository.deleteById(id);
    }
}
