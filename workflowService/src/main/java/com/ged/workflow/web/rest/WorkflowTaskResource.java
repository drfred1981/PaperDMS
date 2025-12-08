package com.ged.workflow.web.rest;

import com.ged.workflow.repository.WorkflowTaskRepository;
import com.ged.workflow.service.WorkflowTaskQueryService;
import com.ged.workflow.service.WorkflowTaskService;
import com.ged.workflow.service.criteria.WorkflowTaskCriteria;
import com.ged.workflow.service.dto.WorkflowTaskDTO;
import com.ged.workflow.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.ged.workflow.domain.WorkflowTask}.
 */
@RestController
@RequestMapping("/api/workflow-tasks")
public class WorkflowTaskResource {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowTaskResource.class);

    private static final String ENTITY_NAME = "workflowServiceWorkflowTask";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkflowTaskService workflowTaskService;

    private final WorkflowTaskRepository workflowTaskRepository;

    private final WorkflowTaskQueryService workflowTaskQueryService;

    public WorkflowTaskResource(
        WorkflowTaskService workflowTaskService,
        WorkflowTaskRepository workflowTaskRepository,
        WorkflowTaskQueryService workflowTaskQueryService
    ) {
        this.workflowTaskService = workflowTaskService;
        this.workflowTaskRepository = workflowTaskRepository;
        this.workflowTaskQueryService = workflowTaskQueryService;
    }

    /**
     * {@code POST  /workflow-tasks} : Create a new workflowTask.
     *
     * @param workflowTaskDTO the workflowTaskDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workflowTaskDTO, or with status {@code 400 (Bad Request)} if the workflowTask has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WorkflowTaskDTO> createWorkflowTask(@Valid @RequestBody WorkflowTaskDTO workflowTaskDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save WorkflowTask : {}", workflowTaskDTO);
        if (workflowTaskDTO.getId() != null) {
            throw new BadRequestAlertException("A new workflowTask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        workflowTaskDTO = workflowTaskService.save(workflowTaskDTO);
        return ResponseEntity.created(new URI("/api/workflow-tasks/" + workflowTaskDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, workflowTaskDTO.getId().toString()))
            .body(workflowTaskDTO);
    }

    /**
     * {@code PUT  /workflow-tasks/:id} : Updates an existing workflowTask.
     *
     * @param id the id of the workflowTaskDTO to save.
     * @param workflowTaskDTO the workflowTaskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workflowTaskDTO,
     * or with status {@code 400 (Bad Request)} if the workflowTaskDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workflowTaskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WorkflowTaskDTO> updateWorkflowTask(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkflowTaskDTO workflowTaskDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update WorkflowTask : {}, {}", id, workflowTaskDTO);
        if (workflowTaskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workflowTaskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workflowTaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        workflowTaskDTO = workflowTaskService.update(workflowTaskDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workflowTaskDTO.getId().toString()))
            .body(workflowTaskDTO);
    }

    /**
     * {@code PATCH  /workflow-tasks/:id} : Partial updates given fields of an existing workflowTask, field will ignore if it is null
     *
     * @param id the id of the workflowTaskDTO to save.
     * @param workflowTaskDTO the workflowTaskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workflowTaskDTO,
     * or with status {@code 400 (Bad Request)} if the workflowTaskDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workflowTaskDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workflowTaskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WorkflowTaskDTO> partialUpdateWorkflowTask(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkflowTaskDTO workflowTaskDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update WorkflowTask partially : {}, {}", id, workflowTaskDTO);
        if (workflowTaskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workflowTaskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workflowTaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkflowTaskDTO> result = workflowTaskService.partialUpdate(workflowTaskDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workflowTaskDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /workflow-tasks} : get all the workflowTasks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workflowTasks in body.
     */
    @GetMapping("")
    public ResponseEntity<List<WorkflowTaskDTO>> getAllWorkflowTasks(
        WorkflowTaskCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get WorkflowTasks by criteria: {}", criteria);

        Page<WorkflowTaskDTO> page = workflowTaskQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /workflow-tasks/count} : count all the workflowTasks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countWorkflowTasks(WorkflowTaskCriteria criteria) {
        LOG.debug("REST request to count WorkflowTasks by criteria: {}", criteria);
        return ResponseEntity.ok().body(workflowTaskQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /workflow-tasks/:id} : get the "id" workflowTask.
     *
     * @param id the id of the workflowTaskDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workflowTaskDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WorkflowTaskDTO> getWorkflowTask(@PathVariable("id") Long id) {
        LOG.debug("REST request to get WorkflowTask : {}", id);
        Optional<WorkflowTaskDTO> workflowTaskDTO = workflowTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workflowTaskDTO);
    }

    /**
     * {@code DELETE  /workflow-tasks/:id} : delete the "id" workflowTask.
     *
     * @param id the id of the workflowTaskDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkflowTask(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete WorkflowTask : {}", id);
        workflowTaskService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
