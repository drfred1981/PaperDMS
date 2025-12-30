package fr.smartprod.paperdms.reporting.web.rest;

import fr.smartprod.paperdms.reporting.repository.ReportingPerformanceMetricRepository;
import fr.smartprod.paperdms.reporting.service.ReportingPerformanceMetricQueryService;
import fr.smartprod.paperdms.reporting.service.ReportingPerformanceMetricService;
import fr.smartprod.paperdms.reporting.service.criteria.ReportingPerformanceMetricCriteria;
import fr.smartprod.paperdms.reporting.service.dto.ReportingPerformanceMetricDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.reporting.domain.ReportingPerformanceMetric}.
 */
@RestController
@RequestMapping("/api/reporting-performance-metrics")
public class ReportingPerformanceMetricResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingPerformanceMetricResource.class);

    private static final String ENTITY_NAME = "reportingServiceReportingPerformanceMetric";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportingPerformanceMetricService reportingPerformanceMetricService;

    private final ReportingPerformanceMetricRepository reportingPerformanceMetricRepository;

    private final ReportingPerformanceMetricQueryService reportingPerformanceMetricQueryService;

    public ReportingPerformanceMetricResource(
        ReportingPerformanceMetricService reportingPerformanceMetricService,
        ReportingPerformanceMetricRepository reportingPerformanceMetricRepository,
        ReportingPerformanceMetricQueryService reportingPerformanceMetricQueryService
    ) {
        this.reportingPerformanceMetricService = reportingPerformanceMetricService;
        this.reportingPerformanceMetricRepository = reportingPerformanceMetricRepository;
        this.reportingPerformanceMetricQueryService = reportingPerformanceMetricQueryService;
    }

    /**
     * {@code POST  /reporting-performance-metrics} : Create a new reportingPerformanceMetric.
     *
     * @param reportingPerformanceMetricDTO the reportingPerformanceMetricDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportingPerformanceMetricDTO, or with status {@code 400 (Bad Request)} if the reportingPerformanceMetric has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReportingPerformanceMetricDTO> createReportingPerformanceMetric(
        @Valid @RequestBody ReportingPerformanceMetricDTO reportingPerformanceMetricDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ReportingPerformanceMetric : {}", reportingPerformanceMetricDTO);
        if (reportingPerformanceMetricDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportingPerformanceMetric cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reportingPerformanceMetricDTO = reportingPerformanceMetricService.save(reportingPerformanceMetricDTO);
        return ResponseEntity.created(new URI("/api/reporting-performance-metrics/" + reportingPerformanceMetricDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reportingPerformanceMetricDTO.getId().toString())
            )
            .body(reportingPerformanceMetricDTO);
    }

    /**
     * {@code PUT  /reporting-performance-metrics/:id} : Updates an existing reportingPerformanceMetric.
     *
     * @param id the id of the reportingPerformanceMetricDTO to save.
     * @param reportingPerformanceMetricDTO the reportingPerformanceMetricDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportingPerformanceMetricDTO,
     * or with status {@code 400 (Bad Request)} if the reportingPerformanceMetricDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportingPerformanceMetricDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReportingPerformanceMetricDTO> updateReportingPerformanceMetric(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReportingPerformanceMetricDTO reportingPerformanceMetricDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReportingPerformanceMetric : {}, {}", id, reportingPerformanceMetricDTO);
        if (reportingPerformanceMetricDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportingPerformanceMetricDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportingPerformanceMetricRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reportingPerformanceMetricDTO = reportingPerformanceMetricService.update(reportingPerformanceMetricDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportingPerformanceMetricDTO.getId().toString())
            )
            .body(reportingPerformanceMetricDTO);
    }

    /**
     * {@code PATCH  /reporting-performance-metrics/:id} : Partial updates given fields of an existing reportingPerformanceMetric, field will ignore if it is null
     *
     * @param id the id of the reportingPerformanceMetricDTO to save.
     * @param reportingPerformanceMetricDTO the reportingPerformanceMetricDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportingPerformanceMetricDTO,
     * or with status {@code 400 (Bad Request)} if the reportingPerformanceMetricDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportingPerformanceMetricDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportingPerformanceMetricDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportingPerformanceMetricDTO> partialUpdateReportingPerformanceMetric(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReportingPerformanceMetricDTO reportingPerformanceMetricDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReportingPerformanceMetric partially : {}, {}", id, reportingPerformanceMetricDTO);
        if (reportingPerformanceMetricDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportingPerformanceMetricDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportingPerformanceMetricRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportingPerformanceMetricDTO> result = reportingPerformanceMetricService.partialUpdate(reportingPerformanceMetricDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportingPerformanceMetricDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /reporting-performance-metrics} : get all the reportingPerformanceMetrics.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportingPerformanceMetrics in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReportingPerformanceMetricDTO>> getAllReportingPerformanceMetrics(
        ReportingPerformanceMetricCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ReportingPerformanceMetrics by criteria: {}", criteria);

        Page<ReportingPerformanceMetricDTO> page = reportingPerformanceMetricQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reporting-performance-metrics/count} : count all the reportingPerformanceMetrics.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReportingPerformanceMetrics(ReportingPerformanceMetricCriteria criteria) {
        LOG.debug("REST request to count ReportingPerformanceMetrics by criteria: {}", criteria);
        return ResponseEntity.ok().body(reportingPerformanceMetricQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /reporting-performance-metrics/:id} : get the "id" reportingPerformanceMetric.
     *
     * @param id the id of the reportingPerformanceMetricDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportingPerformanceMetricDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReportingPerformanceMetricDTO> getReportingPerformanceMetric(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReportingPerformanceMetric : {}", id);
        Optional<ReportingPerformanceMetricDTO> reportingPerformanceMetricDTO = reportingPerformanceMetricService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportingPerformanceMetricDTO);
    }

    /**
     * {@code DELETE  /reporting-performance-metrics/:id} : delete the "id" reportingPerformanceMetric.
     *
     * @param id the id of the reportingPerformanceMetricDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReportingPerformanceMetric(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReportingPerformanceMetric : {}", id);
        reportingPerformanceMetricService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
