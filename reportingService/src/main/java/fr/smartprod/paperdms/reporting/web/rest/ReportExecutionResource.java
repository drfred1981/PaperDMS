package fr.smartprod.paperdms.reporting.web.rest;

import fr.smartprod.paperdms.reporting.repository.ReportExecutionRepository;
import fr.smartprod.paperdms.reporting.service.ReportExecutionService;
import fr.smartprod.paperdms.reporting.service.dto.ReportExecutionDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.reporting.domain.ReportExecution}.
 */
@RestController
@RequestMapping("/api/report-executions")
public class ReportExecutionResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReportExecutionResource.class);

    private static final String ENTITY_NAME = "reportingServiceReportExecution";

    @Value("${jhipster.clientApp.name:reportingService}")
    private String applicationName;

    private final ReportExecutionService reportExecutionService;

    private final ReportExecutionRepository reportExecutionRepository;

    public ReportExecutionResource(ReportExecutionService reportExecutionService, ReportExecutionRepository reportExecutionRepository) {
        this.reportExecutionService = reportExecutionService;
        this.reportExecutionRepository = reportExecutionRepository;
    }

    /**
     * {@code POST  /report-executions} : Create a new reportExecution.
     *
     * @param reportExecutionDTO the reportExecutionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportExecutionDTO, or with status {@code 400 (Bad Request)} if the reportExecution has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReportExecutionDTO> createReportExecution(@Valid @RequestBody ReportExecutionDTO reportExecutionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ReportExecution : {}", reportExecutionDTO);
        if (reportExecutionDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportExecution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reportExecutionDTO = reportExecutionService.save(reportExecutionDTO);
        return ResponseEntity.created(new URI("/api/report-executions/" + reportExecutionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reportExecutionDTO.getId().toString()))
            .body(reportExecutionDTO);
    }

    /**
     * {@code PUT  /report-executions/:id} : Updates an existing reportExecution.
     *
     * @param id the id of the reportExecutionDTO to save.
     * @param reportExecutionDTO the reportExecutionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportExecutionDTO,
     * or with status {@code 400 (Bad Request)} if the reportExecutionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportExecutionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReportExecutionDTO> updateReportExecution(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReportExecutionDTO reportExecutionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReportExecution : {}, {}", id, reportExecutionDTO);
        if (reportExecutionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportExecutionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportExecutionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reportExecutionDTO = reportExecutionService.update(reportExecutionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportExecutionDTO.getId().toString()))
            .body(reportExecutionDTO);
    }

    /**
     * {@code PATCH  /report-executions/:id} : Partial updates given fields of an existing reportExecution, field will ignore if it is null
     *
     * @param id the id of the reportExecutionDTO to save.
     * @param reportExecutionDTO the reportExecutionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportExecutionDTO,
     * or with status {@code 400 (Bad Request)} if the reportExecutionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportExecutionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportExecutionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportExecutionDTO> partialUpdateReportExecution(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReportExecutionDTO reportExecutionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReportExecution partially : {}, {}", id, reportExecutionDTO);
        if (reportExecutionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportExecutionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportExecutionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportExecutionDTO> result = reportExecutionService.partialUpdate(reportExecutionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportExecutionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /report-executions} : get all the reportExecutions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportExecutions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReportExecutionDTO>> getAllReportExecutions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of ReportExecutions");
        Page<ReportExecutionDTO> page = reportExecutionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /report-executions/:id} : get the "id" reportExecution.
     *
     * @param id the id of the reportExecutionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportExecutionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReportExecutionDTO> getReportExecution(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReportExecution : {}", id);
        Optional<ReportExecutionDTO> reportExecutionDTO = reportExecutionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportExecutionDTO);
    }

    /**
     * {@code DELETE  /report-executions/:id} : delete the "id" reportExecution.
     *
     * @param id the id of the reportExecutionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReportExecution(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReportExecution : {}", id);
        reportExecutionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
