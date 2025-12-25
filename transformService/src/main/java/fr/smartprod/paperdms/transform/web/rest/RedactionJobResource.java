package fr.smartprod.paperdms.transform.web.rest;

import fr.smartprod.paperdms.transform.repository.RedactionJobRepository;
import fr.smartprod.paperdms.transform.service.RedactionJobService;
import fr.smartprod.paperdms.transform.service.dto.RedactionJobDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.transform.domain.RedactionJob}.
 */
@RestController
@RequestMapping("/api/redaction-jobs")
public class RedactionJobResource {

    private static final Logger LOG = LoggerFactory.getLogger(RedactionJobResource.class);

    private static final String ENTITY_NAME = "transformServiceRedactionJob";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RedactionJobService redactionJobService;

    private final RedactionJobRepository redactionJobRepository;

    public RedactionJobResource(RedactionJobService redactionJobService, RedactionJobRepository redactionJobRepository) {
        this.redactionJobService = redactionJobService;
        this.redactionJobRepository = redactionJobRepository;
    }

    /**
     * {@code POST  /redaction-jobs} : Create a new redactionJob.
     *
     * @param redactionJobDTO the redactionJobDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new redactionJobDTO, or with status {@code 400 (Bad Request)} if the redactionJob has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RedactionJobDTO> createRedactionJob(@Valid @RequestBody RedactionJobDTO redactionJobDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save RedactionJob : {}", redactionJobDTO);
        if (redactionJobDTO.getId() != null) {
            throw new BadRequestAlertException("A new redactionJob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        redactionJobDTO = redactionJobService.save(redactionJobDTO);
        return ResponseEntity.created(new URI("/api/redaction-jobs/" + redactionJobDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, redactionJobDTO.getId().toString()))
            .body(redactionJobDTO);
    }

    /**
     * {@code PUT  /redaction-jobs/:id} : Updates an existing redactionJob.
     *
     * @param id the id of the redactionJobDTO to save.
     * @param redactionJobDTO the redactionJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated redactionJobDTO,
     * or with status {@code 400 (Bad Request)} if the redactionJobDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the redactionJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RedactionJobDTO> updateRedactionJob(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RedactionJobDTO redactionJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update RedactionJob : {}, {}", id, redactionJobDTO);
        if (redactionJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, redactionJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!redactionJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        redactionJobDTO = redactionJobService.update(redactionJobDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, redactionJobDTO.getId().toString()))
            .body(redactionJobDTO);
    }

    /**
     * {@code PATCH  /redaction-jobs/:id} : Partial updates given fields of an existing redactionJob, field will ignore if it is null
     *
     * @param id the id of the redactionJobDTO to save.
     * @param redactionJobDTO the redactionJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated redactionJobDTO,
     * or with status {@code 400 (Bad Request)} if the redactionJobDTO is not valid,
     * or with status {@code 404 (Not Found)} if the redactionJobDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the redactionJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RedactionJobDTO> partialUpdateRedactionJob(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RedactionJobDTO redactionJobDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RedactionJob partially : {}, {}", id, redactionJobDTO);
        if (redactionJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, redactionJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!redactionJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RedactionJobDTO> result = redactionJobService.partialUpdate(redactionJobDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, redactionJobDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /redaction-jobs} : get all the redactionJobs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of redactionJobs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RedactionJobDTO>> getAllRedactionJobs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of RedactionJobs");
        Page<RedactionJobDTO> page = redactionJobService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /redaction-jobs/:id} : get the "id" redactionJob.
     *
     * @param id the id of the redactionJobDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the redactionJobDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RedactionJobDTO> getRedactionJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RedactionJob : {}", id);
        Optional<RedactionJobDTO> redactionJobDTO = redactionJobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(redactionJobDTO);
    }

    /**
     * {@code DELETE  /redaction-jobs/:id} : delete the "id" redactionJob.
     *
     * @param id the id of the redactionJobDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRedactionJob(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RedactionJob : {}", id);
        redactionJobService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
