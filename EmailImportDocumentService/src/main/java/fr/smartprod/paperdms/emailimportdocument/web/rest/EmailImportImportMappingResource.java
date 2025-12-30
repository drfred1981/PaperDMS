package fr.smartprod.paperdms.emailimportdocument.web.rest;

import fr.smartprod.paperdms.emailimportdocument.repository.EmailImportImportMappingRepository;
import fr.smartprod.paperdms.emailimportdocument.service.EmailImportImportMappingQueryService;
import fr.smartprod.paperdms.emailimportdocument.service.EmailImportImportMappingService;
import fr.smartprod.paperdms.emailimportdocument.service.criteria.EmailImportImportMappingCriteria;
import fr.smartprod.paperdms.emailimportdocument.service.dto.EmailImportImportMappingDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportMapping}.
 */
@RestController
@RequestMapping("/api/email-import-import-mappings")
public class EmailImportImportMappingResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmailImportImportMappingResource.class);

    private static final String ENTITY_NAME = "emailImportDocumentServiceEmailImportImportMapping";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmailImportImportMappingService emailImportImportMappingService;

    private final EmailImportImportMappingRepository emailImportImportMappingRepository;

    private final EmailImportImportMappingQueryService emailImportImportMappingQueryService;

    public EmailImportImportMappingResource(
        EmailImportImportMappingService emailImportImportMappingService,
        EmailImportImportMappingRepository emailImportImportMappingRepository,
        EmailImportImportMappingQueryService emailImportImportMappingQueryService
    ) {
        this.emailImportImportMappingService = emailImportImportMappingService;
        this.emailImportImportMappingRepository = emailImportImportMappingRepository;
        this.emailImportImportMappingQueryService = emailImportImportMappingQueryService;
    }

    /**
     * {@code POST  /email-import-import-mappings} : Create a new emailImportImportMapping.
     *
     * @param emailImportImportMappingDTO the emailImportImportMappingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emailImportImportMappingDTO, or with status {@code 400 (Bad Request)} if the emailImportImportMapping has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmailImportImportMappingDTO> createEmailImportImportMapping(
        @Valid @RequestBody EmailImportImportMappingDTO emailImportImportMappingDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save EmailImportImportMapping : {}", emailImportImportMappingDTO);
        if (emailImportImportMappingDTO.getId() != null) {
            throw new BadRequestAlertException("A new emailImportImportMapping cannot already have an ID", ENTITY_NAME, "idexists");
        }
        emailImportImportMappingDTO = emailImportImportMappingService.save(emailImportImportMappingDTO);
        return ResponseEntity.created(new URI("/api/email-import-import-mappings/" + emailImportImportMappingDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, emailImportImportMappingDTO.getId().toString())
            )
            .body(emailImportImportMappingDTO);
    }

    /**
     * {@code PUT  /email-import-import-mappings/:id} : Updates an existing emailImportImportMapping.
     *
     * @param id the id of the emailImportImportMappingDTO to save.
     * @param emailImportImportMappingDTO the emailImportImportMappingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailImportImportMappingDTO,
     * or with status {@code 400 (Bad Request)} if the emailImportImportMappingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emailImportImportMappingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmailImportImportMappingDTO> updateEmailImportImportMapping(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmailImportImportMappingDTO emailImportImportMappingDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EmailImportImportMapping : {}, {}", id, emailImportImportMappingDTO);
        if (emailImportImportMappingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailImportImportMappingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailImportImportMappingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        emailImportImportMappingDTO = emailImportImportMappingService.update(emailImportImportMappingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emailImportImportMappingDTO.getId().toString()))
            .body(emailImportImportMappingDTO);
    }

    /**
     * {@code PATCH  /email-import-import-mappings/:id} : Partial updates given fields of an existing emailImportImportMapping, field will ignore if it is null
     *
     * @param id the id of the emailImportImportMappingDTO to save.
     * @param emailImportImportMappingDTO the emailImportImportMappingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailImportImportMappingDTO,
     * or with status {@code 400 (Bad Request)} if the emailImportImportMappingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the emailImportImportMappingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the emailImportImportMappingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmailImportImportMappingDTO> partialUpdateEmailImportImportMapping(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmailImportImportMappingDTO emailImportImportMappingDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EmailImportImportMapping partially : {}, {}", id, emailImportImportMappingDTO);
        if (emailImportImportMappingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailImportImportMappingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailImportImportMappingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmailImportImportMappingDTO> result = emailImportImportMappingService.partialUpdate(emailImportImportMappingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emailImportImportMappingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /email-import-import-mappings} : get all the emailImportImportMappings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emailImportImportMappings in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmailImportImportMappingDTO>> getAllEmailImportImportMappings(
        EmailImportImportMappingCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EmailImportImportMappings by criteria: {}", criteria);

        Page<EmailImportImportMappingDTO> page = emailImportImportMappingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /email-import-import-mappings/count} : count all the emailImportImportMappings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEmailImportImportMappings(EmailImportImportMappingCriteria criteria) {
        LOG.debug("REST request to count EmailImportImportMappings by criteria: {}", criteria);
        return ResponseEntity.ok().body(emailImportImportMappingQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /email-import-import-mappings/:id} : get the "id" emailImportImportMapping.
     *
     * @param id the id of the emailImportImportMappingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emailImportImportMappingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmailImportImportMappingDTO> getEmailImportImportMapping(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EmailImportImportMapping : {}", id);
        Optional<EmailImportImportMappingDTO> emailImportImportMappingDTO = emailImportImportMappingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emailImportImportMappingDTO);
    }

    /**
     * {@code DELETE  /email-import-import-mappings/:id} : delete the "id" emailImportImportMapping.
     *
     * @param id the id of the emailImportImportMappingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmailImportImportMapping(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EmailImportImportMapping : {}", id);
        emailImportImportMappingService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
