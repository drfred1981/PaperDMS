package com.ged.workflow.web.rest;

import com.ged.workflow.repository.WorkflowStepRepository;
import com.ged.workflow.service.WorkflowStepService;
import com.ged.workflow.service.dto.WorkflowStepDTO;
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
 * REST controller for managing {@link com.ged.workflow.domain.WorkflowStep}.
 */
@RestController
@RequestMapping("/api/workflow-steps")
public class WorkflowStepResource {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowStepResource.class);

    private static final String ENTITY_NAME = "workflowServiceWorkflowStep";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkflowStepService workflowStepService;

    private final WorkflowStepRepository workflowStepRepository;

    public WorkflowStepResource(WorkflowStepService workflowStepService, WorkflowStepRepository workflowStepRepository) {
        this.workflowStepService = workflowStepService;
        this.workflowStepRepository = workflowStepRepository;
    }

    /**
     * {@code POST  /workflow-steps} : Create a new workflowStep.
     *
     * @param workflowStepDTO the workflowStepDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workflowStepDTO, or with status {@code 400 (Bad Request)} if the workflowStep has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WorkflowStepDTO> createWorkflowStep(@Valid @RequestBody WorkflowStepDTO workflowStepDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save WorkflowStep : {}", workflowStepDTO);
        if (workflowStepDTO.getId() != null) {
            throw new BadRequestAlertException("A new workflowStep cannot already have an ID", ENTITY_NAME, "idexists");
        }
        workflowStepDTO = workflowStepService.save(workflowStepDTO);
        return ResponseEntity.created(new URI("/api/workflow-steps/" + workflowStepDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, workflowStepDTO.getId().toString()))
            .body(workflowStepDTO);
    }

    /**
     * {@code PUT  /workflow-steps/:id} : Updates an existing workflowStep.
     *
     * @param id the id of the workflowStepDTO to save.
     * @param workflowStepDTO the workflowStepDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workflowStepDTO,
     * or with status {@code 400 (Bad Request)} if the workflowStepDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workflowStepDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WorkflowStepDTO> updateWorkflowStep(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkflowStepDTO workflowStepDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update WorkflowStep : {}, {}", id, workflowStepDTO);
        if (workflowStepDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workflowStepDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workflowStepRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        workflowStepDTO = workflowStepService.update(workflowStepDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workflowStepDTO.getId().toString()))
            .body(workflowStepDTO);
    }

    /**
     * {@code PATCH  /workflow-steps/:id} : Partial updates given fields of an existing workflowStep, field will ignore if it is null
     *
     * @param id the id of the workflowStepDTO to save.
     * @param workflowStepDTO the workflowStepDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workflowStepDTO,
     * or with status {@code 400 (Bad Request)} if the workflowStepDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workflowStepDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workflowStepDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WorkflowStepDTO> partialUpdateWorkflowStep(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkflowStepDTO workflowStepDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update WorkflowStep partially : {}, {}", id, workflowStepDTO);
        if (workflowStepDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workflowStepDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workflowStepRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkflowStepDTO> result = workflowStepService.partialUpdate(workflowStepDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workflowStepDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /workflow-steps} : get all the workflowSteps.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workflowSteps in body.
     */
    @GetMapping("")
    public ResponseEntity<List<WorkflowStepDTO>> getAllWorkflowSteps(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of WorkflowSteps");
        Page<WorkflowStepDTO> page = workflowStepService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /workflow-steps/:id} : get the "id" workflowStep.
     *
     * @param id the id of the workflowStepDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workflowStepDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WorkflowStepDTO> getWorkflowStep(@PathVariable("id") Long id) {
        LOG.debug("REST request to get WorkflowStep : {}", id);
        Optional<WorkflowStepDTO> workflowStepDTO = workflowStepService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workflowStepDTO);
    }

    /**
     * {@code DELETE  /workflow-steps/:id} : delete the "id" workflowStep.
     *
     * @param id the id of the workflowStepDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkflowStep(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete WorkflowStep : {}", id);
        workflowStepService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
