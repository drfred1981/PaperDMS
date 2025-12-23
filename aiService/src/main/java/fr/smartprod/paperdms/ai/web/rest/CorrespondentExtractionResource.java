package fr.smartprod.paperdms.ai.web.rest;

import fr.smartprod.paperdms.ai.repository.CorrespondentExtractionRepository;
import fr.smartprod.paperdms.ai.service.CorrespondentExtractionQueryService;
import fr.smartprod.paperdms.ai.service.CorrespondentExtractionService;
import fr.smartprod.paperdms.ai.service.criteria.CorrespondentExtractionCriteria;
import fr.smartprod.paperdms.ai.service.dto.CorrespondentExtractionDTO;
import fr.smartprod.paperdms.ai.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.ai.domain.CorrespondentExtraction}.
 */
@RestController
@RequestMapping("/api/correspondent-extractions")
public class CorrespondentExtractionResource {

    private static final Logger LOG = LoggerFactory.getLogger(CorrespondentExtractionResource.class);

    private static final String ENTITY_NAME = "aiServiceCorrespondentExtraction";

    @Value("${jhipster.clientApp.name:aiService}")
    private String applicationName;

    private final CorrespondentExtractionService correspondentExtractionService;

    private final CorrespondentExtractionRepository correspondentExtractionRepository;

    private final CorrespondentExtractionQueryService correspondentExtractionQueryService;

    public CorrespondentExtractionResource(
        CorrespondentExtractionService correspondentExtractionService,
        CorrespondentExtractionRepository correspondentExtractionRepository,
        CorrespondentExtractionQueryService correspondentExtractionQueryService
    ) {
        this.correspondentExtractionService = correspondentExtractionService;
        this.correspondentExtractionRepository = correspondentExtractionRepository;
        this.correspondentExtractionQueryService = correspondentExtractionQueryService;
    }

    /**
     * {@code POST  /correspondent-extractions} : Create a new correspondentExtraction.
     *
     * @param correspondentExtractionDTO the correspondentExtractionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new correspondentExtractionDTO, or with status {@code 400 (Bad Request)} if the correspondentExtraction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CorrespondentExtractionDTO> createCorrespondentExtraction(
        @Valid @RequestBody CorrespondentExtractionDTO correspondentExtractionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save CorrespondentExtraction : {}", correspondentExtractionDTO);
        if (correspondentExtractionDTO.getId() != null) {
            throw new BadRequestAlertException("A new correspondentExtraction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        correspondentExtractionDTO = correspondentExtractionService.save(correspondentExtractionDTO);
        return ResponseEntity.created(new URI("/api/correspondent-extractions/" + correspondentExtractionDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, correspondentExtractionDTO.getId().toString())
            )
            .body(correspondentExtractionDTO);
    }

    /**
     * {@code PUT  /correspondent-extractions/:id} : Updates an existing correspondentExtraction.
     *
     * @param id the id of the correspondentExtractionDTO to save.
     * @param correspondentExtractionDTO the correspondentExtractionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated correspondentExtractionDTO,
     * or with status {@code 400 (Bad Request)} if the correspondentExtractionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the correspondentExtractionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CorrespondentExtractionDTO> updateCorrespondentExtraction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CorrespondentExtractionDTO correspondentExtractionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CorrespondentExtraction : {}, {}", id, correspondentExtractionDTO);
        if (correspondentExtractionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, correspondentExtractionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!correspondentExtractionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        correspondentExtractionDTO = correspondentExtractionService.update(correspondentExtractionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, correspondentExtractionDTO.getId().toString()))
            .body(correspondentExtractionDTO);
    }

    /**
     * {@code PATCH  /correspondent-extractions/:id} : Partial updates given fields of an existing correspondentExtraction, field will ignore if it is null
     *
     * @param id the id of the correspondentExtractionDTO to save.
     * @param correspondentExtractionDTO the correspondentExtractionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated correspondentExtractionDTO,
     * or with status {@code 400 (Bad Request)} if the correspondentExtractionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the correspondentExtractionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the correspondentExtractionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CorrespondentExtractionDTO> partialUpdateCorrespondentExtraction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CorrespondentExtractionDTO correspondentExtractionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CorrespondentExtraction partially : {}, {}", id, correspondentExtractionDTO);
        if (correspondentExtractionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, correspondentExtractionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!correspondentExtractionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CorrespondentExtractionDTO> result = correspondentExtractionService.partialUpdate(correspondentExtractionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, correspondentExtractionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /correspondent-extractions} : get all the correspondentExtractions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of correspondentExtractions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CorrespondentExtractionDTO>> getAllCorrespondentExtractions(
        CorrespondentExtractionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CorrespondentExtractions by criteria: {}", criteria);

        Page<CorrespondentExtractionDTO> page = correspondentExtractionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /correspondent-extractions/count} : count all the correspondentExtractions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCorrespondentExtractions(CorrespondentExtractionCriteria criteria) {
        LOG.debug("REST request to count CorrespondentExtractions by criteria: {}", criteria);
        return ResponseEntity.ok().body(correspondentExtractionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /correspondent-extractions/:id} : get the "id" correspondentExtraction.
     *
     * @param id the id of the correspondentExtractionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the correspondentExtractionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CorrespondentExtractionDTO> getCorrespondentExtraction(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CorrespondentExtraction : {}", id);
        Optional<CorrespondentExtractionDTO> correspondentExtractionDTO = correspondentExtractionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(correspondentExtractionDTO);
    }

    /**
     * {@code DELETE  /correspondent-extractions/:id} : delete the "id" correspondentExtraction.
     *
     * @param id the id of the correspondentExtractionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCorrespondentExtraction(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CorrespondentExtraction : {}", id);
        correspondentExtractionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
