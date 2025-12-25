package fr.smartprod.paperdms.reporting.web.rest;

import fr.smartprod.paperdms.reporting.repository.ScheduledReportRepository;
import fr.smartprod.paperdms.reporting.service.ScheduledReportQueryService;
import fr.smartprod.paperdms.reporting.service.ScheduledReportService;
import fr.smartprod.paperdms.reporting.service.criteria.ScheduledReportCriteria;
import fr.smartprod.paperdms.reporting.service.dto.ScheduledReportDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.reporting.domain.ScheduledReport}.
 */
@RestController
@RequestMapping("/api/scheduled-reports")
public class ScheduledReportResource {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduledReportResource.class);

    private static final String ENTITY_NAME = "reportingServiceScheduledReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScheduledReportService scheduledReportService;

    private final ScheduledReportRepository scheduledReportRepository;

    private final ScheduledReportQueryService scheduledReportQueryService;

    public ScheduledReportResource(
        ScheduledReportService scheduledReportService,
        ScheduledReportRepository scheduledReportRepository,
        ScheduledReportQueryService scheduledReportQueryService
    ) {
        this.scheduledReportService = scheduledReportService;
        this.scheduledReportRepository = scheduledReportRepository;
        this.scheduledReportQueryService = scheduledReportQueryService;
    }

    /**
     * {@code POST  /scheduled-reports} : Create a new scheduledReport.
     *
     * @param scheduledReportDTO the scheduledReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new scheduledReportDTO, or with status {@code 400 (Bad Request)} if the scheduledReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ScheduledReportDTO> createScheduledReport(@Valid @RequestBody ScheduledReportDTO scheduledReportDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ScheduledReport : {}", scheduledReportDTO);
        if (scheduledReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new scheduledReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        scheduledReportDTO = scheduledReportService.save(scheduledReportDTO);
        return ResponseEntity.created(new URI("/api/scheduled-reports/" + scheduledReportDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, scheduledReportDTO.getId().toString()))
            .body(scheduledReportDTO);
    }

    /**
     * {@code PUT  /scheduled-reports/:id} : Updates an existing scheduledReport.
     *
     * @param id the id of the scheduledReportDTO to save.
     * @param scheduledReportDTO the scheduledReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scheduledReportDTO,
     * or with status {@code 400 (Bad Request)} if the scheduledReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the scheduledReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ScheduledReportDTO> updateScheduledReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ScheduledReportDTO scheduledReportDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ScheduledReport : {}, {}", id, scheduledReportDTO);
        if (scheduledReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scheduledReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scheduledReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        scheduledReportDTO = scheduledReportService.update(scheduledReportDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, scheduledReportDTO.getId().toString()))
            .body(scheduledReportDTO);
    }

    /**
     * {@code PATCH  /scheduled-reports/:id} : Partial updates given fields of an existing scheduledReport, field will ignore if it is null
     *
     * @param id the id of the scheduledReportDTO to save.
     * @param scheduledReportDTO the scheduledReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scheduledReportDTO,
     * or with status {@code 400 (Bad Request)} if the scheduledReportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the scheduledReportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the scheduledReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ScheduledReportDTO> partialUpdateScheduledReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ScheduledReportDTO scheduledReportDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ScheduledReport partially : {}, {}", id, scheduledReportDTO);
        if (scheduledReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scheduledReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scheduledReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ScheduledReportDTO> result = scheduledReportService.partialUpdate(scheduledReportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, scheduledReportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /scheduled-reports} : get all the scheduledReports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of scheduledReports in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ScheduledReportDTO>> getAllScheduledReports(
        ScheduledReportCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ScheduledReports by criteria: {}", criteria);

        Page<ScheduledReportDTO> page = scheduledReportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /scheduled-reports/count} : count all the scheduledReports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countScheduledReports(ScheduledReportCriteria criteria) {
        LOG.debug("REST request to count ScheduledReports by criteria: {}", criteria);
        return ResponseEntity.ok().body(scheduledReportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /scheduled-reports/:id} : get the "id" scheduledReport.
     *
     * @param id the id of the scheduledReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the scheduledReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScheduledReportDTO> getScheduledReport(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ScheduledReport : {}", id);
        Optional<ScheduledReportDTO> scheduledReportDTO = scheduledReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(scheduledReportDTO);
    }

    /**
     * {@code DELETE  /scheduled-reports/:id} : delete the "id" scheduledReport.
     *
     * @param id the id of the scheduledReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScheduledReport(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ScheduledReport : {}", id);
        scheduledReportService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
