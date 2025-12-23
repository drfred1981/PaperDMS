package fr.smartprod.paperdms.transform.web.rest;

import fr.smartprod.paperdms.transform.repository.MergeJobRepository;
import fr.smartprod.paperdms.transform.service.MergeJobService;
import fr.smartprod.paperdms.transform.service.dto.MergeJobDTO;
import fr.smartprod.paperdms.transform.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.transform.domain.MergeJob}.
 */
@RestController
@RequestMapping("/api/merge-jobs")
public class MergeJobResource {

    private static final Logger LOG = LoggerFactory.getLogger(MergeJobResource.class);

    private static final String ENTITY_NAME = "transformServiceMergeJob";

    @Value("${jhipster.clientApp.name:transformService}")
    private String applicationName;

    private final MergeJobService mergeJobService;

    private final MergeJobRepository mergeJobRepository;

    public MergeJobResource(MergeJobService mergeJobService, MergeJobRepository mergeJobRepository) {
        this.mergeJobService = mergeJobService;
        this.mergeJobRepository = mergeJobRepository;
    }

    /**
     * {@code POST  /merge-jobs} : Create a new mergeJob.
     *
     * @param mergeJobDTO the mergeJobDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mergeJobDTO, or with status {@code 400 (Bad Request)} if the mergeJob has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MergeJobDTO> createMergeJob(@Valid @RequestBody MergeJobDTO mergeJobDTO) throws URISyntaxException {
        LOG.debug("REST request to save MergeJob : {}", mergeJobDTO);
        if (mergeJobDTO.getId() != null) {
            throw new BadRequestAlertException("A new mergeJob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        mergeJobDTO = mergeJobService.save(mergeJobDTO);
        return ResponseEntity.created(new URI("/api/merge-jobs/" + mergeJobDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, mergeJobDTO.getId().toString()))
            .body(mergeJobDTO);
    }

    /**
     * {@code PUT  /merge-jobs/:id} : Updates an existing mergeJob.
     *
     * @param id the id of the mergeJobDTO to save.
     * @param mergeJobDTO the mergeJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mergeJobDTO,
     * or with status {@code 400 (Bad Request)} if the mergeJobDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mergeJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MergeJobDTO> updateMergeJob(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MergeJobDTO mergeJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MergeJob : {}, {}", id, mergeJobDTO);
        if (mergeJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mergeJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mergeJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        mergeJobDTO = mergeJobService.update(mergeJobDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mergeJobDTO.getId().toString()))
            .body(mergeJobDTO);
    }

    /**
     * {@code PATCH  /merge-jobs/:id} : Partial updates given fields of an existing mergeJob, field will ignore if it is null
     *
     * @param id the id of the mergeJobDTO to save.
     * @param mergeJobDTO the mergeJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mergeJobDTO,
     * or with status {@code 400 (Bad Request)} if the mergeJobDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mergeJobDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mergeJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MergeJobDTO> partialUpdateMergeJob(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MergeJobDTO mergeJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MergeJob partially : {}, {}", id, mergeJobDTO);
        if (mergeJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mergeJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mergeJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MergeJobDTO> result = mergeJobService.partialUpdate(mergeJobDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mergeJobDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /merge-jobs} : get all the mergeJobs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mergeJobs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MergeJobDTO>> getAllMergeJobs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of MergeJobs");
        Page<MergeJobDTO> page = mergeJobService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /merge-jobs/:id} : get the "id" mergeJob.
     *
     * @param id the id of the mergeJobDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mergeJobDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MergeJobDTO> getMergeJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MergeJob : {}", id);
        Optional<MergeJobDTO> mergeJobDTO = mergeJobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mergeJobDTO);
    }

    /**
     * {@code DELETE  /merge-jobs/:id} : delete the "id" mergeJob.
     *
     * @param id the id of the mergeJobDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMergeJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MergeJob : {}", id);
        mergeJobService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
