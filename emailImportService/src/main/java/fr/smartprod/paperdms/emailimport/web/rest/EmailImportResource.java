package fr.smartprod.paperdms.emailimport.web.rest;

import fr.smartprod.paperdms.emailimport.repository.EmailImportRepository;
import fr.smartprod.paperdms.emailimport.service.EmailImportQueryService;
import fr.smartprod.paperdms.emailimport.service.EmailImportService;
import fr.smartprod.paperdms.emailimport.service.criteria.EmailImportCriteria;
import fr.smartprod.paperdms.emailimport.service.dto.EmailImportDTO;
import fr.smartprod.paperdms.emailimport.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.emailimport.domain.EmailImport}.
 */
@RestController
@RequestMapping("/api/email-imports")
public class EmailImportResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmailImportResource.class);

    private static final String ENTITY_NAME = "emailImportServiceEmailImport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmailImportService emailImportService;

    private final EmailImportRepository emailImportRepository;

    private final EmailImportQueryService emailImportQueryService;

    public EmailImportResource(
        EmailImportService emailImportService,
        EmailImportRepository emailImportRepository,
        EmailImportQueryService emailImportQueryService
    ) {
        this.emailImportService = emailImportService;
        this.emailImportRepository = emailImportRepository;
        this.emailImportQueryService = emailImportQueryService;
    }

    /**
     * {@code POST  /email-imports} : Create a new emailImport.
     *
     * @param emailImportDTO the emailImportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emailImportDTO, or with status {@code 400 (Bad Request)} if the emailImport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmailImportDTO> createEmailImport(@Valid @RequestBody EmailImportDTO emailImportDTO) throws URISyntaxException {
        LOG.debug("REST request to save EmailImport : {}", emailImportDTO);
        if (emailImportDTO.getId() != null) {
            throw new BadRequestAlertException("A new emailImport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        emailImportDTO = emailImportService.save(emailImportDTO);
        return ResponseEntity.created(new URI("/api/email-imports/" + emailImportDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, emailImportDTO.getId().toString()))
            .body(emailImportDTO);
    }

    /**
     * {@code PUT  /email-imports/:id} : Updates an existing emailImport.
     *
     * @param id the id of the emailImportDTO to save.
     * @param emailImportDTO the emailImportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailImportDTO,
     * or with status {@code 400 (Bad Request)} if the emailImportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emailImportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmailImportDTO> updateEmailImport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmailImportDTO emailImportDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EmailImport : {}, {}", id, emailImportDTO);
        if (emailImportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailImportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailImportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        emailImportDTO = emailImportService.update(emailImportDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emailImportDTO.getId().toString()))
            .body(emailImportDTO);
    }

    /**
     * {@code PATCH  /email-imports/:id} : Partial updates given fields of an existing emailImport, field will ignore if it is null
     *
     * @param id the id of the emailImportDTO to save.
     * @param emailImportDTO the emailImportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailImportDTO,
     * or with status {@code 400 (Bad Request)} if the emailImportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the emailImportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the emailImportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmailImportDTO> partialUpdateEmailImport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmailImportDTO emailImportDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EmailImport partially : {}, {}", id, emailImportDTO);
        if (emailImportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailImportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailImportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmailImportDTO> result = emailImportService.partialUpdate(emailImportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emailImportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /email-imports} : get all the emailImports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emailImports in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmailImportDTO>> getAllEmailImports(
        EmailImportCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EmailImports by criteria: {}", criteria);

        Page<EmailImportDTO> page = emailImportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /email-imports/count} : count all the emailImports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEmailImports(EmailImportCriteria criteria) {
        LOG.debug("REST request to count EmailImports by criteria: {}", criteria);
        return ResponseEntity.ok().body(emailImportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /email-imports/:id} : get the "id" emailImport.
     *
     * @param id the id of the emailImportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emailImportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmailImportDTO> getEmailImport(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EmailImport : {}", id);
        Optional<EmailImportDTO> emailImportDTO = emailImportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emailImportDTO);
    }

    /**
     * {@code DELETE  /email-imports/:id} : delete the "id" emailImport.
     *
     * @param id the id of the emailImportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmailImport(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EmailImport : {}", id);
        emailImportService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
