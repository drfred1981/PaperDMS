package fr.smartprod.paperdms.business.web.rest;

import fr.smartprod.paperdms.business.repository.ManualChapterRepository;
import fr.smartprod.paperdms.business.service.ManualChapterService;
import fr.smartprod.paperdms.business.service.dto.ManualChapterDTO;
import fr.smartprod.paperdms.business.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.business.domain.ManualChapter}.
 */
@RestController
@RequestMapping("/api/manual-chapters")
public class ManualChapterResource {

    private static final Logger LOG = LoggerFactory.getLogger(ManualChapterResource.class);

    private static final String ENTITY_NAME = "businessDocServiceManualChapter";

    @Value("${jhipster.clientApp.name:businessDocService}")
    private String applicationName;

    private final ManualChapterService manualChapterService;

    private final ManualChapterRepository manualChapterRepository;

    public ManualChapterResource(ManualChapterService manualChapterService, ManualChapterRepository manualChapterRepository) {
        this.manualChapterService = manualChapterService;
        this.manualChapterRepository = manualChapterRepository;
    }

    /**
     * {@code POST  /manual-chapters} : Create a new manualChapter.
     *
     * @param manualChapterDTO the manualChapterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new manualChapterDTO, or with status {@code 400 (Bad Request)} if the manualChapter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ManualChapterDTO> createManualChapter(@Valid @RequestBody ManualChapterDTO manualChapterDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ManualChapter : {}", manualChapterDTO);
        if (manualChapterDTO.getId() != null) {
            throw new BadRequestAlertException("A new manualChapter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        manualChapterDTO = manualChapterService.save(manualChapterDTO);
        return ResponseEntity.created(new URI("/api/manual-chapters/" + manualChapterDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, manualChapterDTO.getId().toString()))
            .body(manualChapterDTO);
    }

    /**
     * {@code PUT  /manual-chapters/:id} : Updates an existing manualChapter.
     *
     * @param id the id of the manualChapterDTO to save.
     * @param manualChapterDTO the manualChapterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manualChapterDTO,
     * or with status {@code 400 (Bad Request)} if the manualChapterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the manualChapterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ManualChapterDTO> updateManualChapter(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ManualChapterDTO manualChapterDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ManualChapter : {}, {}", id, manualChapterDTO);
        if (manualChapterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, manualChapterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!manualChapterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        manualChapterDTO = manualChapterService.update(manualChapterDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, manualChapterDTO.getId().toString()))
            .body(manualChapterDTO);
    }

    /**
     * {@code PATCH  /manual-chapters/:id} : Partial updates given fields of an existing manualChapter, field will ignore if it is null
     *
     * @param id the id of the manualChapterDTO to save.
     * @param manualChapterDTO the manualChapterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manualChapterDTO,
     * or with status {@code 400 (Bad Request)} if the manualChapterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the manualChapterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the manualChapterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ManualChapterDTO> partialUpdateManualChapter(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ManualChapterDTO manualChapterDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ManualChapter partially : {}, {}", id, manualChapterDTO);
        if (manualChapterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, manualChapterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!manualChapterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ManualChapterDTO> result = manualChapterService.partialUpdate(manualChapterDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, manualChapterDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /manual-chapters} : get all the manualChapters.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of manualChapters in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ManualChapterDTO>> getAllManualChapters(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of ManualChapters");
        Page<ManualChapterDTO> page = manualChapterService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /manual-chapters/:id} : get the "id" manualChapter.
     *
     * @param id the id of the manualChapterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the manualChapterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ManualChapterDTO> getManualChapter(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ManualChapter : {}", id);
        Optional<ManualChapterDTO> manualChapterDTO = manualChapterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(manualChapterDTO);
    }

    /**
     * {@code DELETE  /manual-chapters/:id} : delete the "id" manualChapter.
     *
     * @param id the id of the manualChapterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManualChapter(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ManualChapter : {}", id);
        manualChapterService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
