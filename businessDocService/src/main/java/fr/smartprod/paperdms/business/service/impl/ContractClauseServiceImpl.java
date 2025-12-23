package fr.smartprod.paperdms.business.service.impl;

import fr.smartprod.paperdms.business.domain.ContractClause;
import fr.smartprod.paperdms.business.repository.ContractClauseRepository;
import fr.smartprod.paperdms.business.service.ContractClauseService;
import fr.smartprod.paperdms.business.service.dto.ContractClauseDTO;
import fr.smartprod.paperdms.business.service.mapper.ContractClauseMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.business.domain.ContractClause}.
 */
@Service
@Transactional
public class ContractClauseServiceImpl implements ContractClauseService {

    private static final Logger LOG = LoggerFactory.getLogger(ContractClauseServiceImpl.class);

    private final ContractClauseRepository contractClauseRepository;

    private final ContractClauseMapper contractClauseMapper;

    public ContractClauseServiceImpl(ContractClauseRepository contractClauseRepository, ContractClauseMapper contractClauseMapper) {
        this.contractClauseRepository = contractClauseRepository;
        this.contractClauseMapper = contractClauseMapper;
    }

    @Override
    public ContractClauseDTO save(ContractClauseDTO contractClauseDTO) {
        LOG.debug("Request to save ContractClause : {}", contractClauseDTO);
        ContractClause contractClause = contractClauseMapper.toEntity(contractClauseDTO);
        contractClause = contractClauseRepository.save(contractClause);
        return contractClauseMapper.toDto(contractClause);
    }

    @Override
    public ContractClauseDTO update(ContractClauseDTO contractClauseDTO) {
        LOG.debug("Request to update ContractClause : {}", contractClauseDTO);
        ContractClause contractClause = contractClauseMapper.toEntity(contractClauseDTO);
        contractClause = contractClauseRepository.save(contractClause);
        return contractClauseMapper.toDto(contractClause);
    }

    @Override
    public Optional<ContractClauseDTO> partialUpdate(ContractClauseDTO contractClauseDTO) {
        LOG.debug("Request to partially update ContractClause : {}", contractClauseDTO);

        return contractClauseRepository
            .findById(contractClauseDTO.getId())
            .map(existingContractClause -> {
                contractClauseMapper.partialUpdate(existingContractClause, contractClauseDTO);

                return existingContractClause;
            })
            .map(contractClauseRepository::save)
            .map(contractClauseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContractClauseDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ContractClauses");
        return contractClauseRepository.findAll(pageable).map(contractClauseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContractClauseDTO> findOne(Long id) {
        LOG.debug("Request to get ContractClause : {}", id);
        return contractClauseRepository.findById(id).map(contractClauseMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ContractClause : {}", id);
        contractClauseRepository.deleteById(id);
    }
}
