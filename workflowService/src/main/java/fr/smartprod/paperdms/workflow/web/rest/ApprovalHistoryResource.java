package fr.smartprod.paperdms.workflow.web.rest;

import fr.smartprod.paperdms.workflow.repository.ApprovalHistoryRepository;
import fr.smartprod.paperdms.workflow.service.ApprovalHistoryService;
import fr.smartprod.paperdms.workflow.service.dto.ApprovalHistoryDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.workflow.domain.ApprovalHistory}.
 */
@RestController
@RequestMapping("/api/approval-histories")
public class ApprovalHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(ApprovalHistoryResource.class);

    private static final String ENTITY_NAME = "workflowServiceApprovalHistory";

    @Value("${jhipster.clientApp.name:workflowService}")
    private String applicationName;

    private final ApprovalHistoryService approvalHistoryService;

    private final ApprovalHistoryRepository approvalHistoryRepository;

    public ApprovalHistoryResource(ApprovalHistoryService approvalHistoryService, ApprovalHistoryRepository approvalHistoryRepository) {
        this.approvalHistoryService = approvalHistoryService;
        this.approvalHistoryRepository = approvalHistoryRepository;
    }

    /**
     * {@code POST  /approval-histories} : Create a new approvalHistory.
     *
     * @param approvalHistoryDTO the approvalHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new approvalHistoryDTO, or with status {@code 400 (Bad Request)} if the approvalHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ApprovalHistoryDTO> createApprovalHistory(@Valid @RequestBody ApprovalHistoryDTO approvalHistoryDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ApprovalHistory : {}", approvalHistoryDTO);
        if (approvalHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new approvalHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        approvalHistoryDTO = approvalHistoryService.save(approvalHistoryDTO);
        return ResponseEntity.created(new URI("/api/approval-histories/" + approvalHistoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, approvalHistoryDTO.getId().toString()))
            .body(approvalHistoryDTO);
    }

    /**
     * {@code PUT  /approval-histories/:id} : Updates an existing approvalHistory.
     *
     * @param id the id of the approvalHistoryDTO to save.
     * @param approvalHistoryDTO the approvalHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated approvalHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the approvalHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the approvalHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApprovalHistoryDTO> updateApprovalHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ApprovalHistoryDTO approvalHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ApprovalHistory : {}, {}", id, approvalHistoryDTO);
        if (approvalHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, approvalHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!approvalHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        approvalHistoryDTO = approvalHistoryService.update(approvalHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, approvalHistoryDTO.getId().toString()))
            .body(approvalHistoryDTO);
    }

    /**
     * {@code PATCH  /approval-histories/:id} : Partial updates given fields of an existing approvalHistory, field will ignore if it is null
     *
     * @param id the id of the approvalHistoryDTO to save.
     * @param approvalHistoryDTO the approvalHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated approvalHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the approvalHistoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the approvalHistoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the approvalHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ApprovalHistoryDTO> partialUpdateApprovalHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ApprovalHistoryDTO approvalHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ApprovalHistory partially : {}, {}", id, approvalHistoryDTO);
        if (approvalHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, approvalHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!approvalHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApprovalHistoryDTO> result = approvalHistoryService.partialUpdate(approvalHistoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, approvalHistoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /approval-histories} : get all the approvalHistories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of approvalHistories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ApprovalHistoryDTO>> getAllApprovalHistories(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of ApprovalHistories");
        Page<ApprovalHistoryDTO> page = approvalHistoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /approval-histories/:id} : get the "id" approvalHistory.
     *
     * @param id the id of the approvalHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the approvalHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApprovalHistoryDTO> getApprovalHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ApprovalHistory : {}", id);
        Optional<ApprovalHistoryDTO> approvalHistoryDTO = approvalHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(approvalHistoryDTO);
    }

    /**
     * {@code DELETE  /approval-histories/:id} : delete the "id" approvalHistory.
     *
     * @param id the id of the approvalHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApprovalHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ApprovalHistory : {}", id);
        approvalHistoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
