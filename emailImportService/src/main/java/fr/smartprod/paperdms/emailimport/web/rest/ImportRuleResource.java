package fr.smartprod.paperdms.emailimport.web.rest;

import fr.smartprod.paperdms.emailimport.repository.ImportRuleRepository;
import fr.smartprod.paperdms.emailimport.service.ImportRuleQueryService;
import fr.smartprod.paperdms.emailimport.service.ImportRuleService;
import fr.smartprod.paperdms.emailimport.service.criteria.ImportRuleCriteria;
import fr.smartprod.paperdms.emailimport.service.dto.ImportRuleDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.emailimport.domain.ImportRule}.
 */
@RestController
@RequestMapping("/api/import-rules")
public class ImportRuleResource {

    private static final Logger LOG = LoggerFactory.getLogger(ImportRuleResource.class);

    private static final String ENTITY_NAME = "emailImportServiceImportRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImportRuleService importRuleService;

    private final ImportRuleRepository importRuleRepository;

    private final ImportRuleQueryService importRuleQueryService;

    public ImportRuleResource(
        ImportRuleService importRuleService,
        ImportRuleRepository importRuleRepository,
        ImportRuleQueryService importRuleQueryService
    ) {
        this.importRuleService = importRuleService;
        this.importRuleRepository = importRuleRepository;
        this.importRuleQueryService = importRuleQueryService;
    }

    /**
     * {@code POST  /import-rules} : Create a new importRule.
     *
     * @param importRuleDTO the importRuleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new importRuleDTO, or with status {@code 400 (Bad Request)} if the importRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ImportRuleDTO> createImportRule(@Valid @RequestBody ImportRuleDTO importRuleDTO) throws URISyntaxException {
        LOG.debug("REST request to save ImportRule : {}", importRuleDTO);
        if (importRuleDTO.getId() != null) {
            throw new BadRequestAlertException("A new importRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        importRuleDTO = importRuleService.save(importRuleDTO);
        return ResponseEntity.created(new URI("/api/import-rules/" + importRuleDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, importRuleDTO.getId().toString()))
            .body(importRuleDTO);
    }

    /**
     * {@code PUT  /import-rules/:id} : Updates an existing importRule.
     *
     * @param id the id of the importRuleDTO to save.
     * @param importRuleDTO the importRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated importRuleDTO,
     * or with status {@code 400 (Bad Request)} if the importRuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the importRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ImportRuleDTO> updateImportRule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ImportRuleDTO importRuleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ImportRule : {}, {}", id, importRuleDTO);
        if (importRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, importRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!importRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        importRuleDTO = importRuleService.update(importRuleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, importRuleDTO.getId().toString()))
            .body(importRuleDTO);
    }

    /**
     * {@code PATCH  /import-rules/:id} : Partial updates given fields of an existing importRule, field will ignore if it is null
     *
     * @param id the id of the importRuleDTO to save.
     * @param importRuleDTO the importRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated importRuleDTO,
     * or with status {@code 400 (Bad Request)} if the importRuleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the importRuleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the importRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ImportRuleDTO> partialUpdateImportRule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ImportRuleDTO importRuleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ImportRule partially : {}, {}", id, importRuleDTO);
        if (importRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, importRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!importRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ImportRuleDTO> result = importRuleService.partialUpdate(importRuleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, importRuleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /import-rules} : get all the importRules.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of importRules in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ImportRuleDTO>> getAllImportRules(
        ImportRuleCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ImportRules by criteria: {}", criteria);

        Page<ImportRuleDTO> page = importRuleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /import-rules/count} : count all the importRules.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countImportRules(ImportRuleCriteria criteria) {
        LOG.debug("REST request to count ImportRules by criteria: {}", criteria);
        return ResponseEntity.ok().body(importRuleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /import-rules/:id} : get the "id" importRule.
     *
     * @param id the id of the importRuleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the importRuleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ImportRuleDTO> getImportRule(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ImportRule : {}", id);
        Optional<ImportRuleDTO> importRuleDTO = importRuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(importRuleDTO);
    }

    /**
     * {@code DELETE  /import-rules/:id} : delete the "id" importRule.
     *
     * @param id the id of the importRuleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImportRule(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ImportRule : {}", id);
        importRuleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
