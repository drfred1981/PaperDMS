package fr.smartprod.paperdms.business.service.impl;

import fr.smartprod.paperdms.business.domain.Contract;
import fr.smartprod.paperdms.business.repository.ContractRepository;
import fr.smartprod.paperdms.business.repository.search.ContractSearchRepository;
import fr.smartprod.paperdms.business.service.ContractService;
import fr.smartprod.paperdms.business.service.dto.ContractDTO;
import fr.smartprod.paperdms.business.service.mapper.ContractMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.business.domain.Contract}.
 */
@Service
@Transactional
public class ContractServiceImpl implements ContractService {

    private static final Logger LOG = LoggerFactory.getLogger(ContractServiceImpl.class);

    private final ContractRepository contractRepository;

    private final ContractMapper contractMapper;

    private final ContractSearchRepository contractSearchRepository;

    public ContractServiceImpl(
        ContractRepository contractRepository,
        ContractMapper contractMapper,
        ContractSearchRepository contractSearchRepository
    ) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
        this.contractSearchRepository = contractSearchRepository;
    }

    @Override
    public ContractDTO save(ContractDTO contractDTO) {
        LOG.debug("Request to save Contract : {}", contractDTO);
        Contract contract = contractMapper.toEntity(contractDTO);
        contract = contractRepository.save(contract);
        contractSearchRepository.index(contract);
        return contractMapper.toDto(contract);
    }

    @Override
    public ContractDTO update(ContractDTO contractDTO) {
        LOG.debug("Request to update Contract : {}", contractDTO);
        Contract contract = contractMapper.toEntity(contractDTO);
        contract = contractRepository.save(contract);
        contractSearchRepository.index(contract);
        return contractMapper.toDto(contract);
    }

    @Override
    public Optional<ContractDTO> partialUpdate(ContractDTO contractDTO) {
        LOG.debug("Request to partially update Contract : {}", contractDTO);

        return contractRepository
            .findById(contractDTO.getId())
            .map(existingContract -> {
                contractMapper.partialUpdate(existingContract, contractDTO);

                return existingContract;
            })
            .map(contractRepository::save)
            .map(savedContract -> {
                contractSearchRepository.index(savedContract);
                return savedContract;
            })
            .map(contractMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContractDTO> findOne(Long id) {
        LOG.debug("Request to get Contract : {}", id);
        return contractRepository.findById(id).map(contractMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Contract : {}", id);
        contractRepository.deleteById(id);
        contractSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContractDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Contracts for query {}", query);
        return contractSearchRepository.search(query, pageable).map(contractMapper::toDto);
    }
}
