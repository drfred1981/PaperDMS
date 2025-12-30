package fr.smartprod.paperdms.export.web.rest;

import fr.smartprod.paperdms.export.repository.ExportPatternRepository;
import fr.smartprod.paperdms.export.service.ExportPatternQueryService;
import fr.smartprod.paperdms.export.service.ExportPatternService;
import fr.smartprod.paperdms.export.service.criteria.ExportPatternCriteria;
import fr.smartprod.paperdms.export.service.dto.ExportPatternDTO;
import fr.smartprod.paperdms.export.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.export.domain.ExportPattern}.
 */
@RestController
@RequestMapping("/api/export-patterns")
public class ExportPatternResource {

    private static final Logger LOG = LoggerFactory.getLogger(ExportPatternResource.class);

    private static final String ENTITY_NAME = "exportServiceExportPattern";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExportPatternService exportPatternService;

    private final ExportPatternRepository exportPatternRepository;

    private final ExportPatternQueryService exportPatternQueryService;

    public ExportPatternResource(
        ExportPatternService exportPatternService,
        ExportPatternRepository exportPatternRepository,
        ExportPatternQueryService exportPatternQueryService
    ) {
        this.exportPatternService = exportPatternService;
        this.exportPatternRepository = exportPatternRepository;
        this.exportPatternQueryService = exportPatternQueryService;
    }

    /**
     * {@code POST  /export-patterns} : Create a new exportPattern.
     *
     * @param exportPatternDTO the exportPatternDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exportPatternDTO, or with status {@code 400 (Bad Request)} if the exportPattern has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ExportPatternDTO> createExportPattern(@Valid @RequestBody ExportPatternDTO exportPatternDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ExportPattern : {}", exportPatternDTO);
        if (exportPatternDTO.getId() != null) {
            throw new BadRequestAlertException("A new exportPattern cannot already have an ID", ENTITY_NAME, "idexists");
        }
        exportPatternDTO = exportPatternService.save(exportPatternDTO);
        return ResponseEntity.created(new URI("/api/export-patterns/" + exportPatternDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, exportPatternDTO.getId().toString()))
            .body(exportPatternDTO);
    }

    /**
     * {@code PUT  /export-patterns/:id} : Updates an existing exportPattern.
     *
     * @param id the id of the exportPatternDTO to save.
     * @param exportPatternDTO the exportPatternDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exportPatternDTO,
     * or with status {@code 400 (Bad Request)} if the exportPatternDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exportPatternDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExportPatternDTO> updateExportPattern(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExportPatternDTO exportPatternDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ExportPattern : {}, {}", id, exportPatternDTO);
        if (exportPatternDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exportPatternDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exportPatternRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        exportPatternDTO = exportPatternService.update(exportPatternDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exportPatternDTO.getId().toString()))
            .body(exportPatternDTO);
    }

    /**
     * {@code PATCH  /export-patterns/:id} : Partial updates given fields of an existing exportPattern, field will ignore if it is null
     *
     * @param id the id of the exportPatternDTO to save.
     * @param exportPatternDTO the exportPatternDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exportPatternDTO,
     * or with status {@code 400 (Bad Request)} if the exportPatternDTO is not valid,
     * or with status {@code 404 (Not Found)} if the exportPatternDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the exportPatternDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExportPatternDTO> partialUpdateExportPattern(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExportPatternDTO exportPatternDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ExportPattern partially : {}, {}", id, exportPatternDTO);
        if (exportPatternDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exportPatternDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exportPatternRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExportPatternDTO> result = exportPatternService.partialUpdate(exportPatternDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exportPatternDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /export-patterns} : get all the exportPatterns.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exportPatterns in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ExportPatternDTO>> getAllExportPatterns(
        ExportPatternCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ExportPatterns by criteria: {}", criteria);

        Page<ExportPatternDTO> page = exportPatternQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /export-patterns/count} : count all the exportPatterns.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countExportPatterns(ExportPatternCriteria criteria) {
        LOG.debug("REST request to count ExportPatterns by criteria: {}", criteria);
        return ResponseEntity.ok().body(exportPatternQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /export-patterns/:id} : get the "id" exportPattern.
     *
     * @param id the id of the exportPatternDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exportPatternDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExportPatternDTO> getExportPattern(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ExportPattern : {}", id);
        Optional<ExportPatternDTO> exportPatternDTO = exportPatternService.findOne(id);
        return ResponseUtil.wrapOrNotFound(exportPatternDTO);
    }

    /**
     * {@code DELETE  /export-patterns/:id} : delete the "id" exportPattern.
     *
     * @param id the id of the exportPatternDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExportPattern(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ExportPattern : {}", id);
        exportPatternService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
