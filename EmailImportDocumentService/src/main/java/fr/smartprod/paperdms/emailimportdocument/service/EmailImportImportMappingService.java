package fr.smartprod.paperdms.emailimportdocument.service;

import fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportMapping;
import fr.smartprod.paperdms.emailimportdocument.repository.EmailImportImportMappingRepository;
import fr.smartprod.paperdms.emailimportdocument.service.dto.EmailImportImportMappingDTO;
import fr.smartprod.paperdms.emailimportdocument.service.mapper.EmailImportImportMappingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportMapping}.
 */
@Service
@Transactional
public class EmailImportImportMappingService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailImportImportMappingService.class);

    private final EmailImportImportMappingRepository emailImportImportMappingRepository;

    private final EmailImportImportMappingMapper emailImportImportMappingMapper;

    public EmailImportImportMappingService(
        EmailImportImportMappingRepository emailImportImportMappingRepository,
        EmailImportImportMappingMapper emailImportImportMappingMapper
    ) {
        this.emailImportImportMappingRepository = emailImportImportMappingRepository;
        this.emailImportImportMappingMapper = emailImportImportMappingMapper;
    }

    /**
     * Save a emailImportImportMapping.
     *
     * @param emailImportImportMappingDTO the entity to save.
     * @return the persisted entity.
     */
    public EmailImportImportMappingDTO save(EmailImportImportMappingDTO emailImportImportMappingDTO) {
        LOG.debug("Request to save EmailImportImportMapping : {}", emailImportImportMappingDTO);
        EmailImportImportMapping emailImportImportMapping = emailImportImportMappingMapper.toEntity(emailImportImportMappingDTO);
        emailImportImportMapping = emailImportImportMappingRepository.save(emailImportImportMapping);
        return emailImportImportMappingMapper.toDto(emailImportImportMapping);
    }

    /**
     * Update a emailImportImportMapping.
     *
     * @param emailImportImportMappingDTO the entity to save.
     * @return the persisted entity.
     */
    public EmailImportImportMappingDTO update(EmailImportImportMappingDTO emailImportImportMappingDTO) {
        LOG.debug("Request to update EmailImportImportMapping : {}", emailImportImportMappingDTO);
        EmailImportImportMapping emailImportImportMapping = emailImportImportMappingMapper.toEntity(emailImportImportMappingDTO);
        emailImportImportMapping = emailImportImportMappingRepository.save(emailImportImportMapping);
        return emailImportImportMappingMapper.toDto(emailImportImportMapping);
    }

    /**
     * Partially update a emailImportImportMapping.
     *
     * @param emailImportImportMappingDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmailImportImportMappingDTO> partialUpdate(EmailImportImportMappingDTO emailImportImportMappingDTO) {
        LOG.debug("Request to partially update EmailImportImportMapping : {}", emailImportImportMappingDTO);

        return emailImportImportMappingRepository
            .findById(emailImportImportMappingDTO.getId())
            .map(existingEmailImportImportMapping -> {
                emailImportImportMappingMapper.partialUpdate(existingEmailImportImportMapping, emailImportImportMappingDTO);

                return existingEmailImportImportMapping;
            })
            .map(emailImportImportMappingRepository::save)
            .map(emailImportImportMappingMapper::toDto);
    }

    /**
     * Get one emailImportImportMapping by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmailImportImportMappingDTO> findOne(Long id) {
        LOG.debug("Request to get EmailImportImportMapping : {}", id);
        return emailImportImportMappingRepository.findById(id).map(emailImportImportMappingMapper::toDto);
    }

    /**
     * Delete the emailImportImportMapping by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EmailImportImportMapping : {}", id);
        emailImportImportMappingRepository.deleteById(id);
    }
}
