package fr.smartprod.paperdms.monitoring.web.rest;

import fr.smartprod.paperdms.monitoring.repository.MonitoringMaintenanceTaskRepository;
import fr.smartprod.paperdms.monitoring.service.MonitoringMaintenanceTaskQueryService;
import fr.smartprod.paperdms.monitoring.service.MonitoringMaintenanceTaskService;
import fr.smartprod.paperdms.monitoring.service.criteria.MonitoringMaintenanceTaskCriteria;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringMaintenanceTaskDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.monitoring.domain.MonitoringMaintenanceTask}.
 */
@RestController
@RequestMapping("/api/monitoring-maintenance-tasks")
public class MonitoringMaintenanceTaskResource {

    private static final Logger LOG = LoggerFactory.getLogger(MonitoringMaintenanceTaskResource.class);

    private static final String ENTITY_NAME = "monitoringServiceMonitoringMaintenanceTask";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MonitoringMaintenanceTaskService monitoringMaintenanceTaskService;

    private final MonitoringMaintenanceTaskRepository monitoringMaintenanceTaskRepository;

    private final MonitoringMaintenanceTaskQueryService monitoringMaintenanceTaskQueryService;

    public MonitoringMaintenanceTaskResource(
        MonitoringMaintenanceTaskService monitoringMaintenanceTaskService,
        MonitoringMaintenanceTaskRepository monitoringMaintenanceTaskRepository,
        MonitoringMaintenanceTaskQueryService monitoringMaintenanceTaskQueryService
    ) {
        this.monitoringMaintenanceTaskService = monitoringMaintenanceTaskService;
        this.monitoringMaintenanceTaskRepository = monitoringMaintenanceTaskRepository;
        this.monitoringMaintenanceTaskQueryService = monitoringMaintenanceTaskQueryService;
    }

    /**
     * {@code POST  /monitoring-maintenance-tasks} : Create a new monitoringMaintenanceTask.
     *
     * @param monitoringMaintenanceTaskDTO the monitoringMaintenanceTaskDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new monitoringMaintenanceTaskDTO, or with status {@code 400 (Bad Request)} if the monitoringMaintenanceTask has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MonitoringMaintenanceTaskDTO> createMonitoringMaintenanceTask(
        @Valid @RequestBody MonitoringMaintenanceTaskDTO monitoringMaintenanceTaskDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save MonitoringMaintenanceTask : {}", monitoringMaintenanceTaskDTO);
        if (monitoringMaintenanceTaskDTO.getId() != null) {
            throw new BadRequestAlertException("A new monitoringMaintenanceTask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        monitoringMaintenanceTaskDTO = monitoringMaintenanceTaskService.save(monitoringMaintenanceTaskDTO);
        return ResponseEntity.created(new URI("/api/monitoring-maintenance-tasks/" + monitoringMaintenanceTaskDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, monitoringMaintenanceTaskDTO.getId().toString())
            )
            .body(monitoringMaintenanceTaskDTO);
    }

    /**
     * {@code PUT  /monitoring-maintenance-tasks/:id} : Updates an existing monitoringMaintenanceTask.
     *
     * @param id the id of the monitoringMaintenanceTaskDTO to save.
     * @param monitoringMaintenanceTaskDTO the monitoringMaintenanceTaskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monitoringMaintenanceTaskDTO,
     * or with status {@code 400 (Bad Request)} if the monitoringMaintenanceTaskDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the monitoringMaintenanceTaskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MonitoringMaintenanceTaskDTO> updateMonitoringMaintenanceTask(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MonitoringMaintenanceTaskDTO monitoringMaintenanceTaskDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MonitoringMaintenanceTask : {}, {}", id, monitoringMaintenanceTaskDTO);
        if (monitoringMaintenanceTaskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monitoringMaintenanceTaskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monitoringMaintenanceTaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        monitoringMaintenanceTaskDTO = monitoringMaintenanceTaskService.update(monitoringMaintenanceTaskDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, monitoringMaintenanceTaskDTO.getId().toString())
            )
            .body(monitoringMaintenanceTaskDTO);
    }

    /**
     * {@code PATCH  /monitoring-maintenance-tasks/:id} : Partial updates given fields of an existing monitoringMaintenanceTask, field will ignore if it is null
     *
     * @param id the id of the monitoringMaintenanceTaskDTO to save.
     * @param monitoringMaintenanceTaskDTO the monitoringMaintenanceTaskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monitoringMaintenanceTaskDTO,
     * or with status {@code 400 (Bad Request)} if the monitoringMaintenanceTaskDTO is not valid,
     * or with status {@code 404 (Not Found)} if the monitoringMaintenanceTaskDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the monitoringMaintenanceTaskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MonitoringMaintenanceTaskDTO> partialUpdateMonitoringMaintenanceTask(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MonitoringMaintenanceTaskDTO monitoringMaintenanceTaskDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MonitoringMaintenanceTask partially : {}, {}", id, monitoringMaintenanceTaskDTO);
        if (monitoringMaintenanceTaskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monitoringMaintenanceTaskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monitoringMaintenanceTaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MonitoringMaintenanceTaskDTO> result = monitoringMaintenanceTaskService.partialUpdate(monitoringMaintenanceTaskDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, monitoringMaintenanceTaskDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /monitoring-maintenance-tasks} : get all the monitoringMaintenanceTasks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of monitoringMaintenanceTasks in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MonitoringMaintenanceTaskDTO>> getAllMonitoringMaintenanceTasks(
        MonitoringMaintenanceTaskCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MonitoringMaintenanceTasks by criteria: {}", criteria);

        Page<MonitoringMaintenanceTaskDTO> page = monitoringMaintenanceTaskQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /monitoring-maintenance-tasks/count} : count all the monitoringMaintenanceTasks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMonitoringMaintenanceTasks(MonitoringMaintenanceTaskCriteria criteria) {
        LOG.debug("REST request to count MonitoringMaintenanceTasks by criteria: {}", criteria);
        return ResponseEntity.ok().body(monitoringMaintenanceTaskQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /monitoring-maintenance-tasks/:id} : get the "id" monitoringMaintenanceTask.
     *
     * @param id the id of the monitoringMaintenanceTaskDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the monitoringMaintenanceTaskDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MonitoringMaintenanceTaskDTO> getMonitoringMaintenanceTask(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MonitoringMaintenanceTask : {}", id);
        Optional<MonitoringMaintenanceTaskDTO> monitoringMaintenanceTaskDTO = monitoringMaintenanceTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(monitoringMaintenanceTaskDTO);
    }

    /**
     * {@code DELETE  /monitoring-maintenance-tasks/:id} : delete the "id" monitoringMaintenanceTask.
     *
     * @param id the id of the monitoringMaintenanceTaskDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMonitoringMaintenanceTask(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MonitoringMaintenanceTask : {}", id);
        monitoringMaintenanceTaskService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
