package fr.smartprod.paperdms.emailimport.service.impl;

import fr.smartprod.paperdms.emailimport.domain.EmailImport;
import fr.smartprod.paperdms.emailimport.repository.EmailImportRepository;
import fr.smartprod.paperdms.emailimport.service.EmailImportService;
import fr.smartprod.paperdms.emailimport.service.dto.EmailImportDTO;
import fr.smartprod.paperdms.emailimport.service.mapper.EmailImportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.emailimport.domain.EmailImport}.
 */
@Service
@Transactional
public class EmailImportServiceImpl implements EmailImportService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailImportServiceImpl.class);

    private final EmailImportRepository emailImportRepository;

    private final EmailImportMapper emailImportMapper;

    public EmailImportServiceImpl(EmailImportRepository emailImportRepository, EmailImportMapper emailImportMapper) {
        this.emailImportRepository = emailImportRepository;
        this.emailImportMapper = emailImportMapper;
    }

    @Override
    public EmailImportDTO save(EmailImportDTO emailImportDTO) {
        LOG.debug("Request to save EmailImport : {}", emailImportDTO);
        EmailImport emailImport = emailImportMapper.toEntity(emailImportDTO);
        emailImport = emailImportRepository.save(emailImport);
        return emailImportMapper.toDto(emailImport);
    }

    @Override
    public EmailImportDTO update(EmailImportDTO emailImportDTO) {
        LOG.debug("Request to update EmailImport : {}", emailImportDTO);
        EmailImport emailImport = emailImportMapper.toEntity(emailImportDTO);
        emailImport = emailImportRepository.save(emailImport);
        return emailImportMapper.toDto(emailImport);
    }

    @Override
    public Optional<EmailImportDTO> partialUpdate(EmailImportDTO emailImportDTO) {
        LOG.debug("Request to partially update EmailImport : {}", emailImportDTO);

        return emailImportRepository
            .findById(emailImportDTO.getId())
            .map(existingEmailImport -> {
                emailImportMapper.partialUpdate(existingEmailImport, emailImportDTO);

                return existingEmailImport;
            })
            .map(emailImportRepository::save)
            .map(emailImportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmailImportDTO> findOne(Long id) {
        LOG.debug("Request to get EmailImport : {}", id);
        return emailImportRepository.findById(id).map(emailImportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete EmailImport : {}", id);
        emailImportRepository.deleteById(id);
    }
}
