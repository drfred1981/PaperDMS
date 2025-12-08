package com.ged.workflow.web.rest;

import com.ged.workflow.repository.WorkflowRepository;
import com.ged.workflow.service.WorkflowQueryService;
import com.ged.workflow.service.WorkflowService;
import com.ged.workflow.service.criteria.WorkflowCriteria;
import com.ged.workflow.service.dto.WorkflowDTO;
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
 * REST controller for managing {@link com.ged.workflow.domain.Workflow}.
 */
@RestController
@RequestMapping("/api/workflows")
public class WorkflowResource {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowResource.class);

    private static final String ENTITY_NAME = "workflowServiceWorkflow";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkflowService workflowService;

    private final WorkflowRepository workflowRepository;

    private final WorkflowQueryService workflowQueryService;

    public WorkflowResource(
        WorkflowService workflowService,
        WorkflowRepository workflowRepository,
        WorkflowQueryService workflowQueryService
    ) {
        this.workflowService = workflowService;
        this.workflowRepository = workflowRepository;
        this.workflowQueryService = workflowQueryService;
    }

    /**
     * {@code POST  /workflows} : Create a new workflow.
     *
     * @param workflowDTO the workflowDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workflowDTO, or with status {@code 400 (Bad Request)} if the workflow has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WorkflowDTO> createWorkflow(@Valid @RequestBody WorkflowDTO workflowDTO) throws URISyntaxException {
        LOG.debug("REST request to save Workflow : {}", workflowDTO);
        if (workflowDTO.getId() != null) {
            throw new BadRequestAlertException("A new workflow cannot already have an ID", ENTITY_NAME, "idexists");
        }
        workflowDTO = workflowService.save(workflowDTO);
        return ResponseEntity.created(new URI("/api/workflows/" + workflowDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, workflowDTO.getId().toString()))
            .body(workflowDTO);
    }

    /**
     * {@code PUT  /workflows/:id} : Updates an existing workflow.
     *
     * @param id the id of the workflowDTO to save.
     * @param workflowDTO the workflowDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workflowDTO,
     * or with status {@code 400 (Bad Request)} if the workflowDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workflowDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WorkflowDTO> updateWorkflow(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkflowDTO workflowDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Workflow : {}, {}", id, workflowDTO);
        if (workflowDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workflowDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workflowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        workflowDTO = workflowService.update(workflowDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workflowDTO.getId().toString()))
            .body(workflowDTO);
    }

    /**
     * {@code PATCH  /workflows/:id} : Partial updates given fields of an existing workflow, field will ignore if it is null
     *
     * @param id the id of the workflowDTO to save.
     * @param workflowDTO the workflowDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workflowDTO,
     * or with status {@code 400 (Bad Request)} if the workflowDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workflowDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workflowDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WorkflowDTO> partialUpdateWorkflow(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkflowDTO workflowDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Workflow partially : {}, {}", id, workflowDTO);
        if (workflowDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workflowDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workflowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkflowDTO> result = workflowService.partialUpdate(workflowDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workflowDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /workflows} : get all the workflows.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workflows in body.
     */
    @GetMapping("")
    public ResponseEntity<List<WorkflowDTO>> getAllWorkflows(
        WorkflowCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Workflows by criteria: {}", criteria);

        Page<WorkflowDTO> page = workflowQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /workflows/count} : count all the workflows.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countWorkflows(WorkflowCriteria criteria) {
        LOG.debug("REST request to count Workflows by criteria: {}", criteria);
        return ResponseEntity.ok().body(workflowQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /workflows/:id} : get the "id" workflow.
     *
     * @param id the id of the workflowDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workflowDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WorkflowDTO> getWorkflow(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Workflow : {}", id);
        Optional<WorkflowDTO> workflowDTO = workflowService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workflowDTO);
    }

    /**
     * {@code DELETE  /workflows/:id} : delete the "id" workflow.
     *
     * @param id the id of the workflowDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkflow(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Workflow : {}", id);
        workflowService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
