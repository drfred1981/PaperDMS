package fr.smartprod.paperdms.reporting.web.rest;

import fr.smartprod.paperdms.reporting.repository.ReportingDashboardWidgetRepository;
import fr.smartprod.paperdms.reporting.service.ReportingDashboardWidgetQueryService;
import fr.smartprod.paperdms.reporting.service.ReportingDashboardWidgetService;
import fr.smartprod.paperdms.reporting.service.criteria.ReportingDashboardWidgetCriteria;
import fr.smartprod.paperdms.reporting.service.dto.ReportingDashboardWidgetDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.reporting.domain.ReportingDashboardWidget}.
 */
@RestController
@RequestMapping("/api/reporting-dashboard-widgets")
public class ReportingDashboardWidgetResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingDashboardWidgetResource.class);

    private static final String ENTITY_NAME = "reportingServiceReportingDashboardWidget";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportingDashboardWidgetService reportingDashboardWidgetService;

    private final ReportingDashboardWidgetRepository reportingDashboardWidgetRepository;

    private final ReportingDashboardWidgetQueryService reportingDashboardWidgetQueryService;

    public ReportingDashboardWidgetResource(
        ReportingDashboardWidgetService reportingDashboardWidgetService,
        ReportingDashboardWidgetRepository reportingDashboardWidgetRepository,
        ReportingDashboardWidgetQueryService reportingDashboardWidgetQueryService
    ) {
        this.reportingDashboardWidgetService = reportingDashboardWidgetService;
        this.reportingDashboardWidgetRepository = reportingDashboardWidgetRepository;
        this.reportingDashboardWidgetQueryService = reportingDashboardWidgetQueryService;
    }

    /**
     * {@code POST  /reporting-dashboard-widgets} : Create a new reportingDashboardWidget.
     *
     * @param reportingDashboardWidgetDTO the reportingDashboardWidgetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportingDashboardWidgetDTO, or with status {@code 400 (Bad Request)} if the reportingDashboardWidget has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReportingDashboardWidgetDTO> createReportingDashboardWidget(
        @Valid @RequestBody ReportingDashboardWidgetDTO reportingDashboardWidgetDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ReportingDashboardWidget : {}", reportingDashboardWidgetDTO);
        if (reportingDashboardWidgetDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportingDashboardWidget cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reportingDashboardWidgetDTO = reportingDashboardWidgetService.save(reportingDashboardWidgetDTO);
        return ResponseEntity.created(new URI("/api/reporting-dashboard-widgets/" + reportingDashboardWidgetDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reportingDashboardWidgetDTO.getId().toString())
            )
            .body(reportingDashboardWidgetDTO);
    }

    /**
     * {@code PUT  /reporting-dashboard-widgets/:id} : Updates an existing reportingDashboardWidget.
     *
     * @param id the id of the reportingDashboardWidgetDTO to save.
     * @param reportingDashboardWidgetDTO the reportingDashboardWidgetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportingDashboardWidgetDTO,
     * or with status {@code 400 (Bad Request)} if the reportingDashboardWidgetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportingDashboardWidgetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReportingDashboardWidgetDTO> updateReportingDashboardWidget(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReportingDashboardWidgetDTO reportingDashboardWidgetDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReportingDashboardWidget : {}, {}", id, reportingDashboardWidgetDTO);
        if (reportingDashboardWidgetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportingDashboardWidgetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportingDashboardWidgetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reportingDashboardWidgetDTO = reportingDashboardWidgetService.update(reportingDashboardWidgetDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportingDashboardWidgetDTO.getId().toString()))
            .body(reportingDashboardWidgetDTO);
    }

    /**
     * {@code PATCH  /reporting-dashboard-widgets/:id} : Partial updates given fields of an existing reportingDashboardWidget, field will ignore if it is null
     *
     * @param id the id of the reportingDashboardWidgetDTO to save.
     * @param reportingDashboardWidgetDTO the reportingDashboardWidgetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportingDashboardWidgetDTO,
     * or with status {@code 400 (Bad Request)} if the reportingDashboardWidgetDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportingDashboardWidgetDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportingDashboardWidgetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportingDashboardWidgetDTO> partialUpdateReportingDashboardWidget(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReportingDashboardWidgetDTO reportingDashboardWidgetDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReportingDashboardWidget partially : {}, {}", id, reportingDashboardWidgetDTO);
        if (reportingDashboardWidgetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportingDashboardWidgetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportingDashboardWidgetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportingDashboardWidgetDTO> result = reportingDashboardWidgetService.partialUpdate(reportingDashboardWidgetDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportingDashboardWidgetDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /reporting-dashboard-widgets} : get all the reportingDashboardWidgets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportingDashboardWidgets in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReportingDashboardWidgetDTO>> getAllReportingDashboardWidgets(
        ReportingDashboardWidgetCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ReportingDashboardWidgets by criteria: {}", criteria);

        Page<ReportingDashboardWidgetDTO> page = reportingDashboardWidgetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reporting-dashboard-widgets/count} : count all the reportingDashboardWidgets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReportingDashboardWidgets(ReportingDashboardWidgetCriteria criteria) {
        LOG.debug("REST request to count ReportingDashboardWidgets by criteria: {}", criteria);
        return ResponseEntity.ok().body(reportingDashboardWidgetQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /reporting-dashboard-widgets/:id} : get the "id" reportingDashboardWidget.
     *
     * @param id the id of the reportingDashboardWidgetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportingDashboardWidgetDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReportingDashboardWidgetDTO> getReportingDashboardWidget(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReportingDashboardWidget : {}", id);
        Optional<ReportingDashboardWidgetDTO> reportingDashboardWidgetDTO = reportingDashboardWidgetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportingDashboardWidgetDTO);
    }

    /**
     * {@code DELETE  /reporting-dashboard-widgets/:id} : delete the "id" reportingDashboardWidget.
     *
     * @param id the id of the reportingDashboardWidgetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReportingDashboardWidget(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReportingDashboardWidget : {}", id);
        reportingDashboardWidgetService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
