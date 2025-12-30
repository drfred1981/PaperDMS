package fr.smartprod.paperdms.emailimportdocument.service;

import fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportRule;
import fr.smartprod.paperdms.emailimportdocument.repository.EmailImportImportRuleRepository;
import fr.smartprod.paperdms.emailimportdocument.service.dto.EmailImportImportRuleDTO;
import fr.smartprod.paperdms.emailimportdocument.service.mapper.EmailImportImportRuleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportRule}.
 */
@Service
@Transactional
public class EmailImportImportRuleService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailImportImportRuleService.class);

    private final EmailImportImportRuleRepository emailImportImportRuleRepository;

    private final EmailImportImportRuleMapper emailImportImportRuleMapper;

    public EmailImportImportRuleService(
        EmailImportImportRuleRepository emailImportImportRuleRepository,
        EmailImportImportRuleMapper emailImportImportRuleMapper
    ) {
        this.emailImportImportRuleRepository = emailImportImportRuleRepository;
        this.emailImportImportRuleMapper = emailImportImportRuleMapper;
    }

    /**
     * Save a emailImportImportRule.
     *
     * @param emailImportImportRuleDTO the entity to save.
     * @return the persisted entity.
     */
    public EmailImportImportRuleDTO save(EmailImportImportRuleDTO emailImportImportRuleDTO) {
        LOG.debug("Request to save EmailImportImportRule : {}", emailImportImportRuleDTO);
        EmailImportImportRule emailImportImportRule = emailImportImportRuleMapper.toEntity(emailImportImportRuleDTO);
        emailImportImportRule = emailImportImportRuleRepository.save(emailImportImportRule);
        return emailImportImportRuleMapper.toDto(emailImportImportRule);
    }

    /**
     * Update a emailImportImportRule.
     *
     * @param emailImportImportRuleDTO the entity to save.
     * @return the persisted entity.
     */
    public EmailImportImportRuleDTO update(EmailImportImportRuleDTO emailImportImportRuleDTO) {
        LOG.debug("Request to update EmailImportImportRule : {}", emailImportImportRuleDTO);
        EmailImportImportRule emailImportImportRule = emailImportImportRuleMapper.toEntity(emailImportImportRuleDTO);
        emailImportImportRule = emailImportImportRuleRepository.save(emailImportImportRule);
        return emailImportImportRuleMapper.toDto(emailImportImportRule);
    }

    /**
     * Partially update a emailImportImportRule.
     *
     * @param emailImportImportRuleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmailImportImportRuleDTO> partialUpdate(EmailImportImportRuleDTO emailImportImportRuleDTO) {
        LOG.debug("Request to partially update EmailImportImportRule : {}", emailImportImportRuleDTO);

        return emailImportImportRuleRepository
            .findById(emailImportImportRuleDTO.getId())
            .map(existingEmailImportImportRule -> {
                emailImportImportRuleMapper.partialUpdate(existingEmailImportImportRule, emailImportImportRuleDTO);

                return existingEmailImportImportRule;
            })
            .map(emailImportImportRuleRepository::save)
            .map(emailImportImportRuleMapper::toDto);
    }

    /**
     * Get one emailImportImportRule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmailImportImportRuleDTO> findOne(Long id) {
        LOG.debug("Request to get EmailImportImportRule : {}", id);
        return emailImportImportRuleRepository.findById(id).map(emailImportImportRuleMapper::toDto);
    }

    /**
     * Delete the emailImportImportRule by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EmailImportImportRule : {}", id);
        emailImportImportRuleRepository.deleteById(id);
    }
}
