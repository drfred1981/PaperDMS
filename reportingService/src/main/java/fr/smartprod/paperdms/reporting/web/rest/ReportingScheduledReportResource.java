package fr.smartprod.paperdms.reporting.web.rest;

import fr.smartprod.paperdms.reporting.repository.ReportingScheduledReportRepository;
import fr.smartprod.paperdms.reporting.service.ReportingScheduledReportQueryService;
import fr.smartprod.paperdms.reporting.service.ReportingScheduledReportService;
import fr.smartprod.paperdms.reporting.service.criteria.ReportingScheduledReportCriteria;
import fr.smartprod.paperdms.reporting.service.dto.ReportingScheduledReportDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.reporting.domain.ReportingScheduledReport}.
 */
@RestController
@RequestMapping("/api/reporting-scheduled-reports")
public class ReportingScheduledReportResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingScheduledReportResource.class);

    private static final String ENTITY_NAME = "reportingServiceReportingScheduledReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportingScheduledReportService reportingScheduledReportService;

    private final ReportingScheduledReportRepository reportingScheduledReportRepository;

    private final ReportingScheduledReportQueryService reportingScheduledReportQueryService;

    public ReportingScheduledReportResource(
        ReportingScheduledReportService reportingScheduledReportService,
        ReportingScheduledReportRepository reportingScheduledReportRepository,
        ReportingScheduledReportQueryService reportingScheduledReportQueryService
    ) {
        this.reportingScheduledReportService = reportingScheduledReportService;
        this.reportingScheduledReportRepository = reportingScheduledReportRepository;
        this.reportingScheduledReportQueryService = reportingScheduledReportQueryService;
    }

    /**
     * {@code POST  /reporting-scheduled-reports} : Create a new reportingScheduledReport.
     *
     * @param reportingScheduledReportDTO the reportingScheduledReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportingScheduledReportDTO, or with status {@code 400 (Bad Request)} if the reportingScheduledReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReportingScheduledReportDTO> createReportingScheduledReport(
        @Valid @RequestBody ReportingScheduledReportDTO reportingScheduledReportDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ReportingScheduledReport : {}", reportingScheduledReportDTO);
        if (reportingScheduledReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportingScheduledReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reportingScheduledReportDTO = reportingScheduledReportService.save(reportingScheduledReportDTO);
        return ResponseEntity.created(new URI("/api/reporting-scheduled-reports/" + reportingScheduledReportDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reportingScheduledReportDTO.getId().toString())
            )
            .body(reportingScheduledReportDTO);
    }

    /**
     * {@code PUT  /reporting-scheduled-reports/:id} : Updates an existing reportingScheduledReport.
     *
     * @param id the id of the reportingScheduledReportDTO to save.
     * @param reportingScheduledReportDTO the reportingScheduledReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportingScheduledReportDTO,
     * or with status {@code 400 (Bad Request)} if the reportingScheduledReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportingScheduledReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReportingScheduledReportDTO> updateReportingScheduledReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReportingScheduledReportDTO reportingScheduledReportDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReportingScheduledReport : {}, {}", id, reportingScheduledReportDTO);
        if (reportingScheduledReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportingScheduledReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportingScheduledReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reportingScheduledReportDTO = reportingScheduledReportService.update(reportingScheduledReportDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportingScheduledReportDTO.getId().toString()))
            .body(reportingScheduledReportDTO);
    }

    /**
     * {@code PATCH  /reporting-scheduled-reports/:id} : Partial updates given fields of an existing reportingScheduledReport, field will ignore if it is null
     *
     * @param id the id of the reportingScheduledReportDTO to save.
     * @param reportingScheduledReportDTO the reportingScheduledReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportingScheduledReportDTO,
     * or with status {@code 400 (Bad Request)} if the reportingScheduledReportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportingScheduledReportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportingScheduledReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportingScheduledReportDTO> partialUpdateReportingScheduledReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReportingScheduledReportDTO reportingScheduledReportDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReportingScheduledReport partially : {}, {}", id, reportingScheduledReportDTO);
        if (reportingScheduledReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportingScheduledReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportingScheduledReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportingScheduledReportDTO> result = reportingScheduledReportService.partialUpdate(reportingScheduledReportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportingScheduledReportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /reporting-scheduled-reports} : get all the reportingScheduledReports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportingScheduledReports in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReportingScheduledReportDTO>> getAllReportingScheduledReports(
        ReportingScheduledReportCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ReportingScheduledReports by criteria: {}", criteria);

        Page<ReportingScheduledReportDTO> page = reportingScheduledReportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reporting-scheduled-reports/count} : count all the reportingScheduledReports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReportingScheduledReports(ReportingScheduledReportCriteria criteria) {
        LOG.debug("REST request to count ReportingScheduledReports by criteria: {}", criteria);
        return ResponseEntity.ok().body(reportingScheduledReportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /reporting-scheduled-reports/:id} : get the "id" reportingScheduledReport.
     *
     * @param id the id of the reportingScheduledReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportingScheduledReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReportingScheduledReportDTO> getReportingScheduledReport(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReportingScheduledReport : {}", id);
        Optional<ReportingScheduledReportDTO> reportingScheduledReportDTO = reportingScheduledReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportingScheduledReportDTO);
    }

    /**
     * {@code DELETE  /reporting-scheduled-reports/:id} : delete the "id" reportingScheduledReport.
     *
     * @param id the id of the reportingScheduledReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReportingScheduledReport(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReportingScheduledReport : {}", id);
        reportingScheduledReportService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
