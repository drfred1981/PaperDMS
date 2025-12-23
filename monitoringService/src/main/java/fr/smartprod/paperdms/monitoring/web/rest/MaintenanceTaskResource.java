package fr.smartprod.paperdms.monitoring.web.rest;

import fr.smartprod.paperdms.monitoring.repository.MaintenanceTaskRepository;
import fr.smartprod.paperdms.monitoring.service.MaintenanceTaskQueryService;
import fr.smartprod.paperdms.monitoring.service.MaintenanceTaskService;
import fr.smartprod.paperdms.monitoring.service.criteria.MaintenanceTaskCriteria;
import fr.smartprod.paperdms.monitoring.service.dto.MaintenanceTaskDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.monitoring.domain.MaintenanceTask}.
 */
@RestController
@RequestMapping("/api/maintenance-tasks")
public class MaintenanceTaskResource {

    private static final Logger LOG = LoggerFactory.getLogger(MaintenanceTaskResource.class);

    private static final String ENTITY_NAME = "monitoringServiceMaintenanceTask";

    @Value("${jhipster.clientApp.name:monitoringService}")
    private String applicationName;

    private final MaintenanceTaskService maintenanceTaskService;

    private final MaintenanceTaskRepository maintenanceTaskRepository;

    private final MaintenanceTaskQueryService maintenanceTaskQueryService;

    public MaintenanceTaskResource(
        MaintenanceTaskService maintenanceTaskService,
        MaintenanceTaskRepository maintenanceTaskRepository,
        MaintenanceTaskQueryService maintenanceTaskQueryService
    ) {
        this.maintenanceTaskService = maintenanceTaskService;
        this.maintenanceTaskRepository = maintenanceTaskRepository;
        this.maintenanceTaskQueryService = maintenanceTaskQueryService;
    }

    /**
     * {@code POST  /maintenance-tasks} : Create a new maintenanceTask.
     *
     * @param maintenanceTaskDTO the maintenanceTaskDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new maintenanceTaskDTO, or with status {@code 400 (Bad Request)} if the maintenanceTask has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MaintenanceTaskDTO> createMaintenanceTask(@Valid @RequestBody MaintenanceTaskDTO maintenanceTaskDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save MaintenanceTask : {}", maintenanceTaskDTO);
        if (maintenanceTaskDTO.getId() != null) {
            throw new BadRequestAlertException("A new maintenanceTask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        maintenanceTaskDTO = maintenanceTaskService.save(maintenanceTaskDTO);
        return ResponseEntity.created(new URI("/api/maintenance-tasks/" + maintenanceTaskDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, maintenanceTaskDTO.getId().toString()))
            .body(maintenanceTaskDTO);
    }

    /**
     * {@code PUT  /maintenance-tasks/:id} : Updates an existing maintenanceTask.
     *
     * @param id the id of the maintenanceTaskDTO to save.
     * @param maintenanceTaskDTO the maintenanceTaskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated maintenanceTaskDTO,
     * or with status {@code 400 (Bad Request)} if the maintenanceTaskDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the maintenanceTaskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceTaskDTO> updateMaintenanceTask(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MaintenanceTaskDTO maintenanceTaskDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MaintenanceTask : {}, {}", id, maintenanceTaskDTO);
        if (maintenanceTaskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, maintenanceTaskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!maintenanceTaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        maintenanceTaskDTO = maintenanceTaskService.update(maintenanceTaskDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, maintenanceTaskDTO.getId().toString()))
            .body(maintenanceTaskDTO);
    }

    /**
     * {@code PATCH  /maintenance-tasks/:id} : Partial updates given fields of an existing maintenanceTask, field will ignore if it is null
     *
     * @param id the id of the maintenanceTaskDTO to save.
     * @param maintenanceTaskDTO the maintenanceTaskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated maintenanceTaskDTO,
     * or with status {@code 400 (Bad Request)} if the maintenanceTaskDTO is not valid,
     * or with status {@code 404 (Not Found)} if the maintenanceTaskDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the maintenanceTaskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MaintenanceTaskDTO> partialUpdateMaintenanceTask(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MaintenanceTaskDTO maintenanceTaskDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MaintenanceTask partially : {}, {}", id, maintenanceTaskDTO);
        if (maintenanceTaskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, maintenanceTaskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!maintenanceTaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MaintenanceTaskDTO> result = maintenanceTaskService.partialUpdate(maintenanceTaskDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, maintenanceTaskDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /maintenance-tasks} : get all the maintenanceTasks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of maintenanceTasks in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MaintenanceTaskDTO>> getAllMaintenanceTasks(
        MaintenanceTaskCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MaintenanceTasks by criteria: {}", criteria);

        Page<MaintenanceTaskDTO> page = maintenanceTaskQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /maintenance-tasks/count} : count all the maintenanceTasks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMaintenanceTasks(MaintenanceTaskCriteria criteria) {
        LOG.debug("REST request to count MaintenanceTasks by criteria: {}", criteria);
        return ResponseEntity.ok().body(maintenanceTaskQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /maintenance-tasks/:id} : get the "id" maintenanceTask.
     *
     * @param id the id of the maintenanceTaskDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the maintenanceTaskDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceTaskDTO> getMaintenanceTask(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MaintenanceTask : {}", id);
        Optional<MaintenanceTaskDTO> maintenanceTaskDTO = maintenanceTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(maintenanceTaskDTO);
    }

    /**
     * {@code DELETE  /maintenance-tasks/:id} : delete the "id" maintenanceTask.
     *
     * @param id the id of the maintenanceTaskDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenanceTask(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MaintenanceTask : {}", id);
        maintenanceTaskService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
