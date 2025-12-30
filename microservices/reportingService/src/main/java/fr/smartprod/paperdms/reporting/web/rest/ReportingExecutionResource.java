package fr.smartprod.paperdms.reporting.web.rest;

import fr.smartprod.paperdms.reporting.repository.ReportingExecutionRepository;
import fr.smartprod.paperdms.reporting.service.ReportingExecutionQueryService;
import fr.smartprod.paperdms.reporting.service.ReportingExecutionService;
import fr.smartprod.paperdms.reporting.service.criteria.ReportingExecutionCriteria;
import fr.smartprod.paperdms.reporting.service.dto.ReportingExecutionDTO;
import fr.smartprod.paperdms.reporting.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.reporting.domain.ReportingExecution}.
 */
@RestController
@RequestMapping("/api/reporting-executions")
public class ReportingExecutionResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingExecutionResource.class);

    private static final String ENTITY_NAME = "reportingServiceReportingExecution";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportingExecutionService reportingExecutionService;

    private final ReportingExecutionRepository reportingExecutionRepository;

    private final ReportingExecutionQueryService reportingExecutionQueryService;

    public ReportingExecutionResource(
        ReportingExecutionService reportingExecutionService,
        ReportingExecutionRepository reportingExecutionRepository,
        ReportingExecutionQueryService reportingExecutionQueryService
    ) {
        this.reportingExecutionService = reportingExecutionService;
        this.reportingExecutionRepository = reportingExecutionRepository;
        this.reportingExecutionQueryService = reportingExecutionQueryService;
    }

    /**
     * {@code POST  /reporting-executions} : Create a new reportingExecution.
     *
     * @param reportingExecutionDTO the reportingExecutionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportingExecutionDTO, or with status {@code 400 (Bad Request)} if the reportingExecution has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReportingExecutionDTO> createReportingExecution(@Valid @RequestBody ReportingExecutionDTO reportingExecutionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ReportingExecution : {}", reportingExecutionDTO);
        if (reportingExecutionDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportingExecution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reportingExecutionDTO = reportingExecutionService.save(reportingExecutionDTO);
        return ResponseEntity.created(new URI("/api/reporting-executions/" + reportingExecutionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reportingExecutionDTO.getId().toString()))
            .body(reportingExecutionDTO);
    }

    /**
     * {@code PUT  /reporting-executions/:id} : Updates an existing reportingExecution.
     *
     * @param id the id of the reportingExecutionDTO to save.
     * @param reportingExecutionDTO the reportingExecutionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportingExecutionDTO,
     * or with status {@code 400 (Bad Request)} if the reportingExecutionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportingExecutionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReportingExecutionDTO> updateReportingExecution(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReportingExecutionDTO reportingExecutionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReportingExecution : {}, {}", id, reportingExecutionDTO);
        if (reportingExecutionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportingExecutionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportingExecutionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reportingExecutionDTO = reportingExecutionService.update(reportingExecutionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportingExecutionDTO.getId().toString()))
            .body(reportingExecutionDTO);
    }

    /**
     * {@code PATCH  /reporting-executions/:id} : Partial updates given fields of an existing reportingExecution, field will ignore if it is null
     *
     * @param id the id of the reportingExecutionDTO to save.
     * @param reportingExecutionDTO the reportingExecutionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportingExecutionDTO,
     * or with status {@code 400 (Bad Request)} if the reportingExecutionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportingExecutionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportingExecutionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportingExecutionDTO> partialUpdateReportingExecution(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReportingExecutionDTO reportingExecutionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReportingExecution partially : {}, {}", id, reportingExecutionDTO);
        if (reportingExecutionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportingExecutionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportingExecutionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportingExecutionDTO> result = reportingExecutionService.partialUpdate(reportingExecutionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportingExecutionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /reporting-executions} : get all the reportingExecutions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportingExecutions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReportingExecutionDTO>> getAllReportingExecutions(
        ReportingExecutionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ReportingExecutions by criteria: {}", criteria);

        Page<ReportingExecutionDTO> page = reportingExecutionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reporting-executions/count} : count all the reportingExecutions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReportingExecutions(ReportingExecutionCriteria criteria) {
        LOG.debug("REST request to count ReportingExecutions by criteria: {}", criteria);
        return ResponseEntity.ok().body(reportingExecutionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /reporting-executions/:id} : get the "id" reportingExecution.
     *
     * @param id the id of the reportingExecutionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportingExecutionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReportingExecutionDTO> getReportingExecution(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReportingExecution : {}", id);
        Optional<ReportingExecutionDTO> reportingExecutionDTO = reportingExecutionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportingExecutionDTO);
    }

    /**
     * {@code DELETE  /reporting-executions/:id} : delete the "id" reportingExecution.
     *
     * @param id the id of the reportingExecutionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReportingExecution(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReportingExecution : {}", id);
        reportingExecutionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
