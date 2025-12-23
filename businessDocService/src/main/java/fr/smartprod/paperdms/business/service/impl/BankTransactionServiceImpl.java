package fr.smartprod.paperdms.business.service.impl;

import fr.smartprod.paperdms.business.domain.BankTransaction;
import fr.smartprod.paperdms.business.repository.BankTransactionRepository;
import fr.smartprod.paperdms.business.service.BankTransactionService;
import fr.smartprod.paperdms.business.service.dto.BankTransactionDTO;
import fr.smartprod.paperdms.business.service.mapper.BankTransactionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.business.domain.BankTransaction}.
 */
@Service
@Transactional
public class BankTransactionServiceImpl implements BankTransactionService {

    private static final Logger LOG = LoggerFactory.getLogger(BankTransactionServiceImpl.class);

    private final BankTransactionRepository bankTransactionRepository;

    private final BankTransactionMapper bankTransactionMapper;

    public BankTransactionServiceImpl(BankTransactionRepository bankTransactionRepository, BankTransactionMapper bankTransactionMapper) {
        this.bankTransactionRepository = bankTransactionRepository;
        this.bankTransactionMapper = bankTransactionMapper;
    }

    @Override
    public BankTransactionDTO save(BankTransactionDTO bankTransactionDTO) {
        LOG.debug("Request to save BankTransaction : {}", bankTransactionDTO);
        BankTransaction bankTransaction = bankTransactionMapper.toEntity(bankTransactionDTO);
        bankTransaction = bankTransactionRepository.save(bankTransaction);
        return bankTransactionMapper.toDto(bankTransaction);
    }

    @Override
    public BankTransactionDTO update(BankTransactionDTO bankTransactionDTO) {
        LOG.debug("Request to update BankTransaction : {}", bankTransactionDTO);
        BankTransaction bankTransaction = bankTransactionMapper.toEntity(bankTransactionDTO);
        bankTransaction = bankTransactionRepository.save(bankTransaction);
        return bankTransactionMapper.toDto(bankTransaction);
    }

    @Override
    public Optional<BankTransactionDTO> partialUpdate(BankTransactionDTO bankTransactionDTO) {
        LOG.debug("Request to partially update BankTransaction : {}", bankTransactionDTO);

        return bankTransactionRepository
            .findById(bankTransactionDTO.getId())
            .map(existingBankTransaction -> {
                bankTransactionMapper.partialUpdate(existingBankTransaction, bankTransactionDTO);

                return existingBankTransaction;
            })
            .map(bankTransactionRepository::save)
            .map(bankTransactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BankTransactionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all BankTransactions");
        return bankTransactionRepository.findAll(pageable).map(bankTransactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BankTransactionDTO> findOne(Long id) {
        LOG.debug("Request to get BankTransaction : {}", id);
        return bankTransactionRepository.findById(id).map(bankTransactionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete BankTransaction : {}", id);
        bankTransactionRepository.deleteById(id);
    }
}
