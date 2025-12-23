package fr.smartprod.paperdms.business.service.impl;

import fr.smartprod.paperdms.business.domain.BankStatement;
import fr.smartprod.paperdms.business.repository.BankStatementRepository;
import fr.smartprod.paperdms.business.service.BankStatementService;
import fr.smartprod.paperdms.business.service.dto.BankStatementDTO;
import fr.smartprod.paperdms.business.service.mapper.BankStatementMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.business.domain.BankStatement}.
 */
@Service
@Transactional
public class BankStatementServiceImpl implements BankStatementService {

    private static final Logger LOG = LoggerFactory.getLogger(BankStatementServiceImpl.class);

    private final BankStatementRepository bankStatementRepository;

    private final BankStatementMapper bankStatementMapper;

    public BankStatementServiceImpl(BankStatementRepository bankStatementRepository, BankStatementMapper bankStatementMapper) {
        this.bankStatementRepository = bankStatementRepository;
        this.bankStatementMapper = bankStatementMapper;
    }

    @Override
    public BankStatementDTO save(BankStatementDTO bankStatementDTO) {
        LOG.debug("Request to save BankStatement : {}", bankStatementDTO);
        BankStatement bankStatement = bankStatementMapper.toEntity(bankStatementDTO);
        bankStatement = bankStatementRepository.save(bankStatement);
        return bankStatementMapper.toDto(bankStatement);
    }

    @Override
    public BankStatementDTO update(BankStatementDTO bankStatementDTO) {
        LOG.debug("Request to update BankStatement : {}", bankStatementDTO);
        BankStatement bankStatement = bankStatementMapper.toEntity(bankStatementDTO);
        bankStatement = bankStatementRepository.save(bankStatement);
        return bankStatementMapper.toDto(bankStatement);
    }

    @Override
    public Optional<BankStatementDTO> partialUpdate(BankStatementDTO bankStatementDTO) {
        LOG.debug("Request to partially update BankStatement : {}", bankStatementDTO);

        return bankStatementRepository
            .findById(bankStatementDTO.getId())
            .map(existingBankStatement -> {
                bankStatementMapper.partialUpdate(existingBankStatement, bankStatementDTO);

                return existingBankStatement;
            })
            .map(bankStatementRepository::save)
            .map(bankStatementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BankStatementDTO> findOne(Long id) {
        LOG.debug("Request to get BankStatement : {}", id);
        return bankStatementRepository.findById(id).map(bankStatementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete BankStatement : {}", id);
        bankStatementRepository.deleteById(id);
    }
}
