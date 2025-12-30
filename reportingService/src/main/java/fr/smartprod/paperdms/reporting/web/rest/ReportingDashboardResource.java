package fr.smartprod.paperdms.reporting.web.rest;

import fr.smartprod.paperdms.reporting.repository.ReportingDashboardRepository;
import fr.smartprod.paperdms.reporting.service.ReportingDashboardQueryService;
import fr.smartprod.paperdms.reporting.service.ReportingDashboardService;
import fr.smartprod.paperdms.reporting.service.criteria.ReportingDashboardCriteria;
import fr.smartprod.paperdms.reporting.service.dto.ReportingDashboardDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.reporting.domain.ReportingDashboard}.
 */
@RestController
@RequestMapping("/api/reporting-dashboards")
public class ReportingDashboardResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingDashboardResource.class);

    private static final String ENTITY_NAME = "reportingServiceReportingDashboard";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportingDashboardService reportingDashboardService;

    private final ReportingDashboardRepository reportingDashboardRepository;

    private final ReportingDashboardQueryService reportingDashboardQueryService;

    public ReportingDashboardResource(
        ReportingDashboardService reportingDashboardService,
        ReportingDashboardRepository reportingDashboardRepository,
        ReportingDashboardQueryService reportingDashboardQueryService
    ) {
        this.reportingDashboardService = reportingDashboardService;
        this.reportingDashboardRepository = reportingDashboardRepository;
        this.reportingDashboardQueryService = reportingDashboardQueryService;
    }

    /**
     * {@code POST  /reporting-dashboards} : Create a new reportingDashboard.
     *
     * @param reportingDashboardDTO the reportingDashboardDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportingDashboardDTO, or with status {@code 400 (Bad Request)} if the reportingDashboard has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReportingDashboardDTO> createReportingDashboard(@Valid @RequestBody ReportingDashboardDTO reportingDashboardDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ReportingDashboard : {}", reportingDashboardDTO);
        if (reportingDashboardDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportingDashboard cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reportingDashboardDTO = reportingDashboardService.save(reportingDashboardDTO);
        return ResponseEntity.created(new URI("/api/reporting-dashboards/" + reportingDashboardDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reportingDashboardDTO.getId().toString()))
            .body(reportingDashboardDTO);
    }

    /**
     * {@code PUT  /reporting-dashboards/:id} : Updates an existing reportingDashboard.
     *
     * @param id the id of the reportingDashboardDTO to save.
     * @param reportingDashboardDTO the reportingDashboardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportingDashboardDTO,
     * or with status {@code 400 (Bad Request)} if the reportingDashboardDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportingDashboardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReportingDashboardDTO> updateReportingDashboard(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReportingDashboardDTO reportingDashboardDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReportingDashboard : {}, {}", id, reportingDashboardDTO);
        if (reportingDashboardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportingDashboardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportingDashboardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reportingDashboardDTO = reportingDashboardService.update(reportingDashboardDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportingDashboardDTO.getId().toString()))
            .body(reportingDashboardDTO);
    }

    /**
     * {@code PATCH  /reporting-dashboards/:id} : Partial updates given fields of an existing reportingDashboard, field will ignore if it is null
     *
     * @param id the id of the reportingDashboardDTO to save.
     * @param reportingDashboardDTO the reportingDashboardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportingDashboardDTO,
     * or with status {@code 400 (Bad Request)} if the reportingDashboardDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportingDashboardDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportingDashboardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportingDashboardDTO> partialUpdateReportingDashboard(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReportingDashboardDTO reportingDashboardDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReportingDashboard partially : {}, {}", id, reportingDashboardDTO);
        if (reportingDashboardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportingDashboardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportingDashboardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportingDashboardDTO> result = reportingDashboardService.partialUpdate(reportingDashboardDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportingDashboardDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /reporting-dashboards} : get all the reportingDashboards.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportingDashboards in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReportingDashboardDTO>> getAllReportingDashboards(
        ReportingDashboardCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ReportingDashboards by criteria: {}", criteria);

        Page<ReportingDashboardDTO> page = reportingDashboardQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reporting-dashboards/count} : count all the reportingDashboards.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReportingDashboards(ReportingDashboardCriteria criteria) {
        LOG.debug("REST request to count ReportingDashboards by criteria: {}", criteria);
        return ResponseEntity.ok().body(reportingDashboardQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /reporting-dashboards/:id} : get the "id" reportingDashboard.
     *
     * @param id the id of the reportingDashboardDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportingDashboardDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReportingDashboardDTO> getReportingDashboard(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReportingDashboard : {}", id);
        Optional<ReportingDashboardDTO> reportingDashboardDTO = reportingDashboardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportingDashboardDTO);
    }

    /**
     * {@code DELETE  /reporting-dashboards/:id} : delete the "id" reportingDashboard.
     *
     * @param id the id of the reportingDashboardDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReportingDashboard(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReportingDashboard : {}", id);
        reportingDashboardService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
