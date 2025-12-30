package fr.smartprod.paperdms.emailimportdocument.web.rest;

import fr.smartprod.paperdms.emailimportdocument.repository.EmailImportImportRuleRepository;
import fr.smartprod.paperdms.emailimportdocument.service.EmailImportImportRuleQueryService;
import fr.smartprod.paperdms.emailimportdocument.service.EmailImportImportRuleService;
import fr.smartprod.paperdms.emailimportdocument.service.criteria.EmailImportImportRuleCriteria;
import fr.smartprod.paperdms.emailimportdocument.service.dto.EmailImportImportRuleDTO;
import fr.smartprod.paperdms.emailimportdocument.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportRule}.
 */
@RestController
@RequestMapping("/api/email-import-import-rules")
public class EmailImportImportRuleResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmailImportImportRuleResource.class);

    private static final String ENTITY_NAME = "emailImportDocumentServiceEmailImportImportRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmailImportImportRuleService emailImportImportRuleService;

    private final EmailImportImportRuleRepository emailImportImportRuleRepository;

    private final EmailImportImportRuleQueryService emailImportImportRuleQueryService;

    public EmailImportImportRuleResource(
        EmailImportImportRuleService emailImportImportRuleService,
        EmailImportImportRuleRepository emailImportImportRuleRepository,
        EmailImportImportRuleQueryService emailImportImportRuleQueryService
    ) {
        this.emailImportImportRuleService = emailImportImportRuleService;
        this.emailImportImportRuleRepository = emailImportImportRuleRepository;
        this.emailImportImportRuleQueryService = emailImportImportRuleQueryService;
    }

    /**
     * {@code POST  /email-import-import-rules} : Create a new emailImportImportRule.
     *
     * @param emailImportImportRuleDTO the emailImportImportRuleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emailImportImportRuleDTO, or with status {@code 400 (Bad Request)} if the emailImportImportRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmailImportImportRuleDTO> createEmailImportImportRule(
        @Valid @RequestBody EmailImportImportRuleDTO emailImportImportRuleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save EmailImportImportRule : {}", emailImportImportRuleDTO);
        if (emailImportImportRuleDTO.getId() != null) {
            throw new BadRequestAlertException("A new emailImportImportRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        emailImportImportRuleDTO = emailImportImportRuleService.save(emailImportImportRuleDTO);
        return ResponseEntity.created(new URI("/api/email-import-import-rules/" + emailImportImportRuleDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, emailImportImportRuleDTO.getId().toString()))
            .body(emailImportImportRuleDTO);
    }

    /**
     * {@code PUT  /email-import-import-rules/:id} : Updates an existing emailImportImportRule.
     *
     * @param id the id of the emailImportImportRuleDTO to save.
     * @param emailImportImportRuleDTO the emailImportImportRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailImportImportRuleDTO,
     * or with status {@code 400 (Bad Request)} if the emailImportImportRuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emailImportImportRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmailImportImportRuleDTO> updateEmailImportImportRule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmailImportImportRuleDTO emailImportImportRuleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EmailImportImportRule : {}, {}", id, emailImportImportRuleDTO);
        if (emailImportImportRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailImportImportRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailImportImportRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        emailImportImportRuleDTO = emailImportImportRuleService.update(emailImportImportRuleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emailImportImportRuleDTO.getId().toString()))
            .body(emailImportImportRuleDTO);
    }

    /**
     * {@code PATCH  /email-import-import-rules/:id} : Partial updates given fields of an existing emailImportImportRule, field will ignore if it is null
     *
     * @param id the id of the emailImportImportRuleDTO to save.
     * @param emailImportImportRuleDTO the emailImportImportRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailImportImportRuleDTO,
     * or with status {@code 400 (Bad Request)} if the emailImportImportRuleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the emailImportImportRuleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the emailImportImportRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmailImportImportRuleDTO> partialUpdateEmailImportImportRule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmailImportImportRuleDTO emailImportImportRuleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EmailImportImportRule partially : {}, {}", id, emailImportImportRuleDTO);
        if (emailImportImportRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailImportImportRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailImportImportRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmailImportImportRuleDTO> result = emailImportImportRuleService.partialUpdate(emailImportImportRuleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emailImportImportRuleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /email-import-import-rules} : get all the emailImportImportRules.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emailImportImportRules in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmailImportImportRuleDTO>> getAllEmailImportImportRules(
        EmailImportImportRuleCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EmailImportImportRules by criteria: {}", criteria);

        Page<EmailImportImportRuleDTO> page = emailImportImportRuleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /email-import-import-rules/count} : count all the emailImportImportRules.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEmailImportImportRules(EmailImportImportRuleCriteria criteria) {
        LOG.debug("REST request to count EmailImportImportRules by criteria: {}", criteria);
        return ResponseEntity.ok().body(emailImportImportRuleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /email-import-import-rules/:id} : get the "id" emailImportImportRule.
     *
     * @param id the id of the emailImportImportRuleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emailImportImportRuleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmailImportImportRuleDTO> getEmailImportImportRule(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EmailImportImportRule : {}", id);
        Optional<EmailImportImportRuleDTO> emailImportImportRuleDTO = emailImportImportRuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emailImportImportRuleDTO);
    }

    /**
     * {@code DELETE  /email-import-import-rules/:id} : delete the "id" emailImportImportRule.
     *
     * @param id the id of the emailImportImportRuleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmailImportImportRule(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EmailImportImportRule : {}", id);
        emailImportImportRuleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
