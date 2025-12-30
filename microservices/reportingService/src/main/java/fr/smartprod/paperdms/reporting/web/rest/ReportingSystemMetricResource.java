package fr.smartprod.paperdms.reporting.web.rest;

import fr.smartprod.paperdms.reporting.repository.ReportingSystemMetricRepository;
import fr.smartprod.paperdms.reporting.service.ReportingSystemMetricQueryService;
import fr.smartprod.paperdms.reporting.service.ReportingSystemMetricService;
import fr.smartprod.paperdms.reporting.service.criteria.ReportingSystemMetricCriteria;
import fr.smartprod.paperdms.reporting.service.dto.ReportingSystemMetricDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.reporting.domain.ReportingSystemMetric}.
 */
@RestController
@RequestMapping("/api/reporting-system-metrics")
public class ReportingSystemMetricResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingSystemMetricResource.class);

    private static final String ENTITY_NAME = "reportingServiceReportingSystemMetric";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportingSystemMetricService reportingSystemMetricService;

    private final ReportingSystemMetricRepository reportingSystemMetricRepository;

    private final ReportingSystemMetricQueryService reportingSystemMetricQueryService;

    public ReportingSystemMetricResource(
        ReportingSystemMetricService reportingSystemMetricService,
        ReportingSystemMetricRepository reportingSystemMetricRepository,
        ReportingSystemMetricQueryService reportingSystemMetricQueryService
    ) {
        this.reportingSystemMetricService = reportingSystemMetricService;
        this.reportingSystemMetricRepository = reportingSystemMetricRepository;
        this.reportingSystemMetricQueryService = reportingSystemMetricQueryService;
    }

    /**
     * {@code POST  /reporting-system-metrics} : Create a new reportingSystemMetric.
     *
     * @param reportingSystemMetricDTO the reportingSystemMetricDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportingSystemMetricDTO, or with status {@code 400 (Bad Request)} if the reportingSystemMetric has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReportingSystemMetricDTO> createReportingSystemMetric(
        @Valid @RequestBody ReportingSystemMetricDTO reportingSystemMetricDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ReportingSystemMetric : {}", reportingSystemMetricDTO);
        if (reportingSystemMetricDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportingSystemMetric cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reportingSystemMetricDTO = reportingSystemMetricService.save(reportingSystemMetricDTO);
        return ResponseEntity.created(new URI("/api/reporting-system-metrics/" + reportingSystemMetricDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reportingSystemMetricDTO.getId().toString()))
            .body(reportingSystemMetricDTO);
    }

    /**
     * {@code PUT  /reporting-system-metrics/:id} : Updates an existing reportingSystemMetric.
     *
     * @param id the id of the reportingSystemMetricDTO to save.
     * @param reportingSystemMetricDTO the reportingSystemMetricDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportingSystemMetricDTO,
     * or with status {@code 400 (Bad Request)} if the reportingSystemMetricDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportingSystemMetricDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReportingSystemMetricDTO> updateReportingSystemMetric(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReportingSystemMetricDTO reportingSystemMetricDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReportingSystemMetric : {}, {}", id, reportingSystemMetricDTO);
        if (reportingSystemMetricDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportingSystemMetricDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportingSystemMetricRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reportingSystemMetricDTO = reportingSystemMetricService.update(reportingSystemMetricDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportingSystemMetricDTO.getId().toString()))
            .body(reportingSystemMetricDTO);
    }

    /**
     * {@code PATCH  /reporting-system-metrics/:id} : Partial updates given fields of an existing reportingSystemMetric, field will ignore if it is null
     *
     * @param id the id of the reportingSystemMetricDTO to save.
     * @param reportingSystemMetricDTO the reportingSystemMetricDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportingSystemMetricDTO,
     * or with status {@code 400 (Bad Request)} if the reportingSystemMetricDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportingSystemMetricDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportingSystemMetricDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportingSystemMetricDTO> partialUpdateReportingSystemMetric(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReportingSystemMetricDTO reportingSystemMetricDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReportingSystemMetric partially : {}, {}", id, reportingSystemMetricDTO);
        if (reportingSystemMetricDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportingSystemMetricDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportingSystemMetricRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportingSystemMetricDTO> result = reportingSystemMetricService.partialUpdate(reportingSystemMetricDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportingSystemMetricDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /reporting-system-metrics} : get all the reportingSystemMetrics.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportingSystemMetrics in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReportingSystemMetricDTO>> getAllReportingSystemMetrics(
        ReportingSystemMetricCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ReportingSystemMetrics by criteria: {}", criteria);

        Page<ReportingSystemMetricDTO> page = reportingSystemMetricQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reporting-system-metrics/count} : count all the reportingSystemMetrics.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReportingSystemMetrics(ReportingSystemMetricCriteria criteria) {
        LOG.debug("REST request to count ReportingSystemMetrics by criteria: {}", criteria);
        return ResponseEntity.ok().body(reportingSystemMetricQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /reporting-system-metrics/:id} : get the "id" reportingSystemMetric.
     *
     * @param id the id of the reportingSystemMetricDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportingSystemMetricDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReportingSystemMetricDTO> getReportingSystemMetric(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReportingSystemMetric : {}", id);
        Optional<ReportingSystemMetricDTO> reportingSystemMetricDTO = reportingSystemMetricService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportingSystemMetricDTO);
    }

    /**
     * {@code DELETE  /reporting-system-metrics/:id} : delete the "id" reportingSystemMetric.
     *
     * @param id the id of the reportingSystemMetricDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReportingSystemMetric(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReportingSystemMetric : {}", id);
        reportingSystemMetricService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
