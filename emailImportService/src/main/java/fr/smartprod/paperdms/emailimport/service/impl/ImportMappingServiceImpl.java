package fr.smartprod.paperdms.emailimport.service.impl;

import fr.smartprod.paperdms.emailimport.domain.ImportMapping;
import fr.smartprod.paperdms.emailimport.repository.ImportMappingRepository;
import fr.smartprod.paperdms.emailimport.service.ImportMappingService;
import fr.smartprod.paperdms.emailimport.service.dto.ImportMappingDTO;
import fr.smartprod.paperdms.emailimport.service.mapper.ImportMappingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.emailimport.domain.ImportMapping}.
 */
@Service
@Transactional
public class ImportMappingServiceImpl implements ImportMappingService {

    private static final Logger LOG = LoggerFactory.getLogger(ImportMappingServiceImpl.class);

    private final ImportMappingRepository importMappingRepository;

    private final ImportMappingMapper importMappingMapper;

    public ImportMappingServiceImpl(ImportMappingRepository importMappingRepository, ImportMappingMapper importMappingMapper) {
        this.importMappingRepository = importMappingRepository;
        this.importMappingMapper = importMappingMapper;
    }

    @Override
    public ImportMappingDTO save(ImportMappingDTO importMappingDTO) {
        LOG.debug("Request to save ImportMapping : {}", importMappingDTO);
        ImportMapping importMapping = importMappingMapper.toEntity(importMappingDTO);
        importMapping = importMappingRepository.save(importMapping);
        return importMappingMapper.toDto(importMapping);
    }

    @Override
    public ImportMappingDTO update(ImportMappingDTO importMappingDTO) {
        LOG.debug("Request to update ImportMapping : {}", importMappingDTO);
        ImportMapping importMapping = importMappingMapper.toEntity(importMappingDTO);
        importMapping = importMappingRepository.save(importMapping);
        return importMappingMapper.toDto(importMapping);
    }

    @Override
    public Optional<ImportMappingDTO> partialUpdate(ImportMappingDTO importMappingDTO) {
        LOG.debug("Request to partially update ImportMapping : {}", importMappingDTO);

        return importMappingRepository
            .findById(importMappingDTO.getId())
            .map(existingImportMapping -> {
                importMappingMapper.partialUpdate(existingImportMapping, importMappingDTO);

                return existingImportMapping;
            })
            .map(importMappingRepository::save)
            .map(importMappingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ImportMappingDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ImportMappings");
        return importMappingRepository.findAll(pageable).map(importMappingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ImportMappingDTO> findOne(Long id) {
        LOG.debug("Request to get ImportMapping : {}", id);
        return importMappingRepository.findById(id).map(importMappingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ImportMapping : {}", id);
        importMappingRepository.deleteById(id);
    }
}
