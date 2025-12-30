package fr.smartprod.paperdms.monitoring.web.rest;

import fr.smartprod.paperdms.monitoring.repository.MonitoringServiceStatusRepository;
import fr.smartprod.paperdms.monitoring.service.MonitoringServiceStatusQueryService;
import fr.smartprod.paperdms.monitoring.service.MonitoringServiceStatusService;
import fr.smartprod.paperdms.monitoring.service.criteria.MonitoringServiceStatusCriteria;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringServiceStatusDTO;
import fr.smartprod.paperdms.monitoring.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.monitoring.domain.MonitoringServiceStatus}.
 */
@RestController
@RequestMapping("/api/monitoring-service-statuses")
public class MonitoringServiceStatusResource {

    private static final Logger LOG = LoggerFactory.getLogger(MonitoringServiceStatusResource.class);

    private static final String ENTITY_NAME = "monitoringServiceMonitoringServiceStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MonitoringServiceStatusService monitoringServiceStatusService;

    private final MonitoringServiceStatusRepository monitoringServiceStatusRepository;

    private final MonitoringServiceStatusQueryService monitoringServiceStatusQueryService;

    public MonitoringServiceStatusResource(
        MonitoringServiceStatusService monitoringServiceStatusService,
        MonitoringServiceStatusRepository monitoringServiceStatusRepository,
        MonitoringServiceStatusQueryService monitoringServiceStatusQueryService
    ) {
        this.monitoringServiceStatusService = monitoringServiceStatusService;
        this.monitoringServiceStatusRepository = monitoringServiceStatusRepository;
        this.monitoringServiceStatusQueryService = monitoringServiceStatusQueryService;
    }

    /**
     * {@code POST  /monitoring-service-statuses} : Create a new monitoringServiceStatus.
     *
     * @param monitoringServiceStatusDTO the monitoringServiceStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new monitoringServiceStatusDTO, or with status {@code 400 (Bad Request)} if the monitoringServiceStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MonitoringServiceStatusDTO> createMonitoringServiceStatus(
        @Valid @RequestBody MonitoringServiceStatusDTO monitoringServiceStatusDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save MonitoringServiceStatus : {}", monitoringServiceStatusDTO);
        if (monitoringServiceStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new monitoringServiceStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        monitoringServiceStatusDTO = monitoringServiceStatusService.save(monitoringServiceStatusDTO);
        return ResponseEntity.created(new URI("/api/monitoring-service-statuses/" + monitoringServiceStatusDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, monitoringServiceStatusDTO.getId().toString())
            )
            .body(monitoringServiceStatusDTO);
    }

    /**
     * {@code PUT  /monitoring-service-statuses/:id} : Updates an existing monitoringServiceStatus.
     *
     * @param id the id of the monitoringServiceStatusDTO to save.
     * @param monitoringServiceStatusDTO the monitoringServiceStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monitoringServiceStatusDTO,
     * or with status {@code 400 (Bad Request)} if the monitoringServiceStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the monitoringServiceStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MonitoringServiceStatusDTO> updateMonitoringServiceStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MonitoringServiceStatusDTO monitoringServiceStatusDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MonitoringServiceStatus : {}, {}", id, monitoringServiceStatusDTO);
        if (monitoringServiceStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monitoringServiceStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monitoringServiceStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        monitoringServiceStatusDTO = monitoringServiceStatusService.update(monitoringServiceStatusDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, monitoringServiceStatusDTO.getId().toString()))
            .body(monitoringServiceStatusDTO);
    }

    /**
     * {@code PATCH  /monitoring-service-statuses/:id} : Partial updates given fields of an existing monitoringServiceStatus, field will ignore if it is null
     *
     * @param id the id of the monitoringServiceStatusDTO to save.
     * @param monitoringServiceStatusDTO the monitoringServiceStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monitoringServiceStatusDTO,
     * or with status {@code 400 (Bad Request)} if the monitoringServiceStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the monitoringServiceStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the monitoringServiceStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MonitoringServiceStatusDTO> partialUpdateMonitoringServiceStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MonitoringServiceStatusDTO monitoringServiceStatusDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MonitoringServiceStatus partially : {}, {}", id, monitoringServiceStatusDTO);
        if (monitoringServiceStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monitoringServiceStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monitoringServiceStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MonitoringServiceStatusDTO> result = monitoringServiceStatusService.partialUpdate(monitoringServiceStatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, monitoringServiceStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /monitoring-service-statuses} : get all the monitoringServiceStatuses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of monitoringServiceStatuses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MonitoringServiceStatusDTO>> getAllMonitoringServiceStatuses(
        MonitoringServiceStatusCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MonitoringServiceStatuses by criteria: {}", criteria);

        Page<MonitoringServiceStatusDTO> page = monitoringServiceStatusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /monitoring-service-statuses/count} : count all the monitoringServiceStatuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMonitoringServiceStatuses(MonitoringServiceStatusCriteria criteria) {
        LOG.debug("REST request to count MonitoringServiceStatuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(monitoringServiceStatusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /monitoring-service-statuses/:id} : get the "id" monitoringServiceStatus.
     *
     * @param id the id of the monitoringServiceStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the monitoringServiceStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MonitoringServiceStatusDTO> getMonitoringServiceStatus(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MonitoringServiceStatus : {}", id);
        Optional<MonitoringServiceStatusDTO> monitoringServiceStatusDTO = monitoringServiceStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(monitoringServiceStatusDTO);
    }

    /**
     * {@code DELETE  /monitoring-service-statuses/:id} : delete the "id" monitoringServiceStatus.
     *
     * @param id the id of the monitoringServiceStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMonitoringServiceStatus(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MonitoringServiceStatus : {}", id);
        monitoringServiceStatusService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
