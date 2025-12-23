package fr.smartprod.paperdms.emailimport.service.impl;

import fr.smartprod.paperdms.emailimport.domain.ImportRule;
import fr.smartprod.paperdms.emailimport.repository.ImportRuleRepository;
import fr.smartprod.paperdms.emailimport.service.ImportRuleService;
import fr.smartprod.paperdms.emailimport.service.dto.ImportRuleDTO;
import fr.smartprod.paperdms.emailimport.service.mapper.ImportRuleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.emailimport.domain.ImportRule}.
 */
@Service
@Transactional
public class ImportRuleServiceImpl implements ImportRuleService {

    private static final Logger LOG = LoggerFactory.getLogger(ImportRuleServiceImpl.class);

    private final ImportRuleRepository importRuleRepository;

    private final ImportRuleMapper importRuleMapper;

    public ImportRuleServiceImpl(ImportRuleRepository importRuleRepository, ImportRuleMapper importRuleMapper) {
        this.importRuleRepository = importRuleRepository;
        this.importRuleMapper = importRuleMapper;
    }

    @Override
    public ImportRuleDTO save(ImportRuleDTO importRuleDTO) {
        LOG.debug("Request to save ImportRule : {}", importRuleDTO);
        ImportRule importRule = importRuleMapper.toEntity(importRuleDTO);
        importRule = importRuleRepository.save(importRule);
        return importRuleMapper.toDto(importRule);
    }

    @Override
    public ImportRuleDTO update(ImportRuleDTO importRuleDTO) {
        LOG.debug("Request to update ImportRule : {}", importRuleDTO);
        ImportRule importRule = importRuleMapper.toEntity(importRuleDTO);
        importRule = importRuleRepository.save(importRule);
        return importRuleMapper.toDto(importRule);
    }

    @Override
    public Optional<ImportRuleDTO> partialUpdate(ImportRuleDTO importRuleDTO) {
        LOG.debug("Request to partially update ImportRule : {}", importRuleDTO);

        return importRuleRepository
            .findById(importRuleDTO.getId())
            .map(existingImportRule -> {
                importRuleMapper.partialUpdate(existingImportRule, importRuleDTO);

                return existingImportRule;
            })
            .map(importRuleRepository::save)
            .map(importRuleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ImportRuleDTO> findOne(Long id) {
        LOG.debug("Request to get ImportRule : {}", id);
        return importRuleRepository.findById(id).map(importRuleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ImportRule : {}", id);
        importRuleRepository.deleteById(id);
    }
}
