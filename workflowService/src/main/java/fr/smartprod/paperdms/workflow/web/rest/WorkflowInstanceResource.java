package fr.smartprod.paperdms.workflow.web.rest;

import fr.smartprod.paperdms.workflow.repository.WorkflowInstanceRepository;
import fr.smartprod.paperdms.workflow.service.WorkflowInstanceQueryService;
import fr.smartprod.paperdms.workflow.service.WorkflowInstanceService;
import fr.smartprod.paperdms.workflow.service.criteria.WorkflowInstanceCriteria;
import fr.smartprod.paperdms.workflow.service.dto.WorkflowInstanceDTO;
import fr.smartprod.paperdms.workflow.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.workflow.domain.WorkflowInstance}.
 */
@RestController
@RequestMapping("/api/workflow-instances")
public class WorkflowInstanceResource {

    private static final Logger LOG = LoggerFactory.getLogger(WorkflowInstanceResource.class);

    private static final String ENTITY_NAME = "workflowServiceWorkflowInstance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkflowInstanceService workflowInstanceService;

    private final WorkflowInstanceRepository workflowInstanceRepository;

    private final WorkflowInstanceQueryService workflowInstanceQueryService;

    public WorkflowInstanceResource(
        WorkflowInstanceService workflowInstanceService,
        WorkflowInstanceRepository workflowInstanceRepository,
        WorkflowInstanceQueryService workflowInstanceQueryService
    ) {
        this.workflowInstanceService = workflowInstanceService;
        this.workflowInstanceRepository = workflowInstanceRepository;
        this.workflowInstanceQueryService = workflowInstanceQueryService;
    }

    /**
     * {@code POST  /workflow-instances} : Create a new workflowInstance.
     *
     * @param workflowInstanceDTO the workflowInstanceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workflowInstanceDTO, or with status {@code 400 (Bad Request)} if the workflowInstance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WorkflowInstanceDTO> createWorkflowInstance(@Valid @RequestBody WorkflowInstanceDTO workflowInstanceDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save WorkflowInstance : {}", workflowInstanceDTO);
        if (workflowInstanceDTO.getId() != null) {
            throw new BadRequestAlertException("A new workflowInstance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        workflowInstanceDTO = workflowInstanceService.save(workflowInstanceDTO);
        return ResponseEntity.created(new URI("/api/workflow-instances/" + workflowInstanceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, workflowInstanceDTO.getId().toString()))
            .body(workflowInstanceDTO);
    }

    /**
     * {@code PUT  /workflow-instances/:id} : Updates an existing workflowInstance.
     *
     * @param id the id of the workflowInstanceDTO to save.
     * @param workflowInstanceDTO the workflowInstanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workflowInstanceDTO,
     * or with status {@code 400 (Bad Request)} if the workflowInstanceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workflowInstanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WorkflowInstanceDTO> updateWorkflowInstance(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkflowInstanceDTO workflowInstanceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update WorkflowInstance : {}, {}", id, workflowInstanceDTO);
        if (workflowInstanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workflowInstanceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workflowInstanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        workflowInstanceDTO = workflowInstanceService.update(workflowInstanceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workflowInstanceDTO.getId().toString()))
            .body(workflowInstanceDTO);
    }

    /**
     * {@code PATCH  /workflow-instances/:id} : Partial updates given fields of an existing workflowInstance, field will ignore if it is null
     *
     * @param id the id of the workflowInstanceDTO to save.
     * @param workflowInstanceDTO the workflowInstanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workflowInstanceDTO,
     * or with status {@code 400 (Bad Request)} if the workflowInstanceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workflowInstanceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workflowInstanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WorkflowInstanceDTO> partialUpdateWorkflowInstance(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkflowInstanceDTO workflowInstanceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update WorkflowInstance partially : {}, {}", id, workflowInstanceDTO);
        if (workflowInstanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workflowInstanceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workflowInstanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkflowInstanceDTO> result = workflowInstanceService.partialUpdate(workflowInstanceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workflowInstanceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /workflow-instances} : get all the workflowInstances.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workflowInstances in body.
     */
    @GetMapping("")
    public ResponseEntity<List<WorkflowInstanceDTO>> getAllWorkflowInstances(
        WorkflowInstanceCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get WorkflowInstances by criteria: {}", criteria);

        Page<WorkflowInstanceDTO> page = workflowInstanceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /workflow-instances/count} : count all the workflowInstances.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countWorkflowInstances(WorkflowInstanceCriteria criteria) {
        LOG.debug("REST request to count WorkflowInstances by criteria: {}", criteria);
        return ResponseEntity.ok().body(workflowInstanceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /workflow-instances/:id} : get the "id" workflowInstance.
     *
     * @param id the id of the workflowInstanceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workflowInstanceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WorkflowInstanceDTO> getWorkflowInstance(@PathVariable("id") Long id) {
        LOG.debug("REST request to get WorkflowInstance : {}", id);
        Optional<WorkflowInstanceDTO> workflowInstanceDTO = workflowInstanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workflowInstanceDTO);
    }

    /**
     * {@code DELETE  /workflow-instances/:id} : delete the "id" workflowInstance.
     *
     * @param id the id of the workflowInstanceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkflowInstance(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete WorkflowInstance : {}", id);
        workflowInstanceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
